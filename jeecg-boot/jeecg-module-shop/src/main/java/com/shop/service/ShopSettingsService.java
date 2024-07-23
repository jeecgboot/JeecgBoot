package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.ShopSettings;

import java.util.List;
import java.util.Map;

/**
 * 商店设置服务类
 * Created by Panyoujie on 2021-07-04 03:54:31
 */
public interface ShopSettingsService extends IService<ShopSettings> {

    /**
     * 分页查询
     */
    PageResult<ShopSettings> listPage(PageParam<ShopSettings> page);

    /**
     * 查询所有
     */
    List<ShopSettings> listAll(Map<String, Object> page);

}
