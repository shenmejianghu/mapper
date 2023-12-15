package com.easyweb.mapper.service;

import java.util.Map;

/**
 * Created by shenmejianghu on 2023/12/15.
 */
public interface IDictionaryLoader {
    Map<String,Map<String,String>> loadAllDictionaryData();
}
