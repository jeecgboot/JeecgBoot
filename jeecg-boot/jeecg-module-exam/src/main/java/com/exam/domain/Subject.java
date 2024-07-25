package com.exam.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Subject implements Serializable {

    private static final long serialVersionUID = 8058095034457106501L;

    private Integer id;

    /**
     * 语文 数学 英语 等
     */
    private String name;

    /**
     * 年级 (1-12) 小学 初中
     */
    private Integer level;

    /**
     * 一年级、二年级等
     */
    private String levelName;

    /**
     * 排序
     */
    private Integer itemOrder;

    private Boolean deleted;

}