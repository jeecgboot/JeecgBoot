package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 投票结果
 * </p>
 *
 * @author junko
 * @since 2022-05-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_votation")
public class Votation extends BaseModel<Votation> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer voteId;

    private Integer voteItemId;

    private Long tsCreate;

    /**
     * 对应群或频道成员id
     */
    private Integer memberId;
    /**
     * 动态投票为用户id
     */
    private Integer userId;


}
