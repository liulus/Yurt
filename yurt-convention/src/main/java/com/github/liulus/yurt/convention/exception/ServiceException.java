package com.github.liulus.yurt.convention.exception;

import com.github.liulus.yurt.convention.data.ServiceCode;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
public class ServiceException extends ServiceRuntimeException {

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(String code, String message) {
        super(code, message);
    }

    public ServiceException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ServiceException(ServiceCode serviceCode) {
        super(serviceCode);
    }

    public ServiceException(ServiceCode serviceCode, Throwable cause) {
        super(serviceCode, cause);
    }

}
