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
 * 群聊投票
 * </p>
 *
 * @author junko
 * @since 2022-05-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_vote")
public class Vote extends BaseModel<Vote> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String question;

    /**
     * 1：群聊投票，0：频道投票，2：动态投票
     */
    private Integer type;

    /**
     * 群id
     */
    private Integer mucId;

    /**
     * 频道id
     */
    private Integer channelId;

    /**
     * 发布的群成员id
     */
    private Integer mucMemberId;

    /**
     * 发布的频道成员id
     */
    private Integer channelMemberId;

    /**
     * 多选
     */
    private Boolean isMultiple;

    /**
     * 测验模式
     */
    private Boolean isCheckMode;

    /**
     * 说明，选择错误选项时看到此提示
     */
    private String comment;

    /**
     * 源投票id
     */
    private Integer originId;

    private Long tsCreate;


}
