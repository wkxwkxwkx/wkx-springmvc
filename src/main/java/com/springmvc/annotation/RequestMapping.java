package com.springmvc.annotation;

import java.lang.annotation.*;

/**
 * @project: mvcsourcecode
 * @description: 自定义的RequestMapping注解
 * @author: 王凯旋
 * @date: 2022/7/30 13:07:28
 * @version: 1.0
 */
@Target(value ={ ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
}
