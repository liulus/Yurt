package com.github.liulus.yurt.jdbc;

import com.github.liulus.yurt.convention.util.NameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.util.ReflectionUtils;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/10
 */
public class AnnotationRowMapper<T> extends BeanPropertyRowMapper<T> {

    public AnnotationRowMapper(Class<T> mappedClass) {
        super(mappedClass);
    }

    @Override
    protected void initialize(Class<T> mappedClass) {
        super.initialize(mappedClass);

        Field mappedFields = ReflectionUtils.findField(this.getClass(), "mappedFields");
        Objects.requireNonNull(mappedFields, "mappedFields can not find in this class");
        ReflectionUtils.makeAccessible(mappedFields);
        Field mappedProperties = ReflectionUtils.findField(this.getClass(), "mappedProperties");
        Objects.requireNonNull(mappedProperties, "mappedProperties can not find in this class");
        ReflectionUtils.makeAccessible(mappedProperties);

        //noinspection unchecked
        Map<String, PropertyDescriptor> mappedFieldsMap = (Map<String, PropertyDescriptor>) ReflectionUtils.getField(mappedFields, this);
        //noinspection unchecked
        Set<String> mappedPropertiesSet = (Set<String>) ReflectionUtils.getField(mappedProperties, this);

        ReflectionUtils.doWithFields(mappedClass, field -> {
            if (Modifier.isStatic(field.getModifiers())
                    || Modifier.isFinal(field.getModifiers())
                    || field.isAnnotationPresent(Transient.class)
                    || !BeanUtils.isSimpleProperty(field.getType())) {
                return;
            }
            PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(mappedClass, field.getName());
            if (pd != null && pd.getWriteMethod() != null) {
                Column column = AnnotationUtils.findAnnotation(field, Column.class);
                String columnName = column != null ? column.name().toLowerCase()
                        : NameUtils.getUnderLineName(field.getName());
                mappedFieldsMap.putIfAbsent(columnName, pd);
                String lowerPdName = pd.getName().toLowerCase();
                if (!lowerPdName.equals(columnName)) {
                    mappedFieldsMap.putIfAbsent(lowerPdName, pd);
                }
                mappedPropertiesSet.add(pd.getName());
            }
        });
    }
}
