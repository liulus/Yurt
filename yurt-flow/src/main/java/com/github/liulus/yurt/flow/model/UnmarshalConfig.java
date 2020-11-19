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
public class UnmarshalConfig extends ProcessorConfig {

    /**
     * 反序列化协议, 例如 json, xml, soap 等
     * 支持的协议见 CamelConst # PROTOCOL_开头的常量
     */
    private String protocol;

    /**
     * 解密的key
     */
    private String key;

}
