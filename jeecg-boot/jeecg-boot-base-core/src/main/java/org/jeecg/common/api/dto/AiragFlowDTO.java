package org.jeecg.common.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * 调用AI流程入参
 * for [QQYUN-13634]在baseapi里面封装方法，方便其他模块调用
 * @author chenrui
 * @date 2025/9/2 14:11
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AiragFlowDTO implements Serializable {


    private static final long serialVersionUID = 7431775881170684867L;

    /**
     * 流程id
     */
    private String flowId;


    /**
     * 输入参数
     */
    private Map<String, Object> inputParams;
}
