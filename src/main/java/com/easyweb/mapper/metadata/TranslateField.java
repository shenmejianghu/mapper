package com.easyweb.mapper.metadata;

import com.easyweb.mapper.annotation.CacheType;

import java.lang.reflect.Field;

/**
 * Created by shenmejianghu on 2023/12/31.
 */
public class TranslateField {
    //用那个字典项
    private String category;
    //要翻译的字段
    private String srcFieldName;
    private Field srcField;
    //翻译后的值赋给本字段
    private String destFieldName;
    private Field destField;
    //字典值缓存类型
    private CacheType cacheType;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSrcFieldName() {
        return srcFieldName;
    }

    public void setSrcFieldName(String srcFieldName) {
        this.srcFieldName = srcFieldName;
    }

    public Field getSrcField() {
        return srcField;
    }

    public void setSrcField(Field srcField) {
        this.srcField = srcField;
    }

    public String getDestFieldName() {
        return destFieldName;
    }

    public void setDestFieldName(String destFieldName) {
        this.destFieldName = destFieldName;
    }

    public Field getDestField() {
        return destField;
    }

    public void setDestField(Field destField) {
        this.destField = destField;
    }

    public CacheType getCacheType() {
        return cacheType;
    }

    public void setCacheType(CacheType cacheType) {
        this.cacheType = cacheType;
    }
}
