package org.jeecg.common.system.vo;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lgl
 * @version 1.0
 * @description: 放入ThreadLocal中的Tenant对象
 * @date 2022/2/22 16:26
 */
@Data
@NoArgsConstructor
public class TenantVo {
    //默认租户id
    private String tenantId;
    //多租户对于ids
    private String tenantIds;
    //是否查询所有
    private boolean queryAll;

    public TenantVo(String tenantId){
        this.tenantId=tenantId;
    }

    public TenantVo(String tenantId, String tenantIds){
        this.tenantId=tenantId;
        this.tenantIds=tenantIds;
    }


}
