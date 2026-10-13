package org.jeecg.common.constant.enums;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/**
 * 系统内置固定角色编码枚举（角色 role_code 固定不可改，代码里涉及这些角色的判断统一引用本枚举，禁止散落硬编码字符串）
 *
 * @author scott
 * @since 2026-10-13 【安全】系统固定角色常量化：组织管理类接口放行 admin / 租户管理员
 */
public enum SysRoleEnum {

    /**
     * 系统超级管理员
     */
    ADMIN("admin", "超管角色"),

    /**
     * 系统租户管理员（后台授予；SaaS多租户模式下注册用户默认不含此角色）
     */
    TENANT_ADMIN("zuhuadmin", "租户管理员");

    /**
     * 角色编码（sys_role.role_code，固定不可改）
     */
    private final String roleCode;

    /**
     * 角色名称（仅说明用）
     */
    private final String roleName;

    SysRoleEnum(String roleCode, String roleName) {
        this.roleCode = roleCode;
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    /**
     * 当前登录用户是否拥有任意一个指定的系统固定角色
     *
     * @param roles 待判断的系统固定角色
     * @return 拥有其中任意一个返回true
     */
    public static boolean hasAnyRole(SysRoleEnum... roles) {
        if (roles == null || roles.length == 0) {
            return false;
        }
        Subject subject = SecurityUtils.getSubject();
        for (SysRoleEnum role : roles) {
            if (subject.hasRole(role.roleCode)) {
                return true;
            }
        }
        return false;
    }
}
