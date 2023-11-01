package org.jeecg.modules.im.mq.receiver;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.rabbitmq.core.BaseRabbiMqHandler;
import org.jeecg.boot.starter.rabbitmq.listenter.MqListener;
import org.jeecg.common.annotation.RabbitComponent;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.im.base.constant.*;
import org.jeecg.modules.im.base.util.GsonUtil;
import org.jeecg.modules.im.entity.*;
import org.jeecg.modules.im.service.*;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.handler.annotation.Header;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 新消息消费者
 * （@RabbitListener声明类上，一个类只能监听一个队列）
 */
@Slf4j
@RabbitComponent(value = "mqNewMsgReceiver")
@RabbitListener(queues = ConstantMQ.NEW_MSG)
public class MQNewMsgReceiver extends BaseRabbiMqHandler<String> {
    @Resource
    private FriendService friendService;
    @Resource
    private UserService userService;
    @Resource
    private MucService mucService;
    @Resource
    private MucConfigService mucConfigService;
    @Resource
    private MucMsgService mucMsgService;
    @Resource
    private MsgService msgService;
    @Resource
    private CallRecordService callRecordService;
    @Resource
    private GifService gifService;
    @Resource
    private MyGifService myGifService;
    @Resource
    private CustomEmojiService customEmojiService;
    @Resource
    private MucMemberService mucMemberService;
    @Lazy
    @Resource
    private RedisUtil redisUtil;

    @RabbitHandler
    public void onMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        super.onMessage(message, deliveryTag, channel, new MqListener<String>() {
            @Override
            public void handler(String message, Channel channel) {
                try {
                    //业务处理
                    log.info("新消息,message= " + message);
                    Msg msg = null;
                    MucMsg mucMsg = null;
                    try {
                        msg = GsonUtil.fromJson(message, Msg.class);
                    } catch (Exception e) {
                        log.error("parse sg msg error:{0}", e);
                    }
                    if (msg != null && msg.getToUserId() != null) {
                        processMsg(msg);
                        return;
                    }

                    try {
                        mucMsg = GsonUtil.fromJson(message, MucMsg.class);
                    } catch (Exception e2) {
                        log.error("parse muc msg error:{0}", e2);
                    }
                    if (mucMsg != null && mucMsg.getMucId() != null) {
                        processMucMsg(mucMsg);
                    }
                }catch (Exception e){
                    log.error("消费新单聊/群聊消息异常：", e);
                    try {
                        channel.basicNack(deliveryTag, false, true);
                        log.info("重新返回队列");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
    //保存群消息
    private void processMucMsg(MucMsg mucMsg) {
        MsgType msgType = MsgType.getByType(mucMsg.getType());
        boolean needStore = msgType.isStore();
        try {
            if(mucMsgService.getById(mucMsg.getId())!=null){
                return;
            }
            JSONObject contentJson = null;
            try {
                contentJson = JSONObject.parseObject(mucMsg.getContent());
            }catch (Exception e){}
            Muc muc = mucService.getById(mucMsg.getMucId());
            MucConfig mucConfig = mucConfigService.findByMuc(mucMsg.getMucId());
            //判断是否是撤回消息
            if (msgType.getType() == MsgType.revokeMsg.getType()) {
                String stanzaId = mucMsg.getContent();
                List<Msg> revokeMsgs = msgService.findByStanzaId(stanzaId);
                //撤销者
                User revoker = userService.findById(mucMsg.getUserId());
                for (Msg revokeMsg : revokeMsgs) {
                    revokeMsg.setTsRevoke(mucMsg.getTsSend());
                    //是系统号撤销的
                    if(revoker.getType()==User.Type.sysAccount.getCode()){
                        revokeMsg.setRevokeType(MsgRevokeType.System.getCode());
                    }else {
                        //撤销人和要被撤销的消息发送人是同一个，且消息为发送，则为发送人撤销，否则为接收方撤销
                        if (mucMsg.getUserId().intValue() == revokeMsg.getUserId()) {
                            revokeMsg.setRevokeType(MsgRevokeType.Sender.getCode());
                        } else {
                            revokeMsg.setRevokeType(MsgRevokeType.Receiver.getCode());
                        }
                    }
                }
                msgService.updateBatchById(revokeMsgs);
            }
            //消息置顶
            else if(msgType.getType()==MsgType.pin.getType()){
                assert contentJson!=null;
                int flag = contentJson.getInteger("flag");
                if(flag==0){
                    needStore = false;//取消置顶不保存
                }
                String stanzaId = contentJson.getString("stanzaId");
                List<Msg> msgs = msgService.findByStanzaId(stanzaId);
                for (Msg tempMsg : msgs) {
                    tempMsg.setTsPin(flag==0?0: mucMsg.getTsSend());
                }
                msgService.updateBatchById(msgs);
            }
            //全体禁言
            else if(msgType.getType()==MsgType.mucMute.getType()){
                assert contentJson!=null;
                int flag = contentJson.getInteger("flag");
                muc.setTsMute((long) (flag==1?-1:0));
                mucService.updateById(muc);
            }
            //单独禁言
            else if(msgType.getType()==MsgType.muteOne.getType()){
                assert contentJson!=null;
                int flag = contentJson.getInteger("flag");
                int userId = contentJson.getInteger("userId");
                int duration = contentJson.getInteger("duration");
                int plus = contentJson.getInteger("plus");
                MucMember member = mucMemberService.findByMucIdOfUser(muc.getId(),userId);
                if(flag==1){
                    //永久
                    if(duration==-1){
                        member.setTsMute((long) duration);
                    }
                    member.setTsMute((plus==1?member.getTsMute():getTs())+duration* 1000L);
                    member.setTsMuteBegin(getTs());
                    member.setMuteType(User.MuteType.admin.getCode());
                }else{
                    member.setMuteType(User.MuteType.normal.getCode());
                    member.setTsMute(0L);
                    member.setTsMuteBegin(0L);
                }
                mucMemberService.updateById(member);
            }
            //通知类
            else if(msgType.getType()==MsgType.setMucNotice.getType()){
                assert contentJson!=null;
                int type = contentJson.getInteger("type");
                int notice = contentJson.getInteger("notice");
                if(type== NoticeType.kickNotice.getCode()){
                    mucConfig.setKickNotice(notice);
                }else if(type== NoticeType.muteNotice.getCode()){
                    mucConfig.setMuteNotice(notice);
                }else if(type== NoticeType.quiteNotice.getCode()){
                    mucConfig.setQuitNotice(notice);
                }else if(type== NoticeType.revokeNotice.getCode()){
                    mucConfig.setRevokeNotice(notice);
                }
                mucConfigService.updateById(mucConfig);
            }
            //消息撤回时限
            else if(msgType.getType()==MsgType.msgRevokeDuration.getType()){
                mucConfig.setRevokeDuration(Integer.parseInt(mucMsg.getContent()));
                mucConfigService.updateById(mucConfig);
            }
            //群聊邀请确认
            else if(msgType.getType()==MsgType.mucJoinVerify.getType()){
                mucConfig.setIsJoinVerify(Integer.parseInt(mucMsg.getContent())==1);
                mucConfigService.updateById(mucConfig);
            }
            //显示群成员列表
            else if(msgType.getType()==MsgType.mucShowMemberList.getType()){
                mucConfig.setIsShowMemberList(Integer.parseInt(mucMsg.getContent())==1);
                mucConfigService.updateById(mucConfig);
            }
            //进群后允许发言
            else if(msgType.getType()==MsgType.mucAllowTalkAfterJoin.getType()){
                mucConfig.setIsAllowTalkAfterJoin(Integer.parseInt(mucMsg.getContent())==1);
                mucConfigService.updateById(mucConfig);
            }
            //显示成员群聊名称
            else if(msgType.getType()==MsgType.mucShowNickname.getType()){
                mucConfig.setIsShowNickname(Integer.parseInt(mucMsg.getContent())==1);
                mucConfigService.updateById(mucConfig);
            }
            //移除成员时撤回其历史发言
            else if(msgType.getType()==MsgType.mucRevokeWhenKicked.getType()){
                mucConfig.setIsRevokeAllWhenKicked(Integer.parseInt(mucMsg.getContent())==1);
                mucConfigService.updateById(mucConfig);
            }
            //入群前的消息可见
            else if(msgType.getType()==MsgType.mucShowMsgBeforeJoin.getType()){
                mucConfig.setIsShowMsgBeforeJoin(Integer.parseInt(mucMsg.getContent())==1);
                mucConfigService.updateById(mucConfig);
            }
            //gif消息
            //{"id":1,"origin":"","thumb":""}
            else if(msgType.getType()==MsgType.gif.getType()){
                assert contentJson!=null;
                int gifId = contentJson.getInteger("id");
                //gif发送次数+1
                redisUtil.incr(String.format(ConstantCache.GIF_SEND_TIMES,gifId),1);
                myGifService.addGif(mucMsg.getUserId(),gifId);
            }
            else if(msgType.getType()==MsgType.customEmoji.getType()){
                assert contentJson!=null;
                int id = contentJson.getInteger("id");
                CustomEmoji emoji = customEmojiService.getById(id);
                if(emoji!=null){
                    emoji.setTsSend(getTs());
                    customEmojiService.updateById(emoji);
                }
            }
            //贴纸消息
            else if(msgType.getType()==MsgType.sticker.getType()){
                assert contentJson!=null;
                int stickerId = contentJson.getInteger("id");
                //发送次数+1
                redisUtil.incr(String.format(ConstantCache.STICKER_ITEM_SEND_TIMES,stickerId),1);
            }
            //修改成员权限
            else if(msgType.getType()==MsgType.changeMemberPermission.getType()){
                assert contentJson!=null;
                Integer viewMember = contentJson.getInteger("viewMember");
                Integer invite = contentJson.getInteger("invite");
                Integer pin = contentJson.getInteger("pin");
                Integer capture = contentJson.getInteger("capture");
                Integer msgRate = contentJson.getInteger("msgRate");
                Integer msgCount = contentJson.getInteger("msgCount");
                Integer sendImage = contentJson.getInteger("sendImage");
                Integer sendVoice = contentJson.getInteger("sendVoice");
                Integer sendLocation = contentJson.getInteger("sendLocation");
                Integer sendGif = contentJson.getInteger("sendGif");
                Integer sendSticker = contentJson.getInteger("sendSticker");
                Integer sendVideo = contentJson.getInteger("sendVideo");
                Integer sendRedPack = contentJson.getInteger("sendRedPack");
                Integer sendLink = contentJson.getInteger("sendLink");
                Integer sendCard = contentJson.getInteger("sendCard");
                Integer sendFile = contentJson.getInteger("sendFile");
                if(viewMember!=null){
                    mucConfig.setViewMember(viewMember==1);
                }
                if(invite!=null){
                    mucConfig.setInvite(invite==1);
                }
                if(pin!=null){
                    mucConfig.setPin(pin==1);
                }
                if(capture!=null){
                    mucConfig.setCapture(capture==1);
                }
                if(msgRate!=null){
                    mucConfig.setMsgRate(msgRate);
                }
                if(msgCount!=null){
                    mucConfig.setMsgCount(msgCount);
                }
                if(sendImage!=null){
                    mucConfig.setSendImage(sendImage==1);
                }
                if(sendVoice!=null){
                    mucConfig.setSendVoice(sendVoice==1);
                }
                if(sendLocation!=null){
                    mucConfig.setSendLocation(sendLocation==1);
                }
                if(sendGif!=null){
                    mucConfig.setSendGif(sendGif==1);
                }
                if(sendSticker!=null){
                    mucConfig.setSendSticker(sendSticker==1);
                }
                if(sendVideo!=null){
                    mucConfig.setSendVideo(sendVideo==1);
                }
                if(sendRedPack!=null){
                    mucConfig.setSendRedPack(sendRedPack==1);
                }
                if(sendLink!=null){
                    mucConfig.setSendLink(sendLink==1);
                }
                if(sendCard!=null){
                    mucConfig.setSendCard(sendCard==1);
                }
                if(sendFile!=null){
                    mucConfig.setSendFile(sendFile==1);
                }
                mucConfigService.updateById(mucConfig);
            }
        }catch (Exception e){

        }
        //判断是否需要存储
        if(needStore){
            if (mucMsgService.save(mucMsg)) {
                log.info("save new mucMsg success");
            } else {
                log.info("save new mucMsg fail");
            }
        }
    }
    //处理单聊消息
    private void processMsg(Msg msg) {
        MsgType msgType = MsgType.getByType(msg.getType());
        boolean needStore = msgType.isStore();
        try{
            if(msgService.getById(msg.getId())!=null){
                return;
            }
            if(msg.getId()==null){
                msg.setId(getTs());
            }

            //如果发送者是系统账号
            User user = userService.findById(msg.getUserId());
//            User toUser = userService.findById(msg.getToUserId());
            //判断是否是好友
            Friend friend = friendService.findOne(msg.getUserId(), msg.getToUserId());
            if(friend!=null){
                friend.setTsLastTalk(getTs());
                //普通用户或者业务号
                if (user.getType() <= User.Type.business.getCode()) {
                    Friend friend2 = friendService.findOne(msg.getToUserId(), msg.getUserId());
                    friend2.setTsLastTalk(getTs());
                    friendService.updateById(friend);
                    friendService.updateById(friend2);
                }
            }

            //删除消息
            if(msgType.getType()==MsgType.delMsg.getType()) {
                String stanzaId = msg.getContent();
                Msg msgDel = msgService.findByStanzaIdOfSend(stanzaId,false);
                if(msgDel==null||msgDel.getTsDelete()>0){
                    return;
                }
                msgDel.setTsDelete(getTs());
                msgService.updateById(msgDel);
                return;
            }
            //清空记录
            if(msgType.getType()==MsgType.clearHistory.getType()) {
                JSONObject contentJson = JSONObject.parseObject(msg.getContent());
                msgService.deleteLogic(msg.getUserId(),contentJson.getInteger("toUserId"));
                return;
            }
            //判断是否是撤回消息
            if(msgType.getType()==MsgType.revokeMsg.getType()){
                String stanzaId = msg.getContent();
                List<Msg> revokeMsgs = msgService.findByStanzaId(stanzaId);
                //撤销者
                User revoker = userService.findById(msg.getUserId());
                for (Msg revokeMsg : revokeMsgs) {
                    revokeMsg.setTsRevoke(msg.getTsSend());
                    //是系统号撤销的
                    if(revoker.getType()==User.Type.sysAccount.getCode()){
                        revokeMsg.setRevokeType(MsgRevokeType.System.getCode());
                    }else {
                        //撤销人和要被撤销的消息发送人是同一个，且消息为发送，则为发送人撤销，否则为接收方撤销
                        if (msg.getUserId().intValue() == revokeMsg.getUserId()) {
                            revokeMsg.setRevokeType(MsgRevokeType.Sender.getCode());
                        } else {
                            revokeMsg.setRevokeType(MsgRevokeType.Receiver.getCode());
                        }
                    }
                }
                msgService.updateBatchById(revokeMsgs);
                return;
            }
            if(msgType.getType()==MsgType.text.getType()){
                Msg receiveMsg = new Msg();
                BeanUtils.copyProperties(msg, receiveMsg);
                receiveMsg.setId(msg.getId()+1);
                receiveMsg.setIsSend(false);
                if (msgService.save(msg)) {
                    receiveMsg.setOriginId(msg.getId());
                    if(msgService.save(receiveMsg)){
                        log.info("save new msg success");
                    }
                } else {
                    log.info("save new msg fail");
                }
                return;
            }
            JSONObject contentJson = JSONObject.parseObject(msg.getContent());
            //消息置顶
            if(msgType.getType()==MsgType.pin.getType()){
                int flag = contentJson.getInteger("flag");
                if(flag==0){
                    needStore = false;//取消置顶不保存
                }
                String stanzaId = contentJson.getString("stanzaId");
                List<Msg> msgs = msgService.findByStanzaId(stanzaId);
                for (Msg tempMsg : msgs) {
                    tempMsg.setTsPin(flag==0?0:msg.getTsSend());
                }
                msgService.updateBatchById(msgs);
            }
            //接受通话
            else if(msgType.getType()==MsgType.callAccept.getType()){
                int callRecordId = contentJson.getInteger("id");
                CallRecord callRecord = callRecordService.getById(callRecordId);
                callRecord.setTsAccept(msg.getTsSend());
                callRecord.setStatus(CallRecord.Status.AcceptOk.getCode());
                callRecordService.updateById(callRecord);
            }
            //拒绝通话
            else if(msgType.getType()==MsgType.callReject.getType()){
                int callRecordId = contentJson.getInteger("id");
                CallRecord callRecord = callRecordService.getById(callRecordId);
                callRecord.setTsReject(msg.getTsSend());
                callRecord.setStatus(CallRecord.Status.Reject.getCode());
                callRecordService.updateById(callRecord);
            }
            //gif消息
            //{"id":1,"origin":"","thumb":""}
            else if(msgType.getType()==MsgType.gif.getType()){
                int gifId = contentJson.getInteger("id");
                //gif发送次数+1
                redisUtil.incr(String.format(ConstantCache.GIF_SEND_TIMES,gifId),1);
                myGifService.addGif(msg.getUserId(),gifId);
            }
            else if(msgType.getType()==MsgType.customEmoji.getType()){
                int id = contentJson.getInteger("id");
                CustomEmoji emoji = customEmojiService.getById(id);
                if(emoji!=null){
                    emoji.setTsSend(getTs());
                    customEmojiService.updateById(emoji);
                }
            }
            //贴纸消息
            else if(msgType.getType()==MsgType.sticker.getType()){
                int stickerId = contentJson.getInteger("id");
                //发送次数+1
                redisUtil.incr(String.format(ConstantCache.STICKER_ITEM_SEND_TIMES,stickerId),1);
            }
            //判断是否需要存储
            if(needStore){
                Msg receiveMsg = new Msg();
                BeanUtils.copyProperties(msg, receiveMsg);
                receiveMsg.setId(msg.getId()+1);
                receiveMsg.setIsSend(false);
                if (msgService.save(msg)) {
                    receiveMsg.setOriginId(msg.getId());
                    if(msgService.save(receiveMsg)){
                        log.info("save new msg success");
                    }
                } else {
                    log.info("save new msg fail");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.info("save new msg error");
        }
    }
    private long getTs(){
        return new Date().getTime();
    }
}