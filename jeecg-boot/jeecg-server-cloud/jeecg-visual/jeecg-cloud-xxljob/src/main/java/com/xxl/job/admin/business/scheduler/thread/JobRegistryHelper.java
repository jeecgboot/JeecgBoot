package com.xxl.job.admin.business.scheduler.thread;

import com.xxl.job.admin.business.model.XxlJobGroup;
import com.xxl.job.admin.business.model.XxlJobRegistry;
import com.xxl.job.admin.business.scheduler.config.XxlJobAdminBootstrap;
import com.xxl.job.core.constant.Const;
import com.xxl.job.core.constant.RegistTypeEnum;
import com.xxl.job.core.openapi.model.RegistryRequest;
import com.xxl.tool.concurrent.CyclicThread;
import com.xxl.tool.core.StringTool;
import com.xxl.tool.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * job registry instance helper
 *
 * @author xuxueli 2016-10-02 19:10:24
 */
public class JobRegistryHelper {
	private static final Logger logger = LoggerFactory.getLogger(JobRegistryHelper.class);


	/**
	 * registry or remove thread pool
	 */
	private ThreadPoolExecutor registryOrRemoveThreadPool = null;

	/**
	 * registry monitor thread
	 */
	private CyclicThread registryMonitorThread;

	/**
	 * start
	 */
	public void start(){

		// 1、for registry or remove
		registryOrRemoveThreadPool = new ThreadPoolExecutor(
				2,
				10,
				30L,
				TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(2000),
				new ThreadFactory() {
					@Override
					public Thread newThread(Runnable r) {
						return new Thread(r, "xxl-job, admin JobRegistryMonitorHelper-registryOrRemoveThreadPool-" + r.hashCode());
					}
				},
				new RejectedExecutionHandler() {
					@Override
					public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
						r.run();
						logger.warn(">>>>>>>>>>> xxl-job, registry or remove too fast, match threadpool rejected handler(run now).");
					}
				});

		// 2、for registry monitor
		registryMonitorThread = new CyclicThread("JobRegistryHelper#registryMonitorThread", true, new Runnable() {
			@Override
			public void run() {
				// auto registry group
				List<XxlJobGroup> groupList = XxlJobAdminBootstrap.getInstance().getXxlJobGroupMapper().findByAddressType(0);
				if (groupList!=null && !groupList.isEmpty()) {

					// remove dead address (admin/executor)
					List<Integer> ids = XxlJobAdminBootstrap.getInstance().getXxlJobRegistryMapper().findDead(Const.DEAD_TIMEOUT, new Date());
					if (ids!=null && !ids.isEmpty()) {
						XxlJobAdminBootstrap.getInstance().getXxlJobRegistryMapper().removeDead(ids);
					}

					// fresh online address (admin/executor)
					HashMap<String, List<String>> appAddressMap = new HashMap<String, List<String>>();
					List<XxlJobRegistry> list = XxlJobAdminBootstrap.getInstance().getXxlJobRegistryMapper().findAll(Const.DEAD_TIMEOUT, new Date());
					if (list != null) {
						for (XxlJobRegistry item: list) {
							if (RegistTypeEnum.EXECUTOR.name().equals(item.getRegistryGroup())) {
								String appname = item.getRegistryKey();
								List<String> registryList = appAddressMap.get(appname);
								if (registryList == null) {
									registryList = new ArrayList<String>();
								}

								if (!registryList.contains(item.getRegistryValue())) {
									registryList.add(item.getRegistryValue());
								}
								appAddressMap.put(appname, registryList);
							}
						}
					}

					// fresh group address
					for (XxlJobGroup group: groupList) {
						List<String> registryList = appAddressMap.get(group.getAppname());
						String addressListStr = null;
						if (registryList!=null && !registryList.isEmpty()) {
							Collections.sort(registryList);
							StringBuilder addressListSB = new StringBuilder();
							for (String item:registryList) {
								addressListSB.append(item).append(",");
							}
							addressListStr = addressListSB.toString();
							addressListStr = addressListStr.substring(0, addressListStr.length()-1);
						}
						group.setAddressList(addressListStr);
						group.setUpdateTime(new Date());

						XxlJobAdminBootstrap.getInstance().getXxlJobGroupMapper().update(group);
					}
				}
			}
		}, Const.BEAT_TIMEOUT * 1000L, true);
		registryMonitorThread.start();
	}


	/**
	 * stop
	 */
	public void stop(){

		// 1、registryOrRemoveThreadPool
		registryOrRemoveThreadPool.shutdownNow();

		// 2、registryMonitorThread
		registryMonitorThread.stop();
	}


	// ---------------------- tool ----------------------

	/**
	 * registry
	 */
	public Response<String> registry(RegistryRequest registryParam) {

		// valid
		if (StringTool.isBlank(registryParam.getRegistryGroup())
				|| StringTool.isBlank(registryParam.getRegistryKey())
				|| StringTool.isBlank(registryParam.getRegistryValue())) {
			return Response.ofFail("Illegal Argument.");
		}

		// async execute
		registryOrRemoveThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				// 0-fail; 1-save suc; 2-update suc;
				int ret = XxlJobAdminBootstrap.getInstance().getXxlJobRegistryMapper().registrySaveOrUpdate(registryParam.getRegistryGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue(), new Date());
				if (ret == 1) {
					// fresh (add)
					freshGroupRegistryInfo(registryParam);
				}
				/*int ret = XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryDao().registryUpdate(registryParam.getRegistryGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue(), new Date());
				if (ret < 1) {
					XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryDao().registrySave(registryParam.getRegistryGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue(), new Date());

					// fresh
					freshGroupRegistryInfo(registryParam);
				}*/
			}
		});

		return Response.ofSuccess();
	}

	/**
	 * registry remove
	 */
	public Response<String> registryRemove(RegistryRequest registryParam) {

		// valid
		if (StringTool.isBlank(registryParam.getRegistryGroup())
				|| StringTool.isBlank(registryParam.getRegistryKey())
				|| StringTool.isBlank(registryParam.getRegistryValue())) {
			return Response.ofFail("Illegal Argument.");
		}

		// async execute
		registryOrRemoveThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				int ret = XxlJobAdminBootstrap.getInstance().getXxlJobRegistryMapper().registryDelete(registryParam.getRegistryGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue());
				if (ret > 0) {
					// fresh (delete)
					freshGroupRegistryInfo(registryParam);
				}
			}
		});

		return Response.ofSuccess();
	}

	private void freshGroupRegistryInfo(RegistryRequest registryParam){
		// Under consideration, prevent affecting core tables
	}


}
