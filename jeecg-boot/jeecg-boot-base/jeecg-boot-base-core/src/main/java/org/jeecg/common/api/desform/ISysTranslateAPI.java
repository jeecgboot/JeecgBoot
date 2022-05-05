package org.jeecg.common.api.desform;

import org.jeecg.common.system.vo.DictModel;

import java.util.List;
import java.util.Map;

/**
 * 表单设计器【System】翻译API接口
 *
 * @author sunjianlei
 */
public interface ISysTranslateAPI {

    /**
     * 查询分类字典翻译
     */
    List<String> categoryLoadDictItem(String ids);

    /**
     * 根据字典code加载字典text
     *
     * @param dictCode 顺序：tableName,text,code
     * @param keys     要查询的key
     * @return
     */
    List<String> dictLoadDictItem(String dictCode, String keys);

    /**
     * 获取字典数据
     *
     * @param dictCode 顺序：tableName,text,code
     * @param dictCode 要查询的key
     * @return
     */
    List<DictModel> dictGetDictItems(String dictCode);

    /**
     * 【JSearchSelectTag下拉搜索组件专用接口】
     * 大数据量的字典表 走异步加载  即前端输入内容过滤数据
     *
     * @param dictCode 字典code格式：table,text,code
     * @return
     */
    List<DictModel> dictLoadDict(String dictCode, String keyword, Integer pageSize);

}
