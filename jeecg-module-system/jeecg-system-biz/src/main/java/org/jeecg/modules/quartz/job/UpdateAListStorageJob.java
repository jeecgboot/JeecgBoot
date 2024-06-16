package org.jeecg.modules.quartz.job;

import com.alibaba.fastjson.JSONObject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.dynamic.db.DynamicDBUtil;
import org.jeecg.config.Constant;
import org.jeecg.config.LogUtil;
import org.jeecg.config.ThreadUtil;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 更新分享链接状态，使其能够正常访问
 * 目前仅用于更新阿里云分享
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Slf4j
@Component
public class UpdateAListStorageJob implements Job {

    private static final int batchUpdateSize = 10;

    private static final String dbKey = "jeecg-boot";

    /** 查询状态异常的存储 */
    private static final String query_status_empty_sql = "select * from alist_storages where disabled=0 and driver in('%s') and status!='work'";
    /** 更新为不可用（disabled=2）*/
    private static final String update_disabled2_sql = "update alist_storages set disabled=2 where id=?";
    /** 查询未启用的存储（disabled=2）*/
    private static final String query_disabled2_sql = "select * from alist_storages where disabled=2 and driver like '%Share'";
    /** 删除未启用的存储（disabled=2）*/
    private static final String delete_disabled2_sql = "delete from alist_storages where disabled=2 and driver like '%Share'";
    /** 查询未启用的存储（disabled=1），用于alist启动后初始化 */
    private static final String query_enable_sql = "select id from alist_storages where disabled=1 and driver like '%Share' limit 5000";
    /** 查询未分类存储 */
    private static final String query_resourceType_empty_sql = "select mount_path,resource_type,driver from alist_storages where disabled=0 and driver like '%Share' and (resource_type='' or resource_type is null) limit 5000";
    /** 删除未分类存储 */
    private static final String delete_mountpath_sql = "delete from alist_storages where mount_path=?";
    /** 更新未分类存储，并添加 */
    private static final String update_mountpath_sql = "update alist_shares set mount_path=?,resource_type=? where name=? and driver=?";

    @Setter
    private String parameter;

    @Autowired
    private ISysDictService sysDictService;

    /**
     * 解决阿里云token频繁访问导致加载失败的问题
     * 1、重新加载异常状态的存储
     * 2、storages失效链接状态更新为2
     * 3、shares逻辑删除失效链接
     * 4、storages删除失效链接
     */
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info(" Job Execution key：" + jobExecutionContext.getJobDetail().getKey());
        LogUtil.startTime("UpdateAListStorageJob");
        try {
            reloadStorage();
            LogUtil.endTime("UpdateAListStorageJob", "reloadStorage");
            updateMountPath();
            LogUtil.endTime("UpdateAListStorageJob", "updateMountPath");
            String query =  String.format(query_status_empty_sql,parameter.replaceAll(",", "','"));
            List<Map<String, Object>> removeList = new ArrayList<>();
            List<Map<String, Object>> result = DynamicDBUtil.findList(dbKey, query);
            JSONObject object = null;
            int count = 0;
            Map<String, String> updateMap = new HashMap<>();
            Map<String, String> cancelMap = new HashMap<>();
            do {
                count++;
                List<Object[]> param = new ArrayList<>(result.size());
                for (Map map : result) {
                    if (updateMap.size() > 5000) {
                        break;
                    }
                    if (updateMap.containsKey(String.valueOf(map.get("id"))) || cancelMap.containsKey(String.valueOf(map.get("id")))) {
                        continue;
                    }
                    object = AListJobUtil.updateStorage(map);
                    if (object == null) { //超时
                        continue;
                    }
                    if (object.getIntValue("code") == 200) { //保存成功
                        updateMap.put(String.valueOf(map.get("id")), "");
                        log.info(String.format("%s update success", map.get("mount_path")));
                    } else { //保存失败
                        String errMsg = object.getString("message");
                        log.warn(String.format("%s update error: %s", map.get("mount_path"), object.getString("message")));
                        if (errMsg.contains("share_link is cancelled by the creator") || errMsg.contains("The resource share_link cannot be found")
                                || errMsg.contains("The resource share_pwd is not valid") || errMsg.contains("The input parameter share_id is not valid")
                                || errMsg.contains("分享已取消") || errMsg.contains("链接已失效")) {
                            param.add(new Object[]{((Long) map.get("id")).intValue()});
                            cancelMap.put(String.valueOf(map.get("id")), "");
                            removeList.add(map);
                        } else if (errMsg.contains("用户验证失败") || errMsg.contains("IP登录异常,请稍候再登录！")) {
                            break;
                        } else if (errMsg.contains("too many requests")) { //循环保存
                            Thread.sleep(8000);
                        }
                    }
                    if (param.size() > batchUpdateSize) {
                        DynamicDBUtil.batchUpdate(dbKey, update_disabled2_sql, param);
                        param.clear();
                    }
                }
                log.info(String.format("第%d遍历：%d success, %d cancel", count, updateMap.size(), cancelMap.size()));
                DynamicDBUtil.batchUpdate(dbKey, update_disabled2_sql, param);
                result = DynamicDBUtil.findList(dbKey, query);
            } while (!result.isEmpty() && count < 1);
            DynamicDBUtil.execute(dbKey, "call update_storages_status()");
            result = DynamicDBUtil.findList(dbKey, query_disabled2_sql);
            removeList.addAll(result);
            if (!removeList.isEmpty()) {
                ThreadUtil.execute(() -> AListJobUtil.deleteStorage(removeList));
            }
            DynamicDBUtil.execute(dbKey, delete_disabled2_sql);
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
        LogUtil.endTime("UpdateAListStorageJob");
    }

    public void reloadStorage() {
        ThreadUtil.execute(() -> {
            List<Integer> result = DynamicDBUtil.findList(dbKey, query_enable_sql, Integer.class);
            Map<String, Object> param = new HashMap<>();
            while (!result.isEmpty()) {
                for (int id : result) {
                    param.put("id", id);
                    try {
                        AListJobUtil.alistPostRequest(Constant.ALIST_STORAGE_ENABLE, param);
                    } catch (Exception e) {
                        log.error("reloadStorage error:", e.getMessage());
                    }
                }
                log.info("{} reload storage success", result.size());
                result = DynamicDBUtil.findList(dbKey, query_enable_sql, Integer.class);
            }
        });
    }

    /**
     * 仅处理增量数据
     */
    private void updateMountPath() {
        List<DictModel> resourceTypeList = sysDictService.queryDictItemsByCode("alist_resource_type");
        List<Object[]> sharesParams = new ArrayList<>(200);
        List<Object[]> deleteStorages = new ArrayList<>(200);
        List<Map<String, Object>> result = DynamicDBUtil.findList(dbKey, query_resourceType_empty_sql);
        AListJobUtil.updateMountPath(resourceTypeList, result, sharesParams, deleteStorages);
        DynamicDBUtil.batchUpdate(dbKey, delete_mountpath_sql, deleteStorages);
        DynamicDBUtil.batchUpdate(dbKey, update_mountpath_sql, sharesParams);
        log.info(String.format("updateMountPath: readd %d,total %d", deleteStorages.size(), result.size()));
    }

}
