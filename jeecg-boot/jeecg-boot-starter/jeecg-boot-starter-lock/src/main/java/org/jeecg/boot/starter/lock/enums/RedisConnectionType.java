package org.jeecg.boot.starter.lock.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Redis连接方式
 * @author zyf
 * @date 2020-11-11
 */
@Getter
@AllArgsConstructor
public enum RedisConnectionType {
	/**
	 * 单机部署方式(默认)
	 */
	STANDALONE("standalone", "单机部署方式"),
	/**
	 * 哨兵部署方式
	 */
	SENTINEL("sentinel", "哨兵部署方式"),
	/**
	 * 集群部署方式
	 */
	CLUSTER("cluster", "集群方式"),
	/**
	 * 主从部署方式
	 */
	MASTERSLAVE("masterslave", "主从部署方式");

	/**
	 * 编码
	 */
	private final String code;
	/**
	 * 名称
	 */
	private final String name;
}
