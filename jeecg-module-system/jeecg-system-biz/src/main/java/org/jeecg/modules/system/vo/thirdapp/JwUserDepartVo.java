package org.jeecg.modules.system.vo.thirdapp;

import lombok.Data;

/**
* @Description: 企业微信用户同步工具类
*
* @author: wangshuai
* @date: 2023/11/28 18:17
*/
@Data
public class JwUserDepartVo {
    
    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 企业微信的名字
     */
    private String wechatRealName;

    /**
     * 企业微信对应的部门
     */
    private String wechatDepartId;

    /**
     * 企业微信对应的用户id
     */
    private String wechatUserId;

    /**
     * 第三方id
     */
    private String thirdId;
}
