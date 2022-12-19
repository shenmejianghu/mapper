package com.easyweb.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by shenmejianghu on 2022/9/5.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    //对应数据库列名
    String value() default "";
    //过滤类型
    FilterOperator filterOperator() default FilterOperator.EQ;
    //是否查询
    boolean selectable() default true;
    //是否插入
    boolean insertable() default true;
    //是否更新
    boolean updatable() default true;
}
