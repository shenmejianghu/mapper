package com.easyweb.mapper.metadata;

import com.easyweb.mapper.annotation.TranslateType;

import java.lang.reflect.Field;

/**
 * Created by shenmejianghu on 2023/12/31.
 */
public class TranslateField {
    //翻译类型为字典时：用哪个字典项
    private String category;
    //翻译类型为枚举时：用枚举中哪个字段
    private String enumKey;
    //要翻译的字段
    private String srcFieldName;
    private Field srcField;
    //翻译后的值赋给本字段
    private String destFieldName;
    private Field destField;
    //字典值缓存类型
    private TranslateType translateType;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEnumKey() {
        return enumKey;
    }

    public void setEnumKey(String enumKey) {
        this.enumKey = enumKey;
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

    public TranslateType getTranslateType() {
        return translateType;
    }

    public void setTranslateType(TranslateType translateType) {
        this.translateType = translateType;
    }
}
