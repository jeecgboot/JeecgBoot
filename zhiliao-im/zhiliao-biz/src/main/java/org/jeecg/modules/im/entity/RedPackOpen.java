package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 拆红包记录
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(RedPackOpen.TABLE_NAME)
public class RedPackOpen extends BaseModel<RedPackOpen> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_red_pack_open";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 红包id
     */
    private Integer packId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 金额
     */
    private Integer amount;

    /**
     * 感谢语
     */
    private String thanks;

    private Long tsCreate;

    @TableField(exist = false)
    private RedPack redPack;
    @TableField(exist = false)
    private User user;


}
