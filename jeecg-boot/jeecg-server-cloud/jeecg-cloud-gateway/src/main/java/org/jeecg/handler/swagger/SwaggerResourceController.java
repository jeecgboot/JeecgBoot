package org.jeecg.handler.swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger.web.*;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger聚合接口，三个接口都是 doc.html需要访问的接口
 * @author zyf
 * @date: 2022/4/21 10:55
 */
@RestController
@RequestMapping("/swagger-resources")
public class SwaggerResourceController {
    private MySwaggerResourceProvider swaggerResourceProvider;
    /**
     * 生产环境，关闭swagger文档
     */
    @Value("${knife4j.production:#{null}}")
    private Boolean production;
    
    @Autowired
    public SwaggerResourceController(MySwaggerResourceProvider swaggerResourceProvider) {
        this.swaggerResourceProvider = swaggerResourceProvider;
    }

    @RequestMapping(value = "/configuration/security")
    public ResponseEntity<SecurityConfiguration> securityConfiguration() {
        return new ResponseEntity<>(SecurityConfigurationBuilder.builder().build(), HttpStatus.OK);
    }

    @RequestMapping(value = "/configuration/ui")
    public ResponseEntity<UiConfiguration> uiConfiguration() {
        return new ResponseEntity<>(UiConfigurationBuilder.builder().build(), HttpStatus.OK);
    }

    @RequestMapping
    public ResponseEntity<List<SwaggerResource>> swaggerResources() {
        // 是否开启生产环境屏蔽swagger
        if (production != null && production) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        return new ResponseEntity<>(swaggerResourceProvider.get(), HttpStatus.OK);
    }
}