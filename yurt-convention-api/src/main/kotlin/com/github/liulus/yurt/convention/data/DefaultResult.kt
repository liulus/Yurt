package com.github.liulus.yurt.convention.data

data class DefaultResult<T>(
        override var code: String,
        override val message: String,
        override val success: Boolean,
) : Result<T> {

    override var errorStack: String? = null
    override var data: T? = null
    override var error: Boolean? = null
    override var failure: Boolean? = null





}