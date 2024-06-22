package org.jeecg.config.security;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.config.security.utils.SecureUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

/**
 * spring authorization server自定义权限处理，根据@PreAuthorize注解，判断当前用户是否具备权限
 * @author EightMonth
 * @date 2024/1/10 17:00
 */
@Service("jps")
@AllArgsConstructor
@Slf4j
public class JeecgPermissionService {
    private final String SPLIT = "::";
    private final String PERM_PREFIX = "jps" + SPLIT;

    private final CommonAPI commonAPI;
    private final RedisUtil redisUtil;

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

        Object cache = redisUtil.get(buildKey("permission", loginUser.getId()));
        Set<String> permissionList;
        if (Objects.nonNull(cache)) {
            permissionList = (Set<String>) cache;
        } else {
            permissionList = commonAPI.queryUserAuths(loginUser.getId());
            redisUtil.set(buildKey("permission", loginUser.getId()), permissionList);
        }

        boolean pass = permissionList.stream().filter(StringUtils::hasText)
                .anyMatch(x -> PatternMatchUtils.simpleMatch(permissions, x));
        if (!pass) {
            log.error("权限不足，缺少权限："+ Arrays.toString(permissions));
        }
        return pass;
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

        Object cache = redisUtil.get(buildKey("role", loginUser.getUsername()));
        Set<String> roleList;
        if (Objects.nonNull(cache)) {
            roleList = (Set<String>) cache;
        } else {
            roleList = commonAPI.queryUserRoles(loginUser.getUsername());
            redisUtil.set(buildKey("role", loginUser.getUsername()), roleList);
        }

        boolean pass = roleList.stream().filter(StringUtils::hasText)
                .anyMatch(x -> PatternMatchUtils.simpleMatch(roles, x));
        if (!pass) {
            log.error("权限不足，缺少角色：" + Arrays.toString(roles));
        }
        return pass;
    }

    /**
     * 由于缓存key是以人的维度，角色列表、权限列表在值中，jeecg是以权限列表绑定在角色上，形成的权限集合
     * 权限发生变更时，需要清理全部人的权限缓存
     */
    public void clearCache() {
        redisUtil.removeAll(PERM_PREFIX);
    }

    private String buildKey(String type, String username) {
        return PERM_PREFIX + type + SPLIT + username;
    }
}
