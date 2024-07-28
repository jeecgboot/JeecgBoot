package com.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.entity.UserRole;
import com.shop.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户角色服务实现类
 * 2018-12-24 16:10
 */
@Service
public class UserRoleService extends ServiceImpl<UserRoleMapper, UserRole> {

    public Integer[] getRoleIds(String userId) {
        List<UserRole> userRoles = baseMapper.selectList(new QueryWrapper<UserRole>().eq("user_id", userId));
        Integer[] roleIds = new Integer[userRoles.size()];
        for (int i = 0; i < userRoles.size(); i++) {
            roleIds[i] = userRoles.get(i).getRoleId();
        }
        return roleIds;
    }

}
