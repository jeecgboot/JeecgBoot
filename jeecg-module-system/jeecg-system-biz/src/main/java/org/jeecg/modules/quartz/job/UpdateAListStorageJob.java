package org.jeecg.modules.quartz.job;

import com.alibaba.fastjson.JSONObject;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.dynamic.db.DynamicDBUtil;
import org.jeecg.config.Constant;
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

    private static final String debug = "";

    @Setter
    private String parameter;

    @Autowired
    private ISysDictService sysDictService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info(" Job Execution key："+jobExecutionContext.getJobDetail().getKey());
        JobUtil.startTime("UpdateAListStorageJob");
        try {
            //reloadStorage();
            JobUtil.endTime("UpdateAListStorageJob","reloadStorage");
            updateMountPath();
            JobUtil.endTime("UpdateAListStorageJob","updateMountPath");
            // 解决阿里云token频繁访问导致加载失败的问题
            String query = "select * from alist_storages where disabled=0 and driver in('"+parameter.replaceAll(",","','")+"') and status!='work'"+debug;
            List<Map<String, Object>> result = DynamicDBUtil.findList(dbKey,query);
            JSONObject object = null;
            int count = 0;
            Map<String,String> updateMap = new HashMap<>();
            Map<String,String> cancelMap = new HashMap<>();
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
                    object = JobUtil.updateStorage(map);
                    if (object == null) { //超时
                        continue;
                    }
                    if (object.getIntValue("code") == 200) { //保存成功
                        updateMap.put(String.valueOf(map.get("id")),"");
                        log.info(String.format("%s update success", map.get("mount_path")));
                    } else { //保存失败
                        String errMsg = object.getString("message");
                        log.warn(String.format("%s update error: %s", map.get("mount_path"),object.getString("message")));
                        if (errMsg.contains("share_link is cancelled by the creator") || errMsg.contains("The resource share_link cannot be found")
                            || errMsg.contains("The resource share_pwd is not valid") || errMsg.contains("The input parameter share_id is not valid")
                            || errMsg.contains("分享已取消") || errMsg.contains("链接已失效")) {
                            param.add(new Object[]{((Long) map.get("id")).intValue()});
                            cancelMap.put(String.valueOf(map.get("id")),"");
                        } else if (errMsg.contains("用户验证失败") || errMsg.contains("IP登录异常,请稍候再登录！")) {
                            break;
                        } else if (errMsg.contains("too many requests")) { //循环保存
                            Thread.sleep(8000);
                        }
                    }
                    if (param.size() > batchUpdateSize) {
                        DynamicDBUtil.batchUpdate(dbKey,"update alist_storages set disabled=2 where id=?",param);
                        param.clear();
                    }
                }
                log.info(String.format("第%d遍历：%d success, %d cancel",count,updateMap.size(),cancelMap.size()));
                DynamicDBUtil.batchUpdate(dbKey,"update alist_storages set disabled=2 where id=?",param);
                result = DynamicDBUtil.findList(dbKey,query);
            } while (!result.isEmpty() && count<1);
            DynamicDBUtil.execute(dbKey,"call update_storages_status()");
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
        JobUtil.endTime("UpdateAListStorageJob");
    }

    private void reloadStorage() {
        String query = "select id from alist_storages where disabled!=0 and driver like '%Share'"+debug;
        List<Integer> result = DynamicDBUtil.findList(dbKey,query,Integer.class);
        Map<String,Object> param = new HashMap<>();
        while (!result.isEmpty()) {
            for (int id : result) {
                param.put("id",id);
                try {
                    JobUtil.alistPostRequest(Constant.ALIST_STORAGE_ENABLE,param);
                    //Thread.sleep(100);
                } catch (Exception e) {
                    log.error("reloadStorage error:",e.getMessage());
                }
            }
            log.info("{} reload storage success",result.size());
            result = DynamicDBUtil.findList(dbKey,query,Integer.class);
        }
    }

    /**
     * 仅处理增量数据
     */
    private void updateMountPath() {
        List<DictModel> resourceTypeList = sysDictService.queryDictItemsByCode("alist_resource_type");
        List<Object[]> sharesParams = new ArrayList<>(200);
        List<Object[]> deleteStorages = new ArrayList<>(200);
        String query = "select mount_path,resource_type,driver from alist_storages where disabled=0 and driver like '%Share' and (resource_type='' or resource_type is null)"+debug;
        List<Map<String, Object>> result = DynamicDBUtil.findList(dbKey,query);
        JobUtil.updateMountPath(resourceTypeList,result, sharesParams, deleteStorages);
        DynamicDBUtil.batchUpdate(dbKey,"delete from alist_storages where mount_path=?",deleteStorages);
        DynamicDBUtil.batchUpdate(dbKey,"update alist_shares set mount_path=?,resource_type=? where name=? and driver=?",sharesParams);
        log.info(String.format("updateMountPath: readd %d,total %d",deleteStorages.size(),result.size()));
    }

}
