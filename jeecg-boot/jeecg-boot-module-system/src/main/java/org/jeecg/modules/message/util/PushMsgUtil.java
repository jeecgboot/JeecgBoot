package org.jeecg.modules.message.util;

import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.entity.SysMessageTemplate;
import org.jeecg.modules.message.handle.enums.SendMsgStatusEnum;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.message.service.ISysMessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 消息生成工具
 */

@Component
public class PushMsgUtil {

    @Autowired
    private ISysMessageService sysMessageService;

    @Autowired
    private ISysMessageTemplateService sysMessageTemplateService;

    /**
     * @param msgType 消息类型 1短信 2邮件 3微信
     * @param templateCode    消息模板码
     * @param map     消息参数
     * @param sentTo  接收消息方
     */
    public boolean sendMessage(String msgType, String templateCode, Map<String, String> map, String sentTo) {
        List<SysMessageTemplate> sysSmsTemplates = sysMessageTemplateService.selectByCode(templateCode);
        SysMessage sysMessage = new SysMessage();
        if (sysSmsTemplates.size() > 0) {
            SysMessageTemplate sysSmsTemplate = sysSmsTemplates.get(0);
            sysMessage.setEsType(msgType);
            sysMessage.setEsReceiver(sentTo);
            //模板标题
            String title = sysSmsTemplate.getTemplateName();
            //模板内容
            String content = sysSmsTemplate.getTemplateContent();
            if(map!=null) {
            	 for (Map.Entry<String, String> entry : map.entrySet()) {
                     String str = "${" + entry.getKey() + "}";
                     title = title.replace(str, entry.getValue());
                     content = content.replace(str, entry.getValue());
                 }
            }
            sysMessage.setEsTitle(title);
            sysMessage.setEsContent(content);
            sysMessage.setEsParam(JSONObject.toJSONString(map));
            sysMessage.setEsSendTime(new Date());
            sysMessage.setEsSendStatus(SendMsgStatusEnum.WAIT.getCode());
            sysMessage.setEsSendNum(0);
            if(sysMessageService.save(sysMessage)) {
				return true;
			}
        }
        return false;
    }
}
