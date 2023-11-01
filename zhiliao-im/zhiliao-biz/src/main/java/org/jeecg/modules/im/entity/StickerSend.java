package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 贴纸项发送记录
 * </p>
 *
 * @author junko
 * @since 2021-12-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_sticker_send")
public class StickerSend extends BaseModel<StickerSend> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //贴纸项id
    private Integer stickerItemId;
    //最后一次发送时间
    private Long tsLastSend;

    private Integer userId;
    //发送次数
    private Integer times;

}
