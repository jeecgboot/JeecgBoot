package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * gif收藏
 * </p>
 *
 * @author junko
 * @since 2021-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(Gif.TABLE_NAME)
public class Gif extends BaseModel<Gif> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_gif";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //排序最大为封面
    private Integer orderNo;
    //原图
    private String origin;
    //缩略图
    private String thumb;
    //
    private String md5;
    //联想词，多个;隔开
    private String keyword;

    private String emoji;
    //
    private String emojiCode;
    //图集
    private Integer albumId;
    //收藏次数
    private Integer addTimes;
    //发送次数
    private Integer sendTimes;

    private Long tsCreate;
    //禁用
    private Boolean isLocked;

}
