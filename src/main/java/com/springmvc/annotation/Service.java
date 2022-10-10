package com.springmvc.annotation;

import java.lang.annotation.*;

/**
 * @project: mvcsourcecode
 * @description: 自定义Service注解
 * @author: 王凯旋
 * @date: 2022/7/30 13:02:18
 * @version: 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value() default "";
}
