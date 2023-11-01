package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 动态评论列表
 * </p>
 *
 * @author junko
 * @since 2021-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_activity_comment")
public class ActivityComment extends BaseModel<ActivityComment> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 动态id
     */
    private Integer activityId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 回复对象
     */
    private Integer parentId;

    /**
     * 主评论id
     */
    private Integer commentId;

    /**
     * 点赞次数
     */
    private Integer praiseTimes;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论创建时间
     */
    private Long tsCreate;

    /**
     * 置顶时间
     */
    private Long tsPin;

    /**
     * 评论删除时间
     */
    private Long tsDelete;

    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private ActivityComment parent;

}
