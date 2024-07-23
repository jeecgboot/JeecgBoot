package com.shop.common.core.shiro;

import com.shop.entity.Role;
import com.shop.entity.User;
import com.shop.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.HashSet;
import java.util.Set;

/**
 * Shiro认证和授权
 * Created by Panyoujie on 2017-04-28 09:45
 */
public class UserRealm extends AuthorizingRealm {

    @Lazy
    @Autowired
    private UserService userService;

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();
        User user = userService.getByUsername(username);
        if (user == null) throw new UnknownAccountException(); // 账号不存在
        if (user.getState() != 0) throw new LockedAccountException();  // 账号被锁定
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        userService.selectRoleAndAuth(user);
        // 角色
        Set<String> roles = new HashSet<>();
        for (Role r : user.getRoles()) {
            if (r.getDeleted() == 0) roles.add(r.getRoleCode());
        }
        authorizationInfo.setRoles(roles);
        // 权限
        Set<String> permissions = new HashSet<>();
        for (String auth : user.getAuthorities()) {
            if (auth != null && !auth.trim().isEmpty()) permissions.add(auth);
        }
        authorizationInfo.setStringPermissions(permissions);
        return authorizationInfo;
    }

}
