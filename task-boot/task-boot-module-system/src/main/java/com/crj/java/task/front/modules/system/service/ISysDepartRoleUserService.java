package com.crj.java.task.front.modules.system.service;

import com.crj.java.task.front.modules.system.entity.SysDepartRoleUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 部门角色人员信息
 * @Author: Crj
 * @Date:   2020-02-13
 * @Version: V1.0
 */
public interface ISysDepartRoleUserService extends IService<SysDepartRoleUser> {

    void deptRoleUserAdd(String userId,String newRoleId,String oldRoleId);

    /**
     * 取消用户与部门关联，删除关联关系
     * @param userIds
     * @param depId
     */
    void removeDeptRoleUser(List<String> userIds,String depId);
}
