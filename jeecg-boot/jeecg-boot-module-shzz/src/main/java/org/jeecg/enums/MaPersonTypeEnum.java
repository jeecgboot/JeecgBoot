package org.jeecg.enums;

/**
 *
 */
public enum MaPersonTypeEnum {

    SHR(1, "受害人"),
    XYR(2, "嫌疑人");

    private Integer value;
    private String text;

    MaPersonTypeEnum(Integer value, String text) {
        this.value = value;
        this.text = text;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }}
