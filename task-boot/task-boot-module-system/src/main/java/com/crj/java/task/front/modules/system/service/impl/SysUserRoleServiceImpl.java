package com.crj.java.task.front.modules.system.service.impl;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import com.crj.java.task.front.modules.system.entity.SysRole;
import com.crj.java.task.front.modules.system.entity.SysUser;
import com.crj.java.task.front.modules.system.entity.SysUserRole;
import com.crj.java.task.front.modules.system.mapper.SysUserRoleMapper;
import com.crj.java.task.front.modules.system.service.ISysRoleService;
import com.crj.java.task.front.modules.system.service.ISysUserRoleService;
import com.crj.java.task.front.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @Author Crj
 * @since 2018-12-21
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

}
