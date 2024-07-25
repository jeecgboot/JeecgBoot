package com.exam.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TextContent implements Serializable {

    private static final long serialVersionUID = -1279530310964668131L;

    public TextContent(){

    }

    public TextContent(String content, Date createTime) {
        this.content = content;
        this.createTime = createTime;
    }

    private Integer id;

    /**
     * 内容(Json)
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

}
