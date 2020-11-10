package com.github.liulus.yurt.jdbc.annotation;

import java.lang.annotation.Documented;
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
@Target({})
public @interface If {

    String value();

    String test() default "";

}
