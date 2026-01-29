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
     * 请求头json
     */
    private String headersJson;
    /**
     * 请求参数json
     */
    private String paramsJson;


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

    /**
     * 访问模式：WHITELIST/BLACKLIST
     */
    private String listMode;

    /**
     * 允许/拒绝清单，支持IP、CIDR、域名
     */
    private String allowedList;

    /**
     * 清单备注
     */
    private String comment;

    /**
     * 严格模式开关
     */
    private Boolean enableStrict;

    /**
     * DNS缓存TTL（秒）
     */
    private Integer dnsCacheTtlSeconds;

    /**
     * IP版本：IPv4/IPv6/Dual
     */
    private String ipVersion;
    /**
     * 历史已选接口
     */
    @TableField(exist = false)
    private String ifCheckBox = "0";
}
