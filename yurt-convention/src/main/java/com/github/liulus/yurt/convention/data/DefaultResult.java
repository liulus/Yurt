package com.github.liulus.yurt.convention.data;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
public class DefaultResult<T> implements Result<T> {

    private String code;
    private T data;
    private String message;
    private String errorStack;
    private boolean success;
    private boolean failure;
    private boolean error;

    public DefaultResult() {
    }

    public DefaultResult(T data) {
        this.data = data;
        this.success = true;
    }

    public DefaultResult(String code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getErrorStack() {
        return errorStack;
    }

    public void setErrorStack(String errorStack) {
        this.errorStack = errorStack;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public boolean isFailure() {
        return failure;
    }

    public void setFailure(boolean failure) {
        this.failure = failure;
    }

    @Override
    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
