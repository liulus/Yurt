package com.github.liulus.yurt.jdbc

import com.github.liulus.yurt.convention.util.NameUtils
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.net.URI
import java.net.URL
import java.time.temporal.Temporal
import java.util.Collections
import java.util.Date
import java.util.Locale
import javax.persistence.Column
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Transient

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/2
 */
class TableMetadata private constructor(eClass: Class<*>) {

    companion object {
        @JvmStatic
        private val DEFAULT_CACHE_LIMIT = 128

        @JvmStatic
        private val TABLE_CACHE: MutableMap<Class<*>, TableMetadata> = object :
                LinkedHashMap<Class<*>, TableMetadata>(DEFAULT_CACHE_LIMIT, 0.75f, true) {
            override fun removeEldestEntry(eldest: Map.Entry<Class<*>, TableMetadata>): Boolean {
                return size > DEFAULT_CACHE_LIMIT
            }
        }

        @JvmStatic
        fun forClass(eClass: Class<*>): TableMetadata {
            synchronized(TABLE_CACHE) {
                return TABLE_CACHE.computeIfAbsent(eClass) { clazz -> TableMetadata(clazz) }
            }
        }
    }

    val entityClass: Class<*> = eClass
    val tableName: String
    var idField: String? = null
        private set
    var idColumn: String? = null
        private set
    val fieldColumnMap: Map<String, String>
    val fieldTypeMap: Map<String, Class<*>>

    init {
        val tableAnnotation = eClass.getAnnotation(Table::class.java)
        tableName = tableAnnotation?.name ?: NameUtils.getUnderLineName(eClass.simpleName)
        val columnMap: MutableMap<String, String> = HashMap()
        val typeMap: MutableMap<String, Class<*>> = HashMap()
        val fields = eClass.declaredFields
        for (field in fields) {
            // 过滤静态字段和有 @Transient 注解的字段
            if (shouldIgnore(field)) {
                continue
            }
            val fieldName = field.name
            val column = field.getAnnotation(Column::class.java)
            val columnName = column?.name ?: NameUtils.getUnderLineName(fieldName)

            // 主键信息 : 有 @Id 注解的字段，没有默认是 id
            if (field.isAnnotationPresent(Id::class.java) || fieldName == "id" && idField == null) {
                this.idField = fieldName
                this.idColumn = columnName
            }
            // 将字段对应的列放到 map 中
            columnMap[fieldName] = columnName
            typeMap[fieldName] = field.type
        }
        fieldColumnMap = Collections.unmodifiableMap(columnMap)
        fieldTypeMap = Collections.unmodifiableMap(typeMap)
    }


    private fun shouldIgnore(field: Field): Boolean {
        if (Modifier.isStatic(field.modifiers)
                || Modifier.isFinal(field.modifiers)
                || field.isAnnotationPresent(Transient::class.java)) {
            return true
        }
        val type = field.type
        if (type.isPrimitive
                || Integer::class.java.isAssignableFrom(type)
                || Long::class.java.isAssignableFrom(type)
                || Double::class.java.isAssignableFrom(type)
                || Boolean::class.java.isAssignableFrom(type)
                || Array<Byte>::class.java.isAssignableFrom(type)
                || CharSequence::class.java.isAssignableFrom(type)
                || Temporal::class.java.isAssignableFrom(type)
                || Date::class.java.isAssignableFrom(type)
                || Number::class.java.isAssignableFrom(type)
                || Enum::class.java.isAssignableFrom(type)
                || URI::class.java == type || URL::class.java == type
                || Locale::class.java == type) {
            return true
        }
        return false
    }

}



