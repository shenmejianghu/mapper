package com.easyweb.mapper.annotation;

/**
 * Created by lipan on 2022/9/6.
 */
public enum Order {
    ASC(0),
    DESC(1);

    private final int type;

    private Order(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
