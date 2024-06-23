package org.jeecg.common.api.dto.message;

import lombok.Data;
import org.jeecg.common.constant.CommonConstant;

import java.io.Serializable;
import java.util.*;

/**
 * 普通消息
 * @author: jeecg-boot
 */
@Data
public class MessageDTO implements Serializable {
    private static final long serialVersionUID = -5690444483968058442L;
    
    /**
     * 发送人(用户登录账户)
     */
    protected String fromUser;

    /**
     * 发送给(用户登录账户)
     */
    protected String toUser;

    /**
     * 发送给所有人
     */
    protected Boolean toAll;

    /**
     * 消息主题
     */
    protected String title;

    /**
     * 消息内容
     */
    protected String content;

    /**
     * 消息类型 1:消息  2:系统消息
     */
    protected String category;
    
    /**
     * 消息类型：org.jeecg.common.constant.enums.MessageTypeEnum
     *  XT("system",  "系统消息")
     *  YJ("email",  "邮件消息")
     *  DD("dingtalk", "钉钉消息")
     *  QYWX("wechat_enterprise", "企业微信")
     */
    protected String type;
    

    //---【推送模板相关参数】-------------------------------------------------------------
    /**
     * 是否发送Markdown格式的消息
     */
    protected boolean isMarkdown;
    /**
     * 模板消息对应的模板编码
     */
    protected String templateCode;
    /**
     * 解析模板内容 对应的数据
     */
    protected Map<String, Object> data;
    //---【推送模板相关参数】-------------------------------------------------------------

    //---【邮件相关参数】-------------------------------------------------------------
    /**
     * 邮件抄送人
     */
    private String copyToUser;

    /**
     * 邮件推送地址
     */
    protected Set<String> toEmailList;

    /**
     * 邮件抄送地址
     */
    protected Set<String> ccEmailList;
    //---【邮件相关参数】-------------------------------------------------------------
    
    public MessageDTO(){
    }

    /**
     * 构造器1 系统消息
     */
    public MessageDTO(String fromUser,String toUser,String title, String content){
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.title = title;
        this.content = content;
        //默认 都是2系统消息
        this.category = CommonConstant.MSG_CATEGORY_2;
    }

    /**
     * 构造器2 支持设置category 1:消息  2:系统消息
     */
    public MessageDTO(String fromUser,String toUser,String title, String content, String category){
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public boolean isMarkdown() {
        return this.isMarkdown;
    }

    public void setIsMarkdown(boolean isMarkdown) {
        this.isMarkdown = isMarkdown;
    }
}
