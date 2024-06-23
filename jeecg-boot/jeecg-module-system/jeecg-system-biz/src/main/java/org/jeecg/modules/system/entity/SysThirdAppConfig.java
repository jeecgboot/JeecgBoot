package org.jeecg.modules.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * @Description: 第三方配置表
 * @Author: jeecg-boot
 * @Date:   2023-02-03
 * @Version: V1.0
 */
@Data
@TableName("sys_third_app_config")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="sys_third_app_config对象", description="第三方配置表")
public class SysThirdAppConfig {

    /**编号*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "编号")
    private String id;

    /**租户id*/
    @Excel(name = "租户id", width = 15)
    @ApiModelProperty(value = "租户id")
    private Integer tenantId;

    /**钉钉/企业微信第三方企业应用标识*/
    @Excel(name = "钉钉/企业微信第三方企业应用标识", width = 15)
    @ApiModelProperty(value = "钉钉/企业微信第三方企业应用标识")
    private String agentId;

    /**钉钉/企业微信 应用id*/
    @Excel(name = "钉钉/企业微信 应用id", width = 15)
    @ApiModelProperty(value = "钉钉/企业微信 应用id")
    private String clientId;

    /**钉钉/企业微信应用id对应的秘钥*/
    @Excel(name = "钉钉/企业微信应用id对应的秘钥", width = 15)
    @ApiModelProperty(value = "钉钉/企业微信应用id对应的秘钥")
    private String clientSecret;

    /**企业微信自建应用Secret*/
    @Excel(name = "企业微信自建应用Secret", width = 15)
    @ApiModelProperty(value = "企业微信自建应用Secret")
    private String agentAppSecret;

    /**第三方类别(dingtalk 钉钉 wechat_enterprise 企业微信)*/
    @Excel(name = "第三方类别(dingtalk 钉钉 wechat_enterprise 企业微信)", width = 15)
    @ApiModelProperty(value = "第三方类别(dingtalk 钉钉 wechat_enterprise 企业微信)")
    private String thirdType;

    /**是否启用(0-否,1-是)*/
    @Excel(name = "是否启用(0-否,1-是)", width = 15)
    @ApiModelProperty(value = "是否启用(0-否,1-是)")
    private Integer status;

    /**创建日期*/
    @Excel(name = "创建日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**修改日期*/
    @Excel(name = "修改日期", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
