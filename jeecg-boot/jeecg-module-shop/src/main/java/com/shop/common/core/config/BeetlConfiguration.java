package com.shop.common.core.config;

import com.shop.common.core.shiro.ShiroExt;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Beetl配置
 * 2018-02-22 11:29
 */
@Configuration
public class BeetlConfiguration {
    @Value("${beetl.templatesPath:templates}")
    private String templatesPath;  // 模板根目录
    @Value("${beetl.suffix:html}")
    private String suffix;  // 模板后缀
    @Value("${beetl.dev:true}")
    private boolean dev;  // 开启热加载

    @Bean(name = "beetlConfig")
    public BeetlGroupUtilConfiguration getBeetlGroupUtilConfiguration() {
        BeetlGroupUtilConfiguration bguc = new BeetlGroupUtilConfiguration();
        Properties extProperties = new Properties();
        extProperties.put("RESOURCE.autoCheck", dev ? "true" : "false");
        extProperties.put("HTML_TAG_FLAG", ":");
        bguc.setConfigProperties(extProperties);
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) loader = BeetlConfiguration.class.getClassLoader();
        bguc.setResourceLoader(new ClasspathResourceLoader(loader, templatesPath));
        bguc.init();
        // 增加自定义函数
        bguc.getGroupTemplate().registerFunctionPackage("so", new ShiroExt());
        // 增加自定义标签
        bguc.getGroupTemplate().registerTag("include", org.beetl.ext.tag.html.IncludeResourceHtmlTag.class);
        bguc.getGroupTemplate().registerTag("layout", org.beetl.ext.tag.html.LayoutResourceHtmlTag.class);
        bguc.getGroupTemplate().registerTag("set", org.beetl.ext.tag.html.SetHtmlTag.class);
        bguc.getGroupTemplate().registerTag("if", org.beetl.ext.tag.html.IfHtmlTag.class);
        bguc.getGroupTemplate().registerTag("for", org.beetl.ext.tag.html.ForeachHtmlTag.class);
        return bguc;
    }

    @Bean(name = "beetlViewResolver")
    public BeetlSpringViewResolver getBeetlSpringViewResolver(@Qualifier("beetlConfig") BeetlGroupUtilConfiguration bguc) {
        BeetlSpringViewResolver bsvr = new BeetlSpringViewResolver();
        bsvr.setContentType("text/html;charset=UTF-8");
        bsvr.setViewNames("*." + suffix, "*." + suffix + "#*");
        bsvr.setOrder(0);
        bsvr.setConfig(bguc);
        return bsvr;
    }

}
