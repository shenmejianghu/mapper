package com.easyweb.mapper.metadata;

import com.easyweb.mapper.annotation.CacheType;

/**
 * Created by shenmejianghu on 2023/12/31.
 */
public class TranslateField {
    //要翻译的字段
    private String srcField;
    //翻译后的值赋给本字段
    private String destField;
    //字典值缓存类型
    private CacheType cacheType;

    public String getSrcField() {
        return srcField;
    }

    public void setSrcField(String srcField) {
        this.srcField = srcField;
    }

    public String getDestField() {
        return destField;
    }

    public void setDestField(String destField) {
        this.destField = destField;
    }

    public CacheType getCacheType() {
        return cacheType;
    }

    public void setCacheType(CacheType cacheType) {
        this.cacheType = cacheType;
    }
}
