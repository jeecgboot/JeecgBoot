package com.exam.domain.enums;

import java.util.HashMap;
import java.util.Map;

public enum ExamPaperAnswerStatusEnum {

    WaitJudge(1, "待批改"),
    Complete(2, "完成");

    int code;
    String name;

    ExamPaperAnswerStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    private static final Map<Integer, ExamPaperAnswerStatusEnum> keyMap = new HashMap<>();

    static {
        for (ExamPaperAnswerStatusEnum item : ExamPaperAnswerStatusEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    public static ExamPaperAnswerStatusEnum fromCode(Integer code) {
        return keyMap.get(code);
    }

}
