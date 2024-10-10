package org.jeecg.common.util;

import org.apache.commons.lang3.StringUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
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
        //update-begin---author:wangshuai---date:2024-06-26---for:【TV360X-1344】JDK17 邮箱发送失败，需要换写法---
        /*PegDownProcessor pdp = new PegDownProcessor();
        return pdp.markdownToHtml(markdownContent);*/
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdownContent);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
        //update-end---author:wangshuai---date:2024-06-26---for:【TV360X-1344】JDK17 邮箱发送失败，需要换写法---
    }

}
