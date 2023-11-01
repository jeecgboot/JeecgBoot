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
 * 群聊投票项
 * </p>
 *
 * @author junko
 * @since 2022-05-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_vote_item")
public class VoteItem extends BaseModel<VoteItem> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String content;

    private Integer voteId;

    /**
     * 测验模式下只有一个选项是正确的
     */
    private Boolean isCorrect;


}
