package com.easyweb.mapper.annotation;

/**
 * Created by shenmejianghu on 2022/9/6.
 */
public enum FilterOperator {
    EQ(0),
    NEQ(1),
    LIKE(2),
    GT(3),
    LT(4),
    GTE(5),
    LTE(6),
    LEFTLIKE(7),
    RIGHTLIKE(8);

    private final int type;

    private FilterOperator(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
