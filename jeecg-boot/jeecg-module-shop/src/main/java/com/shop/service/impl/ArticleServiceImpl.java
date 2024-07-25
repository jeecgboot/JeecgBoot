package com.shop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.Article;
import com.shop.mapper.ArticleMapper;
import com.shop.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 文章表服务实现类
 * 2021-11-08 04:44:45
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Override
    public PageResult<Article> listPage(PageParam<Article> page) {
        List<Article> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public List<Article> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

    @Override
    public boolean save(Article entity) {
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setLikes(0);
        entity.setSeeNumber(0);
        return super.save(entity);
    }
}
