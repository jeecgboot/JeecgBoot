package org.jeecg.modules.message.test;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import org.jeecg.JeecgSystemApplication;
import org.jeecg.common.api.dto.message.BusMessageDTO;
import org.jeecg.common.api.dto.message.BusTemplateMessageDTO;
import org.jeecg.common.api.dto.message.MessageDTO;
import org.jeecg.common.api.dto.message.TemplateMessageDTO;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.enums.DySmsEnum;
import org.jeecg.common.constant.enums.EmailTemplateEnum;
import org.jeecg.common.constant.enums.MessageTypeEnum;
import org.jeecg.common.constant.enums.SysAnnmentTypeEnum;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.util.DySmsHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 消息推送测试
 * @Author: lsq
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JeecgSystemApplication.class)
public class SendMessageTest {

    @Autowired
    ISysBaseAPI sysBaseAPI;

    /**
     * 发送系统消息
     */
    @Test
    public void sendSysAnnouncement() {
        //发送人
        String fromUser = "admin";
        //接收人
        String toUser = "jeecg";
        //标题
        String title = "系统消息";
        //内容
        String msgContent = "TEST:今日份日程计划已送达！";
        //发送系统消息
        sysBaseAPI.sendSysAnnouncement(new MessageDTO(fromUser, toUser, title, msgContent));
        //消息类型
        String msgCategory = CommonConstant.MSG_CATEGORY_1;
        //业务类型
        String busType = SysAnnmentTypeEnum.EMAIL.getType();
        //业务ID
        String busId = "11111";
        //发送带业务参数的系统消息
        BusMessageDTO busMessageDTO = new BusMessageDTO(fromUser, toUser, title, msgContent, msgCategory, busType,busId);
        sysBaseAPI.sendBusAnnouncement(busMessageDTO);
    }

    /**
     * 发送模版消息
     */
    @Test
    public void sendTemplateAnnouncement() {
        //发送人
        String fromUser = "admin";
        //接收人
        String toUser = "jeecg";
        //标题
        String title = "通知公告";
        //模版编码
        String templateCode = "412358";
        //模版参数
        Map templateParam = new HashMap<>();
        templateParam.put("realname","JEECG用户");
        sysBaseAPI.sendTemplateAnnouncement(new TemplateMessageDTO(fromUser,toUser,title,templateParam,templateCode));
        //业务类型
        String busType = SysAnnmentTypeEnum.EMAIL.getType();
        //业务ID
        String busId = "11111";
        //发送带业务参数的模版消息
        BusTemplateMessageDTO busMessageDTO = new BusTemplateMessageDTO(fromUser, toUser, title, templateParam ,templateCode, busType,busId);
        sysBaseAPI.sendBusTemplateAnnouncement(busMessageDTO);
        //新发送模版消息
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setType(MessageTypeEnum.XT.getType());
        messageDTO.setToAll(false);
        messageDTO.setToUser(toUser);
        messageDTO.setTitle("【流程错误】");
        messageDTO.setFromUser("admin");
        HashMap data = new HashMap<>();
        data.put(CommonConstant.NOTICE_MSG_BUS_TYPE, "msg_node");
        messageDTO.setData(data);
        messageDTO.setContent("TEST:流程执行失败！任务节点未找到");
        sysBaseAPI.sendTemplateMessage(messageDTO);
    }
    /**
     * 发送邮件
     */
    @Test
    public void sendEmailMsg() {
        String title = "【日程提醒】您的日程任务即将开始";
        String content = "TEST:尊敬的王先生，您购买的演唱会将于本周日10：08分在国家大剧院如期举行，届时请携带好您的门票和身份证到场";
        String email = "250678106@qq.com";
        sysBaseAPI.sendEmailMsg(email,title,content);
    }
    /**
     * 发送html模版邮件
     */
    @Test
    public void sendTemplateEmailMsg() {
        String title = "收到一个催办";
        String email = "250678106@qq.com";
        JSONObject params = new JSONObject();
        params.put("bpm_name","高级设置");
        params.put("bpm_task","审批人");
        params.put("datetime","2023-10-07 18:00:49");
        params.put("url","http://boot3.jeecg.com/message/template");
        params.put("remark","快点快点快点快点快点快点快点快点快点快点快点快点快点快点快点快点快点快点快点快点快点快点快点快点快点快点快点快点");
        sysBaseAPI.sendHtmlTemplateEmail(email,title, EmailTemplateEnum.BPM_CUIBAN_EMAIL,params);
    }
    /**
     * 发送短信
     */
    @Test
    public void sendSms() throws ClientException {
        //手机号
        String mobile = "159***";
        //消息模版
        DySmsEnum templateCode = DySmsEnum.LOGIN_TEMPLATE_CODE;
        //模版所需参数
        JSONObject obj = new JSONObject();
        obj.put("code", "4XDP");
        DySmsHelper.sendSms(mobile, obj, templateCode);
    }
}
