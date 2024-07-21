package com.bomaos.products.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.products.entity.Products;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 商品Mapper接口
 * Created by Panyoujie on 2021-03-27 20:22:00
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
