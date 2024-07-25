package com.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.common.core.web.PageParam;
import com.shop.entity.Classifys;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 分类Mapper接口
 * 2021-03-27 20:22:00
 */
public interface ClassifysMapper extends BaseMapper<Classifys> {

    /**
     * 分页查询
     */
    List<Classifys> listPage(@Param("page") PageParam<Classifys> page);

    /**
     * 查询全部
     */
    List<Classifys> listAll(@Param("page") Map<String, Object> page);

}
