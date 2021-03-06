package com.github.liulus.yurt.convention.data;

import com.github.liulus.yurt.convention.exception.ServiceErrorException;
import com.github.liulus.yurt.convention.exception.ServiceException;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
public interface ServiceCode extends StatusCode {

    default ServiceException failure() {
        return new ServiceException(this);
    }

    default ServiceException failure(Throwable cause) {
        return new ServiceException(this, cause);
    }

    default ServiceErrorException error() {
        return new ServiceErrorException(this);
    }

    default ServiceErrorException error(Throwable cause) {
        return new ServiceErrorException(this, cause);
    }


}
