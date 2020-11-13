package com.github.liulus.yurt.jdbc;

import com.github.liulus.yurt.convention.cache.LRUCache;
import com.github.liulus.yurt.convention.util.NameUtils;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
class TableMetadata {

    private static final Map<Class<?>, TableMetadata> TABLE_CACHE = new LRUCache<>();

    private Class<?> entityClass;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 主键属性名
     */
    private String idField;

    /**
     * 主键对应的列名
     */
    private String idColumn;

    /**
     * 属性名和字段名映射关系的 map
     */
    private Map<String, String> fieldColumnMap;

    /**
     * 字段类型
     */
    private Map<String, Class<?>> fieldTypeMap;

    public static TableMetadata forClass(Class<?> entityClass) {
        synchronized (TABLE_CACHE) {
            return TABLE_CACHE.computeIfAbsent(entityClass, TableMetadata::new);
        }
    }

    private TableMetadata(Class<?> clazz) {
        fieldColumnMap = new HashMap<>();
        fieldTypeMap = new HashMap<>();
        initTableInfo(clazz);
    }


    private void initTableInfo(Class<?> eClass) {
        entityClass = eClass;
        Table tableAnnotation = eClass.getAnnotation(Table.class);
        tableName = tableAnnotation != null ? tableAnnotation.name()
                : NameUtils.getUnderLineName(eClass.getSimpleName());

        Field[] fields = eClass.getDeclaredFields();
        for (Field field : fields) {
            // 过滤静态字段和有 @Transient 注解的字段
            if (shouldIgnore(field)) {
                continue;
            }

            String fieldName = field.getName();
            Column column = field.getAnnotation(Column.class);
            String columnName = column != null ? column.name() : NameUtils.getUnderLineName(fieldName);

            // 主键信息 : 有 @Id 注解的字段，没有默认是 类名+Id
            if (field.isAnnotationPresent(Id.class) || ("id".equals(fieldName) && idField == null)) {
                this.idField = fieldName;
                this.idColumn = columnName;
            }
            // 将字段对应的列放到 map 中
            PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(eClass, fieldName);
            if (descriptor != null && descriptor.getReadMethod() != null && descriptor.getWriteMethod() != null) {
                fieldColumnMap.put(fieldName, columnName);
                fieldTypeMap.put(fieldName, field.getType());
            }
        }
        fieldColumnMap = Collections.unmodifiableMap(fieldColumnMap);
        fieldTypeMap = Collections.unmodifiableMap(fieldTypeMap);
    }

    private boolean shouldIgnore(Field field) {
        return Modifier.isStatic(field.getModifiers())
                || Modifier.isFinal(field.getModifiers())
                || field.isAnnotationPresent(Transient.class)
                || !BeanUtils.isSimpleProperty(field.getType());
    }


    public String getColumns() {
        return fieldColumnMap.values().stream()
                .map(col -> tableName + "." + col)
                .collect(Collectors.joining(", "));
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public String getIdField() {
        return idField;
    }

    public String getIdColumn() {
        return idColumn;
    }

    public String getTableName() {
        return tableName;
    }

    public Map<String, String> getFieldColumnMap() {
        return fieldColumnMap;
    }

    public Map<String, Class<?>> getFieldTypeMap() {
        return fieldTypeMap;
    }
}
