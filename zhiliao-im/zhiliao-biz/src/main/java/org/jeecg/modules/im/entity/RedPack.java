package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * <p>
 * 红包
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(RedPack.TABLE_NAME)
public class RedPack extends BaseModel<RedPack> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_red_pack";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发送人id
     */
    private Integer userId;

    /**
     * 接收方id
     */
    private Integer toUserId;

    /**
     * 0：普通，1：手气，2：口令，3：专属
     */
    private Integer type;

    /**
     * 祝福语
     */
    private String words;

    /**
     * 总个数
     */
    private Integer count;

    /**
     * 总金额
     */
    private Integer amount;

    /**
     * 已开个数
     */
    private Integer openCount;

    /**
     * 剩余金额
     */
    private Integer leftAmount;

    /**
     * 状态，0：发出，1：已领完，2：已退款，3：未领完退款
     */
    private Integer status;

    /**
     * 群组id
     */
    private Integer mucId;

    private Long tsCreate;

    private Long tsValid;


    //支付密码
    @TableField(exist = false)
    private String payPwd;

    //发送方
    @TableField(exist = false)
    private User user;
    //接收方
    @TableField(exist = false)
    private User toUser;
    //群组
    @TableField(exist = false)
    private Muc muc;

    public enum Status{
        SEND(0),DONE(1),REVERT(2),REFUND(3);
        int code;
        Status(int code){
            this.code = code;
        }
        public int getCode() {
            return code;
        }
    }
    public enum Type{
        COMMON(0),LUCKY(1),COMMAND(2),ASSIGN(3);
        int code;
        Type(int code){
            this.code = code;
        }
        public int getCode() {
            return code;
        }
    }
}
