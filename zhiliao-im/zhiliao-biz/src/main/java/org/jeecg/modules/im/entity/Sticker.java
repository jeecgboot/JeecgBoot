package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 贴纸
 * </p>
 *
 * @author junko
 * @since 2021-01-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(Sticker.TABLE_NAME)
public class Sticker extends BaseModel<Sticker> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_sticker";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    //作者
    private Integer userId;
    private String adminId;
    //贴纸名称
    private String name;
    //描述
    private String info;
    //lottie压缩文件
    private String zip;
    //禁用
    private Boolean isLocked;
    //是动画
    private Boolean isAnimated;
    //原生emoji的大图贴纸
    private Boolean isBigEmoji;
    //emoji表情包
    private Boolean isEmoji;

    private Long tsCreate;
    //热门
    private Boolean IsHot;
    //置顶
    private Integer orderNo;

    //被添加次数
    private Integer addTimes;

    @TableField(exist = false)
    private Integer counts;
    @TableField(exist = false)
    private Integer totalSendTimes;
    @TableField(exist = false)
    private List<StickerItem> items;
    @TableField(exist = false)
    private User user;

}
