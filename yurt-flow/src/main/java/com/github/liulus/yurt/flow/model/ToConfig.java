package com.github.liulus.yurt.flow.model;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/18
 */
public class ToConfig extends ProcessorConfig {

    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "ToConfig{" +
                "uri='" + uri + '\'' +
                '}';
    }
}
