package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.Coupon;

import java.util.List;
import java.util.Map;

/**
 * 优惠券服务类
 * 2021-06-23 07:43:23
 */
public interface CouponService extends IService<Coupon> {

    /**
     * 分页查询
     */
    PageResult<Coupon> listPage(PageParam<Coupon> page);

    /**
     * 查询所有
     */
    List<Coupon> listAll(Map<String, Object> page);

    /**
     * 添加优惠券
     *
     * @param coupon
     * @param userId
     * @return
     */
    boolean save(Coupon coupon, Integer userId);
}
