package com.github.liulus.yurt.flow.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 属性映射处理器配置
 * 在处理器流转的java对象都是map, 所以此处理器仅处理map间的映射
 *
 * @author liulu
 * @version V1.0
 * @since 2020/9/2
 */
@Getter
@Setter
@ToString
public class PropertyMappingConfig extends ProcessorConfig {


    private List<PropertyMapping> propertyMappings;


    @Getter
    @Setter
    @ToString
    public static class PropertyMapping {

        /**
         * 源属性, 支持嵌套, 支持数组,
         * 属性嵌套: name.firstName, 表示name下的 firstName
         * 数组: names[].firstName, 表示names是数组类型, 对数组的每个元素都做映射
         * 数组: names[2].firstName, 表示names是数组类型, 但是仅对数组下标为2的元素做映射
         */
        private String source;
        // 写法同 source
        private String target;
        // 备注
        private String remark;

        private String format; // 待定
    }


    public void addMapping(String source, String target) {
        if (propertyMappings == null) {
            propertyMappings = new ArrayList<>();
        }
        PropertyMapping mapping = new PropertyMapping();
        mapping.source = source;
        mapping.target = target;
        propertyMappings.add(mapping);
    }

}
