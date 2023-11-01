package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 群聊消息
 * </p>
 *
 * @author junko
 * @since 2021-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(MucMsg.TABLE_NAME)
public class MucMsg extends BaseModel<MucMsg> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_muc_msg";

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 发送者
     */
    private Integer userId;
    /**
     * 群组id
     */
    private Integer mucId;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 消息id
     */
    private String stanzaId;
    /**
     * 内容
     */
    private String content;
    /**
     * json
     */
    private String body;
    /**
     * 加密
     */
    private Boolean isEncrypt = false;
    /**
     * 敏感内容
     */
    private Boolean isSpam = false;
    //置顶时间
    private Long tsPin;

    /**
     * 发送时间戳
     */
    private Long tsSend;
    /**
     * 删除时间戳
     */
    private Long tsDelete;
    /**
     * 已读数
     */
    private Integer readCount;
    /**
     * 撤回时间
     */
    private Long tsRevoke;
    /**
     * 撤回人
     */
    private Integer revokerId;

    @TableField(exist = false)
    public Boolean isSend;
    @TableField(exist = false)
    public MucMember sender;
    @TableField(exist = false)
    public Muc muc;

}
