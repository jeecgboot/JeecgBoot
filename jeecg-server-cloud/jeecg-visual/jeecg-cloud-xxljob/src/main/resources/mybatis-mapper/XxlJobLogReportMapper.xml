<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
	"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.xxl.job.admin.dao.XxlJobLogReportDao">
	
	<resultMap id="XxlJobLogReport" type="com.xxl.job.admin.core.model.XxlJobLogReport" >
		<result column="id" property="id" />
	    <result column="trigger_day" property="triggerDay" />
		<result column="running_count" property="runningCount" />
	    <result column="suc_count" property="sucCount" />
	    <result column="fail_count" property="failCount" />
	</resultMap>

	<sql id="Base_Column_List">
		t.id,
		t.trigger_day,
		t.running_count,
		t.suc_count,
		t.fail_count
	</sql>
	
	<insert id="save" parameterType="com.xxl.job.admin.core.model.XxlJobLogReport" useGeneratedKeys="true" keyProperty="id" >
		INSERT INTO xxl_job_log_report (
			`trigger_day`,
			`running_count`,
			`suc_count`,
			`fail_count`
		) VALUES (
			#{triggerDay},
			#{runningCount},
			#{sucCount},
			#{failCount}
		);
		<!--<selectKey resultType="java.lang.Integer" order="AFTER" keyProperty="id">
			SELECT LAST_INSERT_ID() 
		</selectKey>-->
	</insert>

	<update id="update" >
        UPDATE xxl_job_log_report
        SET `running_count` = #{runningCount},
        	`suc_count` = #{sucCount},
        	`fail_count` = #{failCount}
        WHERE `trigger_day` = #{triggerDay}
    </update>

	<select id="queryLogReport" resultMap="XxlJobLogReport">
		SELECT <include refid="Base_Column_List" />
		FROM xxl_job_log_report AS t
		WHERE t.trigger_day between #{triggerDayFrom} and #{triggerDayTo}
		ORDER BY t.trigger_day ASC
	</select>

	<select id="queryLogReportTotal" resultMap="XxlJobLogReport">
		SELECT
			SUM(running_count) running_count,
			SUM(suc_count) suc_count,
			SUM(fail_count) fail_count
		FROM xxl_job_log_report AS t
	</select>

</mapper>