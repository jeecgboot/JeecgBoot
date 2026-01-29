package org.jeecg.modules.openapi.filter;

import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.modules.openapi.entity.OpenApi;
import org.jeecg.modules.openapi.entity.OpenApiAuth;
import org.jeecg.modules.openapi.entity.OpenApiPermission;
import org.jeecg.modules.openapi.service.OpenApiAuthService;
import org.jeecg.modules.openapi.service.OpenApiPermissionService;
import org.jeecg.modules.openapi.service.OpenApiService;
import org.jeecg.modules.openapi.service.OpenApiLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ApiAuthFilter单元测试
 * 测试OpenAPI的黑名单和白名单校验功能
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("API认证过滤器测试")
public class ApiAuthFilterTest {

    /**
     * 测试配置类
     */
    @Configuration
    static class TestConfig {
        
        @Bean
        public ApiAuthFilter apiAuthFilter() {
            return new ApiAuthFilter();
        }
        
        @Bean
        public TestableApiAuthFilter testableApiAuthFilter() {
            return new TestableApiAuthFilter();
        }
        
        @Bean
        public OpenApiService openApiService() {
            return mock(OpenApiService.class);
        }
        
        @Bean
        public OpenApiAuthService openApiAuthService() {
            return mock(OpenApiAuthService.class);
        }
        
        @Bean
        public OpenApiPermissionService openApiPermissionService() {
            return mock(OpenApiPermissionService.class);
        }
        
        @Bean
        public OpenApiLogService openApiLogService() {
            return mock(OpenApiLogService.class);
        }
    }

    /**
     * 子类暴露protected方法以便测试
     */
    static class TestableApiAuthFilter extends ApiAuthFilter {
        public void invokeCheckAccessList(OpenApi openApi, String ip) {
            super.checkAccessList(openApi, ip);
        }
        
        public void invokeCheckSignValid(String appkey, String signature, String timestamp) {
            super.checkSignValid(appkey, signature, timestamp);
        }
        
        public void invokeCheckSignature(String appKey, String signature, String timestamp, OpenApiAuth openApiAuth) {
            super.checkSignature(appKey, signature, timestamp, openApiAuth);
        }
        
        public void invokeCheckPermission(OpenApi openApi, OpenApiAuth openApiAuth) {
            super.checkPermission(openApi, openApiAuth);
        }
        
        public OpenApi invokeFindOpenApi(HttpServletRequest request) {
            return super.findOpenApi(request);
        }
        
        public String invokeMd5(String sourceStr) {
            return super.md5(sourceStr);
        }
    }

    @InjectMocks
    private TestableApiAuthFilter filter;
    
    @Mock
    private OpenApiService openApiService;
    
    @Mock
    private OpenApiAuthService openApiAuthService;
    
    @Mock
    private OpenApiPermissionService openApiPermissionService;
    
    @Mock
    private OpenApiLogService openApiLogService;

    @BeforeEach
    void setUp() {
        assertNotNull(filter, "ApiAuthFilter bean should be injected");
        assertNotNull(openApiService, "OpenApiService bean should be injected");
        assertNotNull(openApiAuthService, "OpenApiAuthService bean should be injected");
        assertNotNull(openApiPermissionService, "OpenApiPermissionService bean should be injected");
        assertNotNull(openApiLogService, "OpenApiLogService bean should be injected");
    }

    private OpenApi createOpenApi(String listMode, String allowedList) {
        OpenApi openApi = new OpenApi();
        openApi.setListMode(listMode.toUpperCase());
        openApi.setAllowedList(allowedList);
        return openApi;
    }

    // ==================== 白名单测试用例 ====================

    @Test
    @DisplayName("白名单模式 - 允许IP在白名单中")
    void testWhitelist_AllowedIpInList() {
        OpenApi openApi = createOpenApi("whitelist", "192.168.1.100,10.0.0.1");
        
        assertDoesNotThrow(() -> {
            filter.invokeCheckAccessList(openApi, "192.168.1.100");
        });
    }

    @Test
    @DisplayName("白名单模式 - 拒绝IP不在白名单中")
    void testWhitelist_DeniedIpNotInList() {
        OpenApi openApi = createOpenApi("whitelist", "192.168.1.100,10.0.0.1");
        
        JeecgBootException exception = assertThrows(JeecgBootException.class, () -> {
            filter.invokeCheckAccessList(openApi, "192.168.1.200");
        });
        
        assertTrue(exception.getMessage().contains("目标接口限制IP"));
    }

    @Test
    @DisplayName("白名单模式 - 允许IP段匹配")
    void testWhitelist_AllowedIpRangeMatch() {
        OpenApi openApi = createOpenApi("whitelist", "192.168.1.0/24,10.0.0.0/16");
        
        assertDoesNotThrow(() -> {
            filter.invokeCheckAccessList(openApi, "192.168.1.150");
        });
        
        assertDoesNotThrow(() -> {
            filter.invokeCheckAccessList(openApi, "10.0.5.100");
        });
    }

    @Test
    @DisplayName("白名单模式 - 拒绝IP段不匹配")
    void testWhitelist_DeniedIpRangeNotMatch() {
        OpenApi openApi = createOpenApi("whitelist", "192.168.1.0/24");
        
        JeecgBootException exception = assertThrows(JeecgBootException.class, () -> {
            filter.invokeCheckAccessList(openApi, "192.168.2.100");
        });
        
        assertTrue(exception.getMessage().contains("目标接口限制IP"));
    }

    // ==================== 黑名单测试用例 ====================

    @Test
    @DisplayName("黑名单模式 - 允许IP不在黑名单中")
    void testBlacklist_AllowedIpNotInList() {
        OpenApi openApi = createOpenApi("blacklist", "192.168.1.100,10.0.0.1");
        
        assertDoesNotThrow(() -> {
            filter.invokeCheckAccessList(openApi, "192.168.1.200");
        });
    }

    @Test
    @DisplayName("黑名单模式 - 拒绝IP在黑名单中")
    void testBlacklist_DeniedIpInList() {
        OpenApi openApi = createOpenApi("blacklist", "192.168.1.100,10.0.0.1");
        
        JeecgBootException exception = assertThrows(JeecgBootException.class, () -> {
            filter.invokeCheckAccessList(openApi, "192.168.1.100");
        });
        
        assertTrue(exception.getMessage().contains("目标接口限制IP"));
    }

    @Test
    @DisplayName("黑名单模式 - 拒绝IP段匹配")
    void testBlacklist_DeniedIpRangeMatch() {
        OpenApi openApi = createOpenApi("blacklist", "192.168.1.0/24,10.0.0.0/16");
        
        JeecgBootException exception1 = assertThrows(JeecgBootException.class, () -> {
            filter.invokeCheckAccessList(openApi, "192.168.1.150");
        });
        assertTrue(exception1.getMessage().contains("目标接口限制IP"));
        
        JeecgBootException exception2 = assertThrows(JeecgBootException.class, () -> {
            filter.invokeCheckAccessList(openApi, "10.0.5.100");
        });
        assertTrue(exception2.getMessage().contains("目标接口限制IP"));
    }

    @Test
    @DisplayName("黑名单模式 - 允许IP段不匹配")
    void testBlacklist_AllowedIpRangeNotMatch() {
        OpenApi openApi = createOpenApi("blacklist", "192.168.1.0/24");
        
        assertDoesNotThrow(() -> {
            filter.invokeCheckAccessList(openApi, "192.168.2.100");
        });
    }

    // ==================== 边界条件测试用例 ====================

    @Test
    @DisplayName("白名单模式 - 空访问列表拒绝访问")
    void testWhitelist_EmptyAccessList() {
        OpenApi openApi = createOpenApi("whitelist", "");
        
        JeecgBootException exception = assertThrows(JeecgBootException.class, () -> {
            filter.invokeCheckAccessList(openApi, "192.168.1.100");
        });
        
        assertTrue(exception.getMessage().contains("目标接口白名单为空"));
    }

    @Test
    @DisplayName("黑名单模式 - 空访问列表允许访问")
    void testBlacklist_EmptyAccessList() {
        OpenApi openApi = createOpenApi("blacklist", "");
        
        assertDoesNotThrow(() -> {
            filter.invokeCheckAccessList(openApi, "192.168.1.100");
        });
    }

    @Test
    @DisplayName("白名单模式 - null访问列表拒绝访问")
    void testWhitelist_NullAccessList() {
        OpenApi openApi = createOpenApi("whitelist", null);
        
        JeecgBootException exception = assertThrows(JeecgBootException.class, () -> {
            filter.invokeCheckAccessList(openApi, "192.168.1.100");
        });
        
        assertTrue(exception.getMessage().contains("目标接口白名单为空"));
    }

    @Test
    @DisplayName("黑名单模式 - null访问列表允许访问")
    void testBlacklist_NullAccessList() {
        OpenApi openApi = createOpenApi("blacklist", null);
        
        assertDoesNotThrow(() -> {
            filter.invokeCheckAccessList(openApi, "192.168.1.100");
        });
    }

    @Test
    @DisplayName("无效列表模式 - 默认白名单行为")
    void testInvalidListMode_DefaultWhitelistBehavior() {
        OpenApi openApi = createOpenApi("invalid_mode", "192.168.1.100");
        
        // 无效模式应该默认为白名单行为
        assertDoesNotThrow(() -> {
            filter.invokeCheckAccessList(openApi, "192.168.1.100");
        });
        
        JeecgBootException exception = assertThrows(JeecgBootException.class, () -> {
            filter.invokeCheckAccessList(openApi, "192.168.1.200");
        });
        
        assertTrue(exception.getMessage().contains("目标接口限制IP"));
    }

    // ==================== 复杂场景测试用例 ====================

    @Test
    @DisplayName("白名单模式 - 混合IP和CIDR格式")
    void testWhitelist_MixedIpAndCidrFormat() {
        OpenApi openApi = createOpenApi("whitelist", "192.168.1.100,192.168.2.0/24,10.0.0.1");
        
        // 测试精确IP匹配
        assertDoesNotThrow(() -> {
            filter.invokeCheckAccessList(openApi, "192.168.1.100");
        });
        
        // 测试CIDR网段匹配
        assertDoesNotThrow(() -> {
            filter.invokeCheckAccessList(openApi, "192.168.2.150");
        });
        
        // 测试另一个精确IP匹配
        assertDoesNotThrow(() -> {
            filter.invokeCheckAccessList(openApi, "10.0.0.1");
        });
        
        // 测试不在列表中的IP
        JeecgBootException exception = assertThrows(JeecgBootException.class, () -> {
            filter.invokeCheckAccessList(openApi, "192.168.1.200");
        });
        assertTrue(exception.getMessage().contains("目标接口限制IP"));
    }

    @Test
    @DisplayName("黑名单模式 - 混合IP和CIDR格式")
    void testBlacklist_MixedIpAndCidrFormat() {
        OpenApi openApi = createOpenApi("blacklist", "192.168.1.100,192.168.2.0/24,10.0.0.1");
        
        // 测试被精确IP拒绝
        JeecgBootException exception1 = assertThrows(JeecgBootException.class, () -> {
            filter.invokeCheckAccessList(openApi, "192.168.1.100");
        });
        assertTrue(exception1.getMessage().contains("目标接口限制IP"));
        
        // 测试被CIDR网段拒绝
        JeecgBootException exception2 = assertThrows(JeecgBootException.class, () -> {
            filter.invokeCheckAccessList(openApi, "192.168.2.150");
        });
        assertTrue(exception2.getMessage().contains("目标接口限制IP"));
        
        // 测试被另一个精确IP拒绝
        JeecgBootException exception3 = assertThrows(JeecgBootException.class, () -> {
            filter.invokeCheckAccessList(openApi, "10.0.0.1");
        });
        assertTrue(exception3.getMessage().contains("目标接口限制IP"));
        
        // 测试不在黑名单中的IP允许访问
        assertDoesNotThrow(() -> {
            filter.invokeCheckAccessList(openApi, "192.168.1.200");
        });
    }

    @Test
    @DisplayName("IP列表包含空格和换行符处理")
    void testIpListWithSpacesAndNewlines() {
        OpenApi openApi = createOpenApi("whitelist", " 192.168.1.100 , 10.0.0.1\n, 172.16.0.1 ");
        
        assertDoesNotThrow(() -> {
            filter.invokeCheckAccessList(openApi, "192.168.1.100");
        });
        
        assertDoesNotThrow(() -> {
            filter.invokeCheckAccessList(openApi, "10.0.0.1");
        });
        
        assertDoesNotThrow(() -> {
            filter.invokeCheckAccessList(openApi, "172.16.0.1");
        });
        
        JeecgBootException exception = assertThrows(JeecgBootException.class, () -> {
            filter.invokeCheckAccessList(openApi, "192.168.1.200");
        });
        assertTrue(exception.getMessage().contains("目标接口限制IP"));
    }

    @Test
    @DisplayName("极端CIDR网段测试")
    void testExtremeCidrRanges() {
        OpenApi openApi = createOpenApi("whitelist", "0.0.0.0/0");
        
        // 0.0.0.0/0 应该匹配所有IP
        assertDoesNotThrow(() -> {
            filter.invokeCheckAccessList(openApi, "192.168.1.100");
        });
        
        assertDoesNotThrow(() -> {
            filter.invokeCheckAccessList(openApi, "10.0.0.1");
        });
        
        assertDoesNotThrow(() -> {
            filter.invokeCheckAccessList(openApi, "255.255.255.255");
        });
    }

    @Test
    @DisplayName("本地回环地址测试")
    void testLoopbackAddressTest() {
        OpenApi openApi = createOpenApi("whitelist", "127.0.0.1");
        
        assertDoesNotThrow(() -> {
            filter.invokeCheckAccessList(openApi, "127.0.0.1");
        });
        
        JeecgBootException exception = assertThrows(JeecgBootException.class, () -> {
            filter.invokeCheckAccessList(openApi, "192.168.1.100");
        });
        assertTrue(exception.getMessage().contains("目标接口限制IP"));
    }

    // ==================== doFilter 集成测试用例 ====================

    @Test
    @DisplayName("doFilter - 正常流程测试")
    void testDoFilter_NormalFlow() throws Exception {
        // 准备测试数据
        String appKey = "test-app-key";
        String secretKey = "test-secret-key";
        long timestamp = System.currentTimeMillis();
        String signature = filter.invokeMd5(appKey + secretKey + timestamp);
        
        // 创建模拟对象
        HttpServletRequest request = mock(HttpServletRequest.class);
        ServletResponse response = mock(ServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        
        // 设置请求参数
        when(request.getRemoteAddr()).thenReturn("192.168.1.100");
        when(request.getHeader("appkey")).thenReturn(appKey);
        when(request.getHeader("signature")).thenReturn(signature);
        when(request.getHeader("timestamp")).thenReturn(String.valueOf(timestamp));
        when(request.getRequestURI()).thenReturn("/api/test");
        
        // 创建OpenApi对象
        OpenApi openApi = new OpenApi();
        openApi.setId("test-api-id");
        openApi.setListMode("WHITELIST");
        openApi.setAllowedList("192.168.1.100");
        when(openApiService.findByPath("test")).thenReturn(openApi);
        
        // 创建OpenApiAuth对象
        OpenApiAuth openApiAuth = new OpenApiAuth();
        openApiAuth.setId("test-auth-id");
        openApiAuth.setAk(appKey);
        openApiAuth.setSk(secretKey);
        when(openApiAuthService.getByAppkey(appKey)).thenReturn(openApiAuth);
        
        // 创建权限对象
        OpenApiPermission permission = new OpenApiPermission();
        permission.setApiId("test-api-id");
        permission.setApiAuthId("test-auth-id");
        when(openApiPermissionService.findByAuthId("test-auth-id")).thenReturn(Arrays.asList(permission));
        
        // 执行doFilter
        assertDoesNotThrow(() -> {
            filter.doFilter(request, response, filterChain);
        });
        
        // 验证filterChain被调用
        verify(filterChain, times(1)).doFilter(request, response);
        
        // 验证日志被保存
        verify(openApiLogService, times(1)).save(any());
    }

    @Test
    @DisplayName("doFilter - IP白名单验证失败")
    void testDoFilter_WhitelistValidationFailed() throws Exception {
        // 准备测试数据
        String appKey = "test-app-key";
        String secretKey = "test-secret-key";
        long timestamp = System.currentTimeMillis();
        String signature = filter.invokeMd5(appKey + secretKey + timestamp);
        
        // 创建模拟对象
        HttpServletRequest request = mock(HttpServletRequest.class);
        ServletResponse response = mock(ServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        
        // 设置请求参数 - IP不在白名单中
        when(request.getRemoteAddr()).thenReturn("192.168.1.200");
        when(request.getHeader("appkey")).thenReturn(appKey);
        when(request.getHeader("signature")).thenReturn(signature);
        when(request.getHeader("timestamp")).thenReturn(String.valueOf(timestamp));
        when(request.getRequestURI()).thenReturn("/api/test");
        
        // 创建OpenApi对象 - 白名单只包含192.168.1.100
        OpenApi openApi = new OpenApi();
        openApi.setId("test-api-id");
        openApi.setListMode("WHITELIST");
        openApi.setAllowedList("192.168.1.100");
        when(openApiService.findByPath("test")).thenReturn(openApi);
        
        // 执行doFilter，应该抛出异常
        JeecgBootException exception = assertThrows(JeecgBootException.class, () -> {
            filter.doFilter(request, response, filterChain);
        });
        
        assertTrue(exception.getMessage().contains("目标接口限制IP"));
        
        // 验证filterChain未被调用
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilter - 签名验证失败 - appkey为空")
    void testDoFilter_SignValidationFailed_AppKeyEmpty() throws Exception {
        // 创建模拟对象
        HttpServletRequest request = mock(HttpServletRequest.class);
        ServletResponse response = mock(ServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        
        // 设置请求参数 - appkey为空
        when(request.getRemoteAddr()).thenReturn("192.168.1.100");
        when(request.getHeader("appkey")).thenReturn(null);
        when(request.getHeader("signature")).thenReturn("test-signature");
        when(request.getHeader("timestamp")).thenReturn(String.valueOf(System.currentTimeMillis()));
        when(request.getRequestURI()).thenReturn("/api/test");
        
        // 创建OpenApi对象
        OpenApi openApi = new OpenApi();
        openApi.setId("test-api-id");
        openApi.setListMode("WHITELIST");
        openApi.setAllowedList("192.168.1.100");
        when(openApiService.findByPath("test")).thenReturn(openApi);
        
        // 执行doFilter，应该抛出异常
        JeecgBootException exception = assertThrows(JeecgBootException.class, () -> {
            filter.doFilter(request, response, filterChain);
        });
        
        assertTrue(exception.getMessage().contains("appkey为空"));
        
        // 验证filterChain未被调用
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilter - 签名验证失败 - signature为空")
    void testDoFilter_SignValidationFailed_SignatureEmpty() throws Exception {
        // 创建模拟对象
        HttpServletRequest request = mock(HttpServletRequest.class);
        ServletResponse response = mock(ServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        
        // 设置请求参数 - signature为空
        when(request.getRemoteAddr()).thenReturn("192.168.1.100");
        when(request.getHeader("appkey")).thenReturn("test-app-key");
        when(request.getHeader("signature")).thenReturn(null);
        when(request.getHeader("timestamp")).thenReturn(String.valueOf(System.currentTimeMillis()));
        when(request.getRequestURI()).thenReturn("/api/test");
        
        // 创建OpenApi对象
        OpenApi openApi = new OpenApi();
        openApi.setId("test-api-id");
        openApi.setListMode("WHITELIST");
        openApi.setAllowedList("192.168.1.100");
        when(openApiService.findByPath("test")).thenReturn(openApi);
        
        // 执行doFilter，应该抛出异常
        JeecgBootException exception = assertThrows(JeecgBootException.class, () -> {
            filter.doFilter(request, response, filterChain);
        });
        
        assertTrue(exception.getMessage().contains("signature为空"));
        
        // 验证filterChain未被调用
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilter - 签名验证失败 - timestamp为空")
    void testDoFilter_SignValidationFailed_TimestampEmpty() throws Exception {
        // 创建模拟对象
        HttpServletRequest request = mock(HttpServletRequest.class);
        ServletResponse response = mock(ServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        
        // 设置请求参数 - timestamp为空
        when(request.getRemoteAddr()).thenReturn("192.168.1.100");
        when(request.getHeader("appkey")).thenReturn("test-app-key");
        when(request.getHeader("signature")).thenReturn("test-signature");
        when(request.getHeader("timestamp")).thenReturn(null);
        when(request.getRequestURI()).thenReturn("/api/test");
        
        // 创建OpenApi对象
        OpenApi openApi = new OpenApi();
        openApi.setId("test-api-id");
        openApi.setListMode("WHITELIST");
        openApi.setAllowedList("192.168.1.100");
        when(openApiService.findByPath("test")).thenReturn(openApi);
        
        // 执行doFilter，应该抛出异常
        JeecgBootException exception = assertThrows(JeecgBootException.class, () -> {
            filter.doFilter(request, response, filterChain);
        });
        
        assertTrue(exception.getMessage().contains("timastamp时间戳为空"));
        
        // 验证filterChain未被调用
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilter - 签名验证失败 - timestamp过期")
    void testDoFilter_SignValidationFailed_TimestampExpired() throws Exception {
        // 创建模拟对象
        HttpServletRequest request = mock(HttpServletRequest.class);
        ServletResponse response = mock(ServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        
        // 设置请求参数 - timestamp过期（超过5分钟）
        long expiredTimestamp = System.currentTimeMillis() - 6 * 60 * 1000; // 6分钟前
        when(request.getRemoteAddr()).thenReturn("192.168.1.100");
        when(request.getHeader("appkey")).thenReturn("test-app-key");
        when(request.getHeader("signature")).thenReturn("test-signature");
        when(request.getHeader("timestamp")).thenReturn(String.valueOf(expiredTimestamp));
        when(request.getRequestURI()).thenReturn("/api/test");
        
        // 创建OpenApi对象
        OpenApi openApi = new OpenApi();
        openApi.setId("test-api-id");
        openApi.setListMode("WHITELIST");
        openApi.setAllowedList("192.168.1.100");
        when(openApiService.findByPath("test")).thenReturn(openApi);
        
        // 执行doFilter，应该抛出异常
        JeecgBootException exception = assertThrows(JeecgBootException.class, () -> {
            filter.doFilter(request, response, filterChain);
        });
        
        assertTrue(exception.getMessage().contains("signature签名已过期"));
        
        // 验证filterChain未被调用
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilter - 认证信息验证失败 - appkey不存在")
    void testDoFilter_AuthValidationFailed_AppKeyNotFound() throws Exception {
        // 准备测试数据
        String appKey = "non-existent-app-key";
        long timestamp = System.currentTimeMillis();
        
        // 创建模拟对象
        HttpServletRequest request = mock(HttpServletRequest.class);
        ServletResponse response = mock(ServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        
        // 设置请求参数
        when(request.getRemoteAddr()).thenReturn("192.168.1.100");
        when(request.getHeader("appkey")).thenReturn(appKey);
        when(request.getHeader("signature")).thenReturn("test-signature");
        when(request.getHeader("timestamp")).thenReturn(String.valueOf(timestamp));
        when(request.getRequestURI()).thenReturn("/api/test");
        
        // 创建OpenApi对象
        OpenApi openApi = new OpenApi();
        openApi.setId("test-api-id");
        openApi.setListMode("WHITELIST");
        openApi.setAllowedList("192.168.1.100");
        when(openApiService.findByPath("test")).thenReturn(openApi);
        
        // 模拟appkey不存在
        when(openApiAuthService.getByAppkey(appKey)).thenReturn(null);
        
        // 执行doFilter，应该抛出异常
        JeecgBootException exception = assertThrows(JeecgBootException.class, () -> {
            filter.doFilter(request, response, filterChain);
        });
        
        assertTrue(exception.getMessage().contains("不存在认证信息"));
        
        // 验证filterChain未被调用
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilter - 认证信息验证失败 - appkey错误")
    void testDoFilter_AuthValidationFailed_AppKeyMismatch() throws Exception {
        // 准备测试数据
        String requestAppKey = "test-app-key";
        String storedAppKey = "different-app-key";
        String secretKey = "test-secret-key";
        long timestamp = System.currentTimeMillis();
        String signature = filter.invokeMd5(requestAppKey + secretKey + timestamp);
        
        // 创建模拟对象
        HttpServletRequest request = mock(HttpServletRequest.class);
        ServletResponse response = mock(ServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        
        // 设置请求参数
        when(request.getRemoteAddr()).thenReturn("192.168.1.100");
        when(request.getHeader("appkey")).thenReturn(requestAppKey);
        when(request.getHeader("signature")).thenReturn(signature);
        when(request.getHeader("timestamp")).thenReturn(String.valueOf(timestamp));
        when(request.getRequestURI()).thenReturn("/api/test");
        
        // 创建OpenApi对象
        OpenApi openApi = new OpenApi();
        openApi.setId("test-api-id");
        openApi.setListMode("WHITELIST");
        openApi.setAllowedList("192.168.1.100");
        when(openApiService.findByPath("test")).thenReturn(openApi);
        
        // 创建OpenApiAuth对象 - 存储的appkey与请求的不匹配
        OpenApiAuth openApiAuth = new OpenApiAuth();
        openApiAuth.setId("test-auth-id");
        openApiAuth.setAk(storedAppKey); // 不同的appkey
        openApiAuth.setSk(secretKey);
        when(openApiAuthService.getByAppkey(requestAppKey)).thenReturn(openApiAuth);
        
        // 执行doFilter，应该抛出异常
        JeecgBootException exception = assertThrows(JeecgBootException.class, () -> {
            filter.doFilter(request, response, filterChain);
        });
        
        assertTrue(exception.getMessage().contains("appkey错误"));
        
        // 验证filterChain未被调用
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilter - 认证信息验证失败 - 签名错误")
    void testDoFilter_AuthValidationFailed_SignatureMismatch() throws Exception {
        // 准备测试数据
        String appKey = "test-app-key";
        String secretKey = "test-secret-key";
        long timestamp = System.currentTimeMillis();
        String wrongSignature = "wrong-signature";
        
        // 创建模拟对象
        HttpServletRequest request = mock(HttpServletRequest.class);
        ServletResponse response = mock(ServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        
        // 设置请求参数 - 错误的签名
        when(request.getRemoteAddr()).thenReturn("192.168.1.100");
        when(request.getHeader("appkey")).thenReturn(appKey);
        when(request.getHeader("signature")).thenReturn(wrongSignature);
        when(request.getHeader("timestamp")).thenReturn(String.valueOf(timestamp));
        when(request.getRequestURI()).thenReturn("/api/test");
        
        // 创建OpenApi对象
        OpenApi openApi = new OpenApi();
        openApi.setId("test-api-id");
        openApi.setListMode("WHITELIST");
        openApi.setAllowedList("192.168.1.100");
        when(openApiService.findByPath("test")).thenReturn(openApi);
        
        // 创建OpenApiAuth对象
        OpenApiAuth openApiAuth = new OpenApiAuth();
        openApiAuth.setId("test-auth-id");
        openApiAuth.setAk(appKey);
        openApiAuth.setSk(secretKey);
        when(openApiAuthService.getByAppkey(appKey)).thenReturn(openApiAuth);
        
        // 执行doFilter，应该抛出异常
        JeecgBootException exception = assertThrows(JeecgBootException.class, () -> {
            filter.doFilter(request, response, filterChain);
        });
        
        assertTrue(exception.getMessage().contains("signature签名错误"));
        
        // 验证filterChain未被调用
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilter - 权限验证失败 - 无权限访问接口")
    void testDoFilter_PermissionValidationFailed_NoPermission() throws Exception {
        // 准备测试数据
        String appKey = "test-app-key";
        String secretKey = "test-secret-key";
        long timestamp = System.currentTimeMillis();
        String signature = filter.invokeMd5(appKey + secretKey + timestamp);
        
        // 创建模拟对象
        HttpServletRequest request = mock(HttpServletRequest.class);
        ServletResponse response = mock(ServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        
        // 设置请求参数
        when(request.getRemoteAddr()).thenReturn("192.168.1.100");
        when(request.getHeader("appkey")).thenReturn(appKey);
        when(request.getHeader("signature")).thenReturn(signature);
        when(request.getHeader("timestamp")).thenReturn(String.valueOf(timestamp));
        when(request.getRequestURI()).thenReturn("/api/test");
        
        // 创建OpenApi对象
        OpenApi openApi = new OpenApi();
        openApi.setId("test-api-id");
        openApi.setListMode("WHITELIST");
        openApi.setAllowedList("192.168.1.100");
        when(openApiService.findByPath("test")).thenReturn(openApi);
        
        // 创建OpenApiAuth对象
        OpenApiAuth openApiAuth = new OpenApiAuth();
        openApiAuth.setId("test-auth-id");
        openApiAuth.setAk(appKey);
        openApiAuth.setSk(secretKey);
        when(openApiAuthService.getByAppkey(appKey)).thenReturn(openApiAuth);
        
        // 模拟无权限 - 返回空列表或不包含当前API权限的列表
        OpenApiPermission differentPermission = new OpenApiPermission();
        differentPermission.setApiId("different-api-id");
        differentPermission.setApiAuthId("test-auth-id");
        when(openApiPermissionService.findByAuthId("test-auth-id")).thenReturn(Arrays.asList(differentPermission));
        
        // 执行doFilter，应该抛出异常
        JeecgBootException exception = assertThrows(JeecgBootException.class, () -> {
            filter.doFilter(request, response, filterChain);
        });
        
        assertTrue(exception.getMessage().contains("该appKey未授权当前接口"));
        
        // 验证filterChain未被调用
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilter - 黑名单模式 - IP在黑名单中被拒绝")
    void testDoFilter_Blacklist_IpInBlacklist() throws Exception {
        // 准备测试数据
        String appKey = "test-app-key";
        String secretKey = "test-secret-key";
        long timestamp = System.currentTimeMillis();
        String signature = filter.invokeMd5(appKey + secretKey + timestamp);
        
        // 创建模拟对象
        HttpServletRequest request = mock(HttpServletRequest.class);
        ServletResponse response = mock(ServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        
        // 设置请求参数 - IP在黑名单中
        when(request.getRemoteAddr()).thenReturn("192.168.1.100");
        when(request.getHeader("appkey")).thenReturn(appKey);
        when(request.getHeader("signature")).thenReturn(signature);
        when(request.getHeader("timestamp")).thenReturn(String.valueOf(timestamp));
        when(request.getRequestURI()).thenReturn("/api/test");
        
        // 创建OpenApi对象 - 黑名单模式，包含该IP
        OpenApi openApi = new OpenApi();
        openApi.setId("test-api-id");
        openApi.setListMode("BLACKLIST");
        openApi.setAllowedList("192.168.1.100");
        when(openApiService.findByPath("test")).thenReturn(openApi);
        
        // 执行doFilter，应该抛出异常
        JeecgBootException exception = assertThrows(JeecgBootException.class, () -> {
            filter.doFilter(request, response, filterChain);
        });
        
        assertTrue(exception.getMessage().contains("目标接口限制IP"));
        
        // 验证filterChain未被调用
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("doFilter - 黑名单模式 - IP不在黑名单中允许访问")
    void testDoFilter_Blacklist_IpNotInBlacklist() throws Exception {
        // 准备测试数据
        String appKey = "test-app-key";
        String secretKey = "test-secret-key";
        long timestamp = System.currentTimeMillis();
        String signature = filter.invokeMd5(appKey + secretKey + timestamp);
        
        // 创建模拟对象
        HttpServletRequest request = mock(HttpServletRequest.class);
        ServletResponse response = mock(ServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);
        
        // 设置请求参数 - IP不在黑名单中
        when(request.getRemoteAddr()).thenReturn("192.168.1.200");
        when(request.getHeader("appkey")).thenReturn(appKey);
        when(request.getHeader("signature")).thenReturn(signature);
        when(request.getHeader("timestamp")).thenReturn(String.valueOf(timestamp));
        when(request.getRequestURI()).thenReturn("/api/test");
        
        // 创建OpenApi对象 - 黑名单模式，不包含该IP
        OpenApi openApi = new OpenApi();
        openApi.setId("test-api-id");
        openApi.setListMode("BLACKLIST");
        openApi.setAllowedList("192.168.1.100");
        when(openApiService.findByPath("test")).thenReturn(openApi);
        
        // 创建OpenApiAuth对象
        OpenApiAuth openApiAuth = new OpenApiAuth();
        openApiAuth.setId("test-auth-id");
        openApiAuth.setAk(appKey);
        openApiAuth.setSk(secretKey);
        when(openApiAuthService.getByAppkey(appKey)).thenReturn(openApiAuth);
        
        // 创建权限对象
        OpenApiPermission permission = new OpenApiPermission();
        permission.setApiId("test-api-id");
        permission.setApiAuthId("test-auth-id");
        when(openApiPermissionService.findByAuthId("test-auth-id")).thenReturn(Arrays.asList(permission));
        
        // 执行doFilter
        assertDoesNotThrow(() -> {
            filter.doFilter(request, response, filterChain);
        });
        
        // 验证filterChain被调用
        verify(filterChain, times(1)).doFilter(request, response);
        
        // 验证日志被保存
        verify(openApiLogService, times(1)).save(any());
    }
}