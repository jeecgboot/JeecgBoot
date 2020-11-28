package org.jeecg.boot.starter.rabbitmq.exchange;

import org.springframework.amqp.core.CustomExchange;

import java.util.HashMap;
import java.util.Map;

/**
 * 延迟交换器构造器
 * @author: zyf
 * @date: 2019/3/8 13:31
 * @description:
 */
public class DelayExchangeBuilder {
    /**
     * 默认延迟消息交换器
     */
    public final static  String DEFAULT_DELAY_EXCHANGE = "jeecg.delayed.exchange";
    /**
     * 普通交换器
     */
    public final static  String DELAY_EXCHANGE = "jeecg.direct.exchange";

    /**
     * 构建延迟消息交换器
     * @return
     */
    public static CustomExchange buildExchange() {
        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DEFAULT_DELAY_EXCHANGE, "x-delayed-message", true, false, args);
    }
}
