package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.Classifys;

import java.util.List;
import java.util.Map;

/**
 * 分类服务类
 * 2021-03-27 20:22:00
 */
public interface ClassifysService extends IService<Classifys> {

    /**
     * 分页查询
     */
    PageResult<Classifys> listPage(PageParam<Classifys> page);

    /**
     * 查询所有
     */
    List<Classifys> listAll(Map<String, Object> page);

}
