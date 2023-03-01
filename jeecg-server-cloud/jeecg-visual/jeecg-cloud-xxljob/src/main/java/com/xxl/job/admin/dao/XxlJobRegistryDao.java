package com.xxl.job.admin.dao;

import com.xxl.job.admin.core.model.XxlJobRegistry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by xuxueli on 16/9/30.
 */
@Mapper
public interface XxlJobRegistryDao {

	public List<Integer> findDead(@Param("timeout") int timeout, @Param("nowTime") Date nowTime);

	public int removeDead(@Param("ids") List<Integer> ids);

	public List<XxlJobRegistry> findAll(@Param("timeout") int timeout, @Param("nowTime") Date nowTime);

	public int registryUpdate(@Param("registryGroup") String registryGroup, @Param("registryKey") String registryKey,
			@Param("registryValue") String registryValue, @Param("updateTime") Date updateTime);

	public int registrySave(@Param("registryGroup") String registryGroup, @Param("registryKey") String registryKey,
			@Param("registryValue") String registryValue, @Param("updateTime") Date updateTime);

	public int registryDelete(@Param("registryGroup") String registryGroup, @Param("registryKey") String registryKey,
			@Param("registryValue") String registryValue);

}
