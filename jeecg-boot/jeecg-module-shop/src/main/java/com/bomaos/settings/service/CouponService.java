package com.bomaos.settings.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.core.web.PageResult;
import com.bomaos.settings.entity.Coupon;

import java.util.List;
import java.util.Map;

/**
 * 优惠券服务类
 * Created by Panyoujie on 2021-06-23 07:43:23
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
