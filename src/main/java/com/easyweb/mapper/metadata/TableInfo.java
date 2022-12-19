package com.easyweb.mapper.metadata;

import com.easyweb.mapper.util.Util;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lipan on 2022/9/3.
 */
public class TableInfo {
    //表对应的实体类型
    private Class<?> entityClass;

    //表名
    private String tableName;

    //列
    private List<ColumnInfo> columns;

    //是否联合主键
    private boolean isUnionId;

    public boolean isUnionId() {
        return isUnionId;
    }

    public void setUnionId(boolean unionId) {
        isUnionId = unionId;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ColumnInfo> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnInfo> columns) {
        this.columns = columns;
    }

    public String selectColumnAsProperty(){
        return this.getColumns().stream().filter(ColumnInfo::isSelectable).map(ColumnInfo::columnAsProperty).collect(Collectors.joining(","));
    }

    public String selectColumn(){
        return this.getColumns().stream().filter(ColumnInfo::isSelectable).map(ColumnInfo::getColumn).collect(Collectors.joining(","));
    }

    public String orderByColumn(){
        return this.getColumns().stream().filter(ColumnInfo::needOrderBy).sorted(new Comparator<ColumnInfo>() {
            @Override
            public int compare(ColumnInfo o1, ColumnInfo o2) {
                return o1.getOrderByPriority() - o2.getOrderByPriority();
            }
        }).map(ColumnInfo::orderBy).collect(Collectors.joining(","));
    }

    public String[] updateSetColumn(){
        return this.getColumns().stream().filter(c->!c.isPrimaryKey()).map(ColumnInfo::updateSetColumn).toArray(String[]::new);
    }

    public String[] updateSetSelectiveColumn(Object bean){
        return this.getColumns().stream().filter(c->{
            Object value = Util.getFieldValue(bean,c.getField());
            return !c.isPrimaryKey() && value != null;
        }).map(ColumnInfo::updateSetColumn).toArray(String[]::new);
    }
}
