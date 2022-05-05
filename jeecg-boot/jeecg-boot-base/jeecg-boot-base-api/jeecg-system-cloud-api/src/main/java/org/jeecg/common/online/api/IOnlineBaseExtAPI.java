package org.jeecg.common.online.api;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.constant.ServiceNameConstants;
import org.jeecg.common.online.api.factory.OnlineBaseExtAPIFallbackFactory;
import org.jeecg.common.system.vo.DictModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 【Online】Feign API接口
 */
@Component
@FeignClient(contextId = "onlineBaseRemoteApi", value = ServiceNameConstants.SYSTEM_ONLINE, fallbackFactory = OnlineBaseExtAPIFallbackFactory.class)
public interface IOnlineBaseExtAPI {

    /**
     * 【Online】 表单设计器专用：同步新增
     */
    @PostMapping(value = "/online/api/cgform/crazyForm/{name}")
    String cgformPostCrazyForm(@PathVariable("name") String tableName, @RequestBody JSONObject jsonObject) throws Exception;

    /**
     * 【Online】 表单设计器专用：同步编辑
     */
    @PutMapping(value = "/online/api/cgform/crazyForm/{name}")
    String cgformPutCrazyForm(@PathVariable("name") String tableName, @RequestBody JSONObject jsonObject) throws Exception;

    /**
     * 通过online表名查询数据，同时查询出子表的数据
     *
     * @param tableName online表名
     * @param dataIds   online数据ID
     * @return
     */
    @GetMapping(value = "/online/api/cgform/queryAllDataByTableName")
    JSONObject cgformQueryAllDataByTableName(@RequestParam("tableName") String tableName, @RequestParam("dataIds") String dataIds);

    /**
     * online表单删除数据
     *
     * @param cgformCode Online表单code
     * @param dataIds    数据ID，可逗号分割
     * @return
     */
    @DeleteMapping("/online/api/cgform/cgformDeleteDataByCode")
    String cgformDeleteDataByCode(@RequestParam("cgformCode") String cgformCode, @RequestParam("dataIds") String dataIds);

    /**
     * 【cgreport】通过 head code 获取 sql语句，并执行该语句返回查询数据
     *
     * @param code     报表Code，如果没传ID就通过code查
     * @param forceKey
     * @param dataList
     * @return
     */
    @GetMapping("/online/api/cgreportGetData")
    Map<String, Object> cgreportGetData(@RequestParam("code") String code, @RequestParam("forceKey") String forceKey, @RequestParam("dataList") String dataList);

    /**
     * 【cgreport】对 cgreportGetData 的返回值做优化，封装 DictModel 集合
     *
     * @return
     */
    @GetMapping("/online/api/cgreportGetDataPackage")
    List<DictModel> cgreportGetDataPackage(@RequestParam("code") String code, @RequestParam("dictText") String dictText, @RequestParam("dictCode") String dictCode, @RequestParam("dataList") String dataList);

}
