package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 动态
 * </p>
 *
 * @author junko
 * @since 2021-11-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(Activity.TABLE_NAME)
public class Activity extends BaseModel<Activity> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_activity";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 内容
     */
    private String content;

    /**
     * 评论次数
     */
    private Integer commentCount;

    /**
     * 动态创建时间
     */
    private Long tsCreate;

    /**
     * 当前位置
     */
    private String address;

    /**
     * 设备信息
     */
    private String device;

    /**
     * 点赞次数
     */
    private Integer praiseCount;

    /**
     * 图片内容链接
     */
    private String images;

    /**
     * 视频内容链接
     */
    private String video;

    /**
     * 删除标识
     */
    private Long tsDelete;

    /**
     * 置顶标识
     */
    private Long tsPin;

    /**
     * 转发来源
     */
    private Integer fromId;

    /**
     * 转发次数
     */
    private Integer forwardTimes;
    /**
     * 可见性
     */
    private Integer visibility;
    /**
     * 可见性的用户id
     */
    private String userIds;

    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private List<User> users;


    public enum Visibility{
        all(0,"公开"),none(1,"仅自己可见"),not_all(2,"部分可见"),not_none(3,"部分不可见");
        int code;
        String info;
        Visibility(int code,String info){
            this.code = code;
            this.info = info;
        }
    }
}
