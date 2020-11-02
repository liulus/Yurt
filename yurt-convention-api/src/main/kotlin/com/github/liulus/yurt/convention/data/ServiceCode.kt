package com.github.liulus.yurt.convention.data

import com.github.liulus.yurt.convention.exception.ServiceException

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/10/31
 */
interface ServiceCode : StatusCode {
    /**
     * 返回服务异常
     * @return Service Exception
     */
    fun failure(): ServiceException {
        return ServiceException(this)
    }

    /**
     * 返回服务异常
     * @param cause 原始异常
     * @return Service Exception
     */
    fun failure(cause: Throwable): ServiceException {
        return ServiceException(this, cause)
    }
}