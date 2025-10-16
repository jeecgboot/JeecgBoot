package org.jeecg.common.util;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.vo.LoginUser;

/**
 * 登录用户工具类
 * 替代原有的Shiro SecurityUtils工具类
 * @author jeecg-boot
 */
@Slf4j
public class LoginUserUtils {

    /**
     * Session中存储登录用户信息的key
     */
    private static final String SESSION_KEY_LOGIN_USER = "loginUser";

    /**
     * 执行登录并设置用户信息到Session（推荐）
     *
     * <p>此方法会：
     * <ul>
     *   <li>1. 调用 StpUtil.login(username) 生成token和session</li>
     *   <li>2. 将 LoginUser 存入 Session 缓存（清除不必要的字段（密码等15个字段）</li>
     *   <li>3. 返回生成的 token</li>
     * </ul>
     *
     * @param sysUser 完整的用户对象（从数据库查询得到）
     * @return 生成的 token
     */
    public static String doLogin(LoginUser sysUser) {
        if (sysUser == null) {
            throw new IllegalArgumentException("用户对象不能为空");
        }

        try {
            // 1. 获取 username
            String username = sysUser.getUsername();

            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("用户名不能为空");
            }

            // 2. Sa-Token 登录（使用 username 作为 loginId）
            StpUtil.login(username);

            // 3. 用户信息到 LoginUser 并存入 Session
            setSessionUser(sysUser);

            // 4. 返回生成的 token
            return StpUtil.getTokenValue();

        } catch (Exception e) {
            throw new RuntimeException("登录失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 获取当前登录用户信息
     *
     * <p>说明：
     * <ul>
     *   <li>对于需要认证的接口：Sa-Token Filter 已经校验过登录状态，此方法必然能获取到用户</li>
     *   <li>对于已排除拦截的接口：如果未登录或获取失败则返回 null，由业务代码自行判断处理</li>
     * </ul>
     *
     * @return 登录用户对象，如果未登录或session中没有则返回null
     */
    public static LoginUser getSessionUser() {
        // 尝试从Sa-Token的Session中获取用户信息
        Object loginUser = StpUtil.getSession().get(SESSION_KEY_LOGIN_USER);
        if (loginUser instanceof LoginUser) {
            return (LoginUser) loginUser;
        }
        return null;
    }
    
    /**
     * 根据指定的 token 获取登录用户信息
     * 
     * <p>适用场景：已排除拦截的接口（如 WebSocket），需要显式传入 token 来获取用户信息
     * 
     * <p>实现方式：临时切换到该 token 对应的会话，然后获取用户信息
     * 
     * @param token JWT token
     * @return 登录用户对象，如果 token 无效或session中没有则返回null
     */
    public static LoginUser getSessionUser(String token) {
        try {
            // 根据 token 获取登录ID
            Object loginId = StpUtil.getLoginIdByToken(token);
            if (loginId == null) {
                return null;
            }
            
            // 临时切换到该 token 对应的登录会话
            StpUtil.switchTo(loginId);
            
            // 直接调用无参方法获取用户信息
            return getSessionUser();
            
        } catch (Exception e) {
            log.debug("根据token获取用户信息失败: {}", e.getMessage());
            return null;
        }
    }
    
    
    /**
     * 设置当前登录用户信息到Session
     * 
     * <p>为减少 Redis 存储和保障安全，只保留必要的核心字段：
     * <ul>
     *   <li>id, username, realname - 基础用户信息</li>
     *   <li>orgCode, orgId, departIds - 部门和数据权限</li>
     *   <li>roleCode - 角色权限</li>
     *   <li>loginTenantId, relTenantIds - 多租户</li>
     *   <li>avatar - 用户头像</li>
     * </ul>
     * 
     * <p>⚠️ 注意：调用此方法前需要先调用 StpUtil.login()
     * 
     * @param loginUser 登录用户对象
     */
    public static void setSessionUser(LoginUser loginUser) {
        if (loginUser == null) {
            return;
        }
        
        // ⚠️ 安全与性能：清除不必要的字段，减少 Redis 存储
        loginUser.setPassword(null);        // 密码（安全）
        loginUser.setWorkNo(null);          // 工号
        loginUser.setBirthday(null);        // 生日
        loginUser.setSex(null);             // 性别
        loginUser.setEmail(null);           // 邮箱
        loginUser.setPhone(null);           // 手机号
        loginUser.setStatus(null);          // 状态
        loginUser.setDelFlag(null);         // 删除标志
        loginUser.setActivitiSync(null);    // 工作流同步
        loginUser.setCreateTime(null);      // 创建时间
        loginUser.setUserIdentity(null);    // 用户身份
        loginUser.setPost(null);            // 职务
        loginUser.setTelephone(null);       // 座机
        loginUser.setClientId(null);        // 设备ID
        loginUser.setMainDepPostId(null);   // 主岗位
        
        StpUtil.getSession().set(SESSION_KEY_LOGIN_USER, loginUser);
    }

    /**
     * 获取当前登录用户名（推荐使用此方法，语义更清晰）
     * @return 用户名（username）
     */
    public static String getUsername() {
        return StpUtil.getLoginIdAsString();
    }

    /**
     * 检查是否已登录
     * @return true-已登录，false-未登录
     */
    public static boolean isLogin() {
        return StpUtil.isLogin();
    }

    /**
     * 退出登录
     */
    public static void logout() {
        StpUtil.logout();
    }
}

