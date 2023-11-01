package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.aspect.annotation.Dict;

import java.util.List;

/**
 * <p>
 * 注册邀请码
 * </p>
 *
 * @author junko
 * @since 2023-01-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_invite_code")
public class InviteCode extends BaseModel<InviteCode> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String code;
    //最后使用
    private Long tsLast;

    private Long tsCreate;

    //最大使用次数，-1：不限制，0：不使用，>0：具体次数
    private Integer maxTimes;
    //自动添加的好友
    private String userToAdd;
    //自动加入的群聊
    private String mucToJoin;

    //使用次数
    private Integer times;
    //启用
    private Boolean isEnable;

    private Integer userId;

//    0:不启用，1：选填，2：必填

    public enum Type {
        disable(0),optional(1),require(2);
        Integer code;

        public Integer getCode() {
            return code;
        }

        Type(int code){
            this.code = code;
        }
        public static Type getByCode(int code) {
            for (Type value : Type.values()) {
                if (value.code==code) {
                    return value;
                }
            }
            return disable;
        }
    }
}
