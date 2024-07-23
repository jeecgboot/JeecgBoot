package com.shop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 分类
 * Created by Panyoujie on 2021-03-27 20:22:00
 */
@Data
@TableName("sys_classifys")
public class Classifys implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类状态
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 用户名
     */
    private String username;

    @Override
    public String toString() {
        return "Classifys{" +
                ", id=" + id +
                ", name=" + name +
                ", status=" + status +
                ", sort=" + sort +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                "}";
    }

}
