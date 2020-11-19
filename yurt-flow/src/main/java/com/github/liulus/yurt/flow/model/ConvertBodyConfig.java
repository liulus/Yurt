package com.github.liulus.yurt.flow.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/9/22
 */
@Getter
@Setter
@ToString
public class ConvertBodyConfig extends ProcessorConfig {

    private String type;
    private String charset;


}
