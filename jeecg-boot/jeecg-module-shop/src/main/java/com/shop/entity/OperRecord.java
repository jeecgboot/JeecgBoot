package com.shop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志
 * 2018-12-24 16:10
 */
@Data
@TableName("sys_oper_record")
public class OperRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 模块
     */
    private String model;
    /**
     * 方法
     */
    private String description;
    /**
     * 请求地址
     */
    private String url;
    /**
     * 请求方式
     */
    private String requestMethod;
    /**
     * 调用方法
     */
    private String operMethod;
    /**
     * 请求参数
     */
    private String param;
    /**
     * 返回结果
     */
    private String result;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 消耗时间,单位毫秒
     */
    private Long spendTime;
    /**
     * 状态,0成功,1异常
     */
    private Integer state;
    /**
     * 备注
     */
    private String comments;
    /**
     * 操作时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 用户昵称
     */
    @TableField(exist = false)
    private String nickName;
    /**
     * 用户账号
     */
    @TableField(exist = false)
    private String username;

}
