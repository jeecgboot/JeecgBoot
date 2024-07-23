package com.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.common.core.web.PageParam;
import com.shop.entity.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 文章表Mapper接口
 * Created by Panyoujie on 2021-11-08 04:44:45
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 分页查询
     */
    List<Article> listPage(@Param("page") PageParam<Article> page);

    /**
     * 查询全部
     */
    List<Article> listAll(@Param("page") Map<String, Object> page);

}
