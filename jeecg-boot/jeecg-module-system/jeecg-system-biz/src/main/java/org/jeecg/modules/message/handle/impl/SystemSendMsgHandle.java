package org.jeecg.modules.message.handle.impl;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.WebsocketConst;
import org.jeecg.common.constant.enums.NoticeTypeEnum;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.common.constant.enums.Vue3MessageHrefEnum;
import org.jeecg.modules.message.handle.ISendMsgHandle;
import org.jeecg.modules.message.websocket.WebSocket;
import org.jeecg.modules.system.entity.SysAnnouncement;
import org.jeecg.modules.system.entity.SysAnnouncementSend;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.mapper.SysAnnouncementMapper;
import org.jeecg.modules.system.mapper.SysAnnouncementSendMapper;
import org.jeecg.modules.system.mapper.SysUserMapper;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
* @Description: 发送系统消息
* @Author: wangshuai
* @Date: 2022年3月22日 18:48:20
*/
@Component("systemSendMsgHandle")
public class SystemSendMsgHandle implements ISendMsgHandle {

    public static final String FROM_USER="system";

    @Resource
    private SysAnnouncementMapper sysAnnouncementMapper;

    @Resource
    private SysUserMapper userMapper;

    @Resource
    private SysAnnouncementSendMapper sysAnnouncementSendMapper;

    @Resource
    private WebSocket webSocket;

    /**
     * 该方法会发送3种消息：系统消息、企业微信 钉钉
     * @param esReceiver 发送人
     * @param esTitle 标题
     * @param esContent 内容
     */
    @Override
    public void sendMsg(String esReceiver, String esTitle, String esContent) {
        if(oConvertUtils.isEmpty(esReceiver)){
            throw  new JeecgBootException("被发送人不能为空");
        }
        ISysBaseAPI sysBaseApi = SpringContextUtils.getBean(ISysBaseAPI.class);
        MessageDTO messageDTO = new MessageDTO(FROM_USER,esReceiver,esTitle,esContent);
        sysBaseApi.sendSysAnnouncement(messageDTO);
    }

    /**
     * 仅发送系统消息
     * @param messageDTO
     */
    @Override
    public void sendMessage(MessageDTO messageDTO) {
        //原方法不支持 sysBaseApi.sendSysAnnouncement(messageDTO);  有企业微信消息逻辑，
        String title = messageDTO.getTitle();
        String content = messageDTO.getContent();
        String fromUser = messageDTO.getFromUser();
        Map<String,Object> data = messageDTO.getData();
        String[] arr = messageDTO.getToUser().split(",");
        for(String username: arr){
            //update-begin---author:wangshuai---date:2025-06-26---for:【QQYUN-12162】OA项目改造，系统重消息拆分，目前消息都在一起 需按分类进行拆分---
            doSend(title, content, fromUser, username, data, messageDTO.getNoticeType());
            //update-end---author:wangshuai---date:2025-06-26---for:【QQYUN-12162】OA项目改造，系统重消息拆分，目前消息都在一起 需按分类进行拆分---
        }
    }

    private void doSend(String title, String msgContent, String fromUser, String toUser, Map<String, Object> data, String noticeType){
        SysAnnouncement announcement = new SysAnnouncement();
        if(data!=null){
            //摘要信息
            Object msgAbstract = data.get(CommonConstant.NOTICE_MSG_SUMMARY);
            if(msgAbstract!=null){
                announcement.setMsgAbstract(msgAbstract.toString());
            }
            // 任务节点ID
            Object taskId = data.get(CommonConstant.NOTICE_MSG_BUS_ID);
            if(taskId!=null){
                announcement.setBusId(taskId.toString());
                announcement.setBusType(Vue3MessageHrefEnum.BPM_TASK.getBusType());
                noticeType = NoticeTypeEnum.NOTICE_TYPE_FLOW.getValue();
            }

            // 流程内消息节点 发消息会传一个busType
            Object busType = data.get(CommonConstant.NOTICE_MSG_BUS_TYPE);
            if(busType!=null){
                announcement.setBusType(busType.toString());
                noticeType = NoticeTypeEnum.NOTICE_TYPE_FLOW.getValue();
            }
        }
        announcement.setTitile(title);
        announcement.setMsgContent(msgContent);
        announcement.setSender(fromUser);
        announcement.setPriority(CommonConstant.PRIORITY_M);
        announcement.setMsgType(CommonConstant.MSG_TYPE_UESR);
        announcement.setSendStatus(CommonConstant.HAS_SEND);
        announcement.setSendTime(new Date());
        //系统消息
        announcement.setMsgCategory("2");
        announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
        if(oConvertUtils.isEmpty(noticeType)){
            noticeType = NoticeTypeEnum.NOTICE_TYPE_SYSTEM.getValue();
        }
        announcement.setNoticeType(noticeType);
        announcement.setIzTop(CommonConstant.IZ_TOP_0);
        sysAnnouncementMapper.insert(announcement);
        // 2.插入用户通告阅读标记表记录
        String userId = toUser;
        String[] userIds = userId.split(",");
        String anntId = announcement.getId();
        for(int i=0;i<userIds.length;i++) {
            if(oConvertUtils.isNotEmpty(userIds[i])) {
                SysUser sysUser = userMapper.getUserByName(userIds[i]);
                if(sysUser==null) {
                    continue;
                }
                SysAnnouncementSend announcementSend = new SysAnnouncementSend();
                announcementSend.setAnntId(anntId);
                announcementSend.setUserId(sysUser.getId());
                announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
                sysAnnouncementSendMapper.insert(announcementSend);
                JSONObject obj = new JSONObject();
                obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_USER);
                obj.put(WebsocketConst.MSG_USER_ID, sysUser.getId());
                obj.put(WebsocketConst.MSG_ID, announcement.getId());
                obj.put(WebsocketConst.MSG_TXT, announcement.getTitile());
                obj.put(CommonConstant.NOTICE_TYPE,noticeType);
                webSocket.sendMessage(sysUser.getId(), obj.toJSONString());
            }
        }
    }
}