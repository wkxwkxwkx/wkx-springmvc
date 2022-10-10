package com.springmvc.annotation;

import java.lang.annotation.*;

/**
 * @project: mvcsourcecode
 * @description: 自定义Controller注解
 * @author: 王凯旋
 * @date: 2022/7/30 13:04:53
 * @version: 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
    String value() default "";
}
