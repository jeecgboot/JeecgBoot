package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 用户历史头像
 * </p>
 *
 * @author junko
 * @since 2022-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_user_avatar")
public class UserAvatar extends BaseModel<UserAvatar> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 原图
     */
    private String origin;

    /**
     * 缩略图
     */
    private String thumb;

    private Long tsCreate;
    /**
     * 审核时间,0:待审核，-1：拒绝，具体时间为通过
     */
    private Long tsAudit;

    private Integer userId;

    @TableField(exist = false)
    private User user;
}
