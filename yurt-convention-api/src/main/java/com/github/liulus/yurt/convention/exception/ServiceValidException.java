package com.github.liulus.yurt.convention.exception;

import com.github.liulus.yurt.convention.data.ServiceCode;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
public class ServiceValidException extends ServiceRuntimeException {

    public ServiceValidException() {
        super();
    }

    protected ServiceValidException(String message) {
        super(message);
    }

    protected ServiceValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceValidException(String code, String message) {
        super(code, message);
    }

    public ServiceValidException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ServiceValidException(ServiceCode serviceCode) {
        super(serviceCode);
    }

    public ServiceValidException(ServiceCode serviceCode, Throwable cause) {
        super(serviceCode, cause);
    }
    
}
