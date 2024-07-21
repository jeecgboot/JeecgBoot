package com.bomaos.settings.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bomaos.common.core.web.PageParam;
import com.bomaos.common.core.web.PageResult;
import com.bomaos.settings.entity.Coupon;
import com.bomaos.settings.mapper.CouponMapper;
import com.bomaos.settings.service.CouponService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 优惠券服务实现类
 * Created by Panyoujie on 2021-06-23 07:43:23
 */
@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Override
    public PageResult<Coupon> listPage(PageParam<Coupon> page) {
        List<Coupon> records = baseMapper.listPage(page);
        return new PageResult<>(records, page.getTotal());
    }

    @Override
    public List<Coupon> listAll(Map<String, Object> page) {
        return baseMapper.listAll(page);
    }

    @Override
    public boolean save(Coupon coupon, Integer userId) {

        coupon.setStatus(0); // 默认为未使用
        coupon.setCountUsed(0); // 使用次数默认为0
        coupon.setUpdateTime(new Date());
        coupon.setCreateTime(new Date());

        // 等于0 的话就是表示为一次使用
        if (coupon.getType() == 0) {
            List<Coupon> list = new ArrayList<>();
            for (int i = 0; i < coupon.getCountAll(); i++) {
                Coupon coupon1 = new Coupon();
                BeanUtils.copyProperties(coupon, coupon1);
                coupon1.setCountAll(1);  // 一次性默认为1
                coupon1.setRemark("一次使用");
                coupon1.setCoupon(getRandomString(16).toUpperCase()); // 生成优惠券代码
                list.add(coupon1);
            }

            /**
             * 批量添加优惠券
             */
            return this.saveBatch(list);
        } else if (coupon.getType() == 1) { // 等于1 重复使用

            coupon.setRemark("重复使用");
            // 为空的话就系统给他生成一个优惠券代码
            if (coupon.getCoupon() == null || coupon.getCoupon() == "") {
                coupon.setCoupon(getRandomString(16).toUpperCase()); // 生成优惠券代码
            }

            // 生成单卡优惠券
            return this.save(coupon);
        }

        return false;
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }

        return sb.toString();
    }

}
