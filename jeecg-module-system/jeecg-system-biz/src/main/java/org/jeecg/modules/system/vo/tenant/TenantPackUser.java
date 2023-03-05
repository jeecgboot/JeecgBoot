package org.jeecg.modules.system.vo.tenant;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * 用户产品包 关联的用户信息
 * @Author taoYan
 * @Date 2023/2/16 21:02
 **/
@Data
public class TenantPackUser {
    /**
     * 用户ID
     */
    private String id;
    
    private String username;
    
    private String realname;
    
    private String avatar;
    
    private String phone;

    /**
     * 多个 部门名称集合
     */
    private Set<String> departNames;

    /**
     * 多个 职位名称集合
     */
    private Set<String> positionNames;

    /**
     * 租户产品包名称
     */
    private String packName;

    /**
     * 租户产品包ID
     */
    private String packId;
    
    public void addDepart(String name){
        if(departNames==null){
            departNames = new HashSet<>();
        }
        departNames.add(name);
    }


    public void addPosition(String name){
        if(positionNames==null){
            positionNames = new HashSet<>();
        }
        positionNames.add(name);
    }
}
