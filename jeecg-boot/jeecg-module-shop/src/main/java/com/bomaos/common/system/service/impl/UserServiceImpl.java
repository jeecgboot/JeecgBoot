package com.bomaos.common.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bomaos.common.core.exception.BusinessException;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.core.web.PageResult;
import com.bomaos.common.system.entity.Menu;
import com.bomaos.common.system.entity.Role;
import com.bomaos.common.system.entity.User;
import com.bomaos.common.system.entity.UserRole;
import com.bomaos.common.system.mapper.MenuMapper;
import com.bomaos.common.system.mapper.UserMapper;
import com.bomaos.common.system.mapper.UserRoleMapper;
import com.bomaos.common.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户服务实现类
 * Created by Panyoujie on 2018-12-24 16:10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public User getByUsername(String username) {
        return baseMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }

    @Override
    public User getFullById(Integer userId) {
        List<User> userList = baseMapper.listAll(new PageParam<User>().put("userId", userId).getNoPageParam());
        if (userList == null || userList.size() == 0) return null;
        return selectRoleAndAuth(userList.get(0));
    }

    @Override
    public User selectRoleAndAuth(User user) {
        user.setRoles(userRoleMapper.listByUserId(user.getUserId()));
        List<Menu> menus = menuMapper.listByUserId(user.getUserId(), null);
        List<String> auths = new ArrayList<>();
        for (Menu menu : menus) {
            auths.add(menu.getAuthority());
        }
        user.setAuthorities(auths);
        return user;
    }

    @Override
    public PageResult<User> listPage(PageParam<User> page) {
        List<User> users = baseMapper.listPage(page);
        // 查询用户的角色
        selectUserRoles(users);
        return new PageResult<>(users, page.getTotal());
    }

    @Override
    public List<User> listAll(Map<String, Object> page) {
        List<User> users = baseMapper.listAll(page);
        // 查询用户的角色
        selectUserRoles(users);
        return users;
    }

    @Transactional
    @Override
    public boolean saveUser(User user) {
        if (user.getUsername() != null && baseMapper.selectCount(new QueryWrapper<User>()
                .eq("username", user.getUsername())) > 0) {
            throw new BusinessException("账号已存在");
        }
        if (user.getPhone() != null && baseMapper.selectCount(new QueryWrapper<User>()
                .eq("phone", user.getPhone())) > 0) {
            throw new BusinessException("手机号已存在");
        }
        if (user.getEmail() != null && baseMapper.selectCount(new QueryWrapper<User>()
                .eq("email", user.getEmail())) > 0) {
            throw new BusinessException("邮箱已存在");
        }
        boolean result = baseMapper.insert(user) > 0;
        if (result && user.getRoleIds() != null) {
            addUserRoles(user.getUserId(), user.getRoleIds(), false);
        }
        return result;
    }

    @Transactional
    @Override
    public boolean updateUser(User user) {
        if (user.getUsername() != null && baseMapper.selectCount(new QueryWrapper<User>()
                .eq("username", user.getUsername()).ne("user_id", user.getUserId())) > 0) {
            throw new BusinessException("账号已存在");
        }
        if (user.getPhone() != null && baseMapper.selectCount(new QueryWrapper<User>()
                .eq("phone", user.getPhone()).ne("user_id", user.getUserId())) > 0) {
            throw new BusinessException("手机号已存在");
        }
        if (user.getEmail() != null && baseMapper.selectCount(new QueryWrapper<User>()
                .eq("email", user.getEmail()).ne("user_id", user.getUserId())) > 0) {
            throw new BusinessException("邮箱已存在");
        }
        boolean result = baseMapper.updateById(user) > 0;
        if (result && user.getRoleIds() != null) {
            addUserRoles(user.getUserId(), user.getRoleIds(), true);
        }
        return result;
    }

    @Override
    public boolean comparePsw(String dbPsw, String inputPsw) {
        return dbPsw != null && dbPsw.equals(encodePsw(inputPsw));
    }

    @Override
    public String encodePsw(String psw) {
        if (psw == null) return null;
        return DigestUtils.md5DigestAsHex(psw.getBytes());
    }

    /**
     * 查询用户的角色
     */
    private void selectUserRoles(List<User> users) {
        if (users != null && users.size() > 0) {
            List<Integer> userIds = new ArrayList<>();
            for (User one : users) {
                userIds.add(one.getUserId());
            }
            List<Role> userRoles = userRoleMapper.listByUserIds(userIds);
            for (User user : users) {
                List<Role> roles = new ArrayList<>();
                for (Role userRole : userRoles) {
                    if (user.getUserId().equals(userRole.getUserId())) {
                        roles.add(userRole);
                    }
                }
                user.setRoles(roles);
            }
        }
    }

    /**
     * 添加用户角色
     */
    private void addUserRoles(Integer userId, List<Integer> roleIds, boolean deleteOld) {
        if (deleteOld) {
            userRoleMapper.delete(new UpdateWrapper<UserRole>().eq("user_id", userId));
        }
        if (roleIds.size() > 0) {
            if (userRoleMapper.insertBatch(userId, roleIds) < roleIds.size()) {
                throw new BusinessException("操作失败");
            }
        }
    }

}
