package com.bomaos.website.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 网站设置
 * Created by Panyoujie on 2021-06-06 02:14:54
 */
@ToString
@Data
@TableName("sys_website")
public class Website implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 网站名称
     */
    private String websiteName;

    /**
     * 网站域名
     */
    private String websiteUrl;

    /**
     * 网站logo
     */
    private String websiteLogo;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 备案ICP
     */
    private String beianIcp;

    /**
     * 关键字
     */
    private String keywords;

    /**
     * 网站描述
     */
    private String description;

    /**
     * favicon
     */
    private String favicon;

}
