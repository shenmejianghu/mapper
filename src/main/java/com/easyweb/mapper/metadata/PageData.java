package com.easyweb.mapper.metadata;

import java.util.List;

/**
 * Created by lipan on 2022/12/18.
 */
public class PageData<T> {
    private List<T> rows;
    private Long total;

    public PageData(List<T> rows, Long total) {
        this.rows = rows;
        this.total = total;
    }

    public static <T> PageData<T> of(List<T> rows, Long total){
        return new PageData<>(rows,total);
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
