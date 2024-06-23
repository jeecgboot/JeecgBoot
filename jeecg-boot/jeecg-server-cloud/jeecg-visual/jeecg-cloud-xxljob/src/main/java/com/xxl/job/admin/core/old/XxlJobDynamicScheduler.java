//package com.xxl.job.admin.core.schedule;
//
//import com.xxl.job.admin.core.conf.XxlJobAdminConfig;
//import com.xxl.job.admin.core.jobbean.RemoteHttpJobBean;
//import com.xxl.job.admin.core.model.XxlJobInfo;
//import com.xxl.job.admin.core.thread.JobFailMonitorHelper;
//import com.xxl.job.admin.core.thread.JobRegistryMonitorHelper;
//import com.xxl.job.admin.core.thread.JobTriggerPoolHelper;
//import com.xxl.job.admin.core.util.I18nUtil;
//import com.xxl.job.core.biz.AdminBiz;
//import com.xxl.job.core.biz.ExecutorBiz;
//import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
//import com.xxl.rpc.remoting.invoker.XxlRpcInvokerFactory;
//import com.xxl.rpc.remoting.invoker.call.CallType;
//import com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean;
//import com.xxl.rpc.remoting.invoker.route.LoadBalance;
//import com.xxl.rpc.remoting.net.NetEnum;
//import com.xxl.rpc.remoting.net.impl.servlet.server.ServletServerHandler;
//import com.xxl.rpc.remoting.provider.XxlRpcProviderFactory;
//import com.xxl.rpc.serialize.Serializer;
//import org.quartz.*;
//import org.quartz.Trigger.TriggerState;
//import org.quartz.impl.triggers.CronTriggerImpl;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.Assert;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Date;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * base quartz scheduler util
// * @author xuxueli 2015-12-19 16:13:53
// */
//public final class XxlJobDynamicScheduler {
//    private static final Logger logger = LoggerFactory.getLogger(XxlJobDynamicScheduler_old.class);
//
//    // ---------------------- param ----------------------
//
//    // scheduler
//    private static Scheduler scheduler;
//    public void setScheduler(Scheduler scheduler) {
//		XxlJobDynamicScheduler_old.scheduler = scheduler;
//	}
//
//
//    // ---------------------- init + destroy ----------------------
//    public void start() throws Exception {
//        // valid
//        Assert.notNull(scheduler, "quartz scheduler is null");
//
//        // init i18n
//        initI18n();
//
//        // admin registry monitor run
//        JobRegistryMonitorHelper.getInstance().start();
//
//        // admin monitor run
//        JobFailMonitorHelper.getInstance().start();
//
//        // admin-server
//        initRpcProvider();
//
//        logger.info(">>>>>>>>> init xxl-job admin success.");
//    }
//
//
//    public void destroy() throws Exception {
//        // admin trigger pool stop
//        JobTriggerPoolHelper.toStop();
//
//        // admin registry stop
//        JobRegistryMonitorHelper.getInstance().toStop();
//
//        // admin monitor stop
//        JobFailMonitorHelper.getInstance().toStop();
//
//        // admin-server
//        stopRpcProvider();
//    }
//
//
//    // ---------------------- I18n ----------------------
//
//    private void initI18n(){
//        for (ExecutorBlockStrategyEnum item:ExecutorBlockStrategyEnum.values()) {
//            item.setTitle(I18nUtil.getString("jobconf_block_".concat(item.name())));
//        }
//    }
//
//
//    // ---------------------- admin rpc provider (no server version) ----------------------
//    private static ServletServerHandler servletServerHandler;
//    private void initRpcProvider(){
//        // init
//        XxlRpcProviderFactory xxlRpcProviderFactory = new XxlRpcProviderFactory();
//        xxlRpcProviderFactory.initConfig(
//                NetEnum.NETTY_HTTP,
//                Serializer.SerializeEnum.HESSIAN.getSerializer(),
//                null,
//                0,
//                XxlJobAdminConfig.getAdminConfig().getAccessToken(),
//                null,
//                null);
//
//        // add services
//        xxlRpcProviderFactory.addService(AdminBiz.class.getName(), null, XxlJobAdminConfig.getAdminConfig().getAdminBiz());
//
//        // servlet handler
//        servletServerHandler = new ServletServerHandler(xxlRpcProviderFactory);
//    }
//    private void stopRpcProvider() throws Exception {
//        XxlRpcInvokerFactory.getInstance().stop();
//    }
//    public static void invokeAdminService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        servletServerHandler.handle(null, request, response);
//    }
//
//
//    // ---------------------- executor-client ----------------------
//    private static ConcurrentHashMap<String, ExecutorBiz> executorBizRepository = new ConcurrentHashMap<String, ExecutorBiz>();
//    public static ExecutorBiz getExecutorBiz(String address) throws Exception {
//        // valid
//        if (address==null || address.trim().length()==0) {
//            return null;
//        }
//
//        // load-cache
//        address = address.trim();
//        ExecutorBiz executorBiz = executorBizRepository.get(address);
//        if (executorBiz != null) {
//            return executorBiz;
//        }
//
//        // set-cache
//        executorBiz = (ExecutorBiz) new XxlRpcReferenceBean(
//                NetEnum.NETTY_HTTP,
//                Serializer.SerializeEnum.HESSIAN.getSerializer(),
//                CallType.SYNC,
//                LoadBalance.ROUND,
//                ExecutorBiz.class,
//                null,
//                5000,
//                address,
//                XxlJobAdminConfig.getAdminConfig().getAccessToken(),
//                null,
//                null).getObject();
//
//        executorBizRepository.put(address, executorBiz);
//        return executorBiz;
//    }
//
//
//    // ---------------------- schedule util ----------------------
//
//    /**
//     * fill job info
//     *
//     * @param jobInfo
//     */
//	public static void fillJobInfo(XxlJobInfo jobInfo) {
//
//        String name = String.valueOf(jobInfo.getId());
//
//        // trigger key
//        TriggerKey triggerKey = TriggerKey.triggerKey(name);
//        try {
//
//            // trigger cron
//			Trigger trigger = scheduler.getTrigger(triggerKey);
//			if (trigger!=null && trigger instanceof CronTriggerImpl) {
//				String cronExpression = ((CronTriggerImpl) trigger).getCronExpression();
//				jobInfo.setJobCron(cronExpression);
//			}
//
//            // trigger state
//            TriggerState triggerState = scheduler.getTriggerState(triggerKey);
//			if (triggerState!=null) {
//				jobInfo.setJobStatus(triggerState.name());
//			}
//
//            //JobKey jobKey = new JobKey(jobInfo.getJobName(), String.valueOf(jobInfo.getJobGroup()));
//            //JobDetail jobDetail = scheduler.getJobDetail(jobKey);
//            //String jobClass = jobDetail.getJobClass().getName();
//
//		} catch (SchedulerException e) {
//			logger.error(e.getMessage(), e);
//		}
//	}
//
//
//    /**
//     * add trigger + job
//     *
//     * @param jobName
//     * @param cronExpression
//     * @return
//     * @throws SchedulerException
//     */
//	public static boolean addJob(String jobName, String cronExpression) throws SchedulerException {
//    	// 1、job key
//        TriggerKey triggerKey = TriggerKey.triggerKey(jobName);
//        JobKey jobKey = new JobKey(jobName);
//
//        // 2、valid
//        if (scheduler.checkExists(triggerKey)) {
//            return true;    // PASS
//        }
//
//        // 3、corn trigger
//        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();   // withMisfireHandlingInstructionDoNothing 忽略掉调度终止过程中忽略的调度
//        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
//
//        // 4、job detail
//		Class<? extends Job> jobClass_ = RemoteHttpJobBean.class;   // Class.forName(jobInfo.getJobClass());
//		JobDetail jobDetail = JobBuilder.newJob(jobClass_).withIdentity(jobKey).build();
//
//        /*if (jobInfo.getJobData()!=null) {
//        	JobDataMap jobDataMap = jobDetail.getJobDataMap();
//        	jobDataMap.putAll(JacksonUtil.readValue(jobInfo.getJobData(), Map.class));
//        	// JobExecutionContext context.getMergedJobDataMap().get("mailGuid");
//		}*/
//
//        // 5、schedule job
//        Date date = scheduler.scheduleJob(jobDetail, cronTrigger);
//
//        logger.info(">>>>>>>>>>> addJob success(quartz), jobDetail:{}, cronTrigger:{}, date:{}", jobDetail, cronTrigger, date);
//        return true;
//    }
//
//
//    /**
//     * remove trigger + job
//     *
//     * @param jobName
//     * @return
//     * @throws SchedulerException
//     */
//    public static boolean removeJob(String jobName) throws SchedulerException {
//
//        JobKey jobKey = new JobKey(jobName);
//        scheduler.deleteJob(jobKey);
//
//        /*TriggerKey triggerKey = TriggerKey.triggerKey(jobName);
//        if (scheduler.checkExists(triggerKey)) {
//            scheduler.unscheduleJob(triggerKey);    // trigger + job
//        }*/
//
//        logger.info(">>>>>>>>>>> removeJob success(quartz), jobKey:{}", jobKey);
//        return true;
//    }
//
//
//    /**
//     * updateJobCron
//     *
//     * @param jobName
//     * @param cronExpression
//     * @return
//     * @throws SchedulerException
//     */
//	public static boolean updateJobCron(String jobName, String cronExpression) throws SchedulerException {
//
//        // 1、job key
//        TriggerKey triggerKey = TriggerKey.triggerKey(jobName);
//
//        // 2、valid
//        if (!scheduler.checkExists(triggerKey)) {
//            return true;    // PASS
//        }
//
//        CronTrigger oldTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
//
//        // 3、avoid repeat cron
//        String oldCron = oldTrigger.getCronExpression();
//        if (oldCron.equals(cronExpression)){
//            return true;    // PASS
//        }
//
//        // 4、new cron trigger
//        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression).withMisfireHandlingInstructionDoNothing();
//        oldTrigger = oldTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
//
//        // 5、rescheduleJob
//        scheduler.rescheduleJob(triggerKey, oldTrigger);
//
//        /*
//        JobKey jobKey = new JobKey(jobName);
//
//        // old job detail
//        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
//
//        // new trigger
//        HashSet<Trigger> triggerSet = new HashSet<Trigger>();
//        triggerSet.add(cronTrigger);
//        // cover trigger of job detail
//        scheduler.scheduleJob(jobDetail, triggerSet, true);*/
//
//        logger.info(">>>>>>>>>>> resumeJob success, JobName:{}", jobName);
//        return true;
//    }
//
//
//    /**
//     * pause
//     *
//     * @param jobName
//     * @return
//     * @throws SchedulerException
//     */
//    /*public static boolean pauseJob(String jobName) throws SchedulerException {
//
//    	TriggerKey triggerKey = TriggerKey.triggerKey(jobName);
//
//        boolean result = false;
//        if (scheduler.checkExists(triggerKey)) {
//            scheduler.pauseTrigger(triggerKey);
//            result =  true;
//        }
//
//        logger.info(">>>>>>>>>>> pauseJob {}, triggerKey:{}", (result?"success":"fail"),triggerKey);
//        return result;
//    }*/
//
//
//    /**
//     * resume
//     *
//     * @param jobName
//     * @return
//     * @throws SchedulerException
//     */
//    /*public static boolean resumeJob(String jobName) throws SchedulerException {
//
//        TriggerKey triggerKey = TriggerKey.triggerKey(jobName);
//
//        boolean result = false;
//        if (scheduler.checkExists(triggerKey)) {
//            scheduler.resumeTrigger(triggerKey);
//            result = true;
//        }
//
//        logger.info(">>>>>>>>>>> resumeJob {}, triggerKey:{}", (result?"success":"fail"), triggerKey);
//        return result;
//    }*/
//
//
//    /**
//     * run
//     *
//     * @param jobName
//     * @return
//     * @throws SchedulerException
//     */
//    /*public static boolean triggerJob(String jobName) throws SchedulerException {
//    	// TriggerKey : name + group
//    	JobKey jobKey = new JobKey(jobName);
//        TriggerKey triggerKey = TriggerKey.triggerKey(jobName);
//
//        boolean result = false;
//        if (scheduler.checkExists(triggerKey)) {
//            scheduler.triggerJob(jobKey);
//            result = true;
//            logger.info(">>>>>>>>>>> runJob success, jobKey:{}", jobKey);
//        } else {
//        	logger.info(">>>>>>>>>>> runJob fail, jobKey:{}", jobKey);
//        }
//        return result;
//    }*/
//
//
//    /**
//     * finaAllJobList
//     *
//     * @return
//     *//*
//    @Deprecated
//    public static List<Map<String, Object>> finaAllJobList(){
//        List<Map<String, Object>> jobList = new ArrayList<Map<String,Object>>();
//
//        try {
//            if (scheduler.getJobGroupNames()==null || scheduler.getJobGroupNames().size()==0) {
//                return null;
//            }
//            String groupName = scheduler.getJobGroupNames().get(0);
//            Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName));
//            if (jobKeys!=null && jobKeys.size()>0) {
//                for (JobKey jobKey : jobKeys) {
//                    TriggerKey triggerKey = TriggerKey.triggerKey(jobKey.getName(), Scheduler.DEFAULT_GROUP);
//                    Trigger trigger = scheduler.getTrigger(triggerKey);
//                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
//                    TriggerState triggerState = scheduler.getTriggerState(triggerKey);
//                    Map<String, Object> jobMap = new HashMap<String, Object>();
//                    jobMap.put("TriggerKey", triggerKey);
//                    jobMap.put("Trigger", trigger);
//                    jobMap.put("JobDetail", jobDetail);
//                    jobMap.put("TriggerState", triggerState);
//                    jobList.add(jobMap);
//                }
//            }
//
//        } catch (SchedulerException e) {
//            logger.error(e.getMessage(), e);
//            return null;
//        }
//        return jobList;
//    }*/
//
//}