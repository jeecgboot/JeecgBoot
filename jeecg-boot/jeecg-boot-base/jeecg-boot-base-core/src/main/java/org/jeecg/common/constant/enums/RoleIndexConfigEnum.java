package org.jeecg.common.constant.enums;

/**
 * 首页自定义
 * 通过角色编码与首页组件路径配置
 */
public enum RoleIndexConfigEnum {
    /**
     * 管理员
     */
    ADMIN("admin1", "dashboard/Analysis2"),
    /**
     * 测试
     */
    TEST("test",  "dashboard/Analysis"),
    /**
     * hr
     */
    HR("hr", "dashboard/Analysis1");

    /**
     * 角色编码
     */
    String roleCode;
    /**
     * 路由index
     */
    String componentUrl;

    /**
     * 构造器
     *
     * @param roleCode 角色编码
     * @param componentUrl 首页组件路径（规则跟菜单配置一样）
     */
    RoleIndexConfigEnum(String roleCode, String componentUrl) {
        this.roleCode = roleCode;
        this.componentUrl = componentUrl;
    }
    /**
     * 根据code找枚举
     * @param roleCode 角色编码
     * @return
     */
    public static RoleIndexConfigEnum getEnumByCode(String roleCode) {
        for (RoleIndexConfigEnum e : RoleIndexConfigEnum.values()) {
            if (e.roleCode.equals(roleCode)) {
                return e;
            }
        }
        return null;
    }
    /**
     * 根据code找index
     * @param roleCode 角色编码
     * @return
     */
    public static String getIndexByCode(String roleCode) {
        for (RoleIndexConfigEnum e : RoleIndexConfigEnum.values()) {
            if (e.roleCode.equals(roleCode)) {
                return e.componentUrl;
            }
        }
        return null;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getComponentUrl() {
        return componentUrl;
    }

    public void setComponentUrl(String componentUrl) {
        this.componentUrl = componentUrl;
    }
}
