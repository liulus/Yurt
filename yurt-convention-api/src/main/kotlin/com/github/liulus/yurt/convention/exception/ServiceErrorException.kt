package com.github.liulus.yurt.convention.exception

import com.github.liulus.yurt.convention.enum.ErrorCode

/**
 * 系统级异常
 * 这种异常表示是bug或者系统出现故障了，已经解决不了的只能往外抛的异常
 *
 * @author liulu
 * @version V1.0
 * @since 2020/10/31
 */
class ServiceErrorException : ServiceRuntimeException {

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)

    constructor(code: String, message: String) : super(code, message)

    constructor(code: String, message: String, cause: Throwable) : super(code, message, cause)

    constructor(respCode: ErrorCode) : super(respCode.code(), respCode.text())

    constructor(respCode: ErrorCode, cause: Throwable) : super(respCode.code(), respCode.text(), cause)
}
