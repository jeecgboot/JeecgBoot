package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 加群邀请
 * </p>
 *
 * @author junko
 * @since 2021-03-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(MucInvite.TABLE_NAME)
public class MucInvite extends BaseModel<MucInvite> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_muc_invite";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 群聊id
     */
    private Integer mucId;
    /**
     * 邀请者 成员id
     */
    private Integer inviter;
    /**
     * 受邀者 用户id
     */
    private Integer invitee;

    /**
     * 备注
     */
    private String comment;

    /**
     * 处理者 成员id
     */
    private Integer handler;

    private Long tsCreate;

    /**
     * 状态，0：待处理，1：验证通过，2：拒绝
     */
    private Integer status;

    /**
     * 有效的
     */
    private Boolean isValid;
    /**
     * 需要验证
     */
    private Boolean isNeedVerify;

    /**
     * 处理时间
     */
    private Long tsDeal;

    @TableField(exist = false)
    private MucMember inviterMember;
    @TableField(exist = false)
    private User inviteeUser;
    @TableField(exist = false)
    private MucMember handlerMember;

    public enum Status{
        Waiting(0),Accept(1),Reject(2),Invalid(3);
        private int code;
        Status(int code){
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

}
