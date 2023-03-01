package com.xxl.job.admin.core.route.strategy;

import com.xxl.job.admin.core.route.ExecutorRouter;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.biz.model.TriggerParam;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 单个JOB对应的每个执行器，使用频率最低的优先被选举 a(*)、LFU(Least Frequently Used)：最不经常使用，频率/次数 b、LRU(Least
 * Recently Used)：最近最久未使用，时间
 *
 * Created by xuxueli on 17/3/10.
 */
public class ExecutorRouteLFU extends ExecutorRouter {

	private static ConcurrentMap<Integer, HashMap<String, Integer>> jobLfuMap = new ConcurrentHashMap<Integer, HashMap<String, Integer>>();

	private static long CACHE_VALID_TIME = 0;

	public String route(int jobId, List<String> addressList) {

		// cache clear
		if (System.currentTimeMillis() > CACHE_VALID_TIME) {
			jobLfuMap.clear();
			CACHE_VALID_TIME = System.currentTimeMillis() + 1000 * 60 * 60 * 24;
		}

		// lfu item init
		HashMap<String, Integer> lfuItemMap = jobLfuMap.get(jobId); // Key排序可以用TreeMap+构造入参Compare；Value排序暂时只能通过ArrayList；
		if (lfuItemMap == null) {
			lfuItemMap = new HashMap<String, Integer>();
			jobLfuMap.putIfAbsent(jobId, lfuItemMap); // 避免重复覆盖
		}

		// put new
		for (String address : addressList) {
			if (!lfuItemMap.containsKey(address) || lfuItemMap.get(address) > 1000000) {
				lfuItemMap.put(address, new Random().nextInt(addressList.size())); // 初始化时主动Random一次，缓解首次压力
			}
		}
		// remove old
		List<String> delKeys = new ArrayList<>();
		for (String existKey : lfuItemMap.keySet()) {
			if (!addressList.contains(existKey)) {
				delKeys.add(existKey);
			}
		}
		if (delKeys.size() > 0) {
			for (String delKey : delKeys) {
				lfuItemMap.remove(delKey);
			}
		}

		// load least userd count address
		List<Map.Entry<String, Integer>> lfuItemList = new ArrayList<Map.Entry<String, Integer>>(lfuItemMap.entrySet());
		Collections.sort(lfuItemList, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return o1.getValue().compareTo(o2.getValue());
			}
		});

		Map.Entry<String, Integer> addressItem = lfuItemList.get(0);
		String minAddress = addressItem.getKey();
		addressItem.setValue(addressItem.getValue() + 1);

		return addressItem.getKey();
	}

	@Override
	public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
		String address = route(triggerParam.getJobId(), addressList);
		return new ReturnT<String>(address);
	}

}
