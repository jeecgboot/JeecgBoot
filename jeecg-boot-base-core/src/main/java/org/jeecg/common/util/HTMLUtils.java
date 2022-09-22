package org.jeecg.common.util;

import org.apache.commons.lang3.StringUtils;
import org.pegdown.PegDownProcessor;
import org.springframework.web.util.HtmlUtils;

/**
 * HTML 工具类
 * @author: jeecg-boot
 * @date: 2022/3/30 14:43
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
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

    /**
     * 将Markdown解析成Html
     * @param markdownContent
     * @return
     */
    public static String parseMarkdown(String markdownContent) {
        PegDownProcessor pdp = new PegDownProcessor();
        return pdp.markdownToHtml(markdownContent);
    }

}
