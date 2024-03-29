package com.easyweb.mapper.sqlprovider;

import com.easyweb.mapper.BaseMapper;
import com.easyweb.mapper.annotation.FilterOperator;
import com.easyweb.mapper.metadata.ColumnInfo;
import com.easyweb.mapper.metadata.PageRequest;
import com.easyweb.mapper.metadata.TableInfo;
import com.easyweb.mapper.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by shenmejianghu on 2022/9/3.
 */
public class SqlProvider<T> {
    private static Logger logger = LoggerFactory.getLogger(SqlProvider.class);
    //存储mapper和TableInfo映射关系
    private static Map<Class<?>, TableInfo> tableCache = new ConcurrentHashMap<>();
    //存储entity和TableInfo映射关系
    private static Map<Class<?>, TableInfo> entityCache = new ConcurrentHashMap<>();

    public String insert(T entity, ProviderContext context){
        TableInfo tableInfo = getTableInfo(context);
        String tableName = tableInfo.getTableName();
        String intoColumns = tableInfo.getColumns()
                .stream()
                .filter(ColumnInfo::isInsertable)
                .map(ColumnInfo::getColumn)
                .collect(Collectors.joining(","));
        String values = tableInfo.getColumns()
                .stream()
                .filter(ColumnInfo::isInsertable)
                .map(ColumnInfo::variable)
                .collect(Collectors.joining(","));
        String sql = new SQL()
                .INSERT_INTO(tableName)
                .INTO_COLUMNS(intoColumns)
                .INTO_VALUES(values).toString();
        logger.info("sql->{},params->{}",sql,entity);
        return sql;
    }

    public String batchInsert(@Param("list" ) List<?> entity, ProviderContext context){
        TableInfo tableInfo = getTableInfo(context);
        String tableName = tableInfo.getTableName();
        String intoColumns = tableInfo.getColumns()
                .stream()
                .filter(ColumnInfo::isInsertable)
                .map(ColumnInfo::getColumn)
                .collect(Collectors.joining(","));
        String values = tableInfo.getColumns()
                .stream()
                .filter(ColumnInfo::isInsertable)
                .map(column->column.variableWithPrefix("item"))
                .collect(Collectors.joining(","));
        String sql = new SQL()
                .INSERT_INTO(tableName)
                .INTO_COLUMNS(intoColumns).toString();
        sql += " values ";
        sql += "<foreach collection=\"list\" item=\"item\" separator=\",\">" +
                "  <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">" +
                "    " + values +
                "  </trim>" +
                "</foreach>";
        sql = "<script>"+sql+"</script>";
        logger.info("sql->{},params->{}",sql,entity);
        return sql;
    }

    public String deleteById(@Param("id") T entity, ProviderContext context){
        TableInfo tableInfo = getTableInfo(context);
        String tableName = tableInfo.getTableName();
        String[] where = null;
        if (tableInfo.isUnionId()){
            where = tableInfo.getColumns()
                    .stream()
                    .filter(ColumnInfo::isPrimaryKey)
                    .map(columnInfo -> columnInfo.getColumn()+" = #{id."+columnInfo.getField().getName()+"}")
                    .toArray(String[]::new);
        }else {
            where = tableInfo.getColumns()
                    .stream()
                    .filter(ColumnInfo::isPrimaryKey)
                    .map(columnInfo -> columnInfo.getColumn()+" = #{id}")
                    .toArray(String[]::new);
        }
        String sql = new SQL()
                .DELETE_FROM(tableName)
                .WHERE(where)
                .toString();
        logger.info("sql->{},params->{}",sql,entity);
        return sql;
    }

    public String deleteBatchIds(@Param("ids") Collection<?> entity, ProviderContext context){
        TableInfo tableInfo = getTableInfo(context);
        String tableName = tableInfo.getTableName();
        if (tableInfo.isUnionId()){
            String[] where = new String[entity.size()];
            for (int i = 0; i < entity.size(); i++){
                List<String> list = new ArrayList<>();
                String s = "%s=#{ids[%d].%s}";
                for (ColumnInfo columnInfo:tableInfo.getColumns()){
                    if (columnInfo.isPrimaryKey()){
                        list.add(String.format(s,columnInfo.getColumn(),i,columnInfo.getField().getName()));
                    }
                }
                where[i] = "("+StringUtils.join(list," and ")+")";
            }
            String sql = "delete from %s where %s ";
            sql = String.format(sql,tableName,StringUtils.join(where," or "));
            logger.info("sql->{},params->{}",sql,entity);
            return sql;
        }else {
            String idName = tableInfo.getColumns()
                    .stream()
                    .filter(ColumnInfo::isPrimaryKey)
                    .findFirst()
                    .get()
                    .getColumn();
            String sql = "DELETE FROM %s WHERE %s IN (%s) ";
            String[] arr = new String[entity.size()];
            for (int i = 0; i < entity.size(); i++){
                arr[i] = "#{ids["+i+"]}";
            }
            sql = String.format(sql,tableName,idName,StringUtils.join(arr,","));
            logger.info("sql->{},params->{}",sql,entity);
            return sql;
        }
    }

    public String updateById(T entity, ProviderContext context){
        TableInfo tableInfo = getTableInfo(context);
        String tableName = tableInfo.getTableName();
        String[] where = tableInfo.getColumns()
                .stream()
                .filter(ColumnInfo::isPrimaryKey)
                .map(columnInfo -> columnInfo.getColumn()+" = "+columnInfo.variable())
                .toArray(String[]::new);
        String sql = new SQL().UPDATE(tableName).SET(tableInfo.updateSetColumn()).WHERE(where).toString();
        logger.info("sql->{},params->{}",sql,entity);
        return sql;
    }

    public String updateSelectiveById(T entity, ProviderContext context){
        TableInfo tableInfo = getTableInfo(context);
        String tableName = tableInfo.getTableName();
        String[] where = tableInfo.getColumns()
                .stream()
                .filter(ColumnInfo::isPrimaryKey)
                .map(columnInfo -> columnInfo.getColumn()+" = "+columnInfo.variable())
                .toArray(String[]::new);
        String sql = new SQL().UPDATE(tableName).SET(tableInfo.updateSetSelectiveColumn(entity)).WHERE(where).toString();
        logger.info("sql->{},params->{}",sql,entity);
        return sql;
    }

    public String selectById(@Param("id")T entity, ProviderContext context){
        TableInfo tableInfo = getTableInfo(context);
        String[] where = null;
        if (tableInfo.isUnionId()){
            where = tableInfo.getColumns().stream().filter(ColumnInfo::isPrimaryKey)
                    .map(columnInfo -> columnInfo.getColumn()+" = #{id."+columnInfo.getField().getName()+"}")
                    .toArray(String[]::new);
        }else {
            where = tableInfo.getColumns().stream().filter(ColumnInfo::isPrimaryKey)
                    .map(columnInfo -> columnInfo.getColumn()+" = #{id}")
                    .toArray(String[]::new);
        }
        String sql = new SQL()
                .SELECT(tableInfo.selectColumnAsProperty())
                .FROM(tableInfo.getTableName())
                .WHERE(where)
                .toString();
        logger.info("sql->{},params->{}",sql,entity);
        return sql;
    }

    public String selectBatchIds(@Param("ids")Collection<?> entity, ProviderContext context){
        TableInfo tableInfo = getTableInfo(context);
        String tableName = tableInfo.getTableName();
        if (tableInfo.isUnionId()){
            String[] where = new String[entity.size()];
            for (int i = 0; i < entity.size(); i++){
                List<String> list = new ArrayList<>();
                String s = "%s=#{ids[%d].%s}";
                for (ColumnInfo columnInfo:tableInfo.getColumns()){
                    if (columnInfo.isPrimaryKey()){
                        list.add(String.format(s,columnInfo.getColumn(),i,columnInfo.getField().getName()));
                    }
                }
                where[i] = "("+StringUtils.join(list," and ")+")";
            }
            String sql = "select %s from %s where %s";
            sql = String.format(sql,tableInfo.selectColumnAsProperty(),tableInfo.getTableName(),StringUtils.join(where," or "));
            logger.info("sql->{},params->{}",sql,entity);
            return sql;
        }else {
            String idName = tableInfo.getColumns()
                    .stream()
                    .filter(ColumnInfo::isPrimaryKey)
                    .findFirst()
                    .get()
                    .getColumn();
            String sql = "select %s from %s where %s in (%s) ";
            String[] arr = new String[entity.size()];
            for (int i = 0; i < entity.size(); i++){
                arr[i] = "#{ids["+i+"]}";
            }
            sql = String.format(sql,tableInfo.selectColumnAsProperty(),tableName,idName,StringUtils.join(arr,","));
            logger.info("sql->{},params->{}",sql,entity);
            return sql;
        }
    }

    public String selectAll(T entity, ProviderContext context){
        TableInfo tableInfo = getTableInfo(context);
        SQL sql =  new SQL()
                .SELECT(tableInfo.selectColumnAsProperty())
                .FROM(tableInfo.getTableName());
        String orderBy = tableInfo.orderByColumn();
        if (StringUtils.isNotEmpty(orderBy)){
            sql.ORDER_BY(orderBy);
        }
        return sql.toString();
    }

    public String selectPage(PageRequest<T> entity, ProviderContext context){
        TableInfo tableInfo = getTableInfo(context);
        SQL sql = new SQL()
                .SELECT(tableInfo.selectColumnAsProperty())
                .FROM(tableInfo.getTableName());
        String[] where = tableInfo.getColumns().stream()
                .filter(column -> {
                    Field field = column.getField();
                    T bean = entity.getPageParams();
                    Object value = Util.getFieldValue(bean, field);
                    if (value == null) {
                        return false;
                    }
                    return StringUtils.isNotEmpty(value.toString());
                })
                .map(column -> {
                    String param = " #{pageParams." + column.getField().getName()+"}";
                    if (column.getFilterOperator() == FilterOperator.LIKE){
                        param = "concat('%', "+param+", '%')";
                    }
                    if (column.getFilterOperator() == FilterOperator.LEFTLIKE){
                        param = "concat("+param+", '%')";
                    }
                    if (column.getFilterOperator() == FilterOperator.RIGHTLIKE){
                        param = "concat('%', "+param+")";
                    }
                    return column.getColumn()+column.filterOperator()+param;
                })
                .toArray(String[]::new);
        sql.WHERE(where);
        if (StringUtils.isNotEmpty(entity.getOrder())){
            ColumnInfo columnInfo = tableInfo.getColumns().stream()
                    .filter(columnInfo1 -> columnInfo1.getField().getName().equalsIgnoreCase(entity.getOrder()))
                    .findFirst().orElse(null);
            if (columnInfo != null){
                String direction = entity.getOrderDirection();
                direction = (StringUtils.isEmpty(direction) || direction.equalsIgnoreCase("asc"))?" asc ":" desc ";
                sql.ORDER_BY(columnInfo.getColumn() + direction);
            }
        }else {
            String orderBy = tableInfo.orderByColumn();
            if (StringUtils.isNotEmpty(orderBy)){
                sql.ORDER_BY(orderBy);
            }
        }
        sql.OFFSET("#{offset}").LIMIT("#{pageSize}");
        String s = sql.toString();
        logger.info("sql->{},params->{}",s,entity);
        return s;
    }

    public String selectCount(T entity, ProviderContext context){
        TableInfo tableInfo = getTableInfo(context);
        SQL sql = new SQL()
                .SELECT("count(1)")
                .FROM(tableInfo.getTableName());
        String[] where = tableInfo.getColumns().stream()
                .filter(column -> {
                    Field field = column.getField();
                    Object value = Util.getFieldValue(entity, field);
                    if (value == null) {
                        return false;
                    }
                    return StringUtils.isNotEmpty(value.toString());
                })
                .map(column -> {
                    String param = " #{" + column.getField().getName()+"}";
                    if (column.getFilterOperator() == FilterOperator.LIKE){
                        param = "concat('%', "+param+", '%')";
                    }
                    if (column.getFilterOperator() == FilterOperator.LEFTLIKE){
                        param = "concat("+param+", '%')";
                    }
                    if (column.getFilterOperator() == FilterOperator.RIGHTLIKE){
                        param = "concat('%', "+param+")";
                    }
                    return column.getColumn()+column.filterOperator()+param;
                })
                .toArray(String[]::new);
        sql.WHERE(where);
        String s = sql.toString();
        logger.info("sql->{},params->{}",s,entity);
        return s;
    }

    private TableInfo getTableInfo(ProviderContext context){
        Class<?> clz = getEntityType(context);
        TableInfo tableInfo = entityCache.get(clz);
        if (tableInfo == null){
            tableInfo = tableCache.get(context.getMapperType());
        }
        if (tableInfo == null){
            tableInfo = Util.tableInfo(clz);
        }
        entityCache.putIfAbsent(clz,tableInfo);
        tableCache.putIfAbsent(context.getMapperType(),tableInfo);
        return tableInfo;
    }

    public static TableInfo getTableInfoByEntity(Class<?> clz){
        return entityCache.get(clz);
    }

    private Class<?> getEntityType(ProviderContext context) {
        return Stream.of(context.getMapperType().getGenericInterfaces())
                .filter(ParameterizedType.class::isInstance)
                .map(ParameterizedType.class::cast)
                .filter(type -> type.getRawType() == BaseMapper.class)
                .findFirst()
                .map(type -> type.getActualTypeArguments()[0])
                .filter(Class.class::isInstance)
                .map(Class.class::cast)
                .orElseThrow(() -> new IllegalStateException("未找到BaseMapper的泛型类 " + context.getMapperType().getName() + "."));
    }


}
