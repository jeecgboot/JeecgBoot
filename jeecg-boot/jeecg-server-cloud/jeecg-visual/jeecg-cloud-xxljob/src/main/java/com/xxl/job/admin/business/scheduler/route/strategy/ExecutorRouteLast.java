package com.xxl.job.admin.business.scheduler.route.strategy;

import com.xxl.job.admin.business.scheduler.route.ExecutorRouter;
import com.xxl.job.core.openapi.model.TriggerRequest;
import com.xxl.tool.response.Response;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteLast extends ExecutorRouter {

    @Override
    public Response<String> route(TriggerRequest triggerParam, List<String> addressList) {
        return Response.ofSuccess(addressList.get(addressList.size()-1));
    }

}
