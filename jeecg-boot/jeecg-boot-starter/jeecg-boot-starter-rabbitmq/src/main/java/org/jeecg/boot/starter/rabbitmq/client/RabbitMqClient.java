package org.jeecg.boot.starter.rabbitmq.client;


import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.rabbitmq.event.EventObj;
import org.jeecg.boot.starter.rabbitmq.event.JeecgRemoteApplicationEvent;
import org.jeecg.boot.starter.rabbitmq.exchange.DelayExchangeBuilder;
import org.jeecg.common.annotation.RabbitComponent;
import org.jeecg.common.base.BaseMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 消息队列客户端
 */
@Slf4j
@Configuration
public class RabbitMqClient {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqClient.class);

    private final RabbitAdmin rabbitAdmin;

    private final RabbitTemplate rabbitTemplate;


    @Resource
    private SimpleMessageListenerContainer messageListenerContainer;

    @Resource
    BusProperties busProperties;
    @Resource
    private ApplicationEventPublisher publisher;


    @Resource
    private ApplicationContext applicationContext;


    @Bean
    public void initQueue() {
        Map<String, Object> beansWithRqbbitComponentMap = this.applicationContext.getBeansWithAnnotation(RabbitComponent.class);
        Class<? extends Object> clazz = null;
        for (Map.Entry<String, Object> entry : beansWithRqbbitComponentMap.entrySet()) {
            log.info("初始化队列............");
            //获取到实例对象的class信息
            clazz = entry.getValue().getClass();
            Method[] methods = clazz.getMethods();
            RabbitListener rabbitListener = clazz.getAnnotation(RabbitListener.class);
            if (ObjectUtil.isNotEmpty(rabbitListener)) {
                createQueue(rabbitListener);
            }
            for (Method method : methods) {
                RabbitListener methodRabbitListener = method.getAnnotation(RabbitListener.class);
                if (ObjectUtil.isNotEmpty(methodRabbitListener)) {
                    createQueue(methodRabbitListener);
                }
            }

        }
    }

    /**
     * 初始化队列
     *
     * @param rabbitListener
     */
    private void createQueue(RabbitListener rabbitListener) {
        String[] queues = rabbitListener.queues();
        DirectExchange directExchange = createExchange(DelayExchangeBuilder.DELAY_EXCHANGE);
        //创建交换机
        rabbitAdmin.declareExchange(directExchange);
        if (ObjectUtil.isNotEmpty(queues)) {
            for (String queueName : queues) {
                Properties result = rabbitAdmin.getQueueProperties(queueName);
                if (ObjectUtil.isEmpty(result)) {
                    Queue queue = new Queue(queueName);
                    addQueue(queue);
                    Binding binding = BindingBuilder.bind(queue).to(directExchange).with(queueName);
                    rabbitAdmin.declareBinding(binding);
                    log.info("创建队列:" + queueName);
                }else{
                    log.info("已有队列:" + queueName);
                }
            }
        }
    }


    private Map sentObj = new HashMap<>();


    @Autowired
    public RabbitMqClient(RabbitAdmin rabbitAdmin, RabbitTemplate rabbitTemplate) {
        this.rabbitAdmin = rabbitAdmin;
        this.rabbitTemplate = rabbitTemplate;
    }


    /**
     * 发送远程事件
     *
     * @param handlerName
     * @param baseMap
     */
    public void publishEvent(String handlerName, BaseMap baseMap) {
        EventObj eventObj = new EventObj();
        eventObj.setHandlerName(handlerName);
        eventObj.setBaseMap(baseMap);
        publisher.publishEvent(new JeecgRemoteApplicationEvent(eventObj, busProperties.getId()));
    }

    /**
     * 转换Message对象
     *
     * @param messageType 返回消息类型 MessageProperties类中常量
     * @param msg
     * @return
     */
    public Message getMessage(String messageType, Object msg) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(messageType);
        Message message = new Message(msg.toString().getBytes(), messageProperties);
        return message;
    }

    /**
     * 有绑定Key的Exchange发送
     *
     * @param routingKey
     * @param msg
     */
    public void sendMessageToExchange(TopicExchange topicExchange, String routingKey, Object msg) {
        Message message = getMessage(MessageProperties.CONTENT_TYPE_JSON, msg);
        rabbitTemplate.send(topicExchange.getName(), routingKey, message);
    }

    /**
     * 没有绑定KEY的Exchange发送
     *
     * @param exchange
     * @param msg
     */
    public void sendMessageToExchange(TopicExchange topicExchange, AbstractExchange exchange, String msg) {
        addExchange(exchange);
        logger.info("RabbitMQ send " + exchange.getName() + "->" + msg);
        rabbitTemplate.convertAndSend(topicExchange.getName(), msg);
    }


    /**
     * 发送消息
     *
     * @param queueName 队列名称
     * @param params    消息内容map
     */
    public void sendMessage(String queueName, Object params) {
        log.info("发送消息到mq");
        try {
            rabbitTemplate.convertAndSend(DelayExchangeBuilder.DELAY_EXCHANGE, queueName, params, message -> {
                return message;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息
     *
     * @param queueName 队列名称
     */
    public void sendMessage(String queueName) {
        this.send(queueName, this.sentObj, 0);
        this.sentObj.clear();
    }


    public RabbitMqClient put(String key, Object value) {
        this.sentObj.put(key, value);
        return this;
    }

    /**
     * 延迟发送消息
     *
     * @param queueName  队列名称
     * @param params     消息内容params
     * @param expiration 延迟时间 单位毫秒
     */
    public void sendMessage(String queueName, Object params, Integer expiration) {
        this.send(queueName, params, expiration);
    }

    private void send(String queueName, Object params, Integer expiration) {
        Queue queue = new Queue(queueName);
        addQueue(queue);
        CustomExchange customExchange = DelayExchangeBuilder.buildExchange();
        rabbitAdmin.declareExchange(customExchange);
        Binding binding = BindingBuilder.bind(queue).to(customExchange).with(queueName).noargs();
        rabbitAdmin.declareBinding(binding);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.debug("发送时间：" + sf.format(new Date()));
        messageListenerContainer.setQueueNames(queueName);
/*        messageListenerContainer.setMessageListener(new MqListener<Message>() {
            @Override
            public void onMessage(Message message, Channel channel) {
                MqListener messageListener = SpringContextHolder.getHandler(queueName + "Listener", MqListener.class);
                if (ObjectUtil.isNotEmpty(messageListener)) {
                    messageListener.onMessage(message, channel);
                }
            }
        });*/
        rabbitTemplate.convertAndSend(DelayExchangeBuilder.DEFAULT_DELAY_EXCHANGE, queueName, params, message -> {
            if (expiration != null && expiration > 0) {
                message.getMessageProperties().setHeader("x-delay", expiration);
            }
            return message;
        });
    }


    /**
     * 给queue发送消息
     *
     * @param queueName
     */
    public String receiveFromQueue(String queueName) {
        return receiveFromQueue(DirectExchange.DEFAULT, queueName);
    }

    /**
     * 给direct交换机指定queue发送消息
     *
     * @param directExchange
     * @param queueName
     */
    public String receiveFromQueue(DirectExchange directExchange, String queueName) {
        Queue queue = new Queue(queueName);
        addQueue(queue);
        Binding binding = BindingBuilder.bind(queue).to(directExchange).withQueueName();
        rabbitAdmin.declareBinding(binding);
        String messages = (String) rabbitTemplate.receiveAndConvert(queueName);
        System.out.println("Receive:" + messages);
        return messages;
    }

    /**
     * 创建Exchange
     *
     * @param exchange
     */
    public void addExchange(AbstractExchange exchange) {
        rabbitAdmin.declareExchange(exchange);
    }

    /**
     * 删除一个Exchange
     *
     * @param exchangeName
     */
    public boolean deleteExchange(String exchangeName) {
        return rabbitAdmin.deleteExchange(exchangeName);
    }


    /**
     * 声明其名称自动命名的队列。它是用exclusive=true、autoDelete=true和 durable = false
     *
     * @return Queue
     */
    public Queue addQueue() {
        return rabbitAdmin.declareQueue();
    }

    /**
     * 创建一个指定的Queue
     *
     * @param queue
     * @return queueName
     */
    public String addQueue(Queue queue) {
        return rabbitAdmin.declareQueue(queue);
    }

    /**
     * 删除一个队列
     *
     * @param queueName the name of the queue.
     * @param unused    true if the queue should be deleted only if not in use.
     * @param empty     true if the queue should be deleted only if empty.
     */
    public void deleteQueue(String queueName, boolean unused, boolean empty) {
        rabbitAdmin.deleteQueue(queueName, unused, empty);
    }

    /**
     * 删除一个队列
     *
     * @param queueName
     * @return true if the queue existed and was deleted.
     */
    public boolean deleteQueue(String queueName) {
        return rabbitAdmin.deleteQueue(queueName);
    }

    /**
     * 绑定一个队列到一个匹配型交换器使用一个routingKey
     *
     * @param queue
     * @param exchange
     * @param routingKey
     */
    public void addBinding(Queue queue, TopicExchange exchange, String routingKey) {
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
        rabbitAdmin.declareBinding(binding);
    }

    /**
     * 绑定一个Exchange到一个匹配型Exchange 使用一个routingKey
     *
     * @param exchange
     * @param topicExchange
     * @param routingKey
     */
    public void addBinding(Exchange exchange, TopicExchange topicExchange, String routingKey) {
        Binding binding = BindingBuilder.bind(exchange).to(topicExchange).with(routingKey);
        rabbitAdmin.declareBinding(binding);
    }

    /**
     * 去掉一个binding
     *
     * @param binding
     */
    public void removeBinding(Binding binding) {
        rabbitAdmin.removeBinding(binding);
    }

    /**
     * 创建交换器
     *
     * @param exchangeName
     * @return
     */
    public DirectExchange createExchange(String exchangeName) {
        return new DirectExchange(exchangeName, true, false);
    }
}
