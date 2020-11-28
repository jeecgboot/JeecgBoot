<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
	"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.xxl.job.admin.dao.XxlJobLogGlueDao">
	
	<resultMap id="XxlJobLogGlue" type="com.xxl.job.admin.core.model.XxlJobLogGlue" >
		<result column="id" property="id" />
	    <result column="job_id" property="jobId" />
		<result column="glue_type" property="glueType" />
	    <result column="glue_source" property="glueSource" />
	    <result column="glue_remark" property="glueRemark" />
	    <result column="add_time" property="addTime" />
	    <result column="update_time" property="updateTime" />
	</resultMap>

	<sql id="Base_Column_List">
		t.id,
		t.job_id,
		t.glue_type,
		t.glue_source,
		t.glue_remark,
		t.add_time,
		t.update_time
	</sql>
	
	<insert id="save" parameterType="com.xxl.job.admin.core.model.XxlJobLogGlue" useGeneratedKeys="true" keyProperty="id" >
		INSERT INTO xxl_job_logglue (
			`job_id`,
			`glue_type`,
			`glue_source`,
			`glue_remark`,
			`add_time`, 
			`update_time`
		) VALUES (
			#{jobId},
			#{glueType},
			#{glueSource},
			#{glueRemark},
			#{addTime},
			#{updateTime}
		);
		<!--<selectKey resultType="java.lang.Integer" order="AFTER" keyProperty="id">
			SELECT LAST_INSERT_ID() 
		</selectKey>-->
	</insert>
	
	<select id="findByJobId" parameterType="java.lang.Integer" resultMap="XxlJobLogGlue">
		SELECT <include refid="Base_Column_List" />
		FROM xxl_job_logglue AS t
		WHERE t.job_id = #{jobId}
		ORDER BY id DESC
	</select>
	
	<delete id="removeOld" >
		DELETE FROM xxl_job_logglue
		WHERE id NOT in(
			SELECT id FROM(
				SELECT id FROM xxl_job_logglue
				WHERE `job_id` = #{jobId}
				ORDER BY update_time desc
				LIMIT 0, #{limit}
			) t1
		) AND `job_id` = #{jobId}
	</delete>
	
	<delete id="deleteByJobId" parameterType="java.lang.Integer" >
		DELETE FROM xxl_job_logglue
		WHERE `job_id` = #{jobId}
	</delete>
	
</mapper>