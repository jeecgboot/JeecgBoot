package org.jeecg.modules.im.mq.receiver;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.rabbitmq.core.BaseRabbiMqHandler;
import org.jeecg.boot.starter.rabbitmq.listenter.MqListener;
import org.jeecg.common.annotation.RabbitComponent;
import org.jeecg.modules.im.base.component.BaseConfig;
import org.jeecg.modules.im.base.constant.ConstantMQ;
import org.jeecg.modules.im.base.util.GsonUtil;
import org.jeecg.modules.im.entity.Device;
import org.jeecg.modules.im.entity.Msg;
import org.jeecg.modules.im.entity.MucMsg;
import org.jeecg.modules.im.service.DeviceService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 监听离线消息
 */
@Slf4j
@RabbitListener(queues = ConstantMQ.OFFLINE_MESSAGE)
@RabbitComponent(value = "mqOfflineMessageReceiver")
public class MQOfflineMessageReceiver extends BaseRabbiMqHandler<String> {
    @Autowired
    private BaseConfig baseConfig;
    @Resource
    private DeviceService deviceService;

    private static JPushClient jpushClient;

    @RabbitHandler
    public void onMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        super.onMessage(message, deliveryTag, channel, new MqListener<String>() {
            @Override
            public void handler(String message, Channel channel) {
                try {
                    //业务处理
                    log.info("新离线消息,message= " + message);
                    Msg msg = null;
                    MucMsg mucMsg = null;
                    try {
                        msg = GsonUtil.fromJson(message, Msg.class);
                    } catch (Exception e) {
                    }
                    if (msg != null){
                        log.info("消费了一条离线消息");
                        processSingle(msg);
                        return;
                    }
                    try {
                        mucMsg = GsonUtil.fromJson(message, MucMsg.class);
                    } catch (Exception e) {
                    }
                    if (mucMsg != null){

                        processMuc();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //是否需要将消息放回队列？
                    log.error("离线消息消费异常：", e);
                }
            }
        });
    }
    JPushClient getJpushClient(){
        if(jpushClient==null){
            jpushClient = new JPushClient(baseConfig.getJpushMasterSecret(), baseConfig.getJpushAppKey(), null, ClientConfig.getInstance());
        }
        return  jpushClient;
    }
    //处理单聊
    boolean processSingle(Msg msg){
        return oneToOneToPlatformAll(msg,Device.Platform.android)&&oneToOneToPlatformAll(msg,Device.Platform.ios);
    }
    //一对一指定平台全部推送
    boolean oneToOneToPlatformAll(Msg msg,Device.Platform platform){
        PushPayload payload;
        if(platform.name().equals(Device.Platform.android.name())){
            payload = buildOneToOneToAndroid(msg, deviceService.getJPushIds(msg.getToUserId(),platform.name()));
        } else if(platform.name().equals(Device.Platform.ios.name())){
            payload = buildOneToOneToIos(msg, deviceService.getJPushIds(msg.getToUserId(),platform.name()));
        }else{
            log.error("未支持推送的平台：{}",platform.name());
            return false;
        }
        try {
            if(payload!=null) {
                PushResult result = getJpushClient().sendPush(payload);
                if(result.statusCode==PushResult.ERROR_CODE_OK){
                    log.info("离线消息推送成功：platform={},msg_id={}",platform.name(),result.msg_id);
                }else{
                    log.error("离线消息推送失败：platform={},error={}",platform.name(),result.error);
                }
            }
        } catch (APIConnectionException e) {
            log.error("Connection error, should retry later", e);
        } catch (APIRequestException e) {
            log.error("Should review the error, and fix the request", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
        }
        return true;
    }
    boolean processMuc(){
        return true;
    }
    //一对一安卓推送数据载体
    public static PushPayload buildOneToOneToAndroid(Msg msg,List registrationIds) {
        if(registrationIds.isEmpty()){
            return null;
        }
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.registrationId(registrationIds))
                        .build())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(msg.getContent())
                                .addExtra("from", "JPush")
                                .build())
                        .build())
                .setMessage(Message.content(JSONObject.toJSONString(msg)))
                .setOptions(Options.newBuilder()
                        .setApnsProduction(false)
                        .build())
                .build();
    }
    //一对一苹果推送数据载体
    public static PushPayload buildOneToOneToIos(Msg msg,List registrationIds) {
        if(registrationIds.isEmpty()){
            return null;
        }
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.registrationId(registrationIds))
                        .build())
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(msg.getContent())
                                .setBadge(999)
                                .setSound("happy")
                                .addExtra("from", "JPush")
                                .build())
                        .build())
                .setMessage(Message.content(JSONObject.toJSONString(msg)))
                .setOptions(Options.newBuilder()
                        .setApnsProduction(false)
                        .build())
                .build();
    }
}