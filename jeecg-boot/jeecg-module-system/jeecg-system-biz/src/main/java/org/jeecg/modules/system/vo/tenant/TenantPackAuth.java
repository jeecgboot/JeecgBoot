package org.jeecg.modules.system.vo.tenant;

import lombok.Data;

/**
 * 租户产品包 关联权限详情
 * @Author taoYan
 * @Date 2023/2/16 21:02
 **/
@Data
public class TenantPackAuth {

    /**
     * 一级菜单
     */
    private String category;

    /**
     * 权限菜单名称
     */
    private String authName;


    /**
     * 权限菜单描述
     */
    private String authNote;
}
