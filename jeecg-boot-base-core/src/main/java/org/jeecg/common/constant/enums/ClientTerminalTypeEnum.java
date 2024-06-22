package org.jeecg.common.constant.enums;

/**
 * 客户终端类型
 */
public enum ClientTerminalTypeEnum {

    PC("pc", "电脑终端"),
    H5("h5", "移动网页端"),
    APP("app", "手机app端");

    private String key;
    private String text;

    ClientTerminalTypeEnum(String value, String text) {
        this.key = value;
        this.text = text;
    }

    public String getKey() {
        return this.key;
    }
}
