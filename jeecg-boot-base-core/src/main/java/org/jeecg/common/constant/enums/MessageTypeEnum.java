package org.jeecg.common.constant.enums;

import org.jeecg.common.system.annotation.EnumDict;
import org.jeecg.common.system.vo.DictModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息类型
 * @author: jeecg-boot
 */
@EnumDict("messageType")
public enum MessageTypeEnum {

    XT("system",  "系统消息"),
    YJ("email",  "邮件消息"),
    DD("dingtalk", "钉钉消息"),
    QYWX("wechat_enterprise", "企业微信");

    MessageTypeEnum(String type, String note){
        this.type = type;
        this.note = note;
    }

    /**
     * 消息类型
     */
    String type;

    /**
     * 类型说明
     */
    String note;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    /**
     * 获取字典数据
     * @return
     */
    public static List<DictModel> getDictList(){
        List<DictModel> list = new ArrayList<>();
        DictModel dictModel = null;
        for(MessageTypeEnum e: MessageTypeEnum.values()){
            dictModel = new DictModel();
            dictModel.setValue(e.getType());
            dictModel.setText(e.getNote());
            list.add(dictModel);
        }
        return list;
    }
}
