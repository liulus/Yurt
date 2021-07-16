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
public @interface Select {

    String NOT_DELETED = "NOT_DELETED";

    String COUNT = "count(*)";

    boolean distinct() default false;

    String[] columns() default {};

    String from() default "";

    String join() default "";

    If[] testJoin() default {};

    String innerJoin() default "";

    String outerJoin() default "";

    String leftJoin() default "";

    If[] testLeftJoin() default {};

    String rightJoin() default "";

    String[] where() default {};

    If[] testWheres() default {};

    String orderBy() default "";

    String groupBy() default "";

    String[] having() default {};

}
