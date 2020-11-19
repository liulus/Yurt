package com.github.liulus.yurt.flow.builder;

import com.github.liulus.yurt.flow.model.ProcessorConfig;
import com.github.liulus.yurt.flow.support.ProcessorType;
import org.apache.camel.model.ProcessorDefinition;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/18
 */
public interface ProcessorDefinitionBuilder {

    /**
     * 构建ProcessorDefinition的类型
     *
     * @return ProcessorTypeEmun
     * @see ProcessorType
     */
    ProcessorType getProcessorType();

    /**
     * 根据processorConfig构建具体的processor实例, 并添加到RouteDefinition中
     *
     * @param pd     ProcessorDefinition, 构建RouteDefinition的api
     * @param config ProcessorConfig processor的配置
     */
    void build(ProcessorDefinition<?> pd, ProcessorConfig config);

}
