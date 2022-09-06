package org.jeecg.handler.swagger;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 聚合各个服务的swagger接口
 * @author zyf
 * @date: 2022/4/21 10:55
 */
@Component
@Slf4j
@Primary
public class MySwaggerResourceProvider implements SwaggerResourcesProvider {
    /**
     * swagger2默认的url后缀
     */
    private static final String SWAGGER2URL = "/v2/api-docs";

    /**
     * 网关路由
     */
    private final RouteLocator routeLocator;

    /**
     * nacos服务地址
     */
    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String serverAddr;

    /**
     * Swagger中需要排除的服务
     */
    private String[] excludeServiceIds=new String[]{"jeecg-cloud-monitor"};


    /**
     * 网关应用名称
     */
    @Value("${spring.application.name}")
    private String self;

    @Autowired
    public MySwaggerResourceProvider(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routeHosts = new ArrayList<>();
        // 获取所有可用的host：serviceId
        routeLocator.getRoutes().filter(route -> route.getUri().getHost() != null)
                .filter(route -> !self.equals(route.getUri().getHost()))
                .subscribe(route ->{
                    //update-begin---author:zyf ---date:20220413 for：过滤掉无效路由,避免接口文档报错无法打开
                    boolean hasRoute=checkRoute(route.getId());
                    if(hasRoute){
                        routeHosts.add(route.getUri().getHost());
                    }
                    //update-end---author:zyf ---date:20220413 for：过滤掉无效路由,避免接口文档报错无法打开
                });

        // 记录已经添加过的server，存在同一个应用注册了多个服务在nacos上
        Set<String> dealed = new HashSet<>();
        routeHosts.forEach(instance -> {
            // 拼接url
            String url = "/" + instance.toLowerCase() + SWAGGER2URL;
            if (!dealed.contains(url)) {
                dealed.add(url);
                log.info(" Gateway add SwaggerResource: {}",url);
                SwaggerResource swaggerResource = new SwaggerResource();
                swaggerResource.setUrl(url);
                swaggerResource.setSwaggerVersion("2.0");
                swaggerResource.setName(instance);
                //Swagger排除不展示的服务
                if(!ArrayUtil.contains(excludeServiceIds,instance)){
                    resources.add(swaggerResource);
                }
            }
        });
        return resources;
    }

    /**
     * 检测nacos中是否有健康实例
     * @param routeId
     * @return
     */
    private Boolean checkRoute(String routeId) {
        Boolean hasRoute = false;
        try {
            NamingService naming = NamingFactory.createNamingService(serverAddr);
            List<Instance> list = naming.selectInstances(routeId, true);
            if (ObjectUtil.isNotEmpty(list)) {
                hasRoute = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasRoute;
    }
}