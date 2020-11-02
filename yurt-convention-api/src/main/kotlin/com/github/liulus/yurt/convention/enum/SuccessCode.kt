package com.github.liulus.yurt.convention.enum

import com.github.liulus.yurt.convention.data.StatusCode

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/10/31
 */
enum class SuccessCode(
        private val code: String,
        private val text: String,
) : StatusCode {

    /**
     * 成功返回的编码
     */
    SUCCESS("0", "");

    override fun code(): String {
        return code
    }

    override fun text(): String {
        return text
    }
}