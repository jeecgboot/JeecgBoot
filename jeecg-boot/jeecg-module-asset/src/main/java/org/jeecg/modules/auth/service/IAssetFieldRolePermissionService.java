package org.jeecg.modules.auth.service;

import java.util.Collection;
import java.util.List;

import org.jeecg.modules.auth.config.FieldCategoryEnum;
import org.jeecg.modules.auth.config.PermissionAction;
import org.jeecg.modules.auth.dto.RoleFieldPermissionsDTO;
import org.jeecg.modules.auth.dto.SetRoleFieldPermissionDTO;
import org.jeecg.modules.auth.entity.AssetFieldRolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 资产字段角色权限服务接口
 *
 * @author: local.clk
 * @date: 2026-02-09
 */
public interface IAssetFieldRolePermissionService extends IService<AssetFieldRolePermission> {
    /**
     * 获取角色字段权限
     *
     * @param roleIds       角色ID
     * @param fieldCategory 字段类型
     * @return 角色字段权限DTO
     */
    RoleFieldPermissionsDTO getRoleFieldPermissions(Collection<String> roleIds, FieldCategoryEnum fieldCategory);

    Integer getPermissionBits(List<PermissionAction> permissionActionList);

    void configFieldPermission(SetRoleFieldPermissionDTO setRoleFieldPermissionDTO);
}