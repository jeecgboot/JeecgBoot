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
 * 用户的标签
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(Tag.TABLE_NAME)
public class Tag extends BaseModel<Tag> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_tag";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //归属用户
    private Integer userId;
    //好友id ,连接
    private String friendIds;
    //名称
    private String name;
    //好友数量
    private Integer count;

    private Long tsCreate;

    @TableField(exist = false)
    private List<Friend> friendList;

}
