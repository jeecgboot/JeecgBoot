package org.jeecg.modules.airag.wordtpl.dto;

import lombok.Data;

import java.util.Map;

/**
 * word模版生成入参
 * @author chenrui
 * @date 2025/7/10 14:38
 */
@Data
public class WordTplGenDTO {

    /**
     * 模版id
     */
    String templateId;


    /**
     * 模版code
     */
    String templateCode;

    /**
     * 数据
     */
    Map<String,Object> data;

}
