package com.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.common.core.web.PageParam;
import com.shop.entity.Theme;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 主题配置Mapper接口
 * Created by Panyoujie on 2021-06-28 00:36:29
 */
public interface ThemeMapper extends BaseMapper<Theme> {

    /**
     * 分页查询
     */
    List<Theme> listPage(@Param("page") PageParam<Theme> page);

    /**
     * 查询全部
     */
    List<Theme> listAll(@Param("page") Map<String, Object> page);

}
