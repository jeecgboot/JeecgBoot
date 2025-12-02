package org.jeecg.modules.system.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.SysUserSysDepPostModel;
import org.jeecg.modules.system.vo.SysDepartExportVo;
import org.jeecg.modules.system.vo.SysDepartPositionVo;
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
    List<SysDepartExportVo> getSysDepartList(@Param("parentId") String parentId,@Param("tenantId") Integer tenantId, List<String> idList);

    /**
     * 根据多个部门id获取部门数据
     * 
     * @param departIds
     * @return
     */
    List<SysUserDepVo> getDepartByIds(List<String> departIds);

    /**
     * 根据用户id获取部门数据
     *
     * @param userList
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<SysUserDepVo> getUserDepartByUserId(@Param("userList")List<SysUser> userList);

    /**
     * 根据父级id/职级/部门id获取部门岗位信息
     * 
     * @param parentId
     * @param postLevel
     * @param departId
     */
    List<SysDepart> getDepartPositionByParentId(@Param("parentId") String parentId, @Param("postLevel") Integer postLevel, @Param("departId") String departId);

    /**
     * 根据父级id获取部门中的数据
     * @param parentId
     * @return
     */
    @Select("select id, depart_name, parent_id, iz_leaf, org_category, org_code, depart_order from sys_depart where parent_id = #{parentId} order by depart_order,create_time desc")
    List<SysDepart> getDepartByParentId(@Param("parentId") String parentId);

    /**
     * 根据部门id查询部门信息
     
     * @param departId
     * @return 部门岗位信息
     */
    SysDepartPositionVo getDepartPostByDepartId(@Param("departId") String departId);

    /**
     * 根据父级部门id查询部门信息
     
     * @param orgCode
     * @return 部门岗位信息
     */
    List<SysDepartPositionVo> getDepartPostByOrgCode(@Param("orgCode") String orgCode);

    /**
     * 根据部门id获取部门code
     * @param idList
     * @return
     */
    List<String> getDepCodeByDepIds(@Param("idList") List<String> idList);

    /**
     * 根据父级部门id和职务名称查找部门id
     * 
     * @param parentId
     * @param postName
     * @return
     */
    String getDepIdByDepIdAndPostName(@Param("parentId") String parentId, @Param("postName") String postName);

    /**
     * 根据部门id 获取职级名称
     * 
     * @param depId
     * @return
     */
    String getPostNameByPostId(@Param("depId") String depId);

    /**
     * 根据部门code获取部门数据
     * 
     * @param orgCode
     * @return
     */
    @Select("select depart_name, id, iz_leaf, org_category, parent_id, org_code from sys_depart where org_code = #{orgCode} order by depart_order,create_time desc")
    SysDepart queryDepartByOrgCode(@Param("orgCode") String orgCode);

    /**
     * 根据部门父id获取部门岗位数据
     * 
     * @param parentIds
     * @return
     */
    List<SysDepart> getDepartPositionByParentIds(@Param("parentIds") List<String> parentIds);

    /**
     * 根据用户id集合获取用户的兼职岗位信息
     * 
     * @param userIdList
     * @return
     */
    List<SysUserSysDepPostModel> getDepartOtherPostByUserIds(@Param("userIdList") List<String> userIdList);

    /**
     * 获取没有父级id的部门数据
     * 
     * @return
     */
    @Select("select id, org_code, depart_order from sys_depart where parent_id is null or parent_id = '' order by depart_order,create_time desc")
    List<SysDepart> getDepartNoParent();

    /**
     * 根据父级id统计子节点数量
     * 
     * @param parentId
     * @return
     */
    @Select("select count(1) from sys_depart where parent_id = #{parentId}")
    long countByParentId(@Param("parentId") String parentId);

	/**
	 * 根据用户名和分类查询
	 * @param username
	 * @param category
	 * @return
	 */
	List<SysDepart> queryDeptByUserAndCategory(@Param("username")String username, @Param("category")String category);

    /**
     * 获取负责部门
     * 
     * @param page
     * @param departId
     * @return
     */
    List<SysUser> getDepartmentHead(@Param("page") Page<SysUser> page, @Param("departId") String departId);
}
