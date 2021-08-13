package org.jeecg.modules.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 *
 * @Author: chenli
 * @Date: 2020-06-07
 * @Version: V1.0
 */
@Data
public class SysOnlineVO {
    /**
     * 会话id
     */
    private String id;

    /**
     * 会话编号
     */
    private String token;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户名
     */
    private String realname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 生日
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 性别（1：男 2：女）
     */
    @Dict(dicCode = "sex")
    private Integer sex;

    /**
     * 手机号
     */
    private String phone;
}
