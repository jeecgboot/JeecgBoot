package org.jeecg.modules.bjcl.news.service;

import org.jeecg.modules.bjcl.news.entity.News;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 新闻
 * @Author: jeecg-boot
 * @Date:   2019-07-18
 * @Version: V1.0
 */
public interface INewsService extends IService<News> {

    Integer getPage();

}
