package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * openfire用户
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(OfUser.TABLE_NAME)
public class OfUser extends BaseModel<OfUser> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "ofuser";

    @TableId
    private String username;

}
