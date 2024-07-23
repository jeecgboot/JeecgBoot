package com.shop.controller;

import com.shop.common.core.web.JsonResult;
import com.shop.entity.Orders;
import com.shop.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wf.jwtp.annotation.Ignore;
import org.wf.jwtp.provider.Token;
import org.wf.jwtp.provider.TokenStore;

@RestController
@RequestMapping("/api")
public class SearchApiController {

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private OrdersService ordersService;

    /**
     * 根据id查询订单投诉表
     */
    @Ignore
    @RequestMapping("/getOrderToken")
    public JsonResult get(Integer id, String password) {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(password)) return JsonResult.error("订单ID或查询密码不能为空！！");
        Orders orders = ordersService.getById(id);
        if (ObjectUtils.isEmpty(orders)) return JsonResult.error("商品不存在！！");
        if (!password.equals(orders.getPassword())) return JsonResult.error("密码错误，请核对后再试！");
        Token token = tokenStore.createNewToken(String.valueOf(id), 60 * 60 * 24 * 30L);
        String accessToken = token.getAccessToken();
        return JsonResult.ok("ok").setData(accessToken);
    }

}
