package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.Menu;

import java.util.List;

/**
 * 菜单服务类
 * 2018-12-24 16:10
 */
public interface MenuService extends IService<Menu> {

    /**
     * 关联分页查询菜单
     */
    PageResult<Menu> listPage(PageParam<Menu> pageParam);

    /**
     * 根据用户id查询菜单列表
     *
     * @param userId   用户id
     * @param menuType 菜单类型，为null不筛选
     * @return List<Menu>
     */
    List<Menu> getUserMenu(Integer userId, Integer menuType);

    /**
     * 转化为树形结构
     *
     * @param menus    菜单list
     * @param parentId 最顶级id
     * @return List<Menu>
     */
    List<Menu> toMenuTree(List<Menu> menus, Integer parentId);

}
