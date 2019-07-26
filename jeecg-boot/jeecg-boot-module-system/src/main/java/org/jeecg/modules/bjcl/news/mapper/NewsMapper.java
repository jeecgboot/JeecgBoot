package org.jeecg.modules.bjcl.news.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.bjcl.news.entity.News;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 新闻
 * @Author: jeecg-boot
 * @Date:   2019-07-18
 * @Version: V1.0
 */
public interface NewsMapper extends BaseMapper<News> {

    Integer getPage();

}
