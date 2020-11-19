package com.github.liulus.yurt.convention.exception;

import com.github.liulus.yurt.convention.data.ServiceCode;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
public class ServiceErrorException extends ServiceRuntimeException{

    public ServiceErrorException() {
        super();
    }

    public ServiceErrorException(String message) {
        super(message);
    }

    public ServiceErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceErrorException(String code, String message) {
        super(code, message);
    }

    public ServiceErrorException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ServiceErrorException(ServiceCode serviceCode) {
        super(serviceCode);
    }

    public ServiceErrorException(ServiceCode serviceCode, Throwable cause) {
        super(serviceCode, cause);
    }
    
}
