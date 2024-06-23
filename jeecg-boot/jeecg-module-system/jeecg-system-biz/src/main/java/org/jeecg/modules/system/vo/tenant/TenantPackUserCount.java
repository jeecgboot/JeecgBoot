package org.jeecg.modules.system.vo.tenant;

import lombok.Data;

/**
 * 用于统计 租户产品包的人员数量
 * @Author taoYan
 * @Date 2023/2/16 15:59
 **/
@Data
public class TenantPackUserCount {

    /**
     * 租户产品包编码
     */
    private String packCode;

    /**
     * 用户数量
     */
    private String userCount;
    
}
