package org.jeecg.modules.system.vo.tenant;

import lombok.Data;

import java.util.List;

/**
 * 租户产品包信息
 *  包括+ 用户信息 + 权限信息
 * @Author taoYan
 * @Date 2023/2/16 21:01
 **/
@Data
public class TenantPackModel {

    /**
     * 租户Id
     */
    private Integer tenantId;
    /**
     * 产品包编码
     */
    private String packCode;

    /**
     * 产品包ID
     */
    private String packId;

    /**
     * 产品包名称
     */
    private String packName;

    /**
     * 产品包 权限信息
     */
    private List<TenantPackAuth> authList;

    /**
     * 产品包 用户列表
     */
    private List<TenantPackUser> userList;

    /**
     * 状态 正常状态1 申请状态0
     */
    private Integer packUserStatus;
    
    public Integer getPackUserStatus(){
        if(packUserStatus==null){
            return 1;
        }
        return packUserStatus; 
    }
}
