package com.easyweb.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by shenmejianghu on 2022/9/6.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface OrderBy {
    //排序
    Order order() default Order.ASC;
    //多个排序字段先后顺序
    int orderPriority() default 0;
}
