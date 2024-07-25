package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.entity.UserRole;

/**
 * 用户角色服务类
 * 2018-12-24 16:10
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 查询用户的角色id
     */
    Integer[] getRoleIds(String userId);

}
