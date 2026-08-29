package org.jeecg.modules.airag.llm.handler;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 插件请求头环境变量解析测试。
 *
 * @author kriptoburak
 * @since 2026-08-24 【issues/9846】插件请求头支持环境变量
 */
class PluginHeaderValueResolverTest {

    //update-begin---author:kriptoburak---date:20260824---for:【issues/9846】插件请求头支持环境变量---
    @Test
    void shouldResolveEnvironmentVariablesWithoutChangingInput() {
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("x-api-key", "${JEECG_PLUGIN_XQUIK_API_KEY}");
        headers.put("Authorization", "Bearer ${JEECG_PLUGIN_XQUIK_API_KEY}");
        headers.put("Accept", "application/json");

        Map<String, String> resolved = PluginHeaderValueResolver.resolve(
                headers,
                name -> "JEECG_PLUGIN_XQUIK_API_KEY".equals(name) ? "test$key\\suffix" : null
        );

        assertEquals("test$key\\suffix", resolved.get("x-api-key"));
        assertEquals("Bearer test$key\\suffix", resolved.get("Authorization"));
        assertEquals("application/json", resolved.get("Accept"));
        assertEquals("${JEECG_PLUGIN_XQUIK_API_KEY}", headers.get("x-api-key"));
    }

    @Test
    void shouldRejectMissingEnvironmentVariable() {
        Map<String, String> headers = Map.of("x-api-key", "${JEECG_PLUGIN_XQUIK_API_KEY}");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> PluginHeaderValueResolver.resolve(headers, name -> null)
        );

        assertEquals("缺少插件请求头环境变量：JEECG_PLUGIN_XQUIK_API_KEY。请先配置环境变量。", exception.getMessage());
    }

    @Test
    void shouldRejectEnvironmentVariablesOutsidePluginNamespace() {
        Map<String, String> headers = Map.of("x-api-key", "${DATABASE_PASSWORD}");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> PluginHeaderValueResolver.resolve(headers, name -> {
                    throw new AssertionError("不应读取未授权的环境变量");
                })
        );

        assertEquals(
                "插件请求头只能引用 JEECG_PLUGIN_ 前缀的环境变量：DATABASE_PASSWORD",
                exception.getMessage()
        );
    }

    @Test
    void shouldReturnEmptyHeadersForNullInput() {
        assertEquals(Map.of(), PluginHeaderValueResolver.resolve(null, name -> "unused"));
    }
    //update-end---author:kriptoburak---date:20260824---for:【issues/9846】插件请求头支持环境变量---
}
