package com.xxl.job.admin.business.scheduler.route.strategy;

import com.xxl.job.admin.business.scheduler.config.XxlJobAdminBootstrap;
import com.xxl.job.admin.business.scheduler.route.ExecutorRouter;
import com.xxl.job.admin.framework.util.I18nUtil;
import com.xxl.job.core.openapi.ExecutorBiz;
import com.xxl.job.core.openapi.model.TriggerRequest;
import com.xxl.tool.response.Response;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteFailover extends ExecutorRouter {

    @Override
    public Response<String> route(TriggerRequest triggerParam, List<String> addressList) {

        StringBuffer beatResultSB = new StringBuffer();
        for (String address : addressList) {
            // beat
            Response<String> beatResult = null;
            try {
                ExecutorBiz executorBiz = XxlJobAdminBootstrap.getExecutorBiz(address);
                beatResult = executorBiz.beat();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                beatResult = Response.ofFail(e.getMessage() );
            }
            beatResultSB.append( (beatResultSB.length()>0)?"<br><br>":"")
                    .append(I18nUtil.getString("jobconf_beat") + "：")
                    .append("<br>address：").append(address)
                    .append("<br>code：").append(beatResult.getCode())
                    .append("<br>msg：").append(beatResult.getMsg());

            // beat success
            if (beatResult.isSuccess()) {

                beatResult.setMsg(beatResultSB.toString());
                beatResult.setData(address);
                return beatResult;
            }
        }
        return Response.ofFail( beatResultSB.toString());

    }
}
