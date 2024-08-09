<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
	"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.xxl.job.admin.dao.XxlJobGroupDao">
	
	<resultMap id="XxlJobGroup" type="com.xxl.job.admin.core.model.XxlJobGroup" >
		<result column="id" property="id" />
	    <result column="app_name" property="appname" />
	    <result column="title" property="title" />
		<result column="address_type" property="addressType" />
		<result column="address_list" property="addressList" />
		<result column="update_time" property="updateTime" />
	</resultMap>

	<sql id="Base_Column_List">
		t.id,
		t.app_name,
		t.title,
		t.address_type,
		t.address_list,
		t.update_time
	</sql>

	<select id="findAll" resultMap="XxlJobGroup">
		SELECT <include refid="Base_Column_List" />
		FROM xxl_job_group AS t
		ORDER BY t.app_name, t.title, t.id ASC
	</select>

	<select id="findByAddressType" parameterType="java.lang.Integer" resultMap="XxlJobGroup">
		SELECT <include refid="Base_Column_List" />
		FROM xxl_job_group AS t
		WHERE t.address_type = #{addressType}
		ORDER BY t.app_name, t.title, t.id ASC
	</select>

	<insert id="save" parameterType="com.xxl.job.admin.core.model.XxlJobGroup" useGeneratedKeys="true" keyProperty="id" >
		INSERT INTO xxl_job_group ( `app_name`, `title`, `address_type`, `address_list`, `update_time`)
		values ( #{appname}, #{title}, #{addressType}, #{addressList}, #{updateTime} );
	</insert>

	<update id="update" parameterType="com.xxl.job.admin.core.model.XxlJobGroup" >
		UPDATE xxl_job_group
		SET `app_name` = #{appname},
			`title` = #{title},
			`address_type` = #{addressType},
			`address_list` = #{addressList},
			`update_time` = #{updateTime}
		WHERE id = #{id}
	</update>

	<delete id="remove" parameterType="java.lang.Integer" >
		DELETE FROM xxl_job_group
		WHERE id = #{id}
	</delete>

	<select id="load" parameterType="java.lang.Integer" resultMap="XxlJobGroup">
		SELECT <include refid="Base_Column_List" />
		FROM xxl_job_group AS t
		WHERE t.id = #{id}
	</select>

	<select id="pageList" parameterType="java.util.HashMap" resultMap="XxlJobGroup">
		SELECT <include refid="Base_Column_List" />
		FROM xxl_job_group AS t
		<trim prefix="WHERE" prefixOverrides="AND | OR" >
			<if test="appname != null and appname != ''">
				AND t.app_name like CONCAT(CONCAT('%', #{appname}), '%')
			</if>
			<if test="title != null and title != ''">
				AND t.title like CONCAT(CONCAT('%', #{title}), '%')
			</if>
		</trim>
		ORDER BY t.app_name, t.title, t.id ASC
		LIMIT #{offset}, #{pagesize}
	</select>

	<select id="pageListCount" parameterType="java.util.HashMap" resultType="int">
		SELECT count(1)
		FROM xxl_job_group AS t
		<trim prefix="WHERE" prefixOverrides="AND | OR" >
			<if test="appname != null and appname != ''">
				AND t.app_name like CONCAT(CONCAT('%', #{appname}), '%')
			</if>
			<if test="title != null and title != ''">
				AND t.title like CONCAT(CONCAT('%', #{title}), '%')
			</if>
		</trim>
	</select>

</mapper>