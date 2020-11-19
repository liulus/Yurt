package com.github.liulus.yurt.flow.support;

import com.github.liulus.yurt.flow.model.ChoiceConfig;
import com.github.liulus.yurt.flow.model.ConvertBodyConfig;
import com.github.liulus.yurt.flow.model.ExpressionConfig;
import com.github.liulus.yurt.flow.model.ExpressionNodeConfig;
import com.github.liulus.yurt.flow.model.HttpConfig;
import com.github.liulus.yurt.flow.model.LogConfig;
import com.github.liulus.yurt.flow.model.MarshalConfig;
import com.github.liulus.yurt.flow.model.ProcessorConfig;
import com.github.liulus.yurt.flow.model.SQLConfig;
import com.github.liulus.yurt.flow.model.SetHeaderConfig;
import com.github.liulus.yurt.flow.model.StopConfig;
import com.github.liulus.yurt.flow.model.TemplateConfig;
import com.github.liulus.yurt.flow.model.ToConfig;
import com.github.liulus.yurt.flow.model.UnmarshalConfig;

import java.util.Objects;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/18
 */
public enum ProcessorType {
    DELEGATE("delegate", ProcessorConfig.class),
    CUSTOMER("customer", ProcessorConfig.class),
    TO("to", ToConfig.class),
    LOG("log", LogConfig.class),

    //    EXPRESSION(EXPRESSION, ExpressionConfig.class),
    VALIDATE("validate", ExpressionConfig.class),
    SCRIPT("script", ExpressionConfig.class),
    SET_BODY("set_body", ExpressionConfig.class),
    SET_HEADER("set_header", SetHeaderConfig.class),
    TEMPLATE("template", TemplateConfig.class),

    MARSHAL("marshal", MarshalConfig.class),
    UNMARSHAL("unmarshal", UnmarshalConfig.class),
    CONVERT_BODY("convert_body", ConvertBodyConfig.class),

    START("start", ProcessorConfig.class),
    CHOICE("choice", ChoiceConfig.class),
    WHEN("when", ExpressionNodeConfig.class),
    OTHERWISE("otherwise", ExpressionNodeConfig.class),
    LOOP("loop", ExpressionNodeConfig.class),
    LOOP_CONDITION("loop_condition", ExpressionNodeConfig.class),
    STOP("stop", StopConfig.class),

    //    PROPERTY_MAPPING(PROPERTY_MAPPING, PropertyMappingConfig.class),
    HTTP("http", HttpConfig.class),
    SQL("sql", SQLConfig.class),

    ;

    private final String code;
    private final Class<? extends ProcessorConfig> configClass;

    ProcessorType(String code, Class<? extends ProcessorConfig> configClass) {
        this.code = code;
        this.configClass = configClass;
    }

    public String code() {
        return this.code;
    }

    public Class<? extends ProcessorConfig> configClass() {
        return this.configClass;
    }

    public static ProcessorType of(String processorType) {
        if (Objects.isNull(processorType) || processorType.isEmpty()) {
            return null;
        }
        String lowerCase = processorType.toLowerCase();
        for (ProcessorType value : ProcessorType.values()) {
            if (value.code.equals(lowerCase)) {
                return value;
            }
        }
        return null;
    }
}
