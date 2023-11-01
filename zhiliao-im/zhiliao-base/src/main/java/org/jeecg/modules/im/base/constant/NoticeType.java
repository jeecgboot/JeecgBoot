package org.jeecg.modules.im.base.constant;

import lombok.Getter;

public enum NoticeType {
    unknown(-1, "-"),
    quiteNotice(0, "退群通知"),
    muteNotice(1, "禁言通知"),
    kickNotice(2, "移除通知"),
    revokeNotice(3, "撤回通知");
    @Getter
    int code;
    @Getter
    String desc;
    NoticeType(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

}
