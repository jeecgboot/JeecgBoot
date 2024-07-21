package com.bomaos.content.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.core.web.PageResult;
import com.bomaos.content.entity.Article;
import com.bomaos.content.mapper.ArticleMapper;
import com.bomaos.content.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 文章表服务实现类
 * Created by Panyoujie on 2021-11-08 04:44:45
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
