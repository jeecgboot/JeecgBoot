package org.jeecg.modules.auth.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "PermissionAction")
public enum PermissionAction {
    CREATE(1, "CREATE"), // 0001
    READ(2, "READ"), // 0010
    UPDATE(4, "UPDATE"), // 0100
    DELETE(8, "DELETE"); // 1000

    private final int bit;

    private final String name;

    /**
     * 检查权限位中是否包含该动作
     */
    public boolean isAllowed(int permissionBits) {
        return (permissionBits & bit) == bit;
    }

    /**
     * 添加权限到位图
     */
    public static int addPermission(int current, PermissionAction action) {
        return current | action.getBit();
    }

    /**
     * 移除权限
     */
    public static int removePermission(int current, PermissionAction action) {
        return current & ~action.getBit();
    }

    /**
     * 检查是否有任意权限
     */
    public static boolean hasAnyPermission(int permissionBits) {
        return permissionBits > 0;
    }

    /**
     * 检查是否有全部权限
     */
    public static boolean hasAllPermissions(int permissionBits) {
        return permissionBits == 15; // 1111
    }

    /**
     * 根据权限位获取权限动作
     */
    public static List<PermissionAction> getPermissionActions(Integer permissionBits) {
        if (permissionBits == null) {
            return Collections.emptyList();
        }
        if (PermissionAction.hasAllPermissions(permissionBits)) {
            return Arrays.asList(values());
        }

        return Arrays.stream(values())
                .filter(action -> action.isAllowed(permissionBits))
                .toList();

    }
}
