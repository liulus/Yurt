package com.github.liulus.yurt.flow.model;

import java.util.List;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/18
 */
public class RouteMetadata {

    private String endpoint;
    private String routeId;

    private List<ProcessorConfig> configs;


    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public List<ProcessorConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<ProcessorConfig> configs) {
        this.configs = configs;
    }

    @Override
    public String toString() {
        return "RouteMetadata{" +
                "endpoint='" + endpoint + '\'' +
                ", routeId='" + routeId + '\'' +
                '}';
    }
}
