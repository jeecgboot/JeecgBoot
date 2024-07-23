package com.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.common.core.web.PageParam;
import com.shop.entity.Coupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 优惠券Mapper接口
 * Created by Panyoujie on 2021-06-23 07:43:23
 */
public interface CouponMapper extends BaseMapper<Coupon> {

    /**
     * 分页查询
     */
    List<Coupon> listPage(@Param("page") PageParam<Coupon> page);

    /**
     * 查询全部
     */
    List<Coupon> listAll(@Param("page") Map<String, Object> page);

}
