package com.github.liulus.yurt.convention.data

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/10/31
 */
interface StatusCode {

    /**
     * 获取相应码
     */
    fun code(): String

    /**
     * 获取响应码信息
     */
    fun text(): String


}