package com.exam.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TaskExamCustomerAnswer implements Serializable {

    private static final long serialVersionUID = -556842372977600137L;

    private Integer id;

    /**
     * 任务Id
     */
    private Integer taskExamId;

    /**
     * 创建者
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 任务完成情况(Json)
     */
    private Integer textContentId;

}
