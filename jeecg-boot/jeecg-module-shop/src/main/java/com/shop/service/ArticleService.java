package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.Article;

import java.util.List;
import java.util.Map;

/**
 * 文章表服务类
 * 2021-11-08 04:44:45
 */
public interface ArticleService extends IService<Article> {

    /**
     * 分页查询
     */
    PageResult<Article> listPage(PageParam<Article> page);

    /**
     * 查询所有
     */
    List<Article> listAll(Map<String, Object> page);

}
