package org.jeecg.test.security;

import org.jeecg.common.constant.ServiceNameConstants;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.CommonUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 【issues/9695】OpenAPI 转发 SSRF 漏洞修复 (CWE-918) — 单元测试
 *
 * ── 漏洞是怎么引起的 ──────────────────────────────────────────────────────────
 * OpenApiController#call(/openapi/call/{path}) 是一个"服务端转发"接口：它读取数据库里
 * 配置的 originUrl，由【服务器】自己发起 restTemplate.exchange(...) 去请求该地址，并把
 * 内部用户的 X-Access-Token 一并带上。
 *
 * 当 originUrl 是相对路径（如 /house/list）时，需要补一个 baseUrl 拼成完整地址。
 * baseUrl 由 CommonUtils.getBaseUrl(request) 计算，而该方法为兼容微服务网关，会直接信任
 * 请求头 X_GATEWAY_BASE_PATH 作为 base。
 *
 * 问题就在这里：X_GATEWAY_BASE_PATH 是【客户端可控的请求头】。修复前，攻击者只要发：
 *     POST /openapi/call/xxx
 *     X_GATEWAY_BASE_PATH: http://169.254.169.254   (或 http://attacker.com)
 * 服务器就会把转发目标拼成 http://169.254.169.254/<originUrl>，并由服务器主动访问，
 * 从而探测云元数据 / 内网服务 / 把携带内部 token 的请求打到攻击者站点 —— 典型 SSRF。
 * 根因：把"客户端可控的 header"当成"可信的服务端 base 地址"使用。
 *
 * ── 修复 ──────────────────────────────────────────────────────────────────────
 * 1) CommonUtils.validateGatewayBasePath(header)：对 X_GATEWAY_BASE_PATH 做白名单校验——
 *    仅允许 http/https、禁止 userInfo（防 http://victim@evil 混淆）、host 不能为空，
 *    并【从解析后的 URI 组件重新拼接】返回，过滤掉注入字符；非法则返回 null 直接忽略该头。
 * 2) CommonUtils.checkInternalUrl(url)：relative-path 转发场景下，再校验解析出的 baseUrl
 *    host 必须是内网地址（回环/局域网/链路本地），解析到公网地址则抛 JeecgBootException。
 * SpringContextUtils.getDomain() 走同一套 validateGatewayBasePathForDomain 校验。
 *
 * ── 测试策略 ──────────────────────────────────────────────────────────────────
 * 校验逻辑都收敛在 CommonUtils 的 public static 方法里，可纯单测、无需 Spring / 无需联网
 * （全部使用 URI 解析与字面量 IP，不触发 DNS）。三组用例分别覆盖：
 *   一、入口复现：getBaseUrl 对恶意 X_GATEWAY_BASE_PATH 头不再信任；
 *   二、header 白名单：validateGatewayBasePath 的放行/拒绝边界；
 *   三、SSRF 兜底：checkInternalUrl 拦截公网地址、放行内网地址。
 *
 * @author wangshuai
 * @date 2026-06-17
 */
@ExtendWith(PrintTestResultExtension.class)
public class Issue9695_OpenApiSsrfHeaderInjectionTest {

    /** 构造带指定 X_GATEWAY_BASE_PATH 头的 mock 请求。 */
    private MockHttpServletRequest requestWithGatewayHeader(String headerValue) {
        MockHttpServletRequest req = new MockHttpServletRequest();
        // MockHttpServletRequest 默认 scheme=http, serverName=localhost, serverPort=80
        if (headerValue != null) {
            req.addHeader(ServiceNameConstants.X_GATEWAY_BASE_PATH, headerValue);
        }
        return req;
    }

    // ==================================================================
    // 一、入口复现：复刻 OpenApiController#call 对相对路径的两层防御
    //    第 1 层 getBaseUrl(...)        —— header 格式白名单（协议/userInfo/host）
    //    第 2 层 checkInternalUrl(base) —— 解析出的 baseUrl 必须是内网地址
    // 注意：单看 getBaseUrl 并不足以挡住 http://attacker.com（格式合法），
    //      真正挡住公网外联的是随后那一句 checkInternalUrl —— 两层缺一不可。
    // ==================================================================

    @Nested
    @DisplayName("漏洞入口 —— 复刻 controller 对相对路径转发的两层防御")
    class GetBaseUrlEntry {

        /** 复刻 OpenApiController#call 中相对路径分支：getBaseUrl 后再 checkInternalUrl。 */
        private String resolveBaseUrlAsController(String gatewayHeader) {
            String baseUrl = CommonUtils.getBaseUrl(requestWithGatewayHeader(gatewayHeader));
            CommonUtils.checkInternalUrl(baseUrl);   // controller 在拼接前的强制校验
            return baseUrl;
        }

        @Test
        @DisplayName("【漏洞复现】注入公网地址 http://attacker.com：格式校验放过，但被 checkInternalUrl 拦死")
        void maliciousPublicGatewayHeaderBlockedByInternalCheck() {
            // getBaseUrl 只做格式白名单，attacker.com 格式合法 → 会被原样返回
            String baseUrl = CommonUtils.getBaseUrl(requestWithGatewayHeader("http://attacker.com"));
            assertEquals("http://attacker.com", baseUrl,
                    "getBaseUrl 仅校验头部格式，不负责拦公网（这一层挡不住）");

            // 真正的 SSRF 防线是 controller 紧接着的 checkInternalUrl
            JeecgBootException ex = assertThrows(JeecgBootException.class,
                    () -> resolveBaseUrlAsController("http://attacker.com"));
            System.out.println("[maliciousPublicGatewayHeaderBlockedByInternalCheck] -> " + ex.getMessage());
            assertTrue(ex.getMessage().contains("内网"),
                    "controller 完整流程下，注入公网网关头最终被 checkInternalUrl 拦截");
        }

        @Test
        @DisplayName("【漏洞复现】带 userInfo 的混淆头(http://localhost@evil.com)在第 1 层就被忽略 → 回落本机")
        void userInfoObfuscationHeaderIsIgnored() {
            // userInfo 混淆地址连格式白名单都过不了 → validateGatewayBasePath 返回 null → 忽略该头
            String baseUrl = resolveBaseUrlAsController("http://localhost@evil.com");
            System.out.println("[userInfoObfuscationHeaderIsIgnored] 注入 http://localhost@evil.com -> baseUrl=" + baseUrl);

            assertFalse(baseUrl.contains("evil.com"), "userInfo 混淆地址不得被采纳");
            assertTrue(baseUrl.startsWith("http://localhost"), "非法头被忽略后回落到当前服务自身地址");
        }

        @Test
        @DisplayName("合法的内网网关头被采纳（保证微服务网关正常功能不被误伤）")
        void legitInternalGatewayHeaderIsAccepted() {
            String baseUrl = resolveBaseUrlAsController("http://127.0.0.1:8080/jeecg-boot");
            System.out.println("[legitInternalGatewayHeaderIsAccepted] baseUrl=" + baseUrl);

            assertEquals("http://127.0.0.1:8080/jeecg-boot", baseUrl,
                    "合法内网 http 网关头应通过两层校验并被规范化采纳");
        }
    }

    // ==================================================================
    // 二、header 白名单：validateGatewayBasePath 的放行/拒绝边界
    // ==================================================================

    @Nested
    @DisplayName("网关头白名单 —— validateGatewayBasePathForDomain")
    class GatewayHeaderWhitelist {

        @Test
        @DisplayName("合法 http/https 地址：从 URI 组件重新拼接后返回")
        void validHttpUrlPasses() {
            assertEquals("http://10.0.0.5:9999/jeecg-boot",
                    CommonUtils.validateGatewayBasePathForDomain("http://10.0.0.5:9999/jeecg-boot"));
            assertEquals("https://gateway.internal",
                    CommonUtils.validateGatewayBasePathForDomain("https://gateway.internal"));
        }

        @Test
        @DisplayName("非 http(s) 协议(file/gopher/...)一律拒绝 → 返回 null")
        void nonHttpSchemeRejected() {
            assertNull(CommonUtils.validateGatewayBasePathForDomain("file:///etc/passwd"));
            assertNull(CommonUtils.validateGatewayBasePathForDomain("gopher://127.0.0.1:6379/_xxx"));
            assertNull(CommonUtils.validateGatewayBasePathForDomain("ftp://host/x"));
        }

        @Test
        @DisplayName("含 userInfo 的混淆地址拒绝 → 返回 null")
        void userInfoRejected() {
            assertNull(CommonUtils.validateGatewayBasePathForDomain("http://trusted.com@evil.com/"));
        }

        @Test
        @DisplayName("空值 / 无 host / 非法字符 一律返回 null")
        void emptyOrMalformedReturnsNull() {
            assertNull(CommonUtils.validateGatewayBasePathForDomain(null));
            assertNull(CommonUtils.validateGatewayBasePathForDomain(""));
            assertNull(CommonUtils.validateGatewayBasePathForDomain("not a url"));
            assertNull(CommonUtils.validateGatewayBasePathForDomain("/only/path"));
        }
    }

    // ==================================================================
    // 二·补：网关头只接受纯路径 —— 拒绝查询串/片段/路径穿越
    //   漏洞：validateGatewayBasePath 原用 uri.getPath()（解码后）重建基址，未拒绝
    //   解码后出现的 ? / #，攻击者传 X_GATEWAY_BASE_PATH: http://127.0.0.1:8080/jeecg-boot%3f
    //   （%3f 解码为 ?）即可篡改转发目标。修复：拒绝 rawQuery/rawFragment，且 path 走字符白名单、禁止 ..
    // ==================================================================

    @Nested
    @DisplayName("网关头纯路径校验 —— 拒绝查询串/片段/路径穿越")
    class GatewayHeaderPathOnly {

        @Test
        @DisplayName("【漏洞复现】编码问号 %3f 篡改转发目标 → 被拒绝返回 null")
        void encodedQuestionMarkRejected() {
            // 实测利用载荷：%3f 解码为 ?，原逻辑会把基址变为 http://127.0.0.1:8080/jeecg-boot? 打到上下文根
            assertNull(CommonUtils.validateGatewayBasePathForDomain("http://127.0.0.1:8080/jeecg-boot%3f"),
                    "解码后路径含 ? 必须被拒绝");
            assertNull(CommonUtils.validateGatewayBasePathForDomain("http://127.0.0.1:8080/jeecg-boot%23frag"),
                    "解码后路径含 # 必须被拒绝");
        }

        @Test
        @DisplayName("字面量查询串 ? 与片段 # 被拒绝 → 返回 null")
        void rawQueryAndFragmentRejected() {
            assertNull(CommonUtils.validateGatewayBasePathForDomain("http://127.0.0.1:8080/jeecg-boot?x=1"));
            assertNull(CommonUtils.validateGatewayBasePathForDomain("http://127.0.0.1:8080/jeecg-boot#frag"));
        }

        @Test
        @DisplayName("路径穿越 .. 被拒绝 → 返回 null")
        void pathTraversalRejected() {
            assertNull(CommonUtils.validateGatewayBasePathForDomain("http://127.0.0.1:8080/jeecg-boot/../actuator"));
        }

        @Test
        @DisplayName("正常纯路径基址仍被采纳（不误伤合法网关头）")
        void plainPathStillAccepted() {
            assertEquals("http://127.0.0.1:8080/jeecg-boot",
                    CommonUtils.validateGatewayBasePathForDomain("http://127.0.0.1:8080/jeecg-boot"));
            assertEquals("http://10.0.0.5:9999/jeecg-boot",
                    CommonUtils.validateGatewayBasePathForDomain("http://10.0.0.5:9999/jeecg-boot"));
        }
    }

    // ==================================================================
    // 三、SSRF 兜底：checkInternalUrl 仅放行内网地址
    // ==================================================================

    @Nested
    @DisplayName("SSRF 兜底 —— checkInternalUrl 仅允许内网地址")
    class CheckInternalUrl {

        @Test
        @DisplayName("内网地址（回环/局域网）放行，不抛异常")
        void internalAddressesPass() {
            // 字面量 IP，不触发 DNS，离线可重复执行
            assertDoesNotThrow(() -> CommonUtils.checkInternalUrl("http://127.0.0.1:8080/jeecg-boot"));
            assertDoesNotThrow(() -> CommonUtils.checkInternalUrl("http://10.0.0.8/api"));
            assertDoesNotThrow(() -> CommonUtils.checkInternalUrl("http://192.168.1.20:9999/x"));
        }

        @Test
        @DisplayName("【漏洞复现】公网地址被拦截 → 抛 JeecgBootException，堵死 SSRF 外联")
        void publicAddressIsBlocked() {
            JeecgBootException ex = assertThrows(JeecgBootException.class,
                    () -> CommonUtils.checkInternalUrl("http://8.8.8.8/latest/meta-data/"));
            System.out.println("[publicAddressIsBlocked] 公网 8.8.8.8 被拦截 -> " + ex.getMessage());
            assertTrue(ex.getMessage().contains("内网"), "应明确提示仅允许内网地址");
        }

        @Test
        @DisplayName("host 为空的非法 URL 被拦截")
        void emptyHostBlocked() {
            assertThrows(JeecgBootException.class, () -> CommonUtils.checkInternalUrl("/no/host/url"));
        }
    }
}
