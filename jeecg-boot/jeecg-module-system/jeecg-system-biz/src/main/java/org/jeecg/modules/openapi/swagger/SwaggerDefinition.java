package org.jeecg.modules.openapi.swagger;

import lombok.Data;

import java.util.Map;

/**
 * @date 2025/1/26 11:17
 */
@Data
public class SwaggerDefinition {
    private String type;
    private Map<String, SwaggerDefinitionProperties> properties;
}
