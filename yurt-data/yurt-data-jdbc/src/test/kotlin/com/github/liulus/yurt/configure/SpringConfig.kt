package com.github.liulus.yurt.configure

import ch.vorburger.exec.ManagedProcessException
import ch.vorburger.mariadb4j.DB
import ch.vorburger.mariadb4j.DBConfigurationBuilder
import com.github.liulus.yurt.jdbc.SQLExecutor
import com.github.liulus.yurt.jdbc.SQLExecutorImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import javax.sql.DataSource

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/6
 */
@Configuration
open class SpringConfig {


    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    @Throws(ManagedProcessException::class)
    open fun initDb(): DB {
        val configuration = DBConfigurationBuilder.newBuilder()
                .setPort(3366)
                .addArg("--character-set-server=utf8mb4")
                .addArg("--collation-server=utf8mb4_unicode_ci")
                .addArg("--default-time_zone=+8:00")
                .build()
        val db = DB.newEmbeddedDB(configuration)
        db.start()
        return db
    }

    @Bean
    open fun dataSource(): DataSource {
        val dataSource = SingleConnectionDataSource()
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver")
        dataSource.url = "jdbc:mysql://localhost:3366"
        dataSource.username = "root"
        dataSource.password = ""
        return dataSource
    }

    @Bean
    open fun sqlExecutor(dataSource: DataSource): SQLExecutor {
        return SQLExecutorImpl(dataSource)
    }


}


