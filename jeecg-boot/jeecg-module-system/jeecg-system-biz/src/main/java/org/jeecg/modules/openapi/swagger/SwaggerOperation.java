package org.jeecg.modules.openapi.swagger;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @date 2025/1/26 11:16
 */
@Data
public class SwaggerOperation {
    private List<String> tags;
    private String summary;
    private String description;
    private String operationId;
    private List<String> produces;
    private List<SwaggerOperationParameter> parameters;
    private Map<String, SwaggerOperationResponse> responses;
}
