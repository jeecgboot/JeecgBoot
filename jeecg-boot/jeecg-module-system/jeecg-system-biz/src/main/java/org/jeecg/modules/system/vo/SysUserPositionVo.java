package org.jeecg.modules.system.vo;

import lombok.Data;

/**
* @Description: 用户职位实体类
*
* @author: wangshuai
* @date: 2023/6/14 16:41
*/
@Data
public class SysUserPositionVo {
    
    /**职位id*/
    private String id;

    /**职务名称*/
    private String name;
    
    /**用户id*/
    private String userId;
}
