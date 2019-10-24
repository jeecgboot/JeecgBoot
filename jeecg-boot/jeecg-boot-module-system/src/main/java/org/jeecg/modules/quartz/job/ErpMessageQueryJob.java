package org.jeecg.modules.quartz.job;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.MessageConstant;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.dataReport.entity.ErpMessage;
import org.jeecg.modules.dataReport.service.IErpMessageService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @date 2019-10-22
 */
@Slf4j
public class ErpMessageQueryJob implements Job {

    @Autowired
    IErpMessageService ems;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {


        /**     从数据库查询出消息列表，将消息唯一ID进行缓存，每次新的消息数据需要与
         * 缓存ID进行判别 若存在则不提醒 **/
//        List<String> messageIdCacheList = null;
        List<ErpMessage> erpApproveMessage = ems.getErpApproveMessage();
        if (CollectionUtil.isNotEmpty(erpApproveMessage)) {
//            Cache.ValueWrapper valueWrapper = cacheManager.getCache(MessageConstant.CACHE_ERP_MESSAGE).get(MessageConstant.CACHE_ERP_MESSAGE_MESSAGE_ID);
            List<String> messageIdCacheList = (List<String>) redisUtil.get(MessageConstant.CACHE_ERP_MESSAGE);
            if (CollectionUtil.isNotEmpty(messageIdCacheList)) {
                for (ErpMessage erpMessage : erpApproveMessage) {
                    if (!messageIdCacheList.contains(erpMessage.getMessageId())) {
                        messageIdCacheList.add(erpMessage.getMessageId());
                        String message = JSONUtil.toJsonStr(erpMessage);
                        /**    当查询到有审批消息后，通过MQ进行异步发布由客户端进行异步接收     **/
                        amqpTemplate.convertAndSend(MessageConstant.MQ_QQ_NAME, message);
                        log.info("J1-->  ErpMessageQueryJob is running! message is {},   时间:" + DateUtils.now(), message);
                    } else {
                        log.info("J1-->  ErpMessageQueryJob already have  messageId is {},   时间:" + DateUtils.now(), erpMessage.getMessageId());
                    }
                }
//                cacheManager.getCache(MessageConstant.CACHE_ERP_MESSAGE).put(MessageConstant.CACHE_ERP_MESSAGE_MESSAGE_ID,messageIdCacheList);
                redisUtil.set(MessageConstant.CACHE_ERP_MESSAGE, messageIdCacheList);
            } else {
                messageIdCacheList = new ArrayList<>();
                for (ErpMessage erpMessage : erpApproveMessage) {
                    messageIdCacheList.add(erpMessage.getMessageId());
                    String message = JSONUtil.toJsonStr(erpMessage);
                    /**    当查询到有审批消息后，通过MQ进行异步发布由客户端进行异步接收     **/
                    amqpTemplate.convertAndSend(MessageConstant.MQ_QQ_NAME, message);
                    log.info("J1-->  ErpMessageQueryJob is running! message is {},   时间:" + DateUtils.now(), message);
                }
//                cacheManager.getCache(MessageConstant.CACHE_ERP_MESSAGE).put(MessageConstant.CACHE_ERP_MESSAGE_MESSAGE_ID,messageIdCacheList);
                redisUtil.set(MessageConstant.CACHE_ERP_MESSAGE, messageIdCacheList);
            }
        }


    }
}
