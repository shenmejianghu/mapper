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
    //查询过滤类型
    FilterOperator filterOperator() default FilterOperator.EQ;
    //是否查询，select是否带上该字段
    boolean selectable() default true;
    //是否插入，insert是否带上该字段
    boolean insertable() default true;
    //是否更新，update是否带上该字段
    boolean updatable() default true;
}
