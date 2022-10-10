package com.springmvc.annotation;

import java.lang.annotation.*;

/**
 * @project: mvcsourcecode
 * @description: 自定义的RequestParam注解
 * @author: 王凯旋
 * @date: 2022/7/30 13:09:44
 * @version: 1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    String value() default "";
}
