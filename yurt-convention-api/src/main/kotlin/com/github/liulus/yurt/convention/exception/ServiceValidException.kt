package com.github.liulus.yurt.convention.exception

import com.github.liulus.yurt.convention.enum.CommonCode

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/10/31
 */
class ServiceValidException : ServiceRuntimeException {

    constructor() : super()

    constructor(message: String) : super(CommonCode.INVALID_PARAM.code(), message)

    constructor(message: String, cause: Throwable) : super(CommonCode.INVALID_PARAM.code(), message, cause)

    constructor(cause: Throwable) : super(cause)

    constructor(code: String, message: String) : super(code, message)

    constructor(code: String, message: String, cause: Throwable) : super(code, message, cause)


}

