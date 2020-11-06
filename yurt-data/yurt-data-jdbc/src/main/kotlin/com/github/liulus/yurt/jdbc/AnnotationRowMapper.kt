package com.github.liulus.yurt.jdbc

import com.github.liulus.yurt.convention.util.NameUtils.getUnderLineName
import org.springframework.beans.BeanUtils
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.util.ReflectionUtils
import java.beans.PropertyDescriptor
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import javax.persistence.Column
import javax.persistence.Transient

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/5
 */
class AnnotationRowMapper<T>(mappedClass: Class<T>) : BeanPropertyRowMapper<T>() {

    init {
        initialize(mappedClass)
    }

    override fun initialize(mappedClass: Class<T>) {
        super.initialize(mappedClass)
        val mappedFields = ReflectionUtils.findField(this.javaClass, "mappedFields")
        requireNotNull(mappedFields) { "mappedFields can not find in this class" }
        ReflectionUtils.makeAccessible(mappedFields)
        val mappedProperties = ReflectionUtils.findField(this.javaClass, "mappedProperties")
        requireNotNull(mappedProperties) { "mappedProperties can not find in this class" }
        ReflectionUtils.makeAccessible(mappedProperties)
        val mappedFieldsMap = ReflectionUtils.getField(mappedFields, this) as MutableMap<String, PropertyDescriptor>
        val mappedPropertiesSet = ReflectionUtils.getField(mappedProperties, this) as MutableSet<String>
        ReflectionUtils.doWithFields(mappedClass) { field: Field ->
            if (Modifier.isStatic(field.modifiers)
                    || Modifier.isFinal(field.modifiers)
                    || field.isAnnotationPresent(Transient::class.java)
                    || !BeanUtils.isSimpleProperty(field.type)) {
                return@doWithFields
            }
            val pd = BeanUtils.getPropertyDescriptor(mappedClass, field.name)
            if (pd != null && pd.writeMethod != null) {
                val column = AnnotationUtils.findAnnotation(field, Column::class.java)
                val columnName = column?.name?.toLowerCase() ?: getUnderLineName(field.name)
                mappedFieldsMap.putIfAbsent(columnName, pd)
                val lowerPdName = pd.name.toLowerCase()
                if (lowerPdName != columnName) {
                    mappedFieldsMap.putIfAbsent(lowerPdName, pd)
                }
                mappedPropertiesSet.add(pd.name)
            }
        }
    }


}