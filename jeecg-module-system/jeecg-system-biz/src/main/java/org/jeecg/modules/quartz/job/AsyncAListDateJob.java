package org.jeecg.modules.quartz.job;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.dynamic.db.DynamicDBUtil;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
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
    private static final String dbKey = "alist";

    private static final int batchUpdateSize = 500;

    private List<DictModel> resourceTypeList = new ArrayList<>(20);

    @Autowired
    private ISysDictService sysDictService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Job Execution key："+jobExecutionContext.getJobDetail().getKey());
        JobUtil.startTime("AsyncAListDateJob");
        try {
            resourceTypeList = sysDictService.queryDictItemsByCode("alist_resource_type");
            //1、同步shares数据：x_storages -> jeecg_shares -> x_shares
            DynamicDBUtil.execute(dbKey,"call insert_shares()");
            String syncShare = "insert into x_shares(name, links, password, size, remark, update_date)" +
                    "select name, links, password, size, remark, update_date from `jeecg-boot`.jeecg_shares where links not in(select links collate utf8mb4_general_ci from x_shares)";
            DynamicDBUtil.execute(dbKey,syncShare);
            //2、更新shares资源分类：只更新x_storages表中未分类的数据
            updateShares();
            //3、修复挂载路径，避免重复添加：挂载路径和资源分类不一致，或挂载路径为默认路径(手动更新数据字典)
            updateMountPathNew();
            //4、同步storages
            DynamicDBUtil.execute(dbKey,"call insert_storages()");
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
        JobUtil.endTime("AsyncAListDateJob");
    }

    private void updateShares() {
        List<Object[]> sharesParams = new ArrayList<>(200);
        String query = "select id,name,links,driver from x_shares where length(resource_type)=0 or length(driver)=0";
        List<Map<String, Object>> result = DynamicDBUtil.findList(dbKey,query);
        for (Map map : result) {
            boolean isUpdate = false;
            int id = ((BigInteger) map.get("id")).intValue();
            String name = map.get("name").toString().trim();
            String links = map.get("links").toString();
            String resourceType = (String) map.get("resource_type");
            String driver = (String) map.get("driver");
            if (StringUtils.isBlank(resourceType)) {
                isUpdate = true;
                resourceType = JobUtil.getResourceTypeName(resourceTypeList,name);
            }
            if (StringUtils.isBlank(driver)) {
                isUpdate = true;
                driver = JobUtil.getDriverNameByUrl(links);
            }
            if (StringUtils.isNotEmpty(resourceType) && isUpdate) { //自动分类
                String newPath = "/共享/"+(StringUtils.isBlank(resourceType)?driver:resourceType)+"/"+name;
                sharesParams.add(new Object[]{name,driver,resourceType,newPath,id});
                log.info("{}: {} -> {}",driver,name,resourceType);
            }
            if (sharesParams.size() > batchUpdateSize) {
                DynamicDBUtil.batchUpdate(dbKey,"update x_shares set name=?,driver=?,resource_type=?,mount_path=? where id=?",sharesParams);
                log.info(String.format("updateShares: update %d,total %d",sharesParams.size(),result.size()));
                sharesParams.clear();
            }
        }
        DynamicDBUtil.batchUpdate(dbKey,"update x_shares set name=?,driver=?,resource_type=?,mount_path=? where id=?",sharesParams);
        log.info(String.format("updateShares: update %d,total %d",sharesParams.size(),result.size()));
        JobUtil.endTime("AsyncAListDateJob","updateShares");
    }

    /**
     * 修复数据(不包括新增数据)
     * 未匹配到分类路径的存储自动分类：自动更新resource_type和mount_path
     */
    private void updateMountPathNew() {
        List<Object[]> storagesParams = new ArrayList<>(200);
        List<Object[]> sharesParams = new ArrayList<>(200);
        String query = "select mount_path,resource_type,driver from x_storages where disabled=0 and driver like '%Share' and resource_type!=''" +
                " and mount_path not in(select distinct mount_path from x_shares where disabled=0);";
        List<Map<String, Object>> result = DynamicDBUtil.findList(dbKey,query);
        JobUtil.updateMountPath(resourceTypeList,result, storagesParams, sharesParams);
        DynamicDBUtil.batchUpdate(dbKey,"update x_storages set mount_path=?,resource_type=? where mount_path=? and ? not in(select mount_path from(select mount_path from x_storages)t)",storagesParams);
        DynamicDBUtil.batchUpdate(dbKey,"update x_shares set resource_type=?,mount_path=? where name=? and driver=?",sharesParams);
        log.info(String.format("updateMountPathNew: update %d,total %d",storagesParams.size(),result.size()));
        JobUtil.endTime("AsyncAListDateJob","updateMountPathNew");
    }
}
