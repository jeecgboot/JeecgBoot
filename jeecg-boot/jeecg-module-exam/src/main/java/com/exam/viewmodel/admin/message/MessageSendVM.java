package com.exam.viewmodel.admin.message;



import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


public class MessageSendVM {

    @NotBlank
    private String title;
    @NotBlank
    private String content;

    @Size(min = 1, message = "接收人不能为空")
    private List<Integer> receiveUserIds;

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

    public List<Integer> getReceiveUserIds() {
        return receiveUserIds;
    }

    public void setReceiveUserIds(List<Integer> receiveUserIds) {
        this.receiveUserIds = receiveUserIds;
    }
}
