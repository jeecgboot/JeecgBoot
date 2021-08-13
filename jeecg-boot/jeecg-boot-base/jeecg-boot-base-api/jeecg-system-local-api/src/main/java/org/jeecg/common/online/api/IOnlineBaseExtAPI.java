package org.jeecg.common.online.api;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.system.vo.DictModel;

import java.util.List;
import java.util.Map;

/**
 * 表单设计器【Online】翻译API接口
 *
 * @author sunjianlei
 */
public interface IOnlineBaseExtAPI {

    /**
     * 【Online】 表单设计器专用：同步新增
     */
    String cgformPostCrazyForm(String tableName, JSONObject jsonObject) throws Exception;

    /**
     * 【Online】 表单设计器专用：同步编辑
     */
    String cgformPutCrazyForm(String tableName, JSONObject jsonObject) throws Exception;

    /**
     * online表单删除数据
     *
     * @param cgformCode Online表单code
     * @param dataIds    数据ID，可逗号分割
     * @return
     */
    String cgformDeleteDataByCode(String cgformCode, String dataIds);

    /**
     * 通过online表名查询数据，同时查询出子表的数据
     *
     * @param tableName online表名
     * @param dataIds   online数据ID
     * @return
     */
    JSONObject cgformQueryAllDataByTableName(String tableName, String dataIds);

    /**
     * 对 cgreportGetData 的返回值做优化，封装 DictModel 集合
     *
     * @return
     */
    List<DictModel> cgreportGetDataPackage(String code, String dictText, String dictCode, String dataList);

    /**
     * 【cgreport】通过 head code 获取 sql语句，并执行该语句返回查询数据
     *
     * @param code     报表Code，如果没传ID就通过code查
     * @param forceKey
     * @param dataList
     * @return
     */
    Map<String, Object> cgreportGetData(String code, String forceKey, String dataList);

}
