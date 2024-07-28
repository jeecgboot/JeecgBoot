package com.shop.service.inte;

import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.Article;

import java.util.List;
import java.util.Map;

public interface IBaseService<T> {

    /**
     * 分页查询
     */
    PageResult<T> listPage(PageParam<T> page);

    /**
     * 查询所有
     */
    List<T> listAll(Map<String, Object> page);
}
