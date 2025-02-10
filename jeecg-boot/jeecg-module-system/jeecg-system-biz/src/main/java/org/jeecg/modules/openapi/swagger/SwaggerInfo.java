package org.jeecg.modules.openapi.swagger;

import lombok.Data;

/**
 * @date 2025/1/26 11:05
 */
@Data
public class SwaggerInfo {
    private String description;
    private String version;
    private String title;
    private String termsOfService;
    private SwaggerInfoContact contact;
    private SwaggerInfoLicense license;
}
