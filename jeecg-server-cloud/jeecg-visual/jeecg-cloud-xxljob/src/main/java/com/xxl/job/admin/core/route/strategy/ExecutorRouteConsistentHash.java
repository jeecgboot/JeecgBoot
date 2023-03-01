package com.xxl.job.admin.core.route.strategy;

import com.xxl.job.admin.core.route.ExecutorRouter;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.biz.model.TriggerParam;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 分组下机器地址相同，不同JOB均匀散列在不同机器上，保证分组下机器分配JOB平均；且每个JOB固定调度其中一台机器； a、virtual node：解决不均衡问题
 * b、hash method replace hashCode：String的hashCode可能重复，需要进一步扩大hashCode的取值范围 Created by
 * xuxueli on 17/3/10.
 */
public class ExecutorRouteConsistentHash extends ExecutorRouter {

	private static int VIRTUAL_NODE_NUM = 100;

	/**
	 * get hash code on 2^32 ring (md5散列的方式计算hash值)
	 * @param key
	 * @return
	 */
	private static long hash(String key) {

		// md5 byte
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("MD5 not supported", e);
		}
		md5.reset();
		byte[] keyBytes = null;
		try {
			keyBytes = key.getBytes("UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unknown string :" + key, e);
		}

		md5.update(keyBytes);
		byte[] digest = md5.digest();

		// hash code, Truncate to 32-bits
		long hashCode = ((long) (digest[3] & 0xFF) << 24) | ((long) (digest[2] & 0xFF) << 16)
				| ((long) (digest[1] & 0xFF) << 8) | (digest[0] & 0xFF);

		long truncateHashCode = hashCode & 0xffffffffL;
		return truncateHashCode;
	}

	public String hashJob(int jobId, List<String> addressList) {

		// ------A1------A2-------A3------
		// -----------J1------------------
		TreeMap<Long, String> addressRing = new TreeMap<Long, String>();
		for (String address : addressList) {
			for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
				long addressHash = hash("SHARD-" + address + "-NODE-" + i);
				addressRing.put(addressHash, address);
			}
		}

		long jobHash = hash(String.valueOf(jobId));
		SortedMap<Long, String> lastRing = addressRing.tailMap(jobHash);
		if (!lastRing.isEmpty()) {
			return lastRing.get(lastRing.firstKey());
		}
		return addressRing.firstEntry().getValue();
	}

	@Override
	public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
		String address = hashJob(triggerParam.getJobId(), addressList);
		return new ReturnT<String>(address);
	}

}
