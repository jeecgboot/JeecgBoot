package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.model.TreeModel;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 * 部门 Mapper 接口
 * <p>
 * 
 * @Author: Steve
 * @Since：   2019-01-22
 */
public interface SysDepartMapper extends BaseMapper<SysDepart> {
	
	/**
	 * 根据用户ID查询部门集合
     * @param userId 用户id
     * @return List<SysDepart>
	 */
	public List<SysDepart> queryUserDeparts(@Param("userId") String userId);

	/**
	 * 根据用户名查询部门
	 *
	 * @param username
	 * @return
	 */
	public List<SysDepart> queryDepartsByUsername(@Param("username") String username);

    /**
     * 通过部门编码获取部门id
     * @param orgCode 部门编码
     * @return String
     */
	@Select("select id from sys_depart where org_code=#{orgCode}")
	public String queryDepartIdByOrgCode(@Param("orgCode") String orgCode);

    /**
     * 通过部门id 查询部门id,父id
     * @param departId 部门id
     * @return
     */
	@Select("select id,parent_id from sys_depart where id=#{departId}")
	public SysDepart getParentDepartId(@Param("departId") String departId);

	/**
	 *  根据部门Id查询,当前和下级所有部门IDS
	 * @param departId
	 * @return
	 */
	List<String> getSubDepIdsByDepId(@Param("departId") String departId);

	/**
	 * 根据部门编码获取部门下所有IDS
	 * @param orgCodes
	 * @return
	 */
	List<String> getSubDepIdsByOrgCodes(@org.apache.ibatis.annotations.Param("orgCodes") String[] orgCodes);

    /**
     * 根据parent_id查询下级部门
     * @param parentId 父id
     * @return List<SysDepart>
     */
    List<SysDepart> queryTreeListByPid(@Param("parentId") String parentId);
	/**
	 * 根据id下级部门数量
	 * @param parentId
	 * @return
	 */
	@Select("SELECT count(*) FROM sys_depart where del_flag ='0' AND parent_id = #{parentId,jdbcType=VARCHAR}")
    Integer queryCountByPid(@Param("parentId")String parentId);
	/**
	 * 根据OrgCod查询所属公司信息
	 * @param orgCode
	 * @return
	 */
	SysDepart queryCompByOrgCode(@Param("orgCode")String orgCode);
	/**
	 * 根据id下级部门
	 * @param parentId
	 * @return
	 */
	@Select("SELECT * FROM sys_depart where del_flag ='0' AND parent_id = #{parentId,jdbcType=VARCHAR}")
	List<SysDepart> queryDeptByPid(@Param("parentId")String parentId);
}
