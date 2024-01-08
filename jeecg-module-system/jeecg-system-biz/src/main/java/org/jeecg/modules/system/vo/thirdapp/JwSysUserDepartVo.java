package org.jeecg.modules.system.vo.thirdapp;

import lombok.Data;

import java.util.List;

/**
 * 企业微信的实现类
 */
@Data
public class JwSysUserDepartVo {

    /**
     * 企业微信和用户的映射类
     */
    private List<JwUserDepartVo> jwUserDepartVos;

    /**
     * 用户列表
     */
    private List<JwUserDepartVo> userList;

}
