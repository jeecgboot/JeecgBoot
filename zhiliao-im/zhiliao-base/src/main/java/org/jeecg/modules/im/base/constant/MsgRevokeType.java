package org.jeecg.modules.im.base.constant;

import lombok.Getter;

public enum MsgRevokeType {
    Normal(0, "正常"),
    Sender(1, "发送者撤回"),
    Receiver(2, "接收者撤回"),
    System(3, "系统");
    @Getter
    int code;
    @Getter
    String name;
    MsgRevokeType(int code,String name){
        this.code = code;
        this.name = name;
    }
}