package com.easyweb.mapper.metadata;

/**
 * Created by shenmejianghu on 2022/9/5.
 */
public class PageRequest<T> {
    private Integer pageIndex;
    private Integer pageSize;
    private Integer offset;
    private String order;
    private String orderDirection;
    private T pageParams;

    public PageRequest(Integer pageIndex, Integer pageSize, String order,String orderDirection,T pageParams) {
        this.pageIndex = (pageIndex == null || pageIndex < 0)?0:pageIndex;
        this.pageSize = (pageSize == null || pageSize < 0)?10:pageSize;
        this.offset = (this.pageIndex - 1)*this.pageSize;
        this.order = order;
        this.orderDirection = orderDirection;
        this.pageParams = pageParams;
    }

    public static <T> PageRequest<T> of (Integer pageIndex, Integer pageSize, T pageParams){
        return new PageRequest<>(pageIndex,pageSize,null,null,pageParams);
    }
    public static <T> PageRequest<T> of (Integer pageIndex, Integer pageSize, String order,String orderDirection,T pageParams){
        return new PageRequest<>(pageIndex,pageSize,order,orderDirection,pageParams);
    }

    public static <T> PageRequest<T> of (Paging paging,T pageParams){
        return new PageRequest<>(paging.getPageNum(),paging.getPageSize(),paging.getOrder(),paging.getOrderDirection(),pageParams);
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public T getPageParams() {
        return pageParams;
    }

    public void setPageParams(T pageParams) {
        this.pageParams = pageParams;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderDirection() {
        return orderDirection;
    }

    public void setOrderDirection(String orderDirection) {
        this.orderDirection = orderDirection;
    }
}
