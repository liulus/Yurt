package com.github.liulus.yurt.flow.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/9/23
 */
@Getter
@Setter
@ToString
public class StopConfig extends ProcessorConfig {

    private String code;
    // 失败时 返回给前端的信息，支持表达式
    private String message;
    // 当 code 不为 0 时 是否返回当前body, 默认false
    private Boolean showBody;

}
