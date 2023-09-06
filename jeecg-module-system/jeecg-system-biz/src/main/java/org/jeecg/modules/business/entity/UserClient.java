package org.jeecg.modules.business.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecg.common.aspect.annotation.Dict;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: user_client
 * @Author: jeecg-boot
 * @Date: 2023-08-22
 * @Version: V1.0
 */
@Data
@TableName("user_client")
@ApiModel(value = "user_client对象", description = "user_client")
public class UserClient implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private java.lang.String id;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
    /**
     * 客户ID
     */
    @Excel(name = "系统用户ID", width = 15, dictTable = "client WHERE active = '1'", dicText = "internal_code", dicCode = "id")
    @Dict(dictTable = "client WHERE active = '1'", dicText = "internal_code", dicCode = "id")
    @ApiModelProperty(value = "客户ID")
    private java.lang.String client_id;
    /**
     * 系统用户ID
     */
    @Excel(name = "系统用户ID", width = 15, dictTable = "sys_user WHERE del_flag = 0", dicText = "username", dicCode = "id")
    @Dict(dictTable = "sys_user WHERE del_flag = 0", dicText = "username", dicCode = "id")
    @ApiModelProperty(value = "系统用户ID")
    private java.lang.String user_id;
}
