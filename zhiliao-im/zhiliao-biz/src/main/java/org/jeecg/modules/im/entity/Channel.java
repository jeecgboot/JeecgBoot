package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 频道
 * </p>
 *
 * @author junko
 * @since 2022-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_channel")
public class Channel extends BaseModel<Channel> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 简介
     */
    private String info;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 缩略头像
     */
    private String smallAvatar;

    /**
     * 关联的讨论群组
     */
    private Integer mucId;
    /**
     * 创建者id
     */
    private Integer userId;

    /**
     * 类型，0：私有，1：公开
     */
    private Boolean isPublic;

    /**
     * 消息署名
     */
    private Boolean isMsgSign;

    /**
     * 关注人数
     */
    private Integer followerCount;

    private Long tsCreate;

    /**
     * 截止锁定时间
     */
    private Long tsLocked;

    /**
     * 公开时用于创建唯一链接
     */
    private String publicAccount;

    //创建人
    @TableField(exist = false)
    private User user;
    //关联群组
    @TableField(exist = false)
    private Muc muc;

}
