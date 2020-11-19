package com.github.liulus.yurt.flow.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/18
 */
@Getter
@Setter
@ToString
public class LogConfig extends ProcessorConfig{

    private String message;
    private String level;
    private String logName;
    private String marker;

}
