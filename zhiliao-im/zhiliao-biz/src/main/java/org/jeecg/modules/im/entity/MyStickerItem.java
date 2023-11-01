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
 * 我收藏的贴纸项
 * </p>
 *
 * @author junko
 * @since 2021-12-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_my_sticker_item")
public class MyStickerItem extends BaseModel<MyStickerItem> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer stickerItemId;
    private Integer stickerId;

    private Integer userId;
    private String origin;
    private String thumb;
    private String lottie;

    private Long tsCreate;

    @TableField(exist = false)
    private Boolean isLocked;
}
