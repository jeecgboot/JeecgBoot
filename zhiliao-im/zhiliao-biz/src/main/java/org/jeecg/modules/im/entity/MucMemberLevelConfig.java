package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 群组用户等级配置
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(MucMemberLevelConfig.TABLE_NAME)
public class MucMemberLevelConfig extends BaseModel<MucMemberLevelConfig> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_muc_member_level_config";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer level;

    private Integer wordCount;

    private Integer chatCount;

    private Integer onlineDays;

}
