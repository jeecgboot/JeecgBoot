package org.jeecg.modules.bjcl.news.service.impl;

import org.jeecg.modules.bjcl.news.entity.News;
import org.jeecg.modules.bjcl.news.mapper.NewsMapper;
import org.jeecg.modules.bjcl.news.service.INewsService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 新闻
 * @Author: jeecg-boot
 * @Date:   2019-07-18
 * @Version: V1.0
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements INewsService {

    @Override
    public Integer getPage() {
        return baseMapper.getPage();
    }
}
