package com.github.liulus.yurt.flow.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 对Message设置header,
 *
 * @author liulu
 * @version V1.0
 * @see org.apache.camel.Message
 * @since 2020/9/3
 */
@Getter
@Setter
@ToString
public class SetHeaderConfig extends ProcessorConfig {

    private String name;
    private ExpressionConfig expression;

}
