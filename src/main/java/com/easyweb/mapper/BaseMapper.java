package com.easyweb.mapper;

import com.easyweb.mapper.metadata.PageRequest;
import com.easyweb.mapper.sqlprovider.SqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.Collection;
import java.util.List;

/**
 * Created by shenmejianghu on 2022/9/3.
 */
public interface BaseMapper<T,K> {

    @InsertProvider(type = SqlProvider.class,method = "insert")
    @Options(useGeneratedKeys = true, keyProperty = "id",keyColumn = "id")
    int insert(T t);

    @InsertProvider(type = SqlProvider.class,method = "batchInsert")
    int batchInsert(@Param("list") List<T> entity);

    @DeleteProvider(type = SqlProvider.class,method = "deleteById")
    int deleteById(@Param("id") K id);

    @DeleteProvider(type = SqlProvider.class,method = "deleteBatchIds")
    int deleteBatchIds(@Param("ids") Collection<K> ids);

    @UpdateProvider(type = SqlProvider.class,method = "updateById")
    int updateById(T entity);

    @UpdateProvider(type = SqlProvider.class,method = "updateSelectiveById")
    int updateSelectiveById(T entity);

    @SelectProvider(type = SqlProvider.class,method = "selectById")
    T selectById(@Param("id") K id);

    @SelectProvider(type = SqlProvider.class,method = "selectBatchIds")
    List<T> selectBatchIds(@Param("ids") Collection<K> ids);

    @SelectProvider(type = SqlProvider.class,method = "selectAll")
    List<T> selectAll();

    @SelectProvider(type = SqlProvider.class,method = "selectPage")
    List<T> selectPage(PageRequest<T> pageRequest);

    @SelectProvider(type = SqlProvider.class,method = "selectCount")
    Long selectCount(T entity);

}
