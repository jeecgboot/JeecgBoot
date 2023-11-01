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
 * 频道日志
 * </p>
 *
 * @author junko
 * @since 2022-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_channel_log")
public class ChannelLog extends BaseModel<ChannelLog> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 频道id
     */
    private Integer channelId;

    /**
     * 成员id
     */
    private Integer memberId;

    /**
     * 操作类型
     */
    private Integer type;

    /**
     * 具体操作
     */
    private String operation;

    private Long tsCreate;
}
