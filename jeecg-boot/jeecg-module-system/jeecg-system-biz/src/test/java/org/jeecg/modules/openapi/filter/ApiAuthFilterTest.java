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
    }

    /**
     * 子类暴露protected方法以便测试
     */
    static class TestableApiAuthFilter extends ApiAuthFilter {
        public void invokeCheckAccessList(OpenApi openApi, String ip) {
            super.checkAccessList(openApi, ip);
        }
    }

    @Autowired
    private TestableApiAuthFilter filter;

    @BeforeEach
    void setUp() {
        assertNotNull(filter, "ApiAuthFilter bean should be injected");
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
}