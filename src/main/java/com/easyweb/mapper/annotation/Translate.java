package com.easyweb.mapper.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.FIELD })
public @interface Translate {
    //基于字典、枚举
    TranslateType translateType();
    //使用哪个字典项进行翻译
    String category();
    //使用枚举中的哪个字段
    String enumKey();
    //对应哪个枚举类型
    Class<?> enumClass();
}
