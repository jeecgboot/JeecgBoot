package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.Website;

import java.util.List;
import java.util.Map;

/**
 * 网站设置服务类
 * Created by Panyoujie on 2021-06-06 02:14:54
 */
public interface WebsiteService extends IService<Website> {

    /**
     * 分页查询
     */
    PageResult<Website> listPage(PageParam<Website> page);

    /**
     * 查询所有
     */
    List<Website> listAll(Map<String, Object> page);

}
