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
 * 预警规则表
 * </p>
 *
 * @author jeecg-boot
 * @since 2025-06-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class BizAlertRule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 规则编码
     */
    private String ruleCode;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 关联指标编码
     */
    private String metricCode;

    /**
     * 比较运算符
     */
    private String operator;

    /**
     * 阈值
     */
    private Double threshold;

    /**
     * 通知方式
     */
    private String notifyType;

    /**
     * 屏蔽开始时间
     */
    private String shieldStartTime;

    /**
     * 屏蔽结束时间
     */
    private String shieldEndTime;

    /**
     * 状态
     */
    private Integer status;

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