package org.jeecg.modules.system.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysCategory;
import org.jeecg.modules.system.model.TreeSelectModel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 分类字典
 * @Author: jeecg-boot
 * @Date:   2019-05-29
 * @Version: V1.0
 */
public interface SysCategoryMapper extends BaseMapper<SysCategory> {
	
	/**
	 * 根据父级ID查询树节点数据
	 * @param pid
     * @param query
	 * @return
	 */
	public List<TreeSelectModel> queryListByPid(@Param("pid")  String pid,@Param("query") Map<String, String> query);

    /**
     * 通过code查询分类字典表
     * @param code
     * @return
     */
	@Select("SELECT ID FROM sys_category WHERE CODE = #{code,jdbcType=VARCHAR}")
	public String queryIdByCode(@Param("code")  String code);
	
	/**
	 * 获取分类字典最大的code
	 * @param page
	 * @return
	 */
	@InterceptorIgnore(tenantLine = "true")
	@Select("SELECT code FROM sys_category WHERE code IS NOT NULL AND pid=#{categoryPid} ORDER BY code DESC")
	List<SysCategory> getMaxCategoryCodeByPage(@Param("page") Page<SysCategory> page,@Param("categoryPid") String categoryPid);

	@InterceptorIgnore(tenantLine = "true")
	@Select("SELECT code FROM sys_category WHERE ID = #{id}")
	SysCategory selectSysCategoryById(@Param("id") String id);
}
