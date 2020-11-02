package com.github.liulus.yurt.convention.enum

import com.github.liulus.yurt.convention.data.StatusCode
import com.github.liulus.yurt.convention.exception.ServiceErrorException

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/10/31
 */
enum class ErrorCode(
        private val code: String,
        private val text: String,
) : StatusCode {
    UNKNOWN_ERROR("-1", "未知的系统错误"),
    DB_ERROR("SYS_1", "数据库异常"),
    ;

    override fun code(): String {
        return code
    }

    override fun text(): String {
        return text
    }

    open fun error(): ServiceErrorException {
        return ServiceErrorException(this)
    }

    open fun error(cause: Throwable): ServiceErrorException {
        return ServiceErrorException(this, cause)
    }
}