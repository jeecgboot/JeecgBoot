package org.jeecg.common.api.dto.message;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 带业务参数的模板消息
 * @author: jeecg-boot
 */
@Data
public class BusTemplateMessageDTO extends TemplateMessageDTO implements Serializable {

    private static final long serialVersionUID = -4277810906346929459L;

    /**
     * 业务类型
     */
    private String busType;

    /**
     * 业务id
     */
    private String busId;

    public BusTemplateMessageDTO(){

    }

    /**
     * 构造 带业务参数的模板消息
     * @param fromUser
     * @param toUser
     * @param title
     * @param templateParam
     * @param templateCode
     * @param busType
     * @param busId
     */
    public BusTemplateMessageDTO(String fromUser, String toUser, String title, Map<String, String> templateParam, String templateCode, String busType, String busId){
        super(fromUser, toUser, title, templateParam, templateCode);
        this.busId = busId;
        this.busType = busType;
    }
}
