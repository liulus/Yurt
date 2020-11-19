package com.github.liulus.yurt.flow.model;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/18
 */
public class ProcessorConfig {


    private String type;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ProcessorConfig{" +
                "type='" + type + '\'' +
                '}';
    }
}
