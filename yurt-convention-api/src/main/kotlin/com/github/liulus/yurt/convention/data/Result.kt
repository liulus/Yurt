package com.github.liulus.yurt.convention.data

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/10/31
 */
interface Result<T> {

    /**
     * 获取错误码
     * @return 错误码
     */
    val code: String

    /**
     * 获取成功或错误的信息
     * @return 成功或错误的信息
     */
    val message: String

    /**
     * 获取错误的异常堆栈
     * @return 错误的异常堆栈
     */
    val errorStack: String?

    /**
     * 获取数据
     * @return 数据
     */
    val data: T?

    /**
     * 是否成功
     * @return boolean
     */
    val success: Boolean

    /**
     * 是否错误
     * @return boolean
     */
    val error: Boolean?

    /**
     * 是否是业务处理失败，业务异常
     * @return boolean
     */
    val failure: Boolean?
}