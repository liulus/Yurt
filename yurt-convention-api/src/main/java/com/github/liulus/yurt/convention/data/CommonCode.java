package com.github.liulus.yurt.convention.data;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
public enum CommonCode implements ServiceCode {

    SUCCESS("1", "成功"),

    INVALID_PARAM("C_0001", "无效的参数"),
    NOT_NULL("INVALID_0001", "参数 {} 不能为null"),
    NOT_EMPTY("INVALID_0002", "参数 {} 不能为空"),

    UNKNOWN_ERROR("-1", "未知的系统错误"),
    DB_ERROR("SYS_1", "数据库异常"),
    ;

    private final String code;
    private final String text;

    CommonCode(String code, String text) {
        this.code = code;
        this.text = text;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String text() {
        return text;
    }
}
