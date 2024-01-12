package org.jeecg.config.security;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.config.security.utils.SecureUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * @author EightMonth
 * @date 2024/1/10 17:00
 */
@Service("jps")
@AllArgsConstructor
public class JeecgPermissionService {

    private final CommonAPI commonAPI;

    /**
     * 判断接口是否有任意xxx，xxx权限
     * @param permissions 权限
     * @return {boolean}
     */
    public boolean requiresPermissions(String... permissions) {
        if (ArrayUtil.isEmpty(permissions)) {
            return false;
        }
        LoginUser loginUser = SecureUtil.currentUser();
        Set<String> permissionList = commonAPI.queryUserAuths(loginUser.getUsername());
        return permissionList.stream().filter(StringUtils::hasText)
                .anyMatch(x -> PatternMatchUtils.simpleMatch(permissions, x));
    }

    /**
     * 判断接口是否有任意xxx，xxx角色
     * @param roles 角色
     * @return {boolean}
     */
    public boolean requiresRoles(String... roles) {
        if (ArrayUtil.isEmpty(roles)) {
            return false;
        }
        LoginUser loginUser = SecureUtil.currentUser();
        Set<String> roleList = commonAPI.queryUserRoles(loginUser.getUsername());
        return roleList.stream().filter(StringUtils::hasText)
                .anyMatch(x -> PatternMatchUtils.simpleMatch(roles, x));
    }
}
