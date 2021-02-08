package com.github.liulus.yurt.configure;

import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfiguration;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import com.github.liulus.yurt.jdbc.SQLExecutorImpl;
import com.github.liulus.yurt.jdbc.annotation.JdbcRepositoryScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;


/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/10
 */
@Configuration
@JdbcRepositoryScan("com.github.liulus.yurt.repository")
public class SpringTestConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    public DB initDb() throws Exception {
        DBConfiguration configuration = DBConfigurationBuilder.newBuilder()
                .setPort(3366)
                .addArg("--character-set-server=utf8mb4")
                .addArg("--collation-server=utf8mb4_unicode_ci")
                .addArg("--default-time_zone=+8:00")
                .build();
        DB db = DB.newEmbeddedDB(configuration);
        db.start();
        return db;
    }

    @Bean
    public DataSource dataSource() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3366");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean
    public TransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SQLExecutorImpl sqlExecutor(DataSource dataSource) {
        return new SQLExecutorImpl(dataSource);
    }
}
