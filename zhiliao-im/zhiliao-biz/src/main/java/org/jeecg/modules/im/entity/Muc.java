package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 群组
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(Muc.TABLE_NAME)
public class Muc extends BaseModel<Muc> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_muc";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 创建者id
     */
    private Integer userId;
    /**
     * 创建的管理员id
     */
    private String adminId;

    //群主id
    private Integer masterId;
    /**
     * 群名称
     */
    private String name;
    /**
     * 描述
     */
    private String info;

    /**
     * 群头像
     */
    private String avatar;
    /**
     * 缩略头像
     */
    private String smallAvatar;

    /**
     * 群二维码
     */
    private String qrCode;
    //群号
    private String account;
    /**
     * 主题
     */
    private String subject;

    /**
     * 人数
     */
    private Integer memberCount;
    //虚假成员数
    private Integer fakeMember;
    //虚假在线数
    private Integer fakeOnline;

    /**
     * 截止锁定时间
     */
    private Long tsLocked;

    /**
     * 截止禁言时间
     * -1:永久
     * 0：无
     * ts：具体截止时间
     */
    private Long tsMute;

    private Long tsLastTalk;

    private Long tsUpdate;

    private Long tsCreate;
    //解散时间
    private Long tsDelete;
    /**
     * 群组配置
     */
    @TableField(exist = false)
    private MucConfig mucConfig;
    //创建人
    @TableField(exist = false)
    private User user;
    //当前用户成员
    @TableField(exist = false)
    private MucMember member;
    //群主
    @TableField(exist = false)
    private MucMember master;
    //当前用户在群里的角色
    @TableField(exist = false)
    private Integer role;
    //群成员
    @TableField(exist = false)
    private List<MucMember> members;
    //群组默认权限
    @TableField(exist = false)
    private MucPermission mucPermission;
    //进群邀请
    @TableField(exist = false)
    private List<MucInvite> invites;

}
