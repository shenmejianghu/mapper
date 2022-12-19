package com.easyweb.mapper.util;

import com.easyweb.mapper.annotation.*;
import com.easyweb.mapper.metadata.ColumnInfo;
import com.easyweb.mapper.metadata.TableInfo;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by lipan on 2022/9/3.
 */
public class Util {
    public static TableInfo tableInfo(Class<?> entityClass) {
        TableInfo info = new TableInfo();
        info.setEntityClass(entityClass);
        Table table = entityClass.getAnnotation(Table.class);
        String tableName = entityClass.getSimpleName();
        if (table != null && StringUtils.isNotEmpty(table.value())){
            tableName = table.value();
        }
        info.setTableName(tableName);
        Field[] allFields = getFields(entityClass);
        Field[] fields = Stream.of(allFields)
                //过滤@Transient注解的field
                .filter(field -> !field.isAnnotationPresent(Transient.class))
                .toArray(Field[]::new);
        List<ColumnInfo> columns = new ArrayList<>();
        int idCount = 0;
        for (Field field:fields){
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setFieldClass(field.getDeclaringClass());
            columnInfo.setField(field);
            Id id = field.getAnnotation(Id.class);
            idCount = idCount + (id == null?0:1);
            columnInfo.setPrimaryKey(id == null?Boolean.FALSE:Boolean.TRUE);
            columnInfo.setPrimaryKeyAuto(id == null?Boolean.FALSE:id.auto());
            Column column = field.getAnnotation(Column.class);
            String columnName = field.getName();
            if (column != null && StringUtils.isNotEmpty(column.value())){
                columnName = column.value();
            }
            columnInfo.setColumn(columnName);
            FilterOperator filterOperator = FilterOperator.EQ;
            if (column != null && column.filterOperator() != null){
                filterOperator = column.filterOperator();
            }
            columnInfo.setFilterOperator(filterOperator);

            if (columnInfo.isPrimaryKeyAuto()){
                columnInfo.setInsertable(false);
            }else {
                columnInfo.setInsertable(true);
                if (column != null){
                    columnInfo.setInsertable(column.insertable());
                }
            }
            columnInfo.setUpdatable(true);
            columnInfo.setSelectable(true);
            if (column != null){
                columnInfo.setSelectable(column.selectable());
                columnInfo.setUpdatable(column.updatable());
            }
            OrderBy orderBy = field.getAnnotation(OrderBy.class);
            if (orderBy != null){
                columnInfo.setOrderBy(orderBy.order());
                columnInfo.setOrderByPriority(orderBy.orderPriority());
            }
            columns.add(columnInfo);
        }
        if (idCount > 1){
            info.setUnionId(Boolean.TRUE);
        }
        info.setColumns(columns);
        return info;
    }

    public static Field[] getFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>(32);
        while (Object.class != clazz && clazz != null) {
            // 获得该类所有声明的字段，即包括public、private和protected，但是不包括父类的申明字段，
            // getFields：获得某个类的所有的公共（public）的字段，包括父类中的字段
            for (Field field : clazz.getDeclaredFields()) {
                fields.add(field);
            }
            clazz = clazz.getSuperclass();
        }
        return fields.toArray(new Field[0]);
    }

    public static Object getFieldValue(Object bean, Field field) {
        if(null == bean) {
            return null;
        }
        try {
            field.setAccessible(true);
            return field.get(bean);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } finally {
            field.setAccessible(false);
        }
    }
}
