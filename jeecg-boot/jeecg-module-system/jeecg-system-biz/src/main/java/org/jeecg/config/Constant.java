package org.jeecg.config;

public interface Constant {
    // 登录
    public static final String ALIST_AUTH_LOGIN = "/auth/login";

    // 创建用户
    public static final String ALIST_USER_CREATE = "/admin/user/create";

    // 更新用户
    public static final String ALIST_USER_UPDATE = "/admin/user/update";

    // 禁用存储
    public static final String ALIST_STORAGE_DISABLE = "/admin/storage/disable";

    // 创建存储
    public static final String ALIST_STORAGE_CREATE = "/admin/storage/create";

    // 启用存储
    public static final String ALIST_STORAGE_ENABLE = "/admin/storage/enable";

    // 更新存储
    public static final String ALIST_STORAGE_UPDATE = "/admin/storage/update";

    // 删除存储
    public static final String ALIST_STORAGE_DELETE = "/admin/storage/delete";

    // 重新加载所有存储
    public static final String ALIST_STORAGE_LOADALL = "/admin/storage/load_all";
}
