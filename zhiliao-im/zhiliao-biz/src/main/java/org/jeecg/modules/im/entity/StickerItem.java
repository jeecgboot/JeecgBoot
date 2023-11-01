package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 贴纸项
 * </p>
 *
 * @author junko
 * @since 2021-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(StickerItem.TABLE_NAME)
public class StickerItem extends BaseModel<StickerItem> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_sticker_item";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //
    private Integer stickerId;

    private String emoji;
    //emoji简短代码
    private String emojiCode;
    //原图
    private String origin;
    //缩略图
    private String thumb;
    //json文件
    private String lottie;
    //lottie zip文件
    private String zipName;
    //联想词
    private String keyword;
    //排序
    private Integer orderNo;
    //创建时间
    private Long tsCreate;
    //发送次数
    private Integer sendTimes;
    //禁用
    private Boolean isLocked;

    @TableField(exist = false)
    private StickerSend sendRecord;
}
