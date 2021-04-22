package com.github.liulus.yurt.jdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liulu
 * @version V1.0
 * @since 2020/11/10
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Update {

    /**
     * 表名, 可以不写, 默认当前repository接口泛型类关联的表
     *
     * @return 表名
     */
    String table() default "";

    String[] sets() default {};

    If[] testSets() default {};

    String[] where() default {};

    If[] testWheres() default {};

}
