package com.github.liulus.yurt.convention.data;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
public interface Result<T> {
    /**
     * 获取错误码
     *
     * @return 错误码
     */
    String getCode();

    /**
     * 获取成功或错误的信息
     *
     * @return 成功或错误的信息
     */
    String getMessage();

    /**
     * 获取错误的异常堆栈
     *
     * @return 错误的异常堆栈
     */
    String getErrorStack();

    /**
     * 获取数据
     *
     * @return 数据
     */
    T getData();

    /**
     * 是否成功
     *
     * @return boolean
     */
    boolean isSuccess();

    /**
     * 是否是业务处理失败，业务异常
     *
     * @return boolean
     */
    boolean isFailure();

    /**
     * 是否错误
     *
     * @return boolean
     */
    boolean isError();


}
