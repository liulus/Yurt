package com.github.liulus.yurt.convention.util

import com.github.liulus.yurt.convention.exception.ServiceValidException

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/2
 */
object Asserts {

    @JvmStatic
    fun isNull(obj: Any?, message: String, vararg args: Any?) {
        if (obj != null) {
            throw ServiceValidException(FormatUtils.formatMessage(message, args = args))
        }
    }

    @JvmStatic
    fun notNull(obj: Any?, message: String, vararg args: Any?) {
        if (obj == null) {
            throw ServiceValidException(FormatUtils.formatMessage(message, args = args))
        }
    }

    @JvmStatic
    fun isEmpty(obj: String?, message: String, vararg args: Any?) {
        if (!obj.isNullOrEmpty()) {
            throw ServiceValidException(FormatUtils.formatMessage(message, args = args))
        }
    }

    @JvmStatic
    fun hasText(obj: String?, message: String, vararg args: Any?) {
        if (obj.isNullOrBlank()) {
            throw ServiceValidException(FormatUtils.formatMessage(message, args = args))
        }
    }

    @JvmStatic
    fun isEmpty(collection: Collection<Any>?, message: String, vararg args: Any?) {
        if (!collection.isNullOrEmpty()) {
            throw ServiceValidException(FormatUtils.formatMessage(message, args = args))
        }
    }

    @JvmStatic
    fun notEmpty(collection: Collection<Any>?, message: String, vararg args: Any?) {
        if (collection.isNullOrEmpty()) {
            throw ServiceValidException(FormatUtils.formatMessage(message, args = args))
        }
    }

    @JvmStatic
    fun isEmpty(map: Map<Any, Any>?, message: String, vararg args: Any?) {
        if (!map.isNullOrEmpty()) {
            throw ServiceValidException(FormatUtils.formatMessage(message, args = args))
        }
    }

    @JvmStatic
    fun notEmpty(map: Map<Any, Any>?, message: String, vararg args: Any?) {
        if (map.isNullOrEmpty()) {
            throw ServiceValidException(FormatUtils.formatMessage(message, args = args))
        }
    }

    @JvmStatic
    fun isEmpty(array: Array<Any>?, message: String, vararg args: Any?) {
        if (!array.isNullOrEmpty()) {
            throw ServiceValidException(FormatUtils.formatMessage(message, args = args))
        }
    }

    @JvmStatic
    fun notEmpty(array: Array<Any>?, message: String, vararg args: Any?) {
        if (array.isNullOrEmpty()) {
            throw ServiceValidException(FormatUtils.formatMessage(message, args = args))
        }
    }

    @JvmStatic
    fun isTrue(expression: Boolean, message: String, vararg args: Any?) {
        if (!expression) {
            throw ServiceValidException(FormatUtils.formatMessage(message, args = args))
        }
    }

    @JvmStatic
    fun isFalse(expression: Boolean, message: String, vararg args: Any?) {
        if (expression) {
            throw ServiceValidException(FormatUtils.formatMessage(message, args = args))
        }
    }


    fun isInstanceOf(type: Class<Any>, obj: Any, message: String, vararg args: Any?) {
        if (!type.isInstance(obj)) {
            throw ServiceValidException(FormatUtils.formatMessage(message, args = args))
        }
    }

}