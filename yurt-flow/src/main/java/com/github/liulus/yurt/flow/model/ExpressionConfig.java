package com.github.liulus.yurt.flow.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/9/2
 */
@Getter
@Setter
@ToString
public class ExpressionConfig extends ProcessorConfig {

    // 表达式类型
    private String expressionType;

    private String script;


}
