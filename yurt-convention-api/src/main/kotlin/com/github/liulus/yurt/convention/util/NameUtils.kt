package com.github.liulus.yurt.convention.util

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/2
 */
object NameUtils {
    /**
     * 下划线分割命名转换为驼峰命名
     *
     * @param name 下划线命名
     * @return 驼峰命名
     */
    @JvmStatic
    fun getCamelName(name: String?): String {
        return getCamelName(name, '_')
    }

    /**
     * 获取指定字符分隔的驼峰命名
     *
     * @param name      指定分隔符命名
     * @param delimiter 分隔符
     * @return 驼峰命名
     */
    @JvmStatic
    fun getCamelName(name: String?, delimiter: Char): String {
        if (name.isNullOrEmpty()) {
            return ""
        }
        val sb = StringBuilder()
        var i = 0
        while (i < name.length) {
            val c = name[i]
            if (c == delimiter) {
                i++
                sb.append(Character.toUpperCase(c))
            } else {
                sb.append(Character.toLowerCase(c))
            }
            i++
        }
        return sb.toString()
    }

    /**
     * 将名称首字母大写
     *
     * @param name 原名称
     * @return 首字母大写名称
     */
    @JvmStatic
    fun getFirstUpperName(name: String?): String {
        return when {
            name.isNullOrEmpty() -> ""
            Character.isUpperCase(name[0]) -> name
            else -> Character.toUpperCase(name[0]) + name.substring(1)
        }
    }

    /**
     * @param name 原名称
     * @return 首字母小写名称
     */
    @JvmStatic
    fun getFirstLowerName(name: String?): String {
        return when {
            name.isNullOrEmpty() -> ""
            Character.isLowerCase(name[0]) -> name
            else -> Character.toLowerCase(name[0]) + name.substring(1)
        }
    }

    /**
     * 驼峰命名转换为小写下划线分割命名
     *
     * @param name 驼峰命名
     * @return 下划线命名
     */
    @JvmStatic
    fun getUnderLineName(name: String?): String {
        return getLowerDelimiterName(name, "_")
    }

    /**
     * 驼峰命名转换为小写指定分隔符命名
     *
     * @param name      驼峰命名
     * @param delimiter 指定分隔符
     * @return 小写指定分隔符命名
     */
    @JvmStatic
    fun getLowerDelimiterName(name: String?, delimiter: String?): String {
        if (name.isNullOrEmpty()) {
            return ""
        }
        val sb = StringBuilder()
        for (i in name.indices) {
            val c = name[i]
            if (i > 0 && Character.isUpperCase(c)) {
                sb.append(delimiter)
            }
            sb.append(Character.toLowerCase(c))
        }
        return sb.toString()
    }

    /**
     * 驼峰命名转换为大写指定分隔符命名
     *
     * @param name      驼峰命名
     * @param delimiter 指定分隔符
     * @return 大写指定分隔符命名
     */
    @JvmStatic
    fun getUpperDelimiterName(name: String?, delimiter: String?): String {
        return getLowerDelimiterName(name, delimiter).toUpperCase()
    }
}