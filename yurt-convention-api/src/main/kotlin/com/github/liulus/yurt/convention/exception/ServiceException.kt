package com.github.liulus.yurt.convention.exception

import com.github.liulus.yurt.convention.data.ServiceCode

/**
 * 业务系统业务逻辑相关异常
 * @author liulu
 * @version V1.0
 * @since 2020/10/31
 */
class ServiceException : ServiceRuntimeException {


    constructor() : super()

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)

    constructor(code: String, message: String) : super(code, message)

    constructor(code: String, message: String, cause: Throwable) : super(code, message, cause)

    constructor(respCode: ServiceCode) : super(respCode.code(), respCode.text())

    constructor(respCode: ServiceCode, cause: Throwable) : super(respCode.code(), respCode.text(), cause)

    fun getDetailMessage(): String {
        return toString()
    }

    override fun toString(): String {
        return "ServiceException{" +
                "code='" + code + '\'' +
                "message='" + message + '\'' +
                '}'
    }

    companion object {
        private const val serialVersionUID = -8667394300356618037L
    }
}