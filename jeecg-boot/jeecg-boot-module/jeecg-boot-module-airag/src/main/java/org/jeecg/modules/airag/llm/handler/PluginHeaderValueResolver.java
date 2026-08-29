package org.jeecg.modules.airag.llm.handler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 插件请求头环境变量解析器。
 *
 * @author kriptoburak
 * @since 2026-08-24 【issues/9846】插件请求头支持环境变量
 */
final class PluginHeaderValueResolver {

    //update-begin---author:kriptoburak---date:20260824---for:【issues/9846】插件请求头支持环境变量---
    private static final Pattern ENV_PLACEHOLDER = Pattern.compile("\\$\\{([A-Za-z_][A-Za-z0-9_]*)}");
    private static final String ALLOWED_PREFIX = "JEECG_PLUGIN_";

    private PluginHeaderValueResolver() {
    }

    static Map<String, String> resolve(Map<String, String> headers) {
        return resolve(headers, System::getenv);
    }

    static Map<String, String> resolve(Map<String, String> headers, Function<String, String> environment) {
        Map<String, String> resolvedHeaders = new LinkedHashMap<>();
        if (headers == null || headers.isEmpty()) {
            return resolvedHeaders;
        }

        headers.forEach((name, value) -> resolvedHeaders.put(name, resolveValue(value, environment)));
        return resolvedHeaders;
    }

    private static String resolveValue(String value, Function<String, String> environment) {
        if (value == null) {
            return null;
        }

        Matcher matcher = ENV_PLACEHOLDER.matcher(value);
        StringBuffer resolvedValue = new StringBuffer();
        while (matcher.find()) {
            String variableName = matcher.group(1);
            if (!variableName.startsWith(ALLOWED_PREFIX)) {
                throw new IllegalArgumentException(
                        "插件请求头只能引用 JEECG_PLUGIN_ 前缀的环境变量：" + variableName
                );
            }
            String variableValue = environment.apply(variableName);
            if (variableValue == null || variableValue.isBlank()) {
                throw new IllegalArgumentException(
                        "缺少插件请求头环境变量：" + variableName + "。请先配置环境变量。"
                );
            }
            matcher.appendReplacement(resolvedValue, Matcher.quoteReplacement(variableValue));
        }
        matcher.appendTail(resolvedValue);
        return resolvedValue.toString();
    }
    //update-end---author:kriptoburak---date:20260824---for:【issues/9846】插件请求头支持环境变量---
}
