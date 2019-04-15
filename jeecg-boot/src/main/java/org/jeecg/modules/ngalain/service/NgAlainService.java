package org.jeecg.modules.ngalain.service;

import com.alibaba.fastjson.JSONArray;

import java.util.List;
import java.util.Map;

public interface NgAlainService {
    public JSONArray getMenu(String id) throws Exception;
    public JSONArray getJeecgMenu(String id) throws Exception;
    public List<Map<String, String>> getDictByTable(String table, String key, String value);
}
