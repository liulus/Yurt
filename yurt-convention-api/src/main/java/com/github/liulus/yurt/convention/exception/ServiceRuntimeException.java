package com.github.liulus.yurt.convention.exception;

import com.github.liulus.yurt.convention.data.CommonCode;
import com.github.liulus.yurt.convention.data.ServiceCode;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
abstract class ServiceRuntimeException extends RuntimeException {

    private String code;

    protected ServiceRuntimeException() {
        super();
        this.code = CommonCode.UNKNOWN_ERROR.code();
    }

    protected ServiceRuntimeException(String message) {
        super(message);
        this.code = CommonCode.UNKNOWN_ERROR.code();
    }

    protected ServiceRuntimeException(String message, Throwable cause) {
        super(message, cause);
        this.code = CommonCode.UNKNOWN_ERROR.code();
    }

    protected ServiceRuntimeException(String code, String message) {
        super(message);
        this.code = code;
    }

    protected ServiceRuntimeException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    protected ServiceRuntimeException(ServiceCode serviceCode) {
        super(serviceCode.text());
        this.code = serviceCode.code();
    }

    protected ServiceRuntimeException(ServiceCode serviceCode, Throwable cause) {
        super(serviceCode.text(), cause);
        this.code = serviceCode.code();
    }

    public String getCode() {
        return code;
    }
}
