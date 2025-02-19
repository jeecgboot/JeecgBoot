package org.jeecg.modules.openapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限表
 * @date 2024/12/10 9:38
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OpenApiAuth implements Serializable {

    private static final long serialVersionUID = -5933153354153738498L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 受权名称
     */
    private String name;

    /**
     * access key
     */
    private String ak;

    /**
     * secret key
     */
    private String sk;

    /**
     * 系统用户ID
     */
    private String systemUserId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateTime;
}
