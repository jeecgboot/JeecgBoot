package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.Pays;

import java.util.List;
import java.util.Map;

/**
 * 支付配置服务类
 * 2021-03-29 11:06:11
 */
public interface PaysService extends IService<Pays> {

    /**
     * 分页查询
     */
    PageResult<Pays> listPage(PageParam<Pays> page);

    /**
     * 查询所有
     */
    List<Pays> listAll(Map<String, Object> page);

}
