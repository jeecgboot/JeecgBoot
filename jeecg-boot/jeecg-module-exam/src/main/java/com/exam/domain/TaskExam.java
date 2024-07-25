package com.exam.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TaskExam implements Serializable {

    private static final long serialVersionUID = -7014704644631536195L;

    private Integer id;

    /**
     * 任务标题
     */
    private String title;

    /**
     * 年级
     */
    private Integer gradeLevel;

    /**
     * 任务框架 内容为JSON
     */
    private Integer frameTextContentId;

    /**
     * 创建者
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    private Boolean deleted;

    /**
     * 创建人用户名
     */
    private String createUserName;

}
