package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.Theme;

import java.util.List;
import java.util.Map;

/**
 * 主题配置服务类
 * 2021-06-28 00:36:29
 */
public interface ThemeService extends IService<Theme> {

    /**
     * 分页查询
     */
    PageResult<Theme> listPage(PageParam<Theme> page);

    /**
     * 查询所有
     */
    List<Theme> listAll(Map<String, Object> page);

}
