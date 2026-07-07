package com.xxl.job.admin.business.scheduler.route.strategy;

import com.xxl.job.admin.business.scheduler.config.XxlJobAdminBootstrap;
import com.xxl.job.admin.business.scheduler.route.ExecutorRouter;
import com.xxl.job.admin.framework.util.I18nUtil;
import com.xxl.job.core.openapi.ExecutorBiz;
import com.xxl.job.core.openapi.model.IdleBeatRequest;
import com.xxl.job.core.openapi.model.TriggerRequest;
import com.xxl.tool.response.Response;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteBusyover extends ExecutorRouter {

    @Override
    public Response<String> route(TriggerRequest triggerParam, List<String> addressList) {
        StringBuffer idleBeatResultSB = new StringBuffer();
        for (String address : addressList) {
            // beat
            Response<String> idleBeatResult = null;
            try {
                ExecutorBiz executorBiz = XxlJobAdminBootstrap.getExecutorBiz(address);
                idleBeatResult = executorBiz.idleBeat(new IdleBeatRequest(triggerParam.getJobId()));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                idleBeatResult = Response.ofFail( ""+e );
            }
            idleBeatResultSB.append( (idleBeatResultSB.length()>0)?"<br><br>":"")
                    .append(I18nUtil.getString("jobconf_idleBeat") + "：")
                    .append("<br>address：").append(address)
                    .append("<br>code：").append(idleBeatResult.getCode())
                    .append("<br>msg：").append(idleBeatResult.getMsg());

            // beat success
            if (idleBeatResult.isSuccess()) {
                idleBeatResult.setMsg(idleBeatResultSB.toString());
                idleBeatResult.setData(address);
                return idleBeatResult;
            }
        }

        return Response.ofFail( idleBeatResultSB.toString());
    }

}
