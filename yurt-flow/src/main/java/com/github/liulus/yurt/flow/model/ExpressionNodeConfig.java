package com.github.liulus.yurt.flow.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.liulus.yurt.flow.support.ProcessorConfigJsonDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 配置带表达式和执行流程的配置
 * 例如: when, 当表达式为true时 执行
 * loop, 表达式返回 数字 表示循环次数, 每次循环内执行
 *
 * @author liulu
 * @version V1.0
 * @since 2020/9/2
 */
@Getter
@Setter
@ToString
public class ExpressionNodeConfig extends ProcessorConfig {

    private ExpressionConfig expression;

    @JsonDeserialize(contentUsing = ProcessorConfigJsonDeserializer.class)
    private List<ProcessorConfig> outputs;

}
