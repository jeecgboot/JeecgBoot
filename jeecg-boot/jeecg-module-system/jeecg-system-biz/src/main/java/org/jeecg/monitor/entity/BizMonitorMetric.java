package org.jeecg.monitor.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 业务指标快照表
 * </p>
 *
 * @author jeecg-boot
 * @since 2025-06-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BizMonitorMetric implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 指标编码
     */
    private String metricCode;

    /**
     * 指标名称
     */
    private String metricName;

    /**
     * 指标值
     */
    private Double metricValue;

    /**
     * 业务标签
     */
    private String bizTags;

    /**
     * 采集时间
     */
    private Date collectTime;

    /**
     * 删除状态
     */
    @TableLogic
    private Integer delFlag;

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