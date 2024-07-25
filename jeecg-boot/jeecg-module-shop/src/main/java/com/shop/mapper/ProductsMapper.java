package com.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.common.core.web.PageParam;
import com.shop.entity.Products;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品Mapper接口
 * 2021-03-27 20:22:00
 */
public interface ProductsMapper extends BaseMapper<Products> {

    /**
     * 分页查询
     */
    List<Products> listPage(@Param("page") PageParam<Products> page);

    /**
     * 查询全部
     */
    List<Products> listAll(@Param("page") Map<String, Object> page);

    List<Products> getRandomProductList(int limit);
}
