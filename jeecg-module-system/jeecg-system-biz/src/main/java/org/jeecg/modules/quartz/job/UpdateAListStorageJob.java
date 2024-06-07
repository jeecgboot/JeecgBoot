package org.jeecg.modules.quartz.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.dynamic.db.DynamicDBUtil;
import org.jeecg.modules.system.service.ISysDictService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.net.SocketTimeoutException;
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

    @Autowired
    private ISysDictService sysDictService;

    private static OkHttpClient client = new OkHttpClient().newBuilder().build();

    private String alist_token = "";

    private String domain = "";

    private static final int batchUpdateSize = 10;

    private static final String dbKey = "alist";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info(" Job Execution key："+jobExecutionContext.getJobDetail().getKey());
        JobUtil.startTime("UpdateAListStorageJob");
        try {
            initData();
            updateMountPath();
            JobUtil.endTime("UpdateAListStorageJob update data");
            // 解决阿里云token频繁访问导致加载失败的问题
            String query = "select * from x_storages where disabled=0 and driver in('AliyundriveShare') and status!='work' limit 5000";
            List<Map<String, Object>> result = DynamicDBUtil.findList(dbKey,query);
            JSONObject object = null;
            int count = 0;
            Map<String,String> updateMap = new HashMap<>();
            Map<String,String> cancelMap = new HashMap<>();
            do {
                count++;
                List<Object[]> param = new ArrayList<>(result.size());
                for (Map map : result) {
                    if (updateMap.containsKey(String.valueOf(map.get("id"))) || cancelMap.containsKey(String.valueOf(map.get("id")))) {
                        continue;
                    }
                    map.remove("resource_type");
                    map.remove("modified");
                    map.remove("update_date");
                    object = postRequest("/admin/storage/update",map);
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
                            || errMsg.contains("The resource share_pwd is not valid") || errMsg.contains("The input parameter share_id is not valid")) {
                            param.add(new Object[]{((BigInteger) map.get("id")).intValue()});
                            cancelMap.put(String.valueOf(map.get("id")),"");
                        } else if (errMsg.contains("分享已取消") || errMsg.contains("链接已失效")) {
                            param.add(new Object[]{((BigInteger) map.get("id")).intValue()});
                            cancelMap.put(String.valueOf(map.get("id")),"");
                        } else if (errMsg.contains("too many requests")) { //循环保存
                            Thread.sleep(8000);
                        }
                    }
                    if (cancelMap.size() > batchUpdateSize) {
                        DynamicDBUtil.batchUpdate(dbKey,"update x_storages set disabled=1 where id=?",param);
                        param.clear();
                    }
                }
                log.info(String.format("第%d遍历：%d success, %d cancel",count,updateMap.size(),cancelMap.size()));
                DynamicDBUtil.batchUpdate(dbKey,"update x_storages set disabled=1 where id=?",param);
                result = DynamicDBUtil.findList(dbKey,query);
            } while (!result.isEmpty() && count<1);
            DynamicDBUtil.execute(dbKey,"call update_storages_status()");
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
        JobUtil.endTime("UpdateAListStorageJob");
    }

    private JSONObject postRequest(String api, Map<String, String> param) throws IOException {
        if (domain.isEmpty()) {
            return null;
        }
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(param));
        Request request = new Request.Builder()
                .url(domain+api)
                .method("POST", body)
                .addHeader("Authorization", alist_token)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = null;
        String result = "";
        try {
            response = client.newCall(request).execute();
            result = response.body().string();
        } catch (SocketTimeoutException e) {
            log.error("{} connet timeout",api);
        }
        return JSONObject.parseObject(result);
    }

    private void initData() throws JobExecutionException {
        domain = sysDictService.queryDictTextByKey("alist_param","url");
        if (StringUtils.isBlank(alist_token)) {
            Map<String,String> param = new HashMap<>();
            param.put("username",sysDictService.queryDictTextByKey("alist_param","username"));
            param.put("password",sysDictService.queryDictTextByKey("alist_param","password"));
            JSONObject result = null;
            try {
                result = postRequest("/auth/login",param);
            } catch (IOException e) {
                throw new JobExecutionException(e);
            }
            alist_token = result.getJSONObject("data").getString("token");
        }
    }

    /**
     * 仅处理增量数据
     */
    private void updateMountPath() {
        List<DictModel> resourceTypeList = sysDictService.queryDictItemsByCode("alist_resource_type");
        List<Object[]> storagesParams = new ArrayList<>(200);
        List<Object[]> sharesParams = new ArrayList<>(200);
        String query = "select mount_path,resource_type,driver from x_storages where disabled=0 and driver like '%Share' and resource_type=''" +
                " and mount_path not in(select distinct mount_path from x_shares where disabled=0)";
        List<Map<String, Object>> result = DynamicDBUtil.findList(dbKey,query);
        JobUtil.updateMountPath(resourceTypeList,result, storagesParams, sharesParams);
        DynamicDBUtil.batchUpdate(dbKey,"update x_storages set mount_path=?,resource_type=? where mount_path=? and ? not in(select mount_path from(select mount_path from x_storages)t)",storagesParams);
        DynamicDBUtil.batchUpdate(dbKey,"update x_shares set resource_type=?,mount_path=? where name=? and driver=?",sharesParams);
        log.info(String.format("updateMountPath: update %d,total %d",storagesParams.size(),result.size()));
    }

}
