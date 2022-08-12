<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
	"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.xxl.job.admin.dao.XxlJobRegistryDao">
	
	<resultMap id="XxlJobRegistry" type="com.xxl.job.admin.core.model.XxlJobRegistry" >
		<result column="id" property="id" />
	    <result column="registry_group" property="registryGroup" />
	    <result column="registry_key" property="registryKey" />
	    <result column="registry_value" property="registryValue" />
		<result column="update_time" property="updateTime" />
	</resultMap>

	<sql id="Base_Column_List">
		t.id,
		t.registry_group,
		t.registry_key,
		t.registry_value,
		t.update_time
	</sql>

	<select id="findDead" parameterType="java.util.HashMap" resultType="java.lang.Integer" >
		SELECT t.id
		FROM xxl_job_registry AS t
		WHERE t.update_time <![CDATA[ < ]]> DATE_ADD(#{nowTime},INTERVAL -#{timeout} SECOND)
	</select>
	
	<delete id="removeDead" parameterType="java.lang.Integer" >
		DELETE FROM xxl_job_registry
		WHERE id in
		<foreach collection="ids" item="item" open="(" close=")" separator="," >
			#{item}
		</foreach>
	</delete>

	<select id="findAll" parameterType="java.util.HashMap" resultMap="XxlJobRegistry">
		SELECT <include refid="Base_Column_List" />
		FROM xxl_job_registry AS t
		WHERE t.update_time <![CDATA[ > ]]> DATE_ADD(#{nowTime},INTERVAL -#{timeout} SECOND)
	</select>

    <update id="registryUpdate" >
        UPDATE xxl_job_registry
        SET `update_time` = #{updateTime}
        WHERE `registry_group` = #{registryGroup}
          AND `registry_key` = #{registryKey}
          AND `registry_value` = #{registryValue}
    </update>

    <insert id="registrySave" >
        INSERT INTO xxl_job_registry( `registry_group` , `registry_key` , `registry_value`, `update_time`)
        VALUES( #{registryGroup}  , #{registryKey} , #{registryValue}, #{updateTime})
    </insert>

	<delete id="registryDelete" >
		DELETE FROM xxl_job_registry
		WHERE registry_group = #{registryGroup}
			AND registry_key = #{registryKey}
			AND registry_value = #{registryValue}
	</delete>

</mapper>