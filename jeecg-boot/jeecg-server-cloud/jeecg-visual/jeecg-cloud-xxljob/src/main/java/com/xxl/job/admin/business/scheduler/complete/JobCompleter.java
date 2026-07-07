package com.xxl.job.admin.business.scheduler.complete;

import com.xxl.job.admin.business.mapper.XxlJobInfoMapper;
import com.xxl.job.admin.business.mapper.XxlJobLogMapper;
import com.xxl.job.admin.business.model.XxlJobInfo;
import com.xxl.job.admin.business.model.XxlJobLog;
import com.xxl.job.admin.business.scheduler.config.XxlJobAdminBootstrap;
import com.xxl.job.admin.business.scheduler.trigger.TriggerTypeEnum;
import com.xxl.job.admin.framework.util.I18nUtil;
import com.xxl.job.core.context.XxlJobContext;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.response.Response;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * xxl-job job log complete
 *
 * @author xuxueli 2020-10-30 20:43:10
 */
@Component
public class JobCompleter {
    private static final Logger logger = LoggerFactory.getLogger(JobCompleter.class);


    @Resource
    private XxlJobInfoMapper xxlJobInfoMapper;
    @Resource
    private XxlJobLogMapper xxlJobLogMapper;


    /**
     * complate job (limit only once)
     */
    public int complete(XxlJobLog xxlJobLog) {

        // 1、process child-job
        processChildJob(xxlJobLog);

        // text最大64kb 避免长度过长
        if (xxlJobLog.getHandleMsg().length() > 15000) {
            xxlJobLog.setHandleMsg( xxlJobLog.getHandleMsg().substring(0, 15000) );
        }

        // 2、fix_delay trigger next
        // on the way

        // 3、update job handle-info
        return xxlJobLogMapper.updateHandleInfo(xxlJobLog);
    }


    /**
     * do somethind to finish job
     */
    private void processChildJob(XxlJobLog xxlJobLog){

        // 1、handle success, to trigger child job
        String triggerChildMsg = null;
        if (XxlJobContext.HANDLE_CODE_SUCCESS == xxlJobLog.getHandleCode()) {
            XxlJobInfo xxlJobInfo = xxlJobInfoMapper.loadById(xxlJobLog.getJobId());

            // process child job
            if (xxlJobInfo!=null && StringTool.isNotBlank(xxlJobInfo.getChildJobId())) {
                triggerChildMsg = "<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>"+ I18nUtil.getString("jobconf_trigger_child_run") +"<<<<<<<<<<< </span><br>";
                String[] childJobIds = xxlJobInfo.getChildJobId().split(",");
                for (int i = 0; i < childJobIds.length; i++) {

                    // process eath child
                    int childJobId = (StringTool.isNotBlank(childJobIds[i]) && StringTool.isNumeric(childJobIds[i]))
                            ?Integer.parseInt(childJobIds[i])
                            :-1;
                    if (childJobId > 0) {
                        // valid
                        if (childJobId == xxlJobLog.getJobId()) {
                            logger.debug(">>>>>>>>>>> xxl-job, XxlJobCompleter-finishJob ignore childJobId,  childJobId {} is self.", childJobId);
                            continue;
                        }

                        // trigger child job
                        XxlJobAdminBootstrap.getInstance().getJobTriggerPoolHelper().trigger(childJobId, TriggerTypeEnum.PARENT, -1, null, null, null);
                        Response<String> triggerChildResult = Response.ofSuccess();

                        // add msg
                        triggerChildMsg += MessageFormat.format(I18nUtil.getString("jobconf_callback_child_msg1"),
                                (i+1),
                                childJobIds.length,
                                childJobIds[i],
                                (triggerChildResult.isSuccess()?I18nUtil.getString("system_success"):I18nUtil.getString("system_fail")),
                                triggerChildResult.getMsg());
                    } else {
                        triggerChildMsg += MessageFormat.format(I18nUtil.getString("jobconf_callback_child_msg2"),
                                (i+1),
                                childJobIds.length,
                                childJobIds[i]);
                    }
                }

            }
        }

        // 2、append trigger-child message
        if (StringTool.isNotBlank(triggerChildMsg)) {
            xxlJobLog.setHandleMsg( xxlJobLog.getHandleMsg() + triggerChildMsg );
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

}
