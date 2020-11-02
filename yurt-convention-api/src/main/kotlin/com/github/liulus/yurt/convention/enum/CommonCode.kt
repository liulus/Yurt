package com.github.liulus.yurt.convention.enum

import com.github.liulus.yurt.convention.data.StatusCode

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/10/31
 */

enum class CommonCode(
        private val code: String,
        private val text: String,
) : StatusCode {
    INVALID_PARAM("C_0001", "无效的参数"),
    NOT_NULL("INVALID_0001", "参数 {} 不能为null"),
    NOT_EMPTY("INVALID_0002", "参数 {} 不能为空"),
    ;

    override fun code(): String {
        return code
    }

    override fun text(): String {
        return text
    }

}
