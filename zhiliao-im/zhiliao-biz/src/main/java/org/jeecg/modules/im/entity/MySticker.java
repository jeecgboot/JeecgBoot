package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 我添加的贴纸
 * </p>
 *
 * @author junko
 * @since 2021-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(MySticker.TABLE_NAME)
public class MySticker extends BaseModel<MySticker> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_my_sticker";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private Integer stickerId;

    private Long tsCreate;

    @TableField(exist = false)
    private Sticker sticker;
}
