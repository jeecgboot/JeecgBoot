package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.system.entity.SysRole;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @Author scott
 * @since 2018-12-19
 */
public interface ISysRoleService extends IService<SysRole> {
    /**
     * 查询全部的角色（不做租户隔离）
     * @param page
     * @param role
     * @return
     */
    Page<SysRole> listAllSysRole(@Param("page") Page<SysRole> page, SysRole role);

    /**
     * 查询角色是否存在不做租户隔离
     *
     * @param roleCode
     * @return
     */
    SysRole getRoleNoTenant(@Param("roleCode") String roleCode);
    
    /**
     * 导入 excel ，检查 roleCode 的唯一性
     *
     * @param file
     * @param params
     * @return
     * @throws Exception
     */
    Result importExcelCheckRoleCode(MultipartFile file, ImportParams params) throws Exception;

    /**
     * 删除角色
     * @param roleid
     * @return
     */
    public boolean deleteRole(String roleid);

    /**
     * 批量删除角色
     * @param roleids
     * @return
     */
    public boolean deleteBatchRole(String[] roleids);

    /**
     * 根据角色id和当前租户判断当前角色是否存在这个租户中
     * @param id
     * @return
     */
    Long getRoleCountByTenantId(String id, Integer tenantId);

    /**
     * 验证是否为admin角色
     * 
     * @param ids
     */
    void checkAdminRoleRejectDel(String ids);
}
