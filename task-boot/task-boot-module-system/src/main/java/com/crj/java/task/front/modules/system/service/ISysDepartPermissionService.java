package com.crj.java.task.front.modules.system.service;

import com.crj.java.task.front.modules.system.entity.SysDepartPermission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.crj.java.task.front.modules.system.entity.SysPermissionDataRule;

import java.util.List;

/**
 * @Description: 部门权限表
 * @Author: Crj
 * @Date:   2020-02-11
 * @Version: V1.0
 */
public interface ISysDepartPermissionService extends IService<SysDepartPermission> {
    /**
     * 保存授权 将上次的权限和这次作比较 差异处理提高效率
     * @param departId
     * @param permissionIds
     * @param lastPermissionIds
     */
    public void saveDepartPermission(String departId,String permissionIds,String lastPermissionIds);

    /**
     * 根据部门id，菜单id获取数据规则
     * @param permissionId
     * @return
     */
    List<SysPermissionDataRule> getPermRuleListByDeptIdAndPermId(String departId,String permissionId);
}
