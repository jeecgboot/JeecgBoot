package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 前台ip黑名单
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(BlockIp.TABLE_NAME)
public class BlockIp extends BaseModel<BlockIp> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_block_ip";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String ip;
    @TableField(exist = false)
    private String ip1;
    @TableField(exist = false)
    private String ip2;

    /**
     * ip转成long，起始
     */
    private Long num1;

    /**
     * ip转成long，截止
     */
    private Long num2;

    private String info;

    /**
     * 备注
     */
    private String comment;

    private String type;

    private Long tsCreate;

    public enum Type{
        单个,区间,地址段,国家
    }

}
