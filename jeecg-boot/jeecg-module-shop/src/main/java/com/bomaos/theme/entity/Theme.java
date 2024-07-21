package com.bomaos.theme.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 主题配置
 * Created by Panyoujie on 2021-06-28 00:36:29
 */
@TableName("sys_theme")
public class Theme implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 主题名称
     */
    private String name;

    /**
     * 说明
     */
    private String description;

    /**
     * 主题驱动
     */
    private String driver;

    /**
     * 是否设置
     */
    private Integer enable;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 创建时间
     */
    private Date createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Theme{" +
                ", id=" + id +
                ", name=" + name +
                ", description=" + description +
                ", driver=" + driver +
                ", enable=" + enable +
                ", updateDate=" + updateDate +
                ", createDate=" + createDate +
                "}";
    }

}
