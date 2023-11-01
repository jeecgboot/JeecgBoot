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
 * 收藏的消息
 * </p>
 *
 * @author junko
 * @since 2023-03-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_collection")
public class Collection extends BaseModel<Collection> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer userId;

    private Long tsCreate;
    private Long tsPin;

    private Long msgId;

    private Boolean isMuc;

    @TableField(exist = false)
    private Msg msg;
    @TableField(exist = false)
    private MucMsg mucMsg;

}
