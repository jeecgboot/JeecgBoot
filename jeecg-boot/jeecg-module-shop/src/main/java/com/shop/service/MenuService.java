package com.shop.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.Menu;
import com.shop.mapper.MenuMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单服务实现类
 * 2018-12-24 16:10
 */
@Service
public class MenuService extends ServiceImpl<MenuMapper, Menu> {
    public PageResult<Menu> listPage(PageParam<Menu> pageParam) {
        return new PageResult<>(baseMapper.listPage(pageParam), pageParam.getTotal());
    }

    public List<Menu> getUserMenu(Integer userId, Integer menuType) {
        return baseMapper.listByUserId(userId, menuType);
    }

    public List<Menu> toMenuTree(List<Menu> menus, Integer parentId) {
        List<Menu> list = new ArrayList<>();
        for (Menu menu : menus) {
            if (parentId.equals(menu.getParentId())) {
                menu.setChildren(toMenuTree(menus, menu.getMenuId()));
                list.add(menu);
            }
        }
        return list;
    }

}
