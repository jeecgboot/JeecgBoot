package org.jeecg.common.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.desensitization.annotation.SensitiveField;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>
 * 在线用户信息
 * </p>
 *
 * @Author scott
 * @since 2023-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserAccountInfo {

    /**
     * 登录人id
     */
    private String id;

    /**
     * 登录人账号
     */
    private String username;

    /**
     * 登录人名字
     */
    private String realname;

    /**
     * 电子邮件
     */
    private String email;

    /**
     * 头像
     */
    @SensitiveField
    private String avatar;

    /**
     * 同步工作流引擎1同步0不同步
     */
    private Integer activitiSync;

    /**
     * 电话
     */
    @SensitiveField
    private String phone;
}
