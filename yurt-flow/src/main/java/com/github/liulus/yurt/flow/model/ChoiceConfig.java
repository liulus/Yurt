package com.github.liulus.yurt.flow.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.liulus.yurt.flow.support.ProcessorConfigJsonDeserializer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 分支流程, 分为 when 和 otherwise
 *
 * @author liulu
 * @version V1.0
 * @since 2020/9/2
 */
@Getter
@Setter
@ToString
public class ChoiceConfig extends ProcessorConfig {

    private List<ExpressionNodeConfig> whenClauses;

    @JsonDeserialize(contentUsing = ProcessorConfigJsonDeserializer.class)
    private List<ProcessorConfig> otherwise;
}
