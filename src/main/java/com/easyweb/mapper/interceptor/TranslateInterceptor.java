package com.easyweb.mapper.interceptor;

import com.easyweb.mapper.annotation.CacheType;
import com.easyweb.mapper.metadata.ColumnInfo;
import com.easyweb.mapper.metadata.TableInfo;
import com.easyweb.mapper.metadata.TranslateField;
import com.easyweb.mapper.service.MemoryTranslateService;
import com.easyweb.mapper.sqlprovider.SqlProvider;
import com.easyweb.mapper.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Created by shenmejianghu on 2023/11/21.
 */
@Intercepts({
        @Signature(
                type = ResultSetHandler.class,
                method = "handleResultSets",
                args = {Statement.class})
})
public class TranslateInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();//被代理对象
        Method method = invocation.getMethod();//代理方法
        Object[] args = invocation.getArgs();//方法参数
        Object result = invocation.proceed();
        if (result != null){
            if (result instanceof List){
                List list = (List)result;
                if (!list.isEmpty()){
                    Class<?> clz = list.get(0).getClass();
                    TableInfo tableInfo = SqlProvider.getTableInfoByEntity(clz);
                    if (tableInfo != null){
                        List<ColumnInfo> translateColumnList = tableInfo
                                .getColumns()
                                .stream()
                                .filter(columnInfo -> columnInfo.getTranslateField()!=null)
                                .collect(Collectors.toList());
                        if (translateColumnList != null && !translateColumnList.isEmpty()){
                            for (Object data:list){
                                for (ColumnInfo columnInfo:translateColumnList){
                                    TranslateField translateField = columnInfo.getTranslateField();
                                    Field srcField = translateField.getSrcField();
                                    Field destField = translateField.getDestField();
                                    CacheType cacheType = translateField.getCacheType();
                                    Object srcFieldValue = Util.getFieldValue(data,srcField);
                                    if (srcFieldValue != null){
                                        Object translateValue = null;
                                        if (cacheType == CacheType.MEMORY){
                                            translateValue = MemoryTranslateService.getByCategoryAndCode(translateField.getCategory(),srcFieldValue.toString());
                                        }
                                        if (cacheType == CacheType.REDIS){

                                        }
                                        if (translateValue != null && StringUtils.isNotEmpty(translateValue.toString())){
                                            Util.setFieldValue(data,destField,translateValue);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }else {
                Class<?> clz = result.getClass();
                TableInfo tableInfo = SqlProvider.getTableInfoByEntity(clz);
                if (tableInfo != null){
                    List<ColumnInfo> translateColumnList = tableInfo
                            .getColumns()
                            .stream()
                            .filter(columnInfo -> columnInfo.getTranslateField()!=null)
                            .collect(Collectors.toList());
                    if (translateColumnList != null && !translateColumnList.isEmpty()){
                        for (ColumnInfo columnInfo:translateColumnList){
                            TranslateField translateField = columnInfo.getTranslateField();
                            Field srcField = translateField.getSrcField();
                            Field destField = translateField.getDestField();
                            CacheType cacheType = translateField.getCacheType();
                            Object srcFieldValue = Util.getFieldValue(result,srcField);
                            if (srcFieldValue != null){
                                Object translateValue = null;
                                if (cacheType == CacheType.MEMORY){
                                    translateValue = MemoryTranslateService.getByCategoryAndCode(translateField.getCategory(),srcFieldValue.toString());
                                }
                                if (cacheType == CacheType.REDIS){

                                }
                                if (translateValue != null && StringUtils.isNotEmpty(translateValue.toString())){
                                    Util.setFieldValue(result,destField,translateValue);
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Object plugin(Object target) {
        //只对要拦截的对象生成代理
        if(target instanceof ResultSetHandler){
            //调用插件
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
