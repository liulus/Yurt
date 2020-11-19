package com.github.liulus.yurt.flow.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/18
 */
public class MarshalConfig extends ProcessorConfig {

    private String protocol;

    @Getter
    @Setter
    @ToString
    public static class XML extends MarshalConfig {
        private String namespace; // xml 命名空间
        private String namespacePrefix; // 自定义命名空间前缀
        private Boolean nodeUsePrefix; // 节点是否使用前缀
        private Boolean wrapCdata; // 节点文本是否使用cadata
    }

    @Getter
    @Setter
    @ToString
    public static class SOAP extends XML {

        private String methodName; // 调用方法名
        private Boolean methodUsePrefix; // 方法名节点是否使用前缀
        private Map<String, ?> headers; // 自定义 soap header
    }


    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public String toString() {
        return "MarshalConfig{" +
                "protocol='" + protocol + '\'' +
                '}';
    }
}
