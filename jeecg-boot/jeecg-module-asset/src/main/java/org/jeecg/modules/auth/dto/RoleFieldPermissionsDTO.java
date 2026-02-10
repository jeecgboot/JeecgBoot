package org.jeecg.modules.auth.dto;

import java.util.List;

import org.jeecg.modules.auth.entity.AssetFieldDefinition;

import lombok.Data;

/*
 * 角色字段权限DTO
 * 
 * @author: local.clk
 * @date: 2026-02-09
 */
@Data
public class RoleFieldPermissionsDTO {
    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 读取字段
     */
    private List<AssetFieldDefinition> readFields;
    /**
     * 更新字段
     */
    private List<AssetFieldDefinition> updateFields;
    /**
     * 删除字段
     */
    private List<AssetFieldDefinition> deleteFields;
    /**
     * 创建字段
     */
    private List<AssetFieldDefinition> createFields;
}
