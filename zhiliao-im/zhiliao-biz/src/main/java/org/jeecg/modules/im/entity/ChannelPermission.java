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
 * 频道管理员权限
 * </p>
 *
 * @author junko
 * @since 2022-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_channel_permission")
public class ChannelPermission extends BaseModel<ChannelPermission> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer channelId;

    private Integer memberId;

    /**
     * 更改频道信息
     */
    private Boolean modifyInfo;

    /**
     * 发布消息
     */
    private Boolean publishNews;

    /**
     * 编辑其他管理员的消息
     */
    private Boolean editOthersPublish;

    /**
     * 删除其他管理员的消息
     */
    private Boolean deleteOthersPublish;

    /**
     * 添加成员
     */
    private Boolean addMember;

    /**
     * 管理直播
     */
    private Boolean manageLive;

    /**
     * 添加新管理员
     */
    private Boolean addAdmin;
}
