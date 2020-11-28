package org.jeecg.common.api.dto.message;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 消息模板dto
 */
@Data
public class TemplateDTO implements Serializable {

    private static final long serialVersionUID = 5848247133907528650L;

    /**
     * 模板编码
     */
    protected String templateCode;

    /**
     * 模板参数
     */
    protected Map<String, String> templateParam;

    /**
     * 构造器 通过设置模板参数和模板编码 作为参数获取消息内容
     */
    public TemplateDTO(String templateCode, Map<String, String> templateParam){
        this.templateCode = templateCode;
        this.templateParam = templateParam;
    }

    public TemplateDTO(){

    }
}
