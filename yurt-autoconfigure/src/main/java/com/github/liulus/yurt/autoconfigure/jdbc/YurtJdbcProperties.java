package com.github.liulus.yurt.autoconfigure.jdbc;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/18
 */
@ConfigurationProperties(prefix = "yurt.jdbc")
public class YurtJdbcProperties {

    private Boolean enabled = true;
    private String dbName;







}
