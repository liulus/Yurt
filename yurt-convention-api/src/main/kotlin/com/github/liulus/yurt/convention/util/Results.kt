package com.github.liulus.yurt.convention.util

import com.github.liulus.yurt.convention.data.DefaultResult
import com.github.liulus.yurt.convention.data.Result
import com.github.liulus.yurt.convention.data.ServiceCode
import com.github.liulus.yurt.convention.enum.SuccessCode
import com.github.liulus.yurt.convention.exception.ServiceErrorException
import com.github.liulus.yurt.convention.exception.ServiceException


/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/10/31
 */
object Results {

    @JvmStatic
    fun <T> success(): Result<T> {
        return DefaultResult(SuccessCode.SUCCESS.code(), SuccessCode.SUCCESS.text(), true)
    }

    @JvmStatic
    fun <T> success(data: T): Result<T> {
        val result = DefaultResult<T>(SuccessCode.SUCCESS.code(), SuccessCode.SUCCESS.text(), true)
        result.data = data
        return result
    }

    /**
     *
     * 服务异常，即业务逻辑异常 是一种分支条件，或一种不能处理的状态
     * ResponseCode 的实现参考 CommonCode
     * @param serviceCode 用BaseResponseCode的好处就是强制让大家去继承并实现BaseResponseCode
     * @param <T> 对应data字段的数据类型
     * @return Result<T>
     */
    fun <T> failure(serviceCode: ServiceCode): Result<T> {
        val result = DefaultResult<T>(serviceCode.code(), serviceCode.text(), false)
        result.failure = true
        return result
    }

    fun <T> failure(serviceException: ServiceException): Result<T> {
        val result = DefaultResult<T>(serviceException.code, serviceException.message!!, false)
        result.failure = true
        return result
    }

    fun <T> invalid(serviceCode: ServiceCode): Result<T> {
        return DefaultResult(serviceCode.code(), serviceCode.text(), false)
    }

    /**
     * 根据错误码封装错误信息
     * @param serviceCode
     * @param <T>
     * @return
    </T> */
    fun <T> error(serviceCode: ServiceCode): Result<T> {
        val result = DefaultResult<T>(serviceCode.code(), serviceCode.text(), false)
        result.error = true
        return result
    }

    /**
     * 返回带异常信息的响应结果，当成系统错误（bug）来处理
     *
     * @param ex 异常
     */
    fun <T> error(ex: ServiceErrorException): Result<T> {
        val result = DefaultResult<T>(ex.code, ex.message!!, false)
        result.error = true
        result.errorStack = ex.message
        return result
    }

}