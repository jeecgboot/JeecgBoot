package org.jeecg.modules.system.util;

import org.springframework.web.util.HtmlUtils;

import java.util.regex.Pattern;

/**
 * @Description: 工具类XSSUtils，现在的做法是替换成空字符，CSDN的是进行转义，比如文字开头的"<"转成&lt;
 * @author: lsq
 * @date: 2021年07月26日 19:13
 */
public class XssUtils {

    private static Pattern[] patterns = new Pattern[]{
        //Script fragments
        Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
        //src='...'
        Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        //script tags
        Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
        Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        //eval(...)
        Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        //expression(...)
        Pattern.compile("e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        //javascript:...
        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
        //vbscript:...
        Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
        //onload(...)=...
        Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    };

    //update-begin---author:liusq ---date:2025-04-13  for：【issues/9521】富文本msgContent字段存储型XSS过滤-----------
    /**
     * 针对富文本HTML内容的XSS过滤：移除危险脚本和事件处理器，保留合法HTML标签和样式。
     * 与 scriptXss() 的区别：本方法不对HTML实体进行全局转义，适用于富文本内容（如公告正文）。
     */
    private static final Pattern[] RICH_TEXT_PATTERNS = new Pattern[]{
        // <script> 标签及其内容
        Pattern.compile("<script[^>]*>[\\s\\S]*?</script>", Pattern.CASE_INSENSITIVE),
        // 自闭合 <script/>
        Pattern.compile("<script[^>]*/>", Pattern.CASE_INSENSITIVE),
        //update-begin---author:wangshuai ---date:2026-10-21  for：事件属性前不一定是空白（"/"分隔、紧跟引号），属性名里可能夹带实体编码-----------
        // 所有内联事件处理器属性，如 onerror= onclick= onmouseover= 等
        // 前面可以是空白，也可以紧跟在 "/" 或引号后面（<img/src="x"/onerror=…>、<img src="x"onerror=…>）；名称里允许 & # ; 以匹配 on&#101;rror=
        Pattern.compile("(?:\\s+|(?<=[/\"']))on[\\w&#;-]+\\s*=\\s*(?:\"[^\"]*\"|'[^']*'|(?:[^\\s>]*[^\\s>\"'])?)", Pattern.CASE_INSENSITIVE),
        //update-end---author:wangshuai ---date:2026-10-21  for：事件属性前不一定是空白（"/"分隔、紧跟引号），属性名里可能夹带实体编码-----------
        // javascript: 协议
        Pattern.compile("javascript\\s*:", Pattern.CASE_INSENSITIVE),
        // vbscript: 协议
        Pattern.compile("vbscript\\s*:", Pattern.CASE_INSENSITIVE),
        // CSS expression()（IE 特有）
        Pattern.compile("expression\\s*\\(", Pattern.CASE_INSENSITIVE),
        // <iframe> 标签（防止内嵌恶意页面）
        Pattern.compile("<iframe[^>]*>[\\s\\S]*?</iframe>", Pattern.CASE_INSENSITIVE),
        Pattern.compile("<iframe[^>]*/>", Pattern.CASE_INSENSITIVE),
        // <object> / <embed> 标签
        Pattern.compile("<object[^>]*>[\\s\\S]*?</object>", Pattern.CASE_INSENSITIVE),
        Pattern.compile("<embed[^>]*>", Pattern.CASE_INSENSITIVE),
    };

    public static String richTextXss(String value) {
        if (value == null) {
            return null;
        }
        for (Pattern pattern : RICH_TEXT_PATTERNS) {
            value = pattern.matcher(value).replaceAll("");
        }
        return value;
    }
    //update-end---author:liusq ---date:2025-04-13  for：【issues/9521】富文本msgContent字段存储型XSS过滤-----------

    public static String scriptXss(String value) {
        if (value != null) {
            value = value.replaceAll(" ", "");
            for(Pattern scriptPattern: patterns){
                value = scriptPattern.matcher(value).replaceAll("");
            }
        }
        return HtmlUtils.htmlEscape(value);
    }

    public static void main(String[] args) {
        String s = scriptXss("<img  src=x onload=alert(111).*?><script></script>javascript:eval()\\\\.");
        System.err.println("s======>" + s);
    }
}
