package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.SysDepartTreeModel;
import org.jeecg.modules.system.model.TreeModel;
import org.jeecg.modules.system.vo.SysDepartExportVo;
import org.jeecg.modules.system.vo.SysUserDepVo;
import org.jeecg.modules.system.vo.lowapp.ExportDepartVo;
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
	 * 根据用户名查询部门
	 *
	 * @param userId
	 * @return
	 */
	public List<String> queryDepartsByUserId(@Param("userId") String userId);

    /**
     * 通过部门编码获取部门id
     * @param orgCode 部门编码
     * @return String
     */
	@Select("select id from sys_depart where org_code=#{orgCode}")
	public String queryDepartIdByOrgCode(@Param("orgCode") String orgCode);
	
    /**
     * 通过部门id，查询部门下的用户的账号
     * @param departIds 部门ID集合
     * @return String
     */
	public List<String> queryUserAccountByDepartIds(@Param("departIds") List<String> departIds);

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

	/**
	 * 通过父级id和租户id查询部门
	 * @param parentId
	 * @param tenantId
	 * @return
	 */
	@InterceptorIgnore(tenantLine = "true")
	List<SysDepart> queryBookDepTreeSync(@Param("parentId") String parentId, @Param("tenantId") Integer tenantId, @Param("departName") String departName);

	@InterceptorIgnore(tenantLine = "true")
	@Select("SELECT * FROM sys_depart where id = #{id,jdbcType=VARCHAR}")
	SysDepart getDepartById(@Param("id") String id);

	@InterceptorIgnore(tenantLine = "true")
	List<SysDepart> getMaxCodeDepart(@Param("page") Page<SysDepart> page, @Param("parentId") String parentId);

	/**
	 * 修改部门状态字段： 是否子节点
	 * @param id 部门id
	 * @param leaf 叶子节点
	 * @return int
	 */
	@Update("UPDATE sys_depart SET iz_leaf=#{leaf} WHERE id = #{id}")
	int setMainLeaf(@Param("id") String id, @Param("leaf") Integer leaf);

	/**
	 * 获取租户id和部门父id获取的部门数据
	 * @param tenantId
	 * @param parentId
	 * @return
	 */
    List<ExportDepartVo> getDepartList(@Param("parentId") String parentId, @Param("tenantId") Integer tenantId);

	/**
	 * 根据部门名称和租户id获取部门数据
	 * @param departName
	 * @param tenantId
	 * @return
	 */
	List<SysDepart> getDepartByName(@Param("departName")String departName, @Param("tenantId")Integer tenantId,@Param("parentId") String parentId);

	/**
	 * 根据部门id获取用户id和部门名称
	 * @param userList
	 * @return
	 */
	List<SysUserDepVo> getUserDepartByTenantUserId(@Param("userList") List<SysUser> userList, @Param("tenantId") Integer tenantId);

	/**
	 * 根据部门名称和租户id获取分页部门数据
	 * @param page
	 * @param departName
	 * @param tenantId
	 * @param parentId
	 * @return
	 */
	List<SysDepart> getDepartPageByName(@Param("page") Page<SysDepart> page, @Param("departName") String departName, @Param("tenantId") Integer tenantId, @Param("parentId") String parentId);

	/**
	 * 获取租户id和部门父id获取的部门数据
	 * @param tenantId
	 * @param parentId
	 * @return
	 */
    List<SysDepartExportVo> getSysDepartList(@Param("parentId") String parentId,@Param("tenantId") Integer tenantId);
}
