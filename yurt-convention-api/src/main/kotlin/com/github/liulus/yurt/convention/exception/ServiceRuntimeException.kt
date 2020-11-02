package com.github.liulus.yurt.convention.exception

import com.github.liulus.yurt.convention.enum.ErrorCode

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/10/31
 */
abstract class ServiceRuntimeException : RuntimeException {

    var code: String = ErrorCode.UNKNOWN_ERROR.code()

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause) {
        if (cause is ServiceException) {
            code = cause.code
        }
    }

    constructor(cause: Throwable) : super(cause) {
        if (cause is ServiceException) {
            code = cause.code
        }
    }

    constructor(code: String, message: String) : super(message) {
        this.code = code
    }

    constructor(code: String, message: String, cause: Throwable) : super(message, cause) {
        this.code = code
    }


}


