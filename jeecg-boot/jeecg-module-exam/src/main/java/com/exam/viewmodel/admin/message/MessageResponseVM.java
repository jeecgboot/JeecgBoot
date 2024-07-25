package com.exam.viewmodel.admin.message;


public class MessageResponseVM {
    private Integer id;

    private String title;

    private String content;

    private String sendUserName;

    private String receives;

    private Integer receiveUserCount;

    private Integer readCount;

    private String createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }

    public String getReceives() {
        return receives;
    }

    public void setReceives(String receives) {
        this.receives = receives;
    }

    public Integer getReceiveUserCount() {
        return receiveUserCount;
    }

    public void setReceiveUserCount(Integer receiveUserCount) {
        this.receiveUserCount = receiveUserCount;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
