package org.jeecg.common.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * online 拦截器权限判断
 * cloud api 用到的接口传输对象
 * @author: jeecg-boot
 */
@Data
public class OnlineAuthDTO implements Serializable {
    private static final long serialVersionUID = 1771827545416418203L;


    /**
     * 用户名
     */
    private String username;

    /**
     * 可能的请求地址
     */
    private List<String> possibleUrl;

    /**
     * online开发的菜单地址
     */
    private String onlineFormUrl;

    //update-begin---author:chenrui ---date:20240123  for：[QQYUN-7992]【online】工单申请下的online表单，未配置online表单开发菜单，操作报错无权限------------
    /**
     * online工单的地址
     */
    private String onlineWorkOrderUrl;
    //update-end---author:chenrui ---date:20240123  for：[QQYUN-7992]【online】工单申请下的online表单，未配置online表单开发菜单，操作报错无权限------------

    public OnlineAuthDTO(){

    }

    public OnlineAuthDTO(String username, List<String> possibleUrl, String onlineFormUrl){
        this.username = username;
        this.possibleUrl = possibleUrl;
        this.onlineFormUrl = onlineFormUrl;
    }
}
