package com.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shop.common.core.web.PageParam;
import com.shop.common.core.web.PageResult;
import com.shop.entity.Orders;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 订单表服务类
 * Created by Panyoujie on 2021-03-29 16:24:28
 */
public interface OrdersService extends IService<Orders> {

    /**
     * 分页查询
     */
    PageResult<Orders> listPage(PageParam<Orders> page);

    /**
     * 查询所有
     */
    List<Orders> listAll(Map<String, Object> page);

    Map<String, String> buy(Integer goodsId, Integer number, String contact, Integer couponId, String payType, String password, HttpServletRequest request);

    Orders selectByMember(String member);

    boolean clearRemove();

    boolean clearAllRemove();

    boolean deleteById(Integer id);
}
