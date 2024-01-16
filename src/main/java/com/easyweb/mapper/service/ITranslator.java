package com.easyweb.mapper.service;

import java.util.Map;

/**
 * Created by shenmejianghu on 2024/1/16.
 */
public interface ITranslator {
    void refreshByCategory(String category,Map<String,String> codes);
    void refreshByCategoryAndCode(String category,String code,String value);
    Map<String,String> getByCategory(String category);
    String getByCategoryAndCode(String category,String code);
}
