package org.jeecg.config.satoken;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.stp.StpInterface;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.CommonAPI;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @description: Sa-Token 权限认证接口实现（带缓存）
 * 
 * <p>⚠️ 重要说明：</p>
 * <ul>
 *   <li><strong>Sa-Token 的 StpInterface 默认不提供缓存能力</strong>，需要自己实现缓存逻辑</li>
 *   <li>本实现采用 <strong>[账号id -> 权限/角色列表]</strong> 缓存模型</li>
 *   <li>缓存键格式：
 *     <ul>
 *       <li>用户权限缓存：satoken:user-permission:{username}</li>
 *       <li>用户角色缓存：satoken:user-role:{username}</li>
 *     </ul>
 *   </li>
 *   <li>缓存过期时间：30天</li>
 *   <li>⚠️ 当修改用户的角色或权限时，需要手动清除缓存</li>
 * </ul>
 * 
 * <p>清除缓存示例：</p>
 * <pre>
 * // 清除单个用户的权限和角色缓存
 * StpInterfaceImpl.clearUserCache("admin");
 * 
 * // 清除多个用户的缓存
 * StpInterfaceImpl.clearUserCache(Arrays.asList("admin", "user1", "user2"));
 * </pre>
 */
@Component
@Slf4j
public class StpInterfaceImpl implements StpInterface {

    @Lazy
    @Resource
    private CommonAPI commonApi;

    /**
     * 缓存过期时间（秒）：30天
     */
    private static final long CACHE_TIMEOUT = 60 * 60 * 24 * 30;
    
    /**
     * 权限缓存键前缀
     */
    private static final String PERMISSION_CACHE_PREFIX = "satoken:user-permission:";
    
    /**
     * 角色缓存键前缀
     */
    private static final String ROLE_CACHE_PREFIX = "satoken:user-role:";

    /**
     * 返回一个账号所拥有的权限码集合（带缓存）
     * 
     * @param loginId 账号id（这里是 username）
     * @param loginType 账号类型
     * @return 权限码集合
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<String> getPermissionList(Object loginId, String loginType) {
        String username = loginId.toString();
        String cacheKey = PERMISSION_CACHE_PREFIX + username;
        
        SaTokenDao dao = SaManager.getSaTokenDao();
        
        // 1. 先从缓存获取
        List<String> permissionList = (List<String>) dao.getObject(cacheKey);
        
        if (permissionList == null) {
            // 2. 缓存不存在，从数据库查询
            log.warn("权限缓存未命中，查询数据库 [ username={} ]", username);
            
            String userId = commonApi.getUserIdByName(username);
            if (userId == null) {
                log.warn("用户不存在: {}", username);
                return new ArrayList<>();
            }
            
            Set<String> permissionSet = commonApi.queryUserAuths(userId);
            permissionList = new ArrayList<>(permissionSet);
            
            // 3. 将结果缓存起来
            dao.setObject(cacheKey, permissionList, CACHE_TIMEOUT);
            log.info("权限已缓存 [ username={}, permissions={} ]", username, permissionList.size());
        } else {
            log.debug("权限缓存命中 [ username={}, permissions={} ]", username, permissionList.size());
        }
        
        return permissionList;
    }

    /**
     * 返回一个账号所拥有的角色标识集合（带缓存）
     * 
     * @param loginId 账号id（这里是 username）
     * @param loginType 账号类型
     * @return 角色标识集合
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<String> getRoleList(Object loginId, String loginType) {
        String username = loginId.toString();
        String cacheKey = ROLE_CACHE_PREFIX + username;
        
        SaTokenDao dao = SaManager.getSaTokenDao();
        
        // 1. 先从缓存获取
        List<String> roleList = (List<String>) dao.getObject(cacheKey);
        
        if (roleList == null) {
            // 2. 缓存不存在，从数据库查询
            log.warn("角色缓存未命中，查询数据库 [ username={} ]", username);
            
            String userId = commonApi.getUserIdByName(username);
            if (userId == null) {
                log.warn("用户不存在: {}", username);
                return new ArrayList<>();
            }
            
            Set<String> roleSet = commonApi.queryUserRolesById(userId);
            roleList = new ArrayList<>(roleSet);
            
            // 3. 将结果缓存起来
            dao.setObject(cacheKey, roleList, CACHE_TIMEOUT);
            log.info("角色已缓存 [ username={}, roles={} ]", username, roleList.size());
        } else {
            log.debug("角色缓存命中 [ username={}, roles={} ]", username, roleList.size());
        }
        
        return roleList;
    }
    
    /**
     * 清除单个用户的权限和角色缓存
     * <p>使用场景：修改用户的角色分配后</p>
     * 
     * @param username 用户名
     */
    public static void clearUserCache(String username) {
        SaTokenDao dao = SaManager.getSaTokenDao();
        dao.deleteObject(PERMISSION_CACHE_PREFIX + username);
        dao.deleteObject(ROLE_CACHE_PREFIX + username);
        log.info("已清除用户缓存 [ username={} ]", username);
    }
    
    /**
     * 批量清除多个用户的权限和角色缓存
     * <p>使用场景：修改角色权限后，清除拥有该角色的所有用户的缓存</p>
     * 
     * @param usernameList 用户名列表
     */
    public static void clearUserCache(List<String> usernameList) {
        SaTokenDao dao = SaManager.getSaTokenDao();
        for (String username : usernameList) {
            dao.deleteObject(PERMISSION_CACHE_PREFIX + username);
            dao.deleteObject(ROLE_CACHE_PREFIX + username);
        }
        log.info("已批量清除用户缓存 [ count={} ]", usernameList.size());
    }
}
