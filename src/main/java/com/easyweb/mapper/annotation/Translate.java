package com.easyweb.mapper.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.FIELD })
public @interface Translate {
    //基于内存、redis
    CacheType cacheType();
    //使用哪个字典项进行翻译
    String category();
}
