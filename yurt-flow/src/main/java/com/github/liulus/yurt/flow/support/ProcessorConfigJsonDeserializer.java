package com.github.liulus.yurt.flow.support;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.liulus.yurt.flow.model.ProcessorConfig;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * 自定义 ProcessorConfig 反序列化器,
 * 因为 ProcessorConfig 是抽象类, 所以在反序列化时需指定具体的实现类
 * 通过ProcessorConfig中的processorType确定具体使用的子类
 *
 * @author liulu
 * @version V1.0
 * @since 2020/9/3
 */
public class ProcessorConfigJsonDeserializer extends JsonDeserializer<ProcessorConfig> {

    @Override
    public ProcessorConfig deserialize(JsonParser p, DeserializationContext dct) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode jsonNode = codec.readTree(p);

        String type = Optional.ofNullable(jsonNode)
                .map(jn -> jn.get("type"))
                .map(JsonNode::asText)
                .orElse(null);
        ProcessorType processorType = ProcessorType.of(type);
        if (Objects.isNull(processorType)
                || Objects.equals(processorType.configClass(), ProcessorConfig.class)) {
            ProcessorConfig config = new ProcessorConfig();
            config.setType(type);
            return config;
        }
        return codec.treeToValue(jsonNode, processorType.configClass());
    }
}
