package com.github.liulus.yurt.convention.util;

import com.github.liulus.yurt.convention.data.CommonCode;
import com.github.liulus.yurt.convention.data.DefaultResult;
import com.github.liulus.yurt.convention.data.Result;
import com.github.liulus.yurt.convention.data.ServiceCode;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
public abstract class Results {

    public static Result<?> success() {
        DefaultResult<?> result = new DefaultResult<>(CommonCode.SUCCESS.code(), CommonCode.SUCCESS.text());
        result.setSuccess(true);
        return result;
    }

    public static <T> Result<T> success(T data) {
        DefaultResult<T> result = new DefaultResult<>(CommonCode.SUCCESS.code(), CommonCode.SUCCESS.text());
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    /**
     * 服务异常，即业务逻辑异常 是一种分支条件，或一种不能处理的状态
     * ResponseCode 的实现参考 CommonCode
     *
     * @param serviceCode 用BaseResponseCode的好处就是强制让大家去继承并实现BaseResponseCode
     * @return Result<T>
     */
    public static Result<?> failure(ServiceCode serviceCode) {
        DefaultResult<?> result = new DefaultResult<>(serviceCode.code(), serviceCode.text());
        result.setSuccess(false);
        result.setFailure(true);
        return result;
    }

    public static Result<?> failure(String code, String message) {
        DefaultResult<?> result = new DefaultResult<>(code, message);
        result.setSuccess(false);
        result.setFailure(true);
        return result;
    }

    public static Result<?> error(String code, String message) {
        DefaultResult<?> result = new DefaultResult<>(code, message);
        result.setSuccess(false);
        result.setError(true);
        return result;
    }

    public static Result<?> error(ServiceCode serviceCode) {
        DefaultResult<?> result = new DefaultResult<>(serviceCode.code(), serviceCode.text());
        result.setSuccess(false);
        result.setError(true);
        return result;
    }

}
