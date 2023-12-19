package com.easyweb.mapper.annotation;

/**
 * Created by shenmejianghu on 2022/9/6.
 */
public enum CacheType {
    MEMORY(0),
    REDIS(1);

    private final int type;

    private CacheType(int type) {
        this.type = type;
    }

    public int getCacheType() {
        return this.type;
    }
}
