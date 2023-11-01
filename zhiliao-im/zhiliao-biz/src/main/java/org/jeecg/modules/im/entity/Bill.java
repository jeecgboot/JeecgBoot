package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 账单
 * </p>
 *
 * @author junko
 * @since 2021-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(Bill.TABLE_NAME)
public class Bill extends BaseModel<Bill> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_bill";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    /**
     * 金额
     */
    private Integer amount;

    /**
     * 入账前余额
     */
    private Integer balanceBefore;

    /**
     * 入账后余额
     */
    private Integer balanceAfter;

    private Long tsCreate;

    private Boolean increase;

    /**
     * 标题
     */
    private String title;

    /**
     * 详情
     */
    private String body;

    /**
     * 业务类型
     */
    private String type;

    /**
     * 支付方式
     */
    private String payType;


    @TableField(exist = false)
    private User user;

    public enum Type {
        Withdraw("提现"),Recharge("充值"),RedPack("红包"),TransferOut("转账");
        String name;
        Type(String name){
            this.name = name;
        }
    }
    public enum PayType {
        Manual("后台充值"),Online("在线支付"),Apply("提交申请");
        String name;
        PayType(String name){
            this.name = name;
        }
    }

}
