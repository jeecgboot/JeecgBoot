package org.jeecg.modules.openapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 调用记录表
 * @date 2024/12/10 9:41
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OpenApiRecord implements Serializable {
    private static final long serialVersionUID = -5870384488947863579L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 接口ID
     */
    private String apiId;

    /**
     * 调用ID
     */
    private String callAuthId;

    /**
     * 调用时间
     */
    private Date callTime;

    /**
     * 耗时
     */
    private Long usedTime;

    /**
     * 响应时间
     */
    private Date responseTime;
}
