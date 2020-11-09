package com.github.liulus.yurt.jdbc

import com.github.liulus.yurt.configure.SpringConfig
import com.github.liulus.yurt.model.GoodsQuery
import com.github.liulus.yurt.repository.GoodsRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDateTime
import javax.annotation.Resource

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/6
 */
@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [SpringConfig::class])
@Sql(scripts = ["/sql/init_schema.sql"])
class JdbcRepositoryTest {

    @Resource
    private lateinit var sqlExecutor: SQLExecutor

    private val interfaceClass: Class<GoodsRepository> = GoodsRepository::class.java

    @Test
    fun testSelect() {
        val method = interfaceClass.getDeclaredMethod("selectByCode", String::class.java)
        var result = SQLContext.getContext(interfaceClass, method).execute(sqlExecutor, arrayOf("213324"))

        println(method.name)
    }

    @Test
    fun testSelect2() {

        val goods = GoodsQuery()
        goods.pageSize = 2
        goods.startTime = LocalDateTime.now().minusDays(1L)


        val method = interfaceClass.getDeclaredMethod("selectByQuery", GoodsQuery::class.java)
        var result = SQLContext.getContext(interfaceClass, method).execute(sqlExecutor, arrayOf(goods))

        println(method.name)
    }


}

