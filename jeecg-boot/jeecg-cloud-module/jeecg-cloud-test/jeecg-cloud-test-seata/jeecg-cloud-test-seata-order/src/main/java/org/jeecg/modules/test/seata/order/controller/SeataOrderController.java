package org.jeecg.modules.test.seata.order.controller;

/**
 * @Description: TODO
 * @author: zyf
 * @date: 2022/01/24
 * @version: V1.0
 */
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.jeecg.modules.test.seata.order.dto.PlaceOrderRequest;
import org.jeecg.modules.test.seata.order.service.SeataOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test/seata/order")
@Api(tags = "seata测试")
public class SeataOrderController {

    @Autowired
    private SeataOrderService orderService;

    /**
     * 自由下单
     */
    @PostMapping("/placeOrder")
    @ApiOperation(value = "自由下单", notes = "自由下单")
    public String placeOrder(@Validated @RequestBody PlaceOrderRequest request) {
        orderService.placeOrder(request);
        return "下单成功";
    }

    /**
     * 测试商品库存不足-异常回滚
     */
    @PostMapping("/test1")
    @ApiOperation(value = "测试商品库存不足", notes = "测试商品库存不足")
    public String test1() {
        //商品单价10元，库存20个,用户余额50元，模拟一次性购买22个。 期望异常回滚
        orderService.placeOrder(new PlaceOrderRequest(1L, 1L, 22));
        return "下单成功";
    }

    /**
     * 测试用户账户余额不足-异常回滚
     */
    @PostMapping("/test2")
    @ApiOperation(value = "测试用户账户余额不足", notes = "测试用户账户余额不足")
    public String test2() {
        //商品单价10元，库存20个，用户余额50元，模拟一次性购买6个。 期望异常回滚
        orderService.placeOrder(new PlaceOrderRequest(1L, 1L, 6));
        return "下单成功";
    }
}