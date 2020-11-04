package com.github.liulus.yurt.jdbc

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.springframework.aop.framework.ProxyFactory
import org.springframework.beans.factory.FactoryBean
import org.springframework.core.ResolvableType

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/3
 */
class JdbcRepositoryFactoryBean<T>(
        private val repositoryInterface: Class<T>
) : MethodInterceptor, FactoryBean<T> {

    private var entityClass: Class<*>? = null

    init {
        for (parent in repositoryInterface.genericInterfaces) {
            val parentType = ResolvableType.forType(parent)
            if (parentType.rawClass == JdbcRepository::class.java) {
                entityClass = parentType.getGeneric(0).rawClass
                break
            }
        }
    }

    override fun invoke(invocation: MethodInvocation): Any {
        val method = invocation.method
        return when (method.name) {
            "insert" -> ""
            "updateIgnoreNull" -> "12312"
            else -> "1232"
        }
    }

    override fun getObject(): T {
        return ProxyFactory.getProxy(repositoryInterface, this)
    }

    override fun getObjectType(): Class<T> {
        return repositoryInterface
    }


}

