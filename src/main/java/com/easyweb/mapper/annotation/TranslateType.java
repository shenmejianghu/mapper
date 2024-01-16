package com.easyweb.mapper.annotation;

/**
 * Created by shenmejianghu on 2022/9/6.
 */
public enum TranslateType {
    DICTIONARY(0),
    ENUM(1);

    private final int type;

    private TranslateType(int type) {
        this.type = type;
    }

    public int getTranslateType() {
        return this.type;
    }
}
