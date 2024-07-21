package com.bomaos.common.system.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单
 * Created by AutoGenerator on 2018-12-24 16:10
 */
@TableName("sys_menu")
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int TYPE_MENU = 0;  // 菜单类型
    public static final int TYPE_BTN = 1;  // 按钮类型
    /**
     * 菜单id
     */
    @TableId(value = "menu_id", type = IdType.AUTO)
    private Integer menuId;
    /**
     * 上级id,0是顶级
     */
    private Integer parentId;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 菜单图标
     */
    private String menuIcon;
    /**
     * 图标颜色
     */
    private String iconColor;
    /**
     * 菜单地址
     */
    private String path;
    /**
     * 打开位置
     */
    private String target;
    /**
     * 是否隐藏,0否,1是
     */
    private Integer hide;
    /**
     * 排序号
     */
    private Integer sortNumber;
    /**
     * 权限标识
     */
    private String authority;
    /**
     * 菜单类型,0菜单,1按钮
     */
    private Integer menuType;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 是否删除,0否,1是
     */
    @TableLogic
    private Integer deleted;
    /**
     * 上级菜单名称
     */
    @TableField(exist = false)
    private String parentName;
    /**
     * 子菜单
     */
    @TableField(exist = false)
    private List<Menu> children;
    /**
     * 回显选中状态,0未选中,1选中
     */
    @TableField(exist = false)
    private Boolean checked;
    @TableField(exist = false)
    private Boolean open;

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Integer getHide() {
        return hide;
    }

    public void setHide(Integer hide) {
        this.hide = hide;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    @Override
    public String toString() {
        return "Menu{" +
                ", menuId=" + menuId +
                ", parentId=" + parentId +
                ", menuName=" + menuName +
                ", path=" + path +
                ", menuIcon=" + menuIcon +
                ", iconColor=" + iconColor +
                ", sortNumber=" + sortNumber +
                ", target=" + target +
                ", hide=" + hide +
                ", authority=" + authority +
                ", menuType=" + menuType +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", deleted=" + deleted +
                ", parentName=" + parentName +
                "}";
    }

}
