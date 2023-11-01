package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 广告
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(Adver.TABLE_NAME)
public class Adver extends BaseModel<Adver> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_adver";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String url;

    private String type;

    private String jumpUrl;

    private Integer timeout;

    private Boolean isOn;

    private Long tsCreate;

}
