package org.jeecg.modules.openapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 接口表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OpenApi  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 请求方式，如POST、GET
     */
    private String requestMethod;

    /**
     * 对外开放的相对接口路径
     */
    private String requestUrl;

    /**
     * IP 黑名单
     */
    private String blackList;

    /**
     * 请求头列表
     */
    @TableField(exist = false)
    private List<OpenApiHeader> headers;

    /**
     * 请求参数列表
     */
    @TableField(exist = false)
    private List<OpenApiParam> params;

    /**
     * 目前仅支持json
     */
    private String body;

    /**
     * 原始接口路径
     */
    private String originUrl;

    /**
     * 状态(1：正常  2：废弃 ）
     */
    private Integer status;

    /**
     * 删除状态（0，正常，1已删除）
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
