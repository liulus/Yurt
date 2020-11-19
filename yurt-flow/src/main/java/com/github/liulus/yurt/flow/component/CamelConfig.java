package com.github.liulus.yurt.flow.component;

import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/18
 */
@Component
public class CamelConfig implements InitializingBean {

    @Resource
    private ModelCamelContext camelContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        RestDefinition helloApi = new RestsDefinition()
                .rest("/api")
                .get("/home")
                .route()
                .setBody()
                .constant("hello api")
                .endRest();

        camelContext.addRouteDefinitions(helloApi.asRouteDefinition(camelContext));
    }


}
