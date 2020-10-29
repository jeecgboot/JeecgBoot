package com.crj.java.task.front.modules.demo.test.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.crj.java.task.front.modules.demo.test.entity.TaskDemo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Description: task 测试demo
 * @Author: Crj
 * @Date:  2018-12-29
 * @Version: V1.0
 */
public interface TaskDemoMapper extends BaseMapper<TaskDemo> {

	public List<TaskDemo> getDemoByName(@Param("name") String name);

	/**
	 * 查询列表数据 直接传数据权限的sql进行数据过滤
	 * @param page
	 * @param permissionSql
	 * @return
	 */
	public IPage<TaskDemo> queryListWithPermission(Page<TaskDemo> page,@Param("permissionSql")String permissionSql);

}
