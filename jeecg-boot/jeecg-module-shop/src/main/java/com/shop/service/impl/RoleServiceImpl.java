package com.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.entity.Role;
import com.shop.mapper.RoleMapper;
import com.shop.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * 角色服务实现类
 * Created by Panyoujie on 2018-12-24 16:10
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}
