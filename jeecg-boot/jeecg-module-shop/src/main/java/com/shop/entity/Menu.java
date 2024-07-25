package com.shop.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单
 * Created by AutoGenerator on 2018-12-24 16:10
 */
@Data
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

}
