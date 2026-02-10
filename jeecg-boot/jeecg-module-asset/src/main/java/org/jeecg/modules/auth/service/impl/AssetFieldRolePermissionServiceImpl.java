package org.jeecg.modules.auth.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.auth.config.FieldCategoryEnum;
import org.jeecg.modules.auth.config.PermissionAction;
import org.jeecg.modules.auth.dto.RoleFieldPermissionsDTO;
import org.jeecg.modules.auth.dto.SetRoleFieldPermissionDTO;
import org.jeecg.modules.auth.entity.AssetFieldRolePermission;
import org.jeecg.modules.auth.entity.AssetFieldDefinition;
import org.jeecg.modules.auth.mapper.AssetFieldRolePermissionMapper;
import org.jeecg.modules.auth.service.IAssetFieldRolePermissionService;
import org.jeecg.modules.auth.service.IAssetFieldDefinitionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

/**
 * 资产字段角色权限服务实现
 *
 * @author: local.clk
 * @date: 2026-02-09
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AssetFieldRolePermissionServiceImpl
        extends ServiceImpl<AssetFieldRolePermissionMapper, AssetFieldRolePermission>
        implements IAssetFieldRolePermissionService {

    private final IAssetFieldDefinitionService assetFiledDefinitionService;

    @Override
    public RoleFieldPermissionsDTO getRoleFieldPermissions(Collection<String> roleIds,
            FieldCategoryEnum fieldCategory) {
        RoleFieldPermissionsDTO dto = new RoleFieldPermissionsDTO();
        dto.setCreateFields(new ArrayList<>());
        dto.setUpdateFields(new ArrayList<>());
        dto.setReadFields(new ArrayList<>());
        dto.setDeleteFields(new ArrayList<>());

        if (CollectionUtils.isEmpty(roleIds)) {
            return dto;
        }

        // 当前租户下该角色(集合)的所有字段权限（租户由 MyBatis 租户插件自动过滤）
        List<AssetFieldRolePermission> permissions = lambdaQuery()
                .in(AssetFieldRolePermission::getRoleId, roleIds)
                .list();

        if (permissions.isEmpty()) {
            return dto;
        }

        // 按 fieldDefinitionId 合并权限位：同一字段多条权限取并集（有任一条具备某权限则保留）
        Map<Long, Integer> fieldDefIdToMergedBits = new HashMap<>();
        for (AssetFieldRolePermission p : permissions) {
            Long fid = p.getFieldDefinitionId();
            Integer bits = p.getPermissionBits() != null ? p.getPermissionBits() : 0;
            fieldDefIdToMergedBits.merge(fid, bits, (a, b) -> a | b);
        }

        // 解析为 Long 并批量查询字段定义（无效 id 忽略）
        List<Long> definitionIds = fieldDefIdToMergedBits.keySet().stream()
                .distinct()
                .collect(Collectors.toList());

        if (definitionIds.isEmpty()) {
            return dto;
        }

        List<AssetFieldDefinition> definitions = assetFiledDefinitionService
                .lambdaQuery()
                .eq(AssetFieldDefinition::getFieldCategory, fieldCategory)
                .eq(AssetFieldDefinition::getStatus, 1)
                .in(AssetFieldDefinition::getId, definitionIds)
                .list();

        Map<Long, AssetFieldDefinition> idToDefinition = definitions.stream()
                .collect(Collectors.toMap(AssetFieldDefinition::getId, Function.identity()));

        // 按合并后的权限位归类到 create/read/update/delete
        for (Map.Entry<Long, Integer> entry : fieldDefIdToMergedBits.entrySet()) {
            AssetFieldDefinition definition = idToDefinition.get(entry.getKey());
            if (definition == null) {
                continue;
            }
            int bits = entry.getValue();
            if (PermissionAction.CREATE.isAllowed(bits)) {
                dto.getCreateFields().add(definition);
            }
            if (PermissionAction.READ.isAllowed(bits)) {
                dto.getReadFields().add(definition);
            }
            if (PermissionAction.UPDATE.isAllowed(bits)) {
                dto.getUpdateFields().add(definition);
            }
            if (PermissionAction.DELETE.isAllowed(bits)) {
                dto.getDeleteFields().add(definition);
            }
        }

        if (!roleIds.isEmpty()) {
            dto.setRoleId(roleIds.iterator().next());
        }
        return dto;
    }

    @Override
    public Integer getPermissionBits(List<PermissionAction> permissionActionList) {
        if (CollectionUtils.isEmpty(permissionActionList)) {
            return 0;
        }

        int bits = 0;
        for (PermissionAction action : permissionActionList) {
            bits = PermissionAction.addPermission(bits, action);
        }
        return bits;

    }

    @Override
    @Transactional(rollbackFor = JeecgBootException.class)
    public void configFieldPermission(SetRoleFieldPermissionDTO setRoleFieldPermissionDTO) {
        if (setRoleFieldPermissionDTO == null
                || CollectionUtils.isEmpty(setRoleFieldPermissionDTO.getFieldPermissionList())) {
            return;
        }

        List<AssetFieldRolePermission> saveList = setRoleFieldPermissionDTO.getFieldPermissionList().stream()
                .map(fieldPermission -> {
                    AssetFieldRolePermission permission = new AssetFieldRolePermission();
                    permission.setRoleId(setRoleFieldPermissionDTO.getRoleId());
                    permission.setFieldDefinitionId(fieldPermission.getFieldDefinitionId());
                    permission.setPermissionBits(getPermissionBits(fieldPermission.getPermissionActions()));
                    return permission;
                })
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(saveList)) {
            return;
        }

        // 先删除再插入，避免重复插入
        lambdaUpdate().eq(AssetFieldRolePermission::getRoleId, setRoleFieldPermissionDTO.getRoleId()).remove();
        saveBatch(saveList);
    }
}