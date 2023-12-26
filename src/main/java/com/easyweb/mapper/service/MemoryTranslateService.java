package com.easyweb.mapper.service;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by shenmejianghu on 2023/12/15.
 */
public class MemoryTranslateService {
    private static ConcurrentHashMap<String,Map<String,String>> dictionaryData = new ConcurrentHashMap<>();

    public MemoryTranslateService(IDictionaryLoader loader){
        Map<String,Map<String,String>> map = loader.loadAllDictionaryData();
        dictionaryData.putAll(map);
    }

    public void refreshByCategory(String category,Map<String,String> codes){
        dictionaryData.put(category,codes);
    }

    public void refreshByCategoryAndCode(String category,String code,String value){
        Map<String,String> codes = dictionaryData.get(category);
        if (codes == null){
            codes = new HashMap<>();
        }
        codes.put(code,value);
        dictionaryData.put(category,codes);
    }

    public static Map<String,String> getByCategory(String category){
        return dictionaryData.get(category);
    }

    public static String getByCategoryAndCode(String category,String code){
        Map<String,String> map = dictionaryData.get(category);
        if (map != null){
            return map.get(code);
        }
        return null;
    }

}

