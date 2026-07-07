package com.xxl.job.admin.business.service.impl;

import com.xxl.job.admin.business.constant.TriggerStatus;
import com.xxl.job.admin.business.mapper.*;
import com.xxl.job.admin.business.model.XxlJobGroup;
import com.xxl.job.admin.business.model.XxlJobInfo;
import com.xxl.job.admin.business.model.XxlJobLogReport;
import com.xxl.job.admin.business.scheduler.config.XxlJobAdminBootstrap;
import com.xxl.job.admin.business.scheduler.cron.CronExpression;
import com.xxl.job.admin.business.scheduler.misfire.MisfireStrategyEnum;
import com.xxl.job.admin.business.scheduler.route.ExecutorRouteStrategyEnum;
import com.xxl.job.admin.business.scheduler.thread.JobScheduleHelper;
import com.xxl.job.admin.business.scheduler.trigger.TriggerTypeEnum;
import com.xxl.job.admin.business.scheduler.type.ScheduleTypeEnum;
import com.xxl.job.admin.business.service.XxlJobService;
import com.xxl.job.admin.framework.util.I18nUtil;
import com.xxl.job.admin.framework.util.JobGroupPermissionUtil;
import com.xxl.job.core.constant.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.sso.core.model.LoginInfo;
import com.xxl.tool.core.DateTool;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.json.GsonTool;
import com.xxl.tool.response.PageModel;
import com.xxl.tool.response.Response;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;

/**
 * core job action for xxl-job
 * @author xuxueli 2016-5-28 15:30:33
 */
@Service
public class XxlJobServiceImpl implements XxlJobService {
	private static Logger logger = LoggerFactory.getLogger(XxlJobServiceImpl.class);

	@Resource
	private XxlJobGroupMapper xxlJobGroupMapper;
	@Resource
	private XxlJobInfoMapper xxlJobInfoMapper;
	@Resource
	public XxlJobLogMapper xxlJobLogMapper;
	@Resource
	private XxlJobLogGlueMapper xxlJobLogGlueMapper;
	@Resource
	private XxlJobLogReportMapper xxlJobLogReportMapper;
	
	@Override
	public Response<PageModel<XxlJobInfo>> pageList(int offset, int pagesize, int jobGroup, int triggerStatus, String jobDesc, String executorHandler, String author) {

		// page list
		List<XxlJobInfo> list = xxlJobInfoMapper.pageList(offset, pagesize, jobGroup, triggerStatus, jobDesc, executorHandler, author);
		int list_count = xxlJobInfoMapper.pageListCount(offset, pagesize, jobGroup, triggerStatus, jobDesc, executorHandler, author);

		// package result
		PageModel<XxlJobInfo> pageModel = new PageModel<>();
		pageModel.setData(list);
		pageModel.setTotal(list_count);

		return Response.ofSuccess(pageModel);
	}

	@Override
	public Response<String> add(XxlJobInfo jobInfo, LoginInfo loginInfo) {

		// valid base
		XxlJobGroup group = xxlJobGroupMapper.load(jobInfo.getJobGroup());
		if (group == null) {
			return Response.ofFail (I18nUtil.getString("system_please_choose")+I18nUtil.getString("jobinfo_field_jobgroup"));
		}
		if (StringTool.isBlank(jobInfo.getJobDesc())) {
			return Response.ofFail ( (I18nUtil.getString("system_please_input")+I18nUtil.getString("jobinfo_field_jobdesc")) );
		}
		if (StringTool.isBlank(jobInfo.getAuthor())) {
			return Response.ofFail ( (I18nUtil.getString("system_please_input")+I18nUtil.getString("jobinfo_field_author")) );
		}

		// valid trigger
		ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(jobInfo.getScheduleType(), null);
		if (scheduleTypeEnum == null) {
			return Response.ofFail ( (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_invalid")) );
		}
		if (scheduleTypeEnum == ScheduleTypeEnum.CRON) {
			if (jobInfo.getScheduleConf()==null || !CronExpression.isValidExpression(jobInfo.getScheduleConf())) {
				return Response.ofFail ( "Cron"+I18nUtil.getString("system_invalid"));
			}
		} else if (scheduleTypeEnum == ScheduleTypeEnum.FIX_RATE/* || scheduleTypeEnum == ScheduleTypeEnum.FIX_DELAY*/) {
			if (jobInfo.getScheduleConf() == null) {
				return Response.ofFail ( (I18nUtil.getString("schedule_type")) );
			}
			try {
				int fixSecond = Integer.parseInt(jobInfo.getScheduleConf());
				if (fixSecond < 1) {
					return Response.ofFail ( (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_invalid")) );
				}
			} catch (Exception e) {
				return Response.ofFail ( (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_invalid")) );
			}
		}

		// valid job
		if (GlueTypeEnum.match(jobInfo.getGlueType()) == null) {
			return Response.ofFail ( (I18nUtil.getString("jobinfo_field_gluetype")+I18nUtil.getString("system_invalid")) );
		}
		if (GlueTypeEnum.BEAN==GlueTypeEnum.match(jobInfo.getGlueType()) && StringTool.isBlank(jobInfo.getExecutorHandler()) ) {
			return Response.ofFail ( (I18nUtil.getString("system_please_input")+"JobHandler") );
		}
		// 》fix "\r" in shell
		if (GlueTypeEnum.GLUE_SHELL==GlueTypeEnum.match(jobInfo.getGlueType()) && jobInfo.getGlueSource()!=null) {
			jobInfo.setGlueSource(jobInfo.getGlueSource().replaceAll("\r", ""));
		}

		// valid advanced
		if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
			return Response.ofFail ( (I18nUtil.getString("jobinfo_field_executorRouteStrategy")+I18nUtil.getString("system_invalid")) );
		}
		if (MisfireStrategyEnum.match(jobInfo.getMisfireStrategy(), null) == null) {
			return Response.ofFail ( (I18nUtil.getString("misfire_strategy")+I18nUtil.getString("system_invalid")) );
		}
		if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
			return Response.ofFail ( (I18nUtil.getString("jobinfo_field_executorBlockStrategy")+I18nUtil.getString("system_invalid")) );
		}

		// 》ChildJobId valid
		if (StringTool.isNotBlank(jobInfo.getChildJobId())) {
			String[] childJobIds = jobInfo.getChildJobId().split(",");
			for (String childJobIdItem: childJobIds) {
				if (StringTool.isNotBlank(childJobIdItem) && StringTool.isNumeric(childJobIdItem)) {
					XxlJobInfo childJobInfo = xxlJobInfoMapper.loadById(Integer.parseInt(childJobIdItem));
					if (childJobInfo==null) {
						return Response.ofFail (
								MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId")+"({0})"+I18nUtil.getString("system_not_found")), childJobIdItem));
					}
					// valid jobGroup permission
					if (!JobGroupPermissionUtil.hasJobGroupPermission(loginInfo, childJobInfo.getJobGroup())) {
						return Response.ofFail (
								MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId")+"({0})"+I18nUtil.getString("system_permission_limit")), childJobIdItem));
					}
				} else {
					return Response.ofFail (
							MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId")+"({0})"+I18nUtil.getString("system_invalid")), childJobIdItem));
				}
			}

			// join , avoid "xxx,,"
			String temp = "";
			for (String item:childJobIds) {
				temp += item + ",";
			}
			temp = temp.substring(0, temp.length()-1);

			jobInfo.setChildJobId(temp);
		}

		// add in db
		jobInfo.setAddTime(new Date());
		jobInfo.setUpdateTime(new Date());
		jobInfo.setGlueUpdatetime(new Date());
		// remove the whitespace
		jobInfo.setExecutorHandler(jobInfo.getExecutorHandler().trim());
		xxlJobInfoMapper.save(jobInfo);
		if (jobInfo.getId() < 1) {
			return Response.ofFail ( (I18nUtil.getString("jobinfo_field_add")+I18nUtil.getString("system_fail")) );
		}

		// write operation log
		logger.info(">>>>>>>>>>> xxl-job operation log: operator = {}, type = {}, content = {}",
				loginInfo.getUserName(), "jobinfo-save", GsonTool.toJson(jobInfo));

		return Response.ofSuccess(String.valueOf(jobInfo.getId()));
	}

	@Override
	public Response<String> update(XxlJobInfo jobInfo, LoginInfo loginInfo) {

		// valid base
		if (StringTool.isBlank(jobInfo.getJobDesc())) {
			return Response.ofFail ( (I18nUtil.getString("system_please_input")+I18nUtil.getString("jobinfo_field_jobdesc")) );
		}
		if (StringTool.isBlank(jobInfo.getAuthor())) {
			return Response.ofFail ( (I18nUtil.getString("system_please_input")+I18nUtil.getString("jobinfo_field_author")) );
		}

		// valid trigger
		ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(jobInfo.getScheduleType(), null);
		if (scheduleTypeEnum == null) {
			return Response.ofFail ( (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_invalid")) );
		}
		if (scheduleTypeEnum == ScheduleTypeEnum.CRON) {
			if (jobInfo.getScheduleConf()==null || !CronExpression.isValidExpression(jobInfo.getScheduleConf())) {
				return Response.ofFail ( "Cron"+I18nUtil.getString("system_invalid") );
			}
		} else if (scheduleTypeEnum == ScheduleTypeEnum.FIX_RATE /*|| scheduleTypeEnum == ScheduleTypeEnum.FIX_DELAY*/) {
			if (jobInfo.getScheduleConf() == null) {
				return Response.ofFail ( (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_invalid")) );
			}
			try {
				int fixSecond = Integer.parseInt(jobInfo.getScheduleConf());
				if (fixSecond < 1) {
					return Response.ofFail ( (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_invalid")) );
				}
			} catch (Exception e) {
				return Response.ofFail ( (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_invalid")) );
			}
		}

		// valid advanced
		if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
			return Response.ofFail ( (I18nUtil.getString("jobinfo_field_executorRouteStrategy")+I18nUtil.getString("system_invalid")) );
		}
		if (MisfireStrategyEnum.match(jobInfo.getMisfireStrategy(), null) == null) {
			return Response.ofFail ( (I18nUtil.getString("misfire_strategy")+I18nUtil.getString("system_invalid")) );
		}
		if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
			return Response.ofFail ( (I18nUtil.getString("jobinfo_field_executorBlockStrategy")+I18nUtil.getString("system_invalid")) );
		}

		// 》ChildJobId valid
		if (StringTool.isNotBlank(jobInfo.getChildJobId())) {
			String[] childJobIds = jobInfo.getChildJobId().split(",");
			for (String childJobIdItem: childJobIds) {
				if (StringTool.isNotBlank(childJobIdItem) && StringTool.isNumeric(childJobIdItem)) {
					// parse child
					int childJobId = Integer.parseInt(childJobIdItem);
					if (childJobId == jobInfo.getId()) {
						return Response.ofFail ( (I18nUtil.getString("jobinfo_field_childJobId")+"("+childJobId+")"+I18nUtil.getString("system_invalid")) );
					}

					// valid child
					XxlJobInfo childJobInfo = xxlJobInfoMapper.loadById(childJobId);
					if (childJobInfo==null) {
						return Response.ofFail (
								MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId")+"({0})"+I18nUtil.getString("system_not_found")), childJobIdItem));
					}
					// valid jobGroup permission
					if (!JobGroupPermissionUtil.hasJobGroupPermission(loginInfo, childJobInfo.getJobGroup())) {
						return Response.ofFail (
								MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId")+"({0})"+I18nUtil.getString("system_permission_limit")), childJobIdItem));
					}
				} else {
					return Response.ofFail (
							MessageFormat.format((I18nUtil.getString("jobinfo_field_childJobId")+"({0})"+I18nUtil.getString("system_invalid")), childJobIdItem));
				}
			}

			// join , avoid "xxx,,"
			String temp = "";
			for (String item:childJobIds) {
				temp += item + ",";
			}
			temp = temp.substring(0, temp.length()-1);

			jobInfo.setChildJobId(temp);
		}

		// group valid
		XxlJobGroup jobGroup = xxlJobGroupMapper.load(jobInfo.getJobGroup());
		if (jobGroup == null) {
			return Response.ofFail ( (I18nUtil.getString("jobinfo_field_jobgroup")+I18nUtil.getString("system_invalid")) );
		}

		// stage job info
		XxlJobInfo exists_jobInfo = xxlJobInfoMapper.loadById(jobInfo.getId());
		if (exists_jobInfo == null) {
			return Response.ofFail ( (I18nUtil.getString("jobinfo_field_id")+I18nUtil.getString("system_not_found")) );
		}

		// next trigger time (5s后生效，避开预读周期)
		long nextTriggerTime = exists_jobInfo.getTriggerNextTime();
		boolean scheduleDataNotChanged = jobInfo.getScheduleType().equals(exists_jobInfo.getScheduleType())
				&& jobInfo.getScheduleConf().equals(exists_jobInfo.getScheduleConf());		// 触发配置如果不变，避免重复计算；
		if (exists_jobInfo.getTriggerStatus() == TriggerStatus.RUNNING.getValue() && !scheduleDataNotChanged) {
			try {
				// generate next trigger time
				Date nextValidTime = scheduleTypeEnum.getScheduleType().generateNextTriggerTime(jobInfo, new Date(System.currentTimeMillis() + JobScheduleHelper.PRE_READ_MS));
				if (nextValidTime == null) {
					return Response.ofFail ( (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_invalid")) );
				}
				nextTriggerTime = nextValidTime.getTime();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return Response.ofFail ( (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_invalid")) );
			}
		}

		exists_jobInfo.setJobGroup(jobInfo.getJobGroup());
		exists_jobInfo.setJobDesc(jobInfo.getJobDesc());
		exists_jobInfo.setAuthor(jobInfo.getAuthor());
		exists_jobInfo.setAlarmEmail(jobInfo.getAlarmEmail());
		exists_jobInfo.setScheduleType(jobInfo.getScheduleType());
		exists_jobInfo.setScheduleConf(jobInfo.getScheduleConf());
		exists_jobInfo.setMisfireStrategy(jobInfo.getMisfireStrategy());
		exists_jobInfo.setExecutorRouteStrategy(jobInfo.getExecutorRouteStrategy());
		// remove the whitespace
		exists_jobInfo.setExecutorHandler(jobInfo.getExecutorHandler().trim());
		exists_jobInfo.setExecutorParam(jobInfo.getExecutorParam());
		exists_jobInfo.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
		exists_jobInfo.setExecutorTimeout(jobInfo.getExecutorTimeout());
		exists_jobInfo.setExecutorFailRetryCount(jobInfo.getExecutorFailRetryCount());
		exists_jobInfo.setChildJobId(jobInfo.getChildJobId());
		exists_jobInfo.setTriggerNextTime(nextTriggerTime);

		exists_jobInfo.setUpdateTime(new Date());
        xxlJobInfoMapper.update(exists_jobInfo);

		// write operation log
		logger.info(">>>>>>>>>>> xxl-job operation log: operator = {}, type = {}, content = {}",
				loginInfo.getUserName(), "jobinfo-update", GsonTool.toJson(exists_jobInfo));

		return Response.ofSuccess();
	}

	@Override
	public Response<String> remove(int id, LoginInfo loginInfo) {
		// valid job
		XxlJobInfo xxlJobInfo = xxlJobInfoMapper.loadById(id);
		if (xxlJobInfo == null) {
			return Response.ofSuccess();
		}

		// valid jobGroup permission
		if (!JobGroupPermissionUtil.hasJobGroupPermission(loginInfo, xxlJobInfo.getJobGroup())) {
			return Response.ofFail(I18nUtil.getString("system_permission_limit"));
		}

		xxlJobInfoMapper.delete(id);
		xxlJobLogMapper.delete(id);
		xxlJobLogGlueMapper.deleteByJobId(id);

		// write operation log
		logger.info(">>>>>>>>>>> xxl-job operation log: operator = {}, type = {}, content = {}",
				loginInfo.getUserName(), "jobinfo-remove", id);

		return Response.ofSuccess();
	}

	@Override
	public Response<String> start(int id, LoginInfo loginInfo) {
		// load and valid
		XxlJobInfo xxlJobInfo = xxlJobInfoMapper.loadById(id);
		if (xxlJobInfo == null) {
			return Response.ofFail(I18nUtil.getString("jobinfo_glue_jobid_invalid"));
		}

		// valid jobGroup permission
		if (!JobGroupPermissionUtil.hasJobGroupPermission(loginInfo, xxlJobInfo.getJobGroup())) {
			return Response.ofFail(I18nUtil.getString("system_permission_limit"));
		}

		// valid ScheduleType: can not be none
		ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(xxlJobInfo.getScheduleType(), ScheduleTypeEnum.NONE);
		if (ScheduleTypeEnum.NONE == scheduleTypeEnum) {
			return Response.ofFail(I18nUtil.getString("schedule_type_none_limit_start"));
		}

		// next trigger time (5s后生效，避开预读周期)
		long nextTriggerTime = 0;
		try {
			// generate next trigger time
			Date nextValidTime = scheduleTypeEnum.getScheduleType().generateNextTriggerTime(xxlJobInfo, new Date(System.currentTimeMillis() + JobScheduleHelper.PRE_READ_MS));

			if (nextValidTime == null) {
				return Response.ofFail ( (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_invalid")) );
			}
			nextTriggerTime = nextValidTime.getTime();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return Response.ofFail ( (I18nUtil.getString("schedule_type")+I18nUtil.getString("system_invalid")) );
		}

		xxlJobInfo.setTriggerStatus(TriggerStatus.RUNNING.getValue());
		xxlJobInfo.setTriggerLastTime(0);
		xxlJobInfo.setTriggerNextTime(nextTriggerTime);

		xxlJobInfo.setUpdateTime(new Date());
		xxlJobInfoMapper.update(xxlJobInfo);

		// write operation log
		logger.info(">>>>>>>>>>> xxl-job operation log: operator = {}, type = {}, content = {}",
				loginInfo.getUserName(), "jobinfo-start", id);

		return Response.ofSuccess();
	}

	@Override
	public Response<String> stop(int id, LoginInfo loginInfo) {
		// load and valid
        XxlJobInfo xxlJobInfo = xxlJobInfoMapper.loadById(id);
		if (xxlJobInfo == null) {
			return Response.ofFail(I18nUtil.getString("jobinfo_glue_jobid_invalid"));
		}

		// valid jobGroup permission
		if (!JobGroupPermissionUtil.hasJobGroupPermission(loginInfo, xxlJobInfo.getJobGroup())) {
			return Response.ofFail(I18nUtil.getString("system_permission_limit"));
		}

		// stop
		xxlJobInfo.setTriggerStatus(TriggerStatus.STOPPED.getValue());
		xxlJobInfo.setTriggerLastTime(0);
		xxlJobInfo.setTriggerNextTime(0);

		xxlJobInfo.setUpdateTime(new Date());
		xxlJobInfoMapper.update(xxlJobInfo);

		// write operation log
		logger.info(">>>>>>>>>>> xxl-job operation log: operator = {}, type = {}, content = {}",
				loginInfo.getUserName(), "jobinfo-stop", id);

		return Response.ofSuccess();
	}

	@Override
	public Response<String> trigger(LoginInfo loginInfo, int jobId, String executorParam, String addressList) {
		// valid job
		XxlJobInfo xxlJobInfo = xxlJobInfoMapper.loadById(jobId);
		if (xxlJobInfo == null) {
			return Response.ofFail(I18nUtil.getString("jobinfo_glue_jobid_invalid"));
		}

		// valid jobGroup permission
		if (!JobGroupPermissionUtil.hasJobGroupPermission(loginInfo, xxlJobInfo.getJobGroup())) {
			return Response.ofFail(I18nUtil.getString("system_permission_limit"));
		}

		// force cover job param
		if (executorParam == null) {
			executorParam = "";
		}

		XxlJobAdminBootstrap.getInstance().getJobTriggerPoolHelper().trigger(jobId, TriggerTypeEnum.MANUAL, -1, null, executorParam, addressList);

		// write operation log
		logger.info(">>>>>>>>>>> xxl-job operation log: operator = {}, type = {}, content = {}",
				loginInfo.getUserName(), "jobinfo-trigger", jobId);

		return Response.ofSuccess();
	}

	@Override
	public Map<String, Object> dashboardInfo() {

		int jobInfoCount = xxlJobInfoMapper.findAllCount();
		int jobLogCount = 0;
		int jobLogSuccessCount = 0;
		XxlJobLogReport xxlJobLogReport = xxlJobLogReportMapper.queryLogReportTotal();
		if (xxlJobLogReport != null) {
			jobLogCount = xxlJobLogReport.getRunningCount() + xxlJobLogReport.getSucCount() + xxlJobLogReport.getFailCount();
			jobLogSuccessCount = xxlJobLogReport.getSucCount();
		}

		// executor count
		Set<String> executorAddressSet = new HashSet<String>();
		List<XxlJobGroup> groupList = xxlJobGroupMapper.findAll();

		if (groupList!=null && !groupList.isEmpty()) {
			for (XxlJobGroup group: groupList) {
				if (group.getRegistryList()!=null && !group.getRegistryList().isEmpty()) {
					executorAddressSet.addAll(group.getRegistryList());
				}
			}
		}

		int executorCount = executorAddressSet.size();

		Map<String, Object> dashboardMap = new HashMap<String, Object>();
		dashboardMap.put("jobInfoCount", jobInfoCount);
		dashboardMap.put("jobLogCount", jobLogCount);
		dashboardMap.put("jobLogSuccessCount", jobLogSuccessCount);
		dashboardMap.put("executorCount", executorCount);
		return dashboardMap;
	}

	@Override
	public Response<Map<String, Object>> chartInfo(Date startDate, Date endDate) {

		// process
		List<String> triggerDayList = new ArrayList<String>();
		List<Integer> triggerDayCountRunningList = new ArrayList<Integer>();
		List<Integer> triggerDayCountSucList = new ArrayList<Integer>();
		List<Integer> triggerDayCountFailList = new ArrayList<Integer>();
		int triggerCountRunningTotal = 0;
		int triggerCountSucTotal = 0;
		int triggerCountFailTotal = 0;

		List<XxlJobLogReport> logReportList = xxlJobLogReportMapper.queryLogReport(startDate, endDate);

		if (logReportList!=null && !logReportList.isEmpty()) {
			for (XxlJobLogReport item: logReportList) {
				String day = DateTool.formatDate(item.getTriggerDay());
				int triggerDayCountRunning = item.getRunningCount();
				int triggerDayCountSuc = item.getSucCount();
				int triggerDayCountFail = item.getFailCount();

				triggerDayList.add(day);
				triggerDayCountRunningList.add(triggerDayCountRunning);
				triggerDayCountSucList.add(triggerDayCountSuc);
				triggerDayCountFailList.add(triggerDayCountFail);

				triggerCountRunningTotal += triggerDayCountRunning;
				triggerCountSucTotal += triggerDayCountSuc;
				triggerCountFailTotal += triggerDayCountFail;
			}
		} else {
			for (int i = -6; i <= 0; i++) {
				triggerDayList.add(DateTool.formatDate(DateTool.addDays(new Date(), i)));
				triggerDayCountRunningList.add(0);
				triggerDayCountSucList.add(0);
				triggerDayCountFailList.add(0);
			}
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("triggerDayList", triggerDayList);
		result.put("triggerDayCountRunningList", triggerDayCountRunningList);
		result.put("triggerDayCountSucList", triggerDayCountSucList);
		result.put("triggerDayCountFailList", triggerDayCountFailList);

		result.put("triggerCountRunningTotal", triggerCountRunningTotal);
		result.put("triggerCountSucTotal", triggerCountSucTotal);
		result.put("triggerCountFailTotal", triggerCountFailTotal);

		return Response.ofSuccess(result);
	}

}
