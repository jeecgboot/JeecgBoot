package com.xxl.job.admin.business.mapper;

import com.xxl.job.admin.business.model.XxlJobInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * job info
 * @author xuxueli 2016-1-12 18:03:45
 */
@Mapper
public interface XxlJobInfoMapper {

	public List<XxlJobInfo> pageList(@Param("offset") int offset,
									 @Param("pagesize") int pagesize,
									 @Param("jobGroup") int jobGroup,
									 @Param("triggerStatus") int triggerStatus,
									 @Param("jobDesc") String jobDesc,
									 @Param("executorHandler") String executorHandler,
									 @Param("author") String author);
	public int pageListCount(@Param("offset") int offset,
							 @Param("pagesize") int pagesize,
							 @Param("jobGroup") int jobGroup,
							 @Param("triggerStatus") int triggerStatus,
							 @Param("jobDesc") String jobDesc,
							 @Param("executorHandler") String executorHandler,
							 @Param("author") String author);
	
	public int save(XxlJobInfo info);

	public XxlJobInfo loadById(@Param("id") int id);
	
	public int update(XxlJobInfo xxlJobInfo);
	
	public int delete(@Param("id") long id);

	public List<XxlJobInfo> getJobsByGroup(@Param("jobGroup") int jobGroup);

	public int findAllCount();

	/**
	 * find schedule job, limit "trigger_status = 1"
	 *
	 * @param maxNextTime
	 * @param pagesize
	 * @return
	 */
	public List<XxlJobInfo> scheduleJobQuery(@Param("maxNextTime") long maxNextTime, @Param("pagesize") int pagesize );

	/**
	 * update schedule job
	 *
	 * 	1、can only update "trigger_status = 1", Avoid stopping tasks from being opened
	 * 	2、valid "triggerStatus gte 0", filter illegal state
	 *
	 * @param xxlJobInfo
	 * @return
	 */
	public int scheduleUpdate(XxlJobInfo xxlJobInfo);

	/**
	 * batch update job info
	 *
	 * @param jobInfoList
	 * @return
	 */
	public int scheduleBatchUpdate(@Param("list") List<XxlJobInfo> jobInfoList);

}
