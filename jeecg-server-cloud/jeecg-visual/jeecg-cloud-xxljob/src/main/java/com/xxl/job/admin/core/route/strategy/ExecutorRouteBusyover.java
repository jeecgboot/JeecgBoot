package com.xxl.job.admin.core.route.strategy;

import com.xxl.job.admin.core.scheduler.XxlJobScheduler;
import com.xxl.job.admin.core.route.ExecutorRouter;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.core.biz.ExecutorBiz;
import com.xxl.job.core.biz.model.IdleBeatParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.biz.model.TriggerParam;

import java.util.List;

/**
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteBusyover extends ExecutorRouter {

	@Override
	public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
		StringBuffer idleBeatResultSB = new StringBuffer();
		for (String address : addressList) {
			// beat
			ReturnT<String> idleBeatResult = null;
			try {
				ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(address);
				idleBeatResult = executorBiz.idleBeat(new IdleBeatParam(triggerParam.getJobId()));
			}
			catch (Exception e) {
				logger.error(e.getMessage(), e);
				idleBeatResult = new ReturnT<String>(ReturnT.FAIL_CODE, "" + e);
			}
			idleBeatResultSB.append((idleBeatResultSB.length() > 0) ? "<br><br>" : "")
				.append(I18nUtil.getString("jobconf_idleBeat") + "：")
				.append("<br>address：")
				.append(address)
				.append("<br>code：")
				.append(idleBeatResult.getCode())
				.append("<br>msg：")
				.append(idleBeatResult.getMsg());

			// beat success
			if (idleBeatResult.getCode() == ReturnT.SUCCESS_CODE) {
				idleBeatResult.setMsg(idleBeatResultSB.toString());
				idleBeatResult.setContent(address);
				return idleBeatResult;
			}
		}

		return new ReturnT<String>(ReturnT.FAIL_CODE, idleBeatResultSB.toString());
	}

}
