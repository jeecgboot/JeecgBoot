package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * app版本
 * </p>
 *
 * @author junko
 * @since 2021-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_client_ver")
public class ClientVer extends BaseModel<ClientVer> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 平台
     */
    private String platform;

    /**
     * 版本号
     */
    private String version;

    private String downloadUrl;

    /**
     * 更新类型，0：一般更新，1：静默更新，2：强制更新
     */
    private Integer type;

    private String info;

    /**
     * 上架
     */
    private Boolean isOn;

    /**
     * 1：全量发:，2：灰度发布
     */
    private Integer isAll;

    private Long tsCreate;

}
