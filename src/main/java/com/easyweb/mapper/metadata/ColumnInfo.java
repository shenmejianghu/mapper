package com.easyweb.mapper.metadata;

import com.easyweb.mapper.annotation.FilterOperator;
import com.easyweb.mapper.annotation.Order;

import java.lang.reflect.Field;

/**
 * Created by lipan on 2022/9/6.
 */
public class ColumnInfo {
    //对应的java类型
    private Class<?> fieldClass;
    private Field field;
    private FilterOperator filterOperator;
    //数据库列
    private String column;
    //是否主键
    private boolean isPrimaryKey;
    //主键填充方式
    private boolean isPrimaryKeyAuto;
    //排序
    private Order orderBy;
    private int orderByPriority;
    //是否参与insert
    private boolean insertable;
    //是否参与update
    private boolean updatable;
    //是否参与select
    private boolean selectable;

    public Class<?> getFieldClass() {
        return fieldClass;
    }

    public void setFieldClass(Class<?> fieldClass) {
        this.fieldClass = fieldClass;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public FilterOperator getFilterOperator() {
        return filterOperator;
    }

    public void setFilterOperator(FilterOperator filterOperator) {
        this.filterOperator = filterOperator;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public boolean isPrimaryKeyAuto() {
        return isPrimaryKeyAuto;
    }

    public void setPrimaryKeyAuto(boolean primaryKeyAuto) {
        isPrimaryKeyAuto = primaryKeyAuto;
    }

    public Order getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Order orderBy) {
        this.orderBy = orderBy;
    }

    public int getOrderByPriority() {
        return orderByPriority;
    }

    public void setOrderByPriority(int orderByPriority) {
        this.orderByPriority = orderByPriority;
    }

    public boolean isInsertable() {
        return insertable;
    }

    public void setInsertable(boolean insertable) {
        this.insertable = insertable;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public String variable(){
        return "#{"+field.getName()+"}";
    }

    public String variableWithPrefix(String prefix){
        return "#{"+prefix+"."+field.getName()+"}";
    }

    public String columnAsProperty(){
        return column + " AS " + field.getName();
    }

    public boolean needOrderBy(){
        return orderBy!=null;
    }

    public String orderBy(){
        return column + " " +orderBy.toString();
    }

    public String updateSetColumn(){
        return column + " = "+variable();
    }

    public String filterOperator(){
        String ret = "";
        switch (filterOperator){
            case EQ:
                ret = " = ";
                break;
            case NEQ:
                ret = " != ";
                break;
            case LIKE:
                ret = " like ";
                break;
            case GT:
                ret = " > ";
                break;
            case LT:
                ret = " < ";
                break;
            case GTE:
                ret = " >= ";
                break;
            case LTE:
                ret = " <= ";
                break;
            case LEFTLIKE:
                ret = " like ";
                break;
            case RIGHTLIKE:
                ret = " like ";
                break;
            default :
                ret = " = ";
        }
        return ret;
    }
}
