package com.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.entity.UserRole;
import com.shop.mapper.UserRoleMapper;
import com.shop.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户角色服务实现类
 * 2018-12-24 16:10
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    public Integer[] getRoleIds(String userId) {
        List<UserRole> userRoles = baseMapper.selectList(new QueryWrapper<UserRole>().eq("user_id", userId));
        Integer[] roleIds = new Integer[userRoles.size()];
        for (int i = 0; i < userRoles.size(); i++) {
            roleIds[i] = userRoles.get(i).getRoleId();
        }
        return roleIds;
    }

}
