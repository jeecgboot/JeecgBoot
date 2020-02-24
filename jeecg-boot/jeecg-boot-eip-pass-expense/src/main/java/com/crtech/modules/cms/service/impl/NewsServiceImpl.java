package com.crtech.modules.cms.service.impl;

import com.crtech.modules.cms.entity.News;
import com.crtech.modules.cms.mapper.NewsMapper;
import com.crtech.modules.cms.service.INewsService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 新闻
 * @Author: jeecg-boot
 * @Date:   2019-09-21
 * @Version: V1.0
 */
@Service
public class NewsServiceImpl extends ServiceImpl<NewsMapper, News> implements INewsService {

}
