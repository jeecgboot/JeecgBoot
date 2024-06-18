package org.jeecg.modules.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.dynamic.db.DynamicDBUtil;
import org.jeecg.config.LogUtil;
import org.jeecg.config.StringUtil;
import org.jeecg.config.ThreadUtil;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 同步shares表和storages表
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@Slf4j
@Component
public class AsyncAListDateJob implements Job {
    private static final String dbKey = "jeecg-boot";

    private static final int batchUpdateSize = 500;

    private static final String debug = "";

    private List<DictModel> resourceTypeList = new ArrayList<>(20);

    @Autowired
    private ISysDictService sysDictService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Job Execution key：" + jobExecutionContext.getJobDetail().getKey());
        LogUtil.startTime("AsyncAListDateJob");
        try {
            resourceTypeList = sysDictService.queryDictItemsByCode("alist_resource_type");
            //更新shares资源分类：只更新alist_storages表中未分类的数据
            updateShares();
            //修复挂载路径，避免重复添加：挂载路径和资源分类不一致，或挂载路径为默认路径(手动更新数据字典)
            updateMountPathNew();
            //同步storages
            DynamicDBUtil.execute(dbKey, "call insert_storages()");
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
        LogUtil.endTime("AsyncAListDateJob");
    }

    private void updateShares() {
        List<Object[]> sharesParams = new ArrayList<>(200);
        List<Object[]> deleteStorages = new ArrayList<>(200);
        List<Map<String, Object>> removeList = new ArrayList<>();
        String query = "select id,name,links,resource_type,driver,mount_path from alist_shares where resource_type='' or resource_type is null or driver='' or driver is null or instr(mount_path,resource_type)<=0 limit 5000";
        List<Map<String, Object>> result = DynamicDBUtil.findList(dbKey, query);
        for (Map map : result) {
            boolean isUpdate = false;
            int id = ((Long) map.get("id")).intValue();
            String name = map.get("name").toString().trim();
            String links = map.get("links").toString();
            String resourceType = (String) map.get("resource_type");
            String driver = (String) map.get("driver");
            String mountPath = (String) map.get("mount_path");
            try {
                if (StringUtils.isBlank(resourceType)) {
                    isUpdate = true;
                    resourceType = AListJobUtil.getResourceTypeName(resourceTypeList, name);
                }
                if (StringUtils.isBlank(driver)) {
                    isUpdate = true;
                    driver = StringUtil.getDriverNameByUrl(links);
                }
                if (StringUtils.isNotEmpty(resourceType) && isUpdate) { //自动分类
                    String newPath = "/共享/" + (StringUtils.isBlank(resourceType) ? driver : resourceType) + "/" + name;
                    sharesParams.add(new Object[]{name, driver, resourceType, newPath, id});
                    if (StringUtils.isNotBlank(mountPath)) {
                        deleteStorages.add(new Object[]{mountPath});
                        removeList.add(map);
                    }
                    log.info("{}: {} -> {}", driver, name, resourceType);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("updateShares error:", e.getMessage());
            }
            if (sharesParams.size() > batchUpdateSize) {
                DynamicDBUtil.batchUpdate(dbKey, "update alist_shares set name=?,driver=?,resource_type=?,mount_path=? where id=?", sharesParams);
                if (!removeList.isEmpty()) {
                    ThreadUtil.execute(() -> {
                        AListJobUtil.deleteStorage(removeList);
                        DynamicDBUtil.batchUpdate(dbKey, "delete from alist_storages where mount_path=?", deleteStorages);
                    });
                }
                log.info(String.format("updateShares: delete %d,total %d", deleteStorages.size(), result.size()));
                sharesParams.clear();
                deleteStorages.clear();
                removeList.clear();
            }
        }
        DynamicDBUtil.batchUpdate(dbKey, "update alist_shares set name=?,driver=?,resource_type=?,mount_path=? where id=?", sharesParams);
        if (!removeList.isEmpty()) {
            ThreadUtil.execute(() -> {
                AListJobUtil.deleteStorage(removeList);
                DynamicDBUtil.batchUpdate(dbKey, "delete from alist_storages where mount_path=?", deleteStorages);
            });
        }
        log.info(String.format("updateShares: update %d,total %d", sharesParams.size(), result.size()));
        LogUtil.endTime("AsyncAListDateJob", "updateShares");
    }

    /**
     * 修复数据(不包括新增数据)
     * 未匹配到分类路径的存储自动分类：自动更新resource_type和mount_path
     */
    private void updateMountPathNew() {
        List<Object[]> sharesParams = new ArrayList<>(200);
        List<Object[]> deleteStorages = new ArrayList<>(200);
        String query = "select mount_path,resource_type,driver from alist_storages where disabled=0 and driver like '%Share' and resource_type!='' and instr(mount_path,resource_type)<=0" + debug;
        List<Map<String, Object>> result = DynamicDBUtil.findList(dbKey, query);
        AListJobUtil.updateMountPath(resourceTypeList, result, sharesParams, deleteStorages);
        DynamicDBUtil.batchUpdate(dbKey, "delete from alist_storages where mount_path=?", deleteStorages);
        DynamicDBUtil.batchUpdate(dbKey, "update alist_shares set mount_path=?,resource_type=? where name=? and driver=?", sharesParams);
        log.info(String.format("updateMountPathNew: readd %d,total %d", deleteStorages.size(), result.size()));
        LogUtil.endTime("AsyncAListDateJob", "updateMountPathNew");
    }
}
