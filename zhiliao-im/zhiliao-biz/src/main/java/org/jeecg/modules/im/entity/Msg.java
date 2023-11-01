package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 聊天消息
 * </p>
 *
 * @author junko
 * @since 2021-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(Msg.TABLE_NAME)
public class Msg extends BaseModel<Msg> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_msg";

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    //消息来源id
    private Long originId;
    /**
     * 发送者
     */
    private Integer userId;
    /**
     * 接收者
     */
    private Integer toUserId;
    /**
     * 内容
     */
    private String content;
    /**
     * json
     */
    private String body;
    /**
     * 消息id
     */
    private String stanzaId;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 加密
     */
    private Boolean isEncrypt = false;
    /**
     * 发送时间戳
     */
    private Long tsSend;
    /**
     * 置顶
     */
    private Long tsPin;
    /**
     * 删除时间戳
     */
    private Long tsDelete = 0L;
    /**
     * 已读时间戳
     */
    private Long tsRead = 0L;
    /**
     * 撤回时间
     */
    private Long tsRevoke = 0L;
    /**
     *0：正常，1：发送人撤回，2：接收人撤回，3：系统撤回
     */
    private Integer revokeType=0;
    /**
     * 阅后即焚
     */
    private Boolean isReadDel = false;
    /**
     * 敏感内容
     */
    private Boolean isSpam = false;

    /**
     * 过滤
     */
    @TableField(exist = false)
    private Integer filter;
    /**
     * 发送/接收
     */
    private Boolean isSend;

    @TableField(exist = false)
    public User fromUser;
    @TableField(exist = false)
    public User toUser;

}
