package org.jeecg.modules.ngalain.service;

import com.alibaba.fastjson.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * @Description: NgAlainService接口
 * @author: jeecg-boot
 */
public interface NgAlainService {
    /**
     * 菜单
     * @param id
     * @return JSONArray
     * @throws Exception
     */
    JSONArray getMenu(String id) throws Exception;

    /**
     * jeecg菜单
     * @param id
     * @return JSONArray
     */
    JSONArray getJeecgMenu(String id);

    /**
     * 获取字典值
     * @param table
     * @param key
     * @param value
     * @return List<Map<String, String>>
     */
    List<Map<String, String>> getDictByTable(String table, String key, String value);
}
