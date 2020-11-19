package com.github.liulus.yurt.flow.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/17
 */
@Getter
@Setter
@ToString
public class SQLConfig extends ProcessorConfig {

    private String sql;
    private String outputType;
    private String outputClass;

    private String outputHeader;

    private String storedProcedure;
    private Boolean isFunction;


}
