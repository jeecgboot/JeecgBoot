package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 通讯录联系人
 * </p>
 *
 *
 * @author junko
 * @since 2021-02-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(Contact.TABLE_NAME)
public class Contact extends BaseModel<Contact> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_contact";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    /**
     * 备注
     */
    private String name;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 首字母
     */
    private String capital;

    private Long tsCreate;

    /*
     * 未注册：邀请注册
     * 已注册：发送添加请求
     */
    @TableField(exist = false)
    private User user;
    /*
    与当前用户是否是好友
     */
    @TableField(exist = false)
    private Friend friend;

}
