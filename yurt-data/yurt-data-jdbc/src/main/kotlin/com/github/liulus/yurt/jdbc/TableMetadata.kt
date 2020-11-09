package com.github.liulus.yurt.jdbc

import com.github.liulus.yurt.convention.util.NameUtils
import org.springframework.beans.BeanUtils
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.Collections
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
internal class TableMetadata private constructor(eClass: Class<*>) {

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
    var idField: String = ""
        private set
    lateinit var idColumn: String
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
            if (field.isAnnotationPresent(Id::class.java) || fieldName == "id" && idField.isEmpty()) {
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
        return Modifier.isStatic(field.modifiers)
                || Modifier.isFinal(field.modifiers)
                || field.isAnnotationPresent(Transient::class.java)
                || !BeanUtils.isSimpleProperty(field.type)
    }

    val columns: String
        get() = fieldColumnMap.values.joinToString(transform = { "${tableName}.$it" })


}



