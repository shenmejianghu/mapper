package com.easyweb.mapper.interceptor;

import com.easyweb.mapper.metadata.TableInfo;
import com.easyweb.mapper.sqlprovider.SqlProvider;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

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
                if (!((List)result).isEmpty()){
                    Class<?> clz = ((List)result).get(0).getClass();
                    // TODO: 2023/11/21 找到实体类需要翻译的字段
                    TableInfo tableInfo = SqlProvider.getTableInfoByEntity(clz);
                    if (tableInfo != null){

                    }
                }
            }else {
                Class<?> clz = result.getClass();
                // TODO: 2023/11/21 找到实体类需要翻译的字段
                TableInfo tableInfo = SqlProvider.getTableInfoByEntity(clz);
                if (tableInfo != null){

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
