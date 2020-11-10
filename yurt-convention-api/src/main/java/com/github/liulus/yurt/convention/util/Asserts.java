package com.github.liulus.yurt.convention.util;

import com.github.liulus.yurt.convention.data.CommonCode;
import com.github.liulus.yurt.convention.exception.ServiceValidException;

import java.util.Collection;
import java.util.Map;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/9
 */
public abstract class Asserts {

    private Asserts() {
    }

    public static void isNull(Object obj, String message, Object... args) {
        if (obj != null) {
            throw validException(message, args);
        }
    }

    public static void notNull(Object obj, String message, Object... args) {
        if (obj == null) {
            throw validException(message, args);
        }
    }

    public static void isEmpty(String obj, String message, Object... args) {
        if (!"".equals(obj)) {
            throw validException(message, args);
        }
    }

    public static void hasText(String obj, String message, Object... args) {
        if (obj == null || "".equals(obj)) {
            throw validException(message, args);
        }
    }


    public static void isEmpty(Collection<?> collection, String message, Object... args) {
        if (collection != null && !collection.isEmpty()) {
            throw validException(message, args);
        }
    }

    public static void notEmpty(Collection<?> collection, String message, Object... args) {
        if (collection == null || collection.isEmpty()) {
            throw validException(message, args);
        }
    }

    public static void isEmpty(Map<?, ?> map, String message, Object... args) {
        if (map != null && !map.isEmpty()) {
            throw validException(message, args);
        }
    }

    public static void notEmpty(Map<?, ?> map, String message, Object... args) {
        if (map == null || map.isEmpty()) {
            throw validException(message, args);
        }
    }

    public static void isEmpty(Object[] array, String message, Object... args) {
        if (array == null || array.length == 0) {
            throw validException(message, args);
        }
    }

    public static void notEmpty(Object[] array, String message, Object... args) {
        if (array != null && array.length > 0) {
            throw validException(message, args);
        }
    }


    public static void isTrue(boolean expression, String message, Object... args) {
        if (!expression) {
            throw validException(message, args);
        }
    }

    public static void isFalse(boolean expression, String message, Object... args) {
        if (expression) {
            throw validException(message, args);
        }
    }

    public void isInstanceOf(Class<?> type, Object obj, String message, Object... args) {
        if (!type.isInstance(obj)) {
            throw validException(message, args);
        }
    }


    private static ServiceValidException validException(String message, Object... args) {
        String msg = FormatUtils.formatMessage(message, args);
        return new ServiceValidException(CommonCode.INVALID_PARAM.code(), msg);
    }

}
