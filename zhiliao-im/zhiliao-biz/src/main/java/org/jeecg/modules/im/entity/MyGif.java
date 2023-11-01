package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 我的gif
 * </p>
 *
 * @author junko
 * @since 2021-11-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_my_gif")
public class MyGif extends BaseModel<MyGif> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;
    //原图
    private String origin;
    //缩略图
    private String thumb;
    //排序
    private Integer orderNo;
    //gif id
    private Integer gifId;

    private Long tsCreate;
    //最后一次发送时间
    private Long tsLastSend;
    //置顶时间
    private Long tsPin;

    @TableField(exist = false)
    private Boolean isLocked;
    @TableField(exist = false)
    private String emoji;
    @TableField(exist = false)
    private String emojiCode;

}
