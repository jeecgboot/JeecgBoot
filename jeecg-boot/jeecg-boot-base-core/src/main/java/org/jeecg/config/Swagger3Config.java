package org.jeecg.config;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.CommonConstant;
import org.springdoc.core.customizers.GlobalOpenApiCustomizer;
import org.springdoc.core.filters.GlobalOpenApiMethodFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author eightmonth
 */
@Slf4j
@Configuration
@PropertySource("classpath:config/default-spring-doc.properties")
public class Swagger3Config implements WebMvcConfigurer {
    // 定义不需要注入安全要求的路径集合
    Set<String> excludedPaths = new HashSet<>(Arrays.asList(
            "/sys/randomImage/{key}",
            "/sys/login",
            "/sys/phoneLogin",
            "/sys/mLogin",
            "/sys/sms",
            "/sys/cas/client/validateLogin",
            "/test/jeecgDemo/demo3",
            "/sys/thirdLogin/**",
            "/sys/user/register"
    ));

    /**
     *
     * 显示swagger-ui.html文档展示页，还必须注入swagger资源：
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public GlobalOpenApiMethodFilter globalOpenApiMethodFilter() {
        return method -> method.isAnnotationPresent(Operation.class);
    }

    @Bean
    public GlobalOpenApiCustomizer globalOpenApiCustomizer() {
        return openApi -> {
            // 全局添加鉴权参数
            if (openApi.getPaths() != null) {
                openApi.getPaths().forEach((path, pathItem) -> {
                    //log.debug("path: {}", path);
                    // 检查当前路径是否在排除列表中
                    boolean isExcluded = excludedPaths.stream().anyMatch(excludedPath ->
                            excludedPath.equals(path) ||
                                    (excludedPath.endsWith("**") && path.startsWith(excludedPath.substring(0, excludedPath.length() - 2)))
                    );

                    if (!isExcluded) {
                        // 接口添加鉴权参数
                        pathItem.readOperations()
                                .forEach(operation ->
                                        operation.addSecurityItem(new SecurityRequirement().addList(CommonConstant.X_ACCESS_TOKEN))
                                );
                    }
                });
            }
        };
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("JeecgBoot 后台服务API接口文档")
                        .version("3.8.0")
                        .contact(new Contact().name("北京国炬信息技术有限公司").url("www.jeccg.com").email("jeecgos@163.com"))
                        .description( "后台API接口")
                        .termsOfService("NO terms of service")
                        .license(new License().name("Apache 2.0").url("http://www.apache.org/licenses/LICENSE-2.0.html")))
                .addSecurityItem(new SecurityRequirement().addList(CommonConstant.X_ACCESS_TOKEN))
                .components(new Components().addSecuritySchemes(CommonConstant.X_ACCESS_TOKEN,
                        new SecurityScheme().name(CommonConstant.X_ACCESS_TOKEN).type(SecurityScheme.Type.HTTP)));
    }
}