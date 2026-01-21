package org.jeecg.config.sign.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.JeecgBaseConfig;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 签名工具类
 * 
 * @author jeecg
 * @date 20210621
 */
@Slf4j
public class SignUtil {
    public static final String X_PATH_VARIABLE = "x-path-variable";

    /**
     * @param params
     *            所有的请求参数都会在这里进行排序加密
     * @return 验证签名结果
     */
    public static boolean verifySign(SortedMap<String, String> params,String headerSign) {
        if (params == null || StringUtils.isEmpty(headerSign)) {
            return false;
        }
        // 把参数加密
        String paramsSign = getParamsSign(params);
        log.debug("Param Sign : {}", paramsSign);
        return !StringUtils.isEmpty(paramsSign) && headerSign.equals(paramsSign);
    }

    /**
     * @param params
     *            所有的请求参数都会在这里进行排序加密
     * @return 得到签名
     */
    public static String getParamsSign(SortedMap<String, String> params) {
        //去掉 Url 里的时间戳
        params.remove("_t");
        String paramsJsonStr = JSONObject.toJSONString(params);
        log.debug("Param paramsJsonStr : {}", paramsJsonStr);
        //设置签名秘钥
        String signatureSecret = SignUtil.getSignatureSecret();
        try {
            //【issues/I484RW】2.4.6部署后，下拉搜索框提示“sign签名检验失败”
            return DigestUtils.md5DigestAsHex((paramsJsonStr + signatureSecret).getBytes("UTF-8")).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(),e);
            return null;
        }
    }

    /**
     * 通过前端签名算法生成签名
     *
     * @param url               请求的完整URL（包含查询参数）
     * @param requestParams     使用 @RequestParam 获取的参数集合
     * @param requestBodyParams 使用 @RequestBody 获取的参数集合
     * @return 计算得到的签名（大写MD5），若参数不足返回 null
     */
    public static String generateRequestSign(String url, Map<String, Object> requestParams, Map<String, Object> requestBodyParams) {
        if (oConvertUtils.isEmpty(url)) {
            return null;
        }
        // 解析URL上的查询参数与路径变量
        Map<String, String> urlParams = parseQueryString(url);
        // 合并URL参数与@RequestParam参数，确保数值和布尔类型转换为字符串
        Map<String, String> mergedParams = mergeObject(urlParams, requestParams);
        // 按需合并@RequestBody参数
        if (requestBodyParams != null && !requestBodyParams.isEmpty()) {
            mergedParams = mergeObject(mergedParams, requestBodyParams);
        }
        // 按键名升序排序，保持与前端一致的签名顺序
        SortedMap<String, String> sortedParams = new TreeMap<>(mergedParams);
        // 去除时间戳字段，避免参与签名
        sortedParams.remove("_t");
        // 序列化为JSON字符串
        String paramsJsonStr = JSONObject.toJSONString(sortedParams);
        // 读取签名秘钥
        String signatureSecret = getSignatureSecret();
        // 计算MD5摘要并转大写
        return DigestUtils.md5DigestAsHex((paramsJsonStr + signatureSecret).getBytes(StandardCharsets.UTF_8)).toUpperCase();
    }

    /**
     * 解析URL中的查询参数，并处理末尾逗号分隔的路径变量片段。
     *
     * @param url 请求的完整URL
     * @return 解析后的参数映射，数值与布尔类型均转换为字符串
     */
    private static Map<String, String> parseQueryString(String url) {
        Map<String, String> result = new HashMap<>(16);
        int fragmentIndex = url.indexOf('#');
        if (fragmentIndex >= 0) {
            url = url.substring(0, fragmentIndex);
        }
        int questionIndex = url.indexOf('?');
        String paramString = null;
        if (questionIndex >= 0 && questionIndex < url.length() - 1) {
            paramString = url.substring(questionIndex + 1);
        }
        // 处理路径变量末尾以逗号分隔的段，例如 /sys/dict/getDictItems/sys_user,realname,username
        int lastSlashIndex = url.lastIndexOf(SymbolConstant.SINGLE_SLASH);
        if (lastSlashIndex >= 0 && lastSlashIndex < url.length() - 1) {
            String lastPathVariable = url.substring(lastSlashIndex + 1);
            int qIndexInPath = lastPathVariable.indexOf('?');
            if (qIndexInPath >= 0) {
                lastPathVariable = lastPathVariable.substring(0, qIndexInPath);
            }
            if (lastPathVariable.contains(SymbolConstant.COMMA)) {
                String decodedPathVariable = URLDecoder.decode(lastPathVariable, StandardCharsets.UTF_8);
                result.put(X_PATH_VARIABLE, decodedPathVariable);
            }
        }
        if (oConvertUtils.isNotEmpty(paramString)) {
            String[] pairs = paramString.split(SymbolConstant.AND);
            for (String pair : pairs) {
                int equalIndex = pair.indexOf('=');
                if (equalIndex > 0 && equalIndex < pair.length() - 1) {
                    String key = pair.substring(0, equalIndex);
                    String value = pair.substring(equalIndex + 1);
                    // 解码并统一类型为字符串
                    String decodedKey = URLDecoder.decode(key, StandardCharsets.UTF_8);
                    String decodedValue = URLDecoder.decode(value, StandardCharsets.UTF_8);
                    result.put(decodedKey, decodedValue);
                }
            }
        }
        return result;
    }

    /**
     * 合并两个参数映射，并保证数值与布尔类型统一转为字符串。
     *
     * @param target 初始参数映射
     * @param source 待合并的参数映射
     * @return 合并后的新映射
     */
    private static Map<String, String> mergeObject(Map<String, String> target, Map<String, Object> source) {
        Map<String, String> merged = new HashMap<>(16);
        if (target != null && !target.isEmpty()) {
            merged.putAll(target);
        }
        if (source != null && !source.isEmpty()) {
            for (Map.Entry<String, Object> entry : source.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof Number) {
                    // 数值类型转字符串，保持前后端一致
                    merged.put(key, String.valueOf(value));
                } else if (value instanceof Boolean) {
                    // 布尔类型转字符串，保持前后端一致
                    merged.put(key, String.valueOf(value));
                } else if (value != null) {
                    merged.put(key, String.valueOf(value));
                }
            }
        }
        return merged;
    }

    /**
     * 读取并校验签名秘钥配置。
     *
     * @return 有效的签名秘钥
     */
    private static String getSignatureSecret() {
        JeecgBaseConfig jeecgBaseConfig = SpringContextUtils.getBean(JeecgBaseConfig.class);
        String signatureSecret = jeecgBaseConfig.getSignatureSecret();
        String curlyBracket = SymbolConstant.DOLLAR + SymbolConstant.LEFT_CURLY_BRACKET;
        if (oConvertUtils.isEmpty(signatureSecret) || signatureSecret.contains(curlyBracket)) {
            throw new JeecgBootException("签名密钥 ${jeecg.signatureSecret} 缺少配置 ！！");
        }
        return signatureSecret;
    }

}
