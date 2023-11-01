package org.jeecg.modules.im.entity;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author junko
 * @since 2021-09-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(UserInfo.TABLE_NAME)
public class UserInfo extends BaseModel<UserInfo> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_user_info";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String province;

    private String city;

    private String district;
    private String address;

    private String country;
    private String countryCode;
    private String countryDialCode;

    /**
     * 性别
     */
    private String gender;
    /**
     * 生日
     */
    private Date birthday;

    /**
     * 签名
     */
    private String signature;

    /**
     * 创建的群数量
     */
    @TableField(exist = false)
    private Integer mucCreate;

    /**
     * 管理的群数量
     */
    @TableField(exist = false)
    private Integer mucManage;

    /**
     * 加入的群数量
     */
    @TableField(exist = false)
    private Integer mucJoin;

    /**
     * 好友数
     */
    @TableField(exist = false)
    private Integer friendCount;

    /**
     * 设备数
     */
    @TableField(exist = false)
    private Integer deviceCount;

    /**
     * 通讯录数量
     */
    @TableField(exist = false)
    private Integer contactCount;

    /**
     * 余额
     */
    private Integer balance;

    /**
     * 冻结余额
     */
    private Integer balanceFreeze;

    /**
     * 连续签到天数
     */
    private Integer continueSignInDay;

    /**
     * 累计签到天数
     */
    private Integer signInDay;

    /**
     * 状态
     */
    private String state;

}
