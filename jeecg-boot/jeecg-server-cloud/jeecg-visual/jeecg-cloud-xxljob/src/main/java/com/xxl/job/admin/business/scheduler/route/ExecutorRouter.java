package com.xxl.job.admin.business.scheduler.route;

import com.xxl.job.core.openapi.model.TriggerRequest;
import com.xxl.tool.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public abstract class ExecutorRouter {
    protected static Logger logger = LoggerFactory.getLogger(ExecutorRouter.class);

    /**
     * route address
     *
     * @param addressList  executor address list
     * @return  ReturnT.content=address
     */
    public abstract Response<String> route(TriggerRequest triggerParam, List<String> addressList);

}
