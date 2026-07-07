package com.xxl.job.admin.business.scheduler.trigger;

import com.xxl.job.admin.business.mapper.XxlJobGroupMapper;
import com.xxl.job.admin.business.mapper.XxlJobInfoMapper;
import com.xxl.job.admin.business.mapper.XxlJobLogMapper;
import com.xxl.job.admin.business.model.XxlJobGroup;
import com.xxl.job.admin.business.model.XxlJobInfo;
import com.xxl.job.admin.business.model.XxlJobLog;
import com.xxl.job.admin.business.scheduler.config.XxlJobAdminBootstrap;
import com.xxl.job.admin.business.scheduler.route.ExecutorRouteStrategyEnum;
import com.xxl.job.admin.framework.util.I18nUtil;
import com.xxl.job.core.constant.ExecutorBlockStrategyEnum;
import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.core.openapi.ExecutorBiz;
import com.xxl.job.core.openapi.model.TriggerRequest;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.error.ThrowableTool;
import com.xxl.tool.http.IPTool;
import com.xxl.tool.response.Response;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * xxl-job trigger
 *
 * @author xuxueli 17/7/13.
 */
@Component
public class JobTrigger {
    private static final Logger logger = LoggerFactory.getLogger(JobTrigger.class);


    @Resource
    private XxlJobInfoMapper xxlJobInfoMapper;
    @Resource
    private XxlJobGroupMapper xxlJobGroupMapper;
    @Resource
    private XxlJobLogMapper xxlJobLogMapper;


    /**
     * trigger job
     *
     * @param jobId
     * @param triggerType
     * @param failRetryCount
     * 			>=0: use this param
     * 			<0: use param from job info config
     * @param executorShardingParam
     *          null: new sharding, all nodes
     *          not null: for retry, only one node
     * @param executorParam
     *          null: use job param
     *          not null: cover job param
     * @param addressList
     *          null: use executor addressList
     *          not null: cover
     */
    public void trigger(int jobId,
                               TriggerTypeEnum triggerType,
                               int failRetryCount,
                               String executorShardingParam,
                               String executorParam,
                               String addressList) {

        // load data
        XxlJobInfo jobInfo = xxlJobInfoMapper.loadById(jobId);
        if (jobInfo == null) {
            logger.warn(">>>>>>>>>>>> trigger fail, jobId invalid，jobId={}", jobId);
            return;
        }
        if (executorParam != null) {
            jobInfo.setExecutorParam(executorParam);
        }
        int finalFailRetryCount = failRetryCount>=0?failRetryCount:jobInfo.getExecutorFailRetryCount();
        XxlJobGroup group = xxlJobGroupMapper.load(jobInfo.getJobGroup());

        // cover addressList
        if (StringTool.isNotBlank(addressList)) {
            group.setAddressType(1);
            group.setAddressList(addressList.trim());
        }

        // sharding param
        int[] shardingParam = null;
        Date triggerTime = new Date();
        if (executorShardingParam!=null){
            String[] shardingArr = executorShardingParam.split("/");
            if (shardingArr.length==2 && StringTool.isNumeric(shardingArr[0]) && StringTool.isNumeric(shardingArr[1])) {
                shardingParam = new int[2];
                shardingParam[0] = Integer.parseInt(shardingArr[0]);
                shardingParam[1] = Integer.parseInt(shardingArr[1]);
            }
        }
        if (ExecutorRouteStrategyEnum.SHARDING_BROADCAST==ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null)
                && group.getRegistryList()!=null && !group.getRegistryList().isEmpty()
                && shardingParam==null) {
            for (int i = 0; i < group.getRegistryList().size(); i++) {
                processTrigger(group, jobInfo, finalFailRetryCount, triggerType, triggerTime, i, group.getRegistryList().size());
            }
        } else {
            if (shardingParam == null) {
                shardingParam = new int[]{0, 1};
            }
            processTrigger(group, jobInfo, finalFailRetryCount, triggerType, triggerTime, shardingParam[0], shardingParam[1]);
        }

    }

    /*private static boolean isNumeric(String str){
        try {
            int result = Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }*/

    /**
     * process trigger with log
     *
     * @param group                     job group, registry list may be empty
     * @param jobInfo                   job info
     * @param finalFailRetryCount       the fail-retry count
     * @param triggerType               trigger type
     * @param triggerTime               trigger time
     * @param index                     sharding index
     * @param total                     sharding index
     */
    private void processTrigger(XxlJobGroup group,
                                XxlJobInfo jobInfo,
                                int finalFailRetryCount,
                                TriggerTypeEnum triggerType,
                                Date triggerTime,
                                int index,
                                int total){

        // param
        ExecutorBlockStrategyEnum blockStrategy = ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), ExecutorBlockStrategyEnum.SERIAL_EXECUTION);  // block strategy
        ExecutorRouteStrategyEnum executorRouteStrategyEnum = ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null);    // route strategy
        String shardingParam = (ExecutorRouteStrategyEnum.SHARDING_BROADCAST==executorRouteStrategyEnum)?String.valueOf(index).concat("/").concat(String.valueOf(total)):null;

        // 1、save log-id
        XxlJobLog jobLog = new XxlJobLog();
        jobLog.setJobGroup(jobInfo.getJobGroup());
        jobLog.setJobId(jobInfo.getId());
        jobLog.setTriggerTime(triggerTime);
        xxlJobLogMapper.save(jobLog);
        logger.debug(">>>>>>>>>>> xxl-job trigger start, jobId:{}", jobLog.getJobId());

        // 2、init trigger-param
        TriggerRequest triggerParam = new TriggerRequest();
        triggerParam.setJobId(jobInfo.getId());
        triggerParam.setExecutorHandler(jobInfo.getExecutorHandler());
        triggerParam.setExecutorParams(jobInfo.getExecutorParam());
        triggerParam.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
        triggerParam.setExecutorTimeout(jobInfo.getExecutorTimeout());
        triggerParam.setLogId(jobLog.getId());
        triggerParam.setLogDateTime(jobLog.getTriggerTime().getTime());
        triggerParam.setGlueType(jobInfo.getGlueType());
        triggerParam.setGlueSource(jobInfo.getGlueSource());
        triggerParam.setGlueUpdatetime(jobInfo.getGlueUpdatetime().getTime());
        triggerParam.setBroadcastIndex(index);
        triggerParam.setBroadcastTotal(total);

        // 3、init address
        String address = null;
        Response<String> routeAddressResult = null;
        if (group.getRegistryList()!=null && !group.getRegistryList().isEmpty()) {
            if (ExecutorRouteStrategyEnum.SHARDING_BROADCAST == executorRouteStrategyEnum) {
                if (index < group.getRegistryList().size()) {
                    address = group.getRegistryList().get(index);
                } else {
                    address = group.getRegistryList().get(0);
                }
            } else {
                routeAddressResult = executorRouteStrategyEnum.getRouter().route(triggerParam, group.getRegistryList());
                if (routeAddressResult.isSuccess()) {
                    address = routeAddressResult.getData();
                }
            }
        } else {
            routeAddressResult = Response.of(XxlJobContext.HANDLE_CODE_FAIL, I18nUtil.getString("jobconf_trigger_address_empty"));
        }

        // 4、trigger remote executor
        Response<String> triggerResult = null;
        if (address != null) {
            triggerResult = doTrigger(triggerParam, address);
        } else {
            triggerResult = Response.of(XxlJobContext.HANDLE_CODE_FAIL, "Address Router Fail.");
        }

        // 5、collection trigger info
        // trigger config
        StringBuilder triggerMsgSb = new StringBuilder();
        triggerMsgSb.append(I18nUtil.getString("jobconf_trigger_type")).append("：").append(triggerType.getTitle());
        triggerMsgSb.append("<br>").append(I18nUtil.getString("jobconf_trigger_admin_adress")).append("：").append(IPTool.getIp());
        triggerMsgSb.append("<br>").append(I18nUtil.getString("jobconf_trigger_exe_regtype")).append("：")
                .append( (group.getAddressType() == 0)?I18nUtil.getString("jobgroup_field_addressType_0"):I18nUtil.getString("jobgroup_field_addressType_1") );
        triggerMsgSb.append("<br>").append(I18nUtil.getString("jobconf_trigger_exe_regaddress")).append("：").append(group.getRegistryList());
        triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_executorRouteStrategy")).append("：").append(executorRouteStrategyEnum.getTitle());
        if (shardingParam != null) {
            triggerMsgSb.append("(").append(shardingParam).append(")");
        }
        triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_executorBlockStrategy")).append("：").append(blockStrategy.getTitle());
        triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_timeout")).append("：").append(jobInfo.getExecutorTimeout());
        triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_executorFailRetryCount")).append("：").append(finalFailRetryCount);

        // trigger data
        triggerMsgSb.append("<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>").append(I18nUtil.getString("jobconf_trigger_run")).append("<<<<<<<<<<< </span><br>");
        triggerMsgSb.append("<br>").append(I18nUtil.getString("joblog_field_executorAddress")).append("：");
        if (StringTool.isNotBlank(address)) {
            triggerMsgSb.append(address);
        } else if (routeAddressResult!=null && !routeAddressResult.isSuccess() && routeAddressResult.getMsg()!=null) {
            triggerMsgSb.append("address route fail, ").append(routeAddressResult.getMsg());
        } else {
            triggerMsgSb.append("address route fail.");
        }
        if (StringTool.isNotBlank(jobInfo.getExecutorHandler())) {
            triggerMsgSb.append("<br>").append("JobHandler").append("：").append(jobInfo.getExecutorHandler());
        }
        triggerMsgSb.append("<br>").append(I18nUtil.getString("jobinfo_field_executorparam")).append("：").append(jobInfo.getExecutorParam());
        triggerMsgSb.append("<br>").append(I18nUtil.getString("joblog_field_triggerMsg")).append("：");
        if (triggerResult.isSuccess()) {
            triggerMsgSb.append("success");
        } else if (triggerResult.getMsg()!=null) {
            triggerMsgSb.append("error, ").append(triggerResult.getMsg());
        } else {
            triggerMsgSb.append("fail");
        }

        // 6、save log trigger-info
        jobLog.setExecutorAddress(address);
        jobLog.setExecutorHandler(jobInfo.getExecutorHandler());
        jobLog.setExecutorParam(jobInfo.getExecutorParam());
        jobLog.setExecutorShardingParam(shardingParam);
        jobLog.setExecutorFailRetryCount(finalFailRetryCount);
        //jobLog.setTriggerTime();
        jobLog.setTriggerCode(triggerResult.getCode());
        jobLog.setTriggerMsg(triggerMsgSb.toString());
        xxlJobLogMapper.updateTriggerInfo(jobLog);

        logger.debug(">>>>>>>>>>> xxl-job trigger end, jobId:{}", jobLog.getJobId());
    }

    /**
     * do trigger with address
     *
     * @param triggerParam  trigger param
     * @param address       the address
     * @return return
     */
    private Response<String> doTrigger(TriggerRequest triggerParam, String address){
        try {
            // build client
            ExecutorBiz executorBiz = XxlJobAdminBootstrap.getExecutorBiz(address);

            // invoke
            Response<String> runResult = executorBiz.run(triggerParam);

            // build result
            StringBuffer runResultSB = new StringBuffer(I18nUtil.getString("jobconf_trigger_run") + "：");
            runResultSB.append("<br>address：").append(address);
            runResultSB.append("<br>code：").append(runResult.getCode());
            runResultSB.append("<br>msg：").append(runResult.getMsg());

            // return
            runResult.setMsg(runResultSB.toString());
            return runResult;
        } catch (Exception e) {
            logger.error(">>>>>>>>>>> xxl-job trigger error, please check if the executor[{}] is running.", address, e);
            return Response.of(XxlJobContext.HANDLE_CODE_FAIL, ThrowableTool.toString(e));
        }
    }

}
