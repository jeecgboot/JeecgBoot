package org.jeecg.common.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.HtmlUtils;

/**
 * HTML 工具类
 */
public class HTMLUtils {

    /**
     * 获取HTML内的文本，不包含标签
     *
     * @param html HTML 代码
     */
    public static String getInnerText(String html) {
        if (StringUtils.isNotBlank(html)) {
            //去掉 html 的标签
            String content = html.replaceAll("</?[^>]+>", "");
            // 将多个空格合并成一个空格
            content = content.replaceAll("(&nbsp;)+", "&nbsp;");
            // 反向转义字符
            content = HtmlUtils.htmlUnescape(content);
            return content.trim();
        }
        return "";
    }

}
