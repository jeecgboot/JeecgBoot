package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 验证码
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(VerifyCode.TABLE_NAME)
public class VerifyCode extends BaseModel<VerifyCode> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_verify_code";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    /**
     * 号码前缀
     */
    private Integer prefix;
    /**
     * 邮箱后缀
     */
    private Integer suffix;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 验证码
     */
    private String code;

    /**
     * 渠道
     */
    private String way;

    /**
     * 类型：登录，注册，找回密码
     */
    private String type;

    private Long tsCreate;


    @TableField(exist = false)
    private User user;

    public enum Type {
        Register, FindPwd, Login,ResetPwd, Binding
    }
}
