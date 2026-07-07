package com.xxl.job.admin.framework.model.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *  XxlBootResource DTO
 *
 *  Created by xuxueli on 2024-08-04
 */
public class XxlBootResourceDTO implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     * 资源ID
     */
    private int id;

    /**
     * 父节点ID
     */
    private int parentId;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型：0-目录, 1-菜单, 2-按钮
     */
    private int type;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 菜单地址
     */
    private String url;

    /**
     * ICON
     */
    private String icon;

    /**
     * 顺序
     */
    private int order;

    /**
     * 状态：0-正常、1-禁用
     */
    private int status;

    /**
     * 新增时间
     */
    private Date addTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * child data
     */
    private List<XxlBootResourceDTO> children;

    public XxlBootResourceDTO() {
    }
    public XxlBootResourceDTO(int id, int parentId, String name, int type, String permission, String url, String icon, int order, int status, List<XxlBootResourceDTO> children) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
        this.type = type;
        this.permission = permission;
        this.url = url;
        this.icon = icon;
        this.order = order;
        this.status = status;
        this.children = children;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<XxlBootResourceDTO> getChildren() {
        return children;
    }

    public void setChildren(List<XxlBootResourceDTO> children) {
        this.children = children;
    }
}
