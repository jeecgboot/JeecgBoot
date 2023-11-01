package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 群组用户等级分类
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(MucMemberLevelCtg.TABLE_NAME)
public class MucMemberLevelCtg extends BaseModel<MucMemberLevelCtg> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_member_level_ctg";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //名称
    private String name;
    //排序
    private Integer orderNo;
    //封面图
    private String cover;

}
