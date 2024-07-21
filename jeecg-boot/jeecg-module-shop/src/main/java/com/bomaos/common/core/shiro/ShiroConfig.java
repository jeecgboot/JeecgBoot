package com.bomaos.common.core.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro框架配置
 * Created by Panyoujie on 2017-04-28 09:45
 */
@Configuration
public class ShiroConfig {

    /**
     * shiro过滤器
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        // 登录配置
        shiroFilter.setLoginUrl("/login");
        shiroFilter.setSuccessUrl("/admin");
        shiroFilter.setUnauthorizedUrl("/error?code=403");

        // 自定义过滤器
        Map<String, Filter> filtersMap = new LinkedHashMap<>();
        filtersMap.put("slf", new ShiroLoginFilter());
        shiroFilter.setFilters(filtersMap);

        // 拦截配置
        Map<String, String> filterChainDefinitions = new LinkedHashMap<>();
        filterChainDefinitions.put("/assets/**", "anon");
        filterChainDefinitions.put("/default/**", "anon");
        filterChainDefinitions.put("/favicon.ico", "anon");
        filterChainDefinitions.put("/file/**", "anon");
        filterChainDefinitions.put("/ads.txt", "anon");

        /**
         * 首页
         */
        filterChainDefinitions.put("/", "anon");
        filterChainDefinitions.put("/index", "anon");
        filterChainDefinitions.put("/article", "anon");
        filterChainDefinitions.put("/article/**", "anon");
        filterChainDefinitions.put("/getArticleList", "anon");
        filterChainDefinitions.put("/search", "anon");
        filterChainDefinitions.put("/search/order/**", "anon");
        filterChainDefinitions.put("/exportCards", "anon");
        filterChainDefinitions.put("/orders/orders/pageAll", "anon");
        filterChainDefinitions.put("/getProductSearchList", "anon");
        filterChainDefinitions.put("/getShoppingNotes", "anon");
        filterChainDefinitions.put("/file/enQrcode", "anon");
        filterChainDefinitions.put("/content/article/updateLike", "anon");

        /**
         * 商品
         */
        filterChainDefinitions.put("/product/**", "anon");
        filterChainDefinitions.put("/getProductById", "anon");
        filterChainDefinitions.put("/getProductList", "anon");

        // 订单创建
        filterChainDefinitions.put("/buy", "anon");

        // 支付
        filterChainDefinitions.put("/pay/**", "anon");
        filterChainDefinitions.put("/alipayPc/**", "anon");
        filterChainDefinitions.put("/order/state/**", "anon");
        filterChainDefinitions.put("/pay/state/**", "anon");

        // 异步通知
        filterChainDefinitions.put("/mqpay/notifyUrl", "anon");
        filterChainDefinitions.put("/mqpay/returnUrl", "anon");
        filterChainDefinitions.put("/epay/notifyUrl", "anon");
        filterChainDefinitions.put("/epay/returnUrl", "anon");
        filterChainDefinitions.put("/budpay/notifyUrl", "anon");
        filterChainDefinitions.put("/budpay/returnUrl", "anon");
        filterChainDefinitions.put("/xunhupay/notifyUrl", "anon");
        filterChainDefinitions.put("/xunhupay/returnUrl", "anon");
        filterChainDefinitions.put("/yungouos/notify", "anon");
        filterChainDefinitions.put("/payjs/notify", "anon");
        filterChainDefinitions.put("/wxpay/notify", "anon");
        filterChainDefinitions.put("/alipay/notify", "anon");
        filterChainDefinitions.put("/alipay/return_url", "anon");
        filterChainDefinitions.put("/paypal/cancel", "anon");
        filterChainDefinitions.put("/paypal/success", "anon");
        filterChainDefinitions.put("/epusdt/notifyUrl", "anon");
        filterChainDefinitions.put("/epusdt/returnUrl", "anon");
        filterChainDefinitions.put("/wxpusher/callback", "anon");

        // api
        filterChainDefinitions.put("/api/**", "anon");
        filterChainDefinitions.put("/error", "anon");
        filterChainDefinitions.put("/login", "anon");
        filterChainDefinitions.put("/logout", "logout");
        //filterChainDefinitions.put("/**", "slf,authc");
        filterChainDefinitions.put("/**", "slf,user");  // 记住密码也能访问
        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitions);
        return shiroFilter;
    }

    /**
     * userRealm
     */
    @Bean(name = "userRealm")
    @DependsOn("lifecycleBeanPostProcessor")
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();
        // 密码凭证器
        userRealm.setCredentialsMatcher(new HashedCredentialsMatcher("md5"));
        return userRealm;
    }

    /**
     * 安全管理器
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm());
        securityManager.setSessionManager(sessionManager());
        securityManager.setCacheManager(cacheManager());
        securityManager.setRememberMeManager(cookieRememberMeManager());
        return securityManager;
    }

    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    /**
     * 缓存管理器
     */
    @Bean(name = "cacheManager")
    public EhCacheManager cacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        net.sf.ehcache.CacheManager cm = net.sf.ehcache.CacheManager.getCacheManager("shirocache");
        if (cm == null) {
            String configFile = "classpath:ehcache/ehcache-shiro.xml";
            InputStream is = null;
            try {
                is = ResourceUtils.getInputStreamForPath(configFile);
                cm = new net.sf.ehcache.CacheManager(is);
            } catch (IOException e) {
                throw new IllegalStateException("Unable to obtain input stream for cacheManagerConfigFile [" + configFile + "]", e);
            } finally {
                ResourceUtils.close(is);
            }
        }
        cacheManager.setCacheManager(cm);
        return cacheManager;
    }

    /**
     * 记住密码cookie
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setMaxAge(60 * 60 * 24 * 7);  // 过期时间7天
        return simpleCookie;
    }

    /**
     * 记住密码cookie管理器
     */
    @Bean
    public CookieRememberMeManager cookieRememberMeManager() {
        CookieRememberMeManager manager = new CookieRememberMeManager();
        manager.setCookie(rememberMeCookie());
        manager.setCipherKey(Base64.decode("BT7lf0hw4W/QMxpS/Rb+Ng=="));
        return manager;
    }

    /**
     * 开启shiro注解功能
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        return advisor;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

}
