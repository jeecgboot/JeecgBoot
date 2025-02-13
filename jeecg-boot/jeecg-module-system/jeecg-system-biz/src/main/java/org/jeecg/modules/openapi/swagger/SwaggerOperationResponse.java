package org.jeecg.modules.openapi.swagger;

import lombok.Data;

import java.util.Map;

/**
 * @date 2025/1/26 11:47
 */
@Data
public class SwaggerOperationResponse {
    private String description;
    private Map<String, String> schema;
}
