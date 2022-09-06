package org.jeecg.common.api.dto.message;

import lombok.Data;
import java.io.Serializable;
import java.util.Map;

/**
 * 模板消息
 * @author: jeecg-boot
 */
@Data
public class TemplateMessageDTO extends TemplateDTO implements Serializable {

    private static final long serialVersionUID = 411137565170647585L;


    /**
     * 发送人(用户登录账户)
     */
    protected String fromUser;

    /**
     * 发送给(用户登录账户)
     */
    protected String toUser;

    /**
     * 消息主题
     */
    protected String title;


    public TemplateMessageDTO(){

    }

    /**
     * 构造器1 发模板消息用
     */
    public TemplateMessageDTO(String fromUser, String toUser,String title, Map<String, String> templateParam, String templateCode){
        super(templateCode, templateParam);
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.title = title;
    }



}
