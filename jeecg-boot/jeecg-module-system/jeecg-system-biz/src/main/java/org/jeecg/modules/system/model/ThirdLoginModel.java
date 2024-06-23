package org.jeecg.modules.system.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 第三方登录 信息存储
 * @author: jeecg-boot
 */
@Data
public class ThirdLoginModel implements Serializable {
    private static final long serialVersionUID = 4098628709290780891L;

    /**
     * 第三方登录 来源
     */
    private String source;

    /**
     * 第三方登录 uuid
     */
    private String uuid;

    /**
     * 第三方登录 username
     */
    private String username;

    /**
     * 第三方登录 头像
     */
    private String avatar;

    /**
     * 账号 后缀第三方登录 防止账号重复
     */
    private String suffix;

    /**
     * 操作码 防止被攻击
     */
    private String operateCode;

    public ThirdLoginModel(){

    }

    /**
     * 构造器
     * @param source
     * @param uuid
     * @param username
     * @param avatar
     */
    public ThirdLoginModel(String source,String uuid,String username,String avatar){
        this.source = source;
        this.uuid = uuid;
        this.username = username;
        this.avatar = avatar;
    }

    /**
     * 获取登录账号名
     * @return
     */
    public String getUserLoginAccount(){
        if(suffix==null){
            return this.uuid;
        }
        return this.uuid + this.suffix;
    }
}
