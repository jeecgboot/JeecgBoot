package org.jeecg.modules.openapi.swagger;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @date 2025/1/26 11:05
 */
@Data
public class SwaggerModel {
    private String swagger;
    private SwaggerInfo info;
    private String host;
    private String basePath;
    private List<SwaggerTag> tags;
    private List<String> schemes;
    private Map<String, Map<String, SwaggerOperation>> paths;
    private Map<String, SwaggerDefinition> definitions;
}
