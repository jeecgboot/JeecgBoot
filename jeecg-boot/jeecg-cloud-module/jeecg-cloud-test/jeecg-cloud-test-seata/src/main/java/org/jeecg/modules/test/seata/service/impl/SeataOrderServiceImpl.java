package org.jeecg.modules.test.seata.service.impl;
import com.baomidou.dynamic.datasource.annotation.DS;

import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.test.seata.dto.PlaceOrderRequest;
import org.jeecg.modules.test.seata.entity.SeataOrder;
import org.jeecg.modules.test.seata.enums.OrderStatus;
import org.jeecg.modules.test.seata.mapper.SeataOrderMapper;
import org.jeecg.modules.test.seata.service.SeataAccountService;
import org.jeecg.modules.test.seata.service.SeataOrderService;
import org.jeecg.modules.test.seata.service.SeataProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
/**
 * @Description: 订单服务类
 * @author: zyf
 * @date: 2022/01/24
 * @version: V1.0
 */
@Slf4j
@Service
public class SeataOrderServiceImpl implements SeataOrderService {

    @Resource
    private SeataOrderMapper orderMapper;
    @Autowired
    private SeataAccountService accountService;
    @Autowired
    private SeataProductService productService;

    @DS("order")
    @Override
    @Transactional
    @GlobalTransactional
    public void placeOrder(PlaceOrderRequest request) {
        log.info("=============ORDER START=================");
        Long userId = request.getUserId();
        Long productId = request.getProductId();
        Integer amount = request.getAmount();
        log.info("收到下单请求,用户:{}, 商品:{},数量:{}", userId, productId, amount);


        SeataOrder order = SeataOrder.builder()
                .userId(userId)
                .productId(productId)
                .status(OrderStatus.INIT)
                .amount(amount)
                .build();

        orderMapper.insert(order);
        log.info("订单一阶段生成，等待扣库存付款中");
        // 扣减库存并计算总价
        Double totalPrice = productService.reduceStock(productId, amount);
        // 扣减余额
        accountService.reduceBalance(userId, totalPrice);

        order.setStatus(OrderStatus.SUCCESS);
        order.setTotalPrice(totalPrice);
        orderMapper.updateById(order);
        log.info("订单已成功下单");
        log.info("=============ORDER END=================");
    }
}