package org.jeecg.modules.system.util;

import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;

import java.util.Collection;

/**
 * Shiro 权限缓存工具
 * 角色授权变更后清理相关用户的权限缓存，使其无需重新登录即生效
 *
 * @author scott
 * @since 2026-10-29 【LHZP-1129】角色/部门角色授权后清理相关用户权限缓存
 */
public class ShiroAuthCacheUtil {

    private ShiroAuthCacheUtil() {
    }

    /**
     * 清理用户的Shiro权限缓存
     * @param redisUtil redis工具
     * @param userIds 用户id集合
     * @author scott
     * @since 2026-10-29 【LHZP-1129】角色/部门角色授权后清理相关用户权限缓存
     */
    public static void clearUserAuthCache(RedisUtil redisUtil, Collection<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return;
        }
        String[] keys = userIds.stream().filter(oConvertUtils::isNotEmpty).distinct()
                .map(userId -> CommonConstant.PREFIX_USER_SHIRO_CACHE + userId).toArray(String[]::new);
        if (keys.length > 0) {
            redisUtil.del(keys);
        }
    }
}
