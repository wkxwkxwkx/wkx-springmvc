package com.springmvc.annotation;

import java.lang.annotation.*;

/**
 * @project: mvcsourcecode
 * @description: 自定义的AutoWired注解
 * @author: 王凯旋
 * @date: 2022/7/30 13:05:39
 * @version: 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoWired {
    String value() default "";
}
