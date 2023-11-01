package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 频道邀请链接
 * </p>
 *
 * @author junko
 * @since 2022-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_channel_invitation")
public class ChannelInvitation extends BaseModel<ChannelInvitation> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 频道id
     */
    private Integer channelId;

    /**
     * 创建人
     */
    private Integer memberId;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 有效时长
     */
    private Integer validHours;

    /**
     * 次数限制
     */
    private Integer maxTimes;

    /**
     * 已撤销
     */
    private Boolean isRevoked;
    /**
     * 1：公开，0：私有
     */
    private Boolean isPublic;

}
