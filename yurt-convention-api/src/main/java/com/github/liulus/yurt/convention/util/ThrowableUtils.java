package com.github.liulus.yurt.convention.util;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/16
 */
public abstract class ThrowableUtils {

    private ThrowableUtils() {
    }


    public static String buildErrorMessage(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; ex != null && i < 5; i++) {
            if (sb.length() > 0) {
                sb.append(" caused by: ");
            }
            sb.append(ex.getMessage());
            ex = ex.getCause();
        }
        return sb.toString();
    }


    public static <T extends Exception> T findException(Class<T> exClass, Throwable ex) {
        for (int i = 0; ex != null && i < 5; i++) {
            if (exClass.isAssignableFrom(ex.getClass())) {
                //noinspection unchecked
                return (T) ex;
            }
            ex = ex.getCause();
        }
        return null;
    }


}
