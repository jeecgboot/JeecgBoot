package com.exam.service.enums;

public enum ActionEnum {

    ADD(1, "新增"),
    UPDATE(2, "修改");

    int code;
    String Name;

    ActionEnum(int code, String name) {
        this.code = code;
        Name = name;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


}
