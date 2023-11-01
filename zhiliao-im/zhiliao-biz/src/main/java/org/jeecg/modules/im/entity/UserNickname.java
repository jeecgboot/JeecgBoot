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
 * 昵称记录，用于审核
 * </p>
 *
 * @author junko
 * @since 2022-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_user_nickname")
public class UserNickname extends BaseModel<UserNickname> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String nickname;

    private Long tsCreate;

    /**
     * 审核时间,0:待审核，-1：拒绝，具体时间为通过
     */
    private Long tsAudit;

    @TableField(exist = false)
    private User user;
}
