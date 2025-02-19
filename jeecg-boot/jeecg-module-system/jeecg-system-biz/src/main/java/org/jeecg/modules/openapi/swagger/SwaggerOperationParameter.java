package org.jeecg.modules.openapi.swagger;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @date 2025/1/26 11:43
 */
@Data
public class SwaggerOperationParameter {
    private String name;
    private String in;
    private String description;
    private Boolean required;
    private Map<String, String> schema;
}
