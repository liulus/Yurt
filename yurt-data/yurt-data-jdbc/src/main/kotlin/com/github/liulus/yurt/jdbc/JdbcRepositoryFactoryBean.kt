package com.github.liulus.yurt.jdbc

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.springframework.aop.framework.ProxyFactory
import org.springframework.beans.factory.FactoryBean
import org.springframework.core.ResolvableType
import javax.annotation.Resource
import javax.sql.DataSource

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/3
 */
class JdbcRepositoryFactoryBean<T>(
        private val repositoryInterface: Class<T>
) : MethodInterceptor, FactoryBean<T> {

    private lateinit var entityClass: Class<*>

    @Resource
    private lateinit var dataSourceMap: Map<String, DataSource>

    @Resource
    private lateinit var sqlExecutor: SQLExecutor

    init {
        for (parent in repositoryInterface.genericInterfaces) {
            val parentType = ResolvableType.forType(parent)
            if (parentType.rawClass == JdbcRepository::class.java) {
                entityClass = parentType.getGeneric(0).rawClass!!
                break
            }
        }
    }

    override fun invoke(invocation: MethodInvocation): Any? {
        val method = invocation.method
        return when (method.name) {
            "insert" -> sqlExecutor.insert(invocation.arguments[0])
            "updateIgnoreNull" -> sqlExecutor.updateIgnoreNull(invocation.arguments[0])
            "deleteById" -> sqlExecutor.deleteById(entityClass, invocation.arguments[0] as Long)
            "selectById" -> sqlExecutor.selectById(entityClass, invocation.arguments[0] as Long)
            "selectByIds" -> sqlExecutor.selectByIds(entityClass, invocation.arguments[0] as Collection<Long>)
            else -> SQLContext.getContext(repositoryInterface, method).execute(sqlExecutor, invocation.arguments)
        }
    }


    override fun getObject(): T {
        return ProxyFactory.getProxy(repositoryInterface, this)
    }

    override fun getObjectType(): Class<T> {
        return repositoryInterface
    }


}

