package com.github.liulus.yurt.flow.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

/**
 * 发送http请求的一些配置
 *
 * @author liulu
 * @version V1.0
 * @since 2020/9/2
 */
@Getter
@Setter
@ToString
public class HttpConfig extends ProcessorConfig {

    // 默认超时时间 30 秒
    public static final int DEFAULT_READ_TIMEOUT = 30 * 1000;
    public static final String DEFAULT_METHOD = "POST";


    private String url;
    private Integer readTimeout;
    private String method;

    private Map<String, String> headers;


}
