package org.jeecg.config;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtil {
    //匹配中文
    private static final Pattern chinesePat = Pattern.compile("[\u4e00-\u9fa5]");
    //匹配英文名
    private static final Pattern englishPat = Pattern.compile("[A-Za-z]+\\s+[A-Za-z]+");
    //匹配script
    private static final Pattern scriptPat = Pattern.compile("]*?>[\\s\\S]*?<\\/script>", Pattern.CASE_INSENSITIVE);
    //匹配style
    private static final Pattern stylePat = Pattern.compile("]*?>[\\s\\S]*?<\\/style>", Pattern.CASE_INSENSITIVE);
    //匹配html
    private static final Pattern htmlPat = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
    //匹配域名
    private static final Pattern domainPat = Pattern.compile("(?<=://)([-\\w]+\\.)+[a-zA-Z]{2,6}");
    //匹配链接
    private static final Pattern urlPat = Pattern.compile("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]", Pattern.CASE_INSENSITIVE);
    //匹配括号内容
    private static final Pattern bracketPat = Pattern.compile("\\《(.*?)\\》|\\【(.*?)\\】");

    /**
     * key:网盘域名
     * value:网盘名称
     * eg:<www.aliyundrive.com,阿里云盘>
     */
    private static final Map<String, String> typeMap = new HashMap<>();

    static {
        typeMap.put("pan.quark.cn", "夸克网盘");
        typeMap.put("www.alipan.com", "阿里云盘");
        typeMap.put("www.aliyundrive.com", "阿里云盘");
        typeMap.put("pan.baidu.com", "百度网盘");
        typeMap.put("115.com", "115");
        typeMap.put("www.123pan.com", "123云盘");
        typeMap.put("yun.139.com", "移动云盘");
        typeMap.put("cloud.189.cn", "天翼云盘");
        typeMap.put("eva.lanzoue.com", "蓝奏云");
        typeMap.put("drive.google.com", "谷歌硬盘");
    }

    /**
     * 获取网盘名称（默认存储路径）
     *
     * @param links 分享链接
     * @return
     */
    public static String getDriverNameByUrl(String links) {
        String type = "其它";
        Matcher m = domainPat.matcher(links);
        if (m.find()) {
            type = m.group();
        }
        if (typeMap.containsKey(type)) {
            type = typeMap.get(type);
        }
        return type;
    }

    /**
     * 删除html标签
     *
     * @param name
     * @return
     */
    public static String delHTMLTag(String name) {
        if (name.isEmpty()) {
            return name;
        }
        Matcher m_script = scriptPat.matcher(name);
        name = m_script.replaceAll("");

        Matcher m_style = stylePat.matcher(name);
        name = m_style.replaceAll("");

        Matcher m_html = htmlPat.matcher(name);
        name = m_html.replaceAll("");
        return name.trim();
    }

    /**
     * 获取链接
     *
     * @param text
     * @return
     */
    private static String getUrl(String text) {
        if (text.isEmpty()) {
            return text;
        }
        Matcher m = urlPat.matcher(text);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

    private static boolean isShareLink(String links) {
        for (Map.Entry<String, String> map : typeMap.entrySet()) {
            if (links.contains(map.getKey())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取分享链接（不带密码）
     *
     * @param str
     * @return
     */
    public static String getShareLink(String str) {
        String url = getUrl(str);
        if (isShareLink(url)) {
            if (url.contains("?pwd=")) {
                url = url.substring(0, url.indexOf("?pwd="));
            } else if (url.contains("?password=")) {
                url = url.substring(0, url.indexOf("?password="));
            } else if (url.endsWith("#")) {
                url = url.substring(0, url.length() - 1);
            }
            return url;
        }
        return "";
    }

    /**
     * 名称校验
     *
     * @param str
     * @return
     */
    public static boolean validShareName(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        Matcher m = chinesePat.matcher(str);
        if (m.find()) {
            return true;
        }
        m = englishPat.matcher(str);
        return m.find();
    }

    /**
     * 获取书名号内容
     *
     * @param name
     * @return
     */
    public static String getbracketContent(String name) {
        if (name.isEmpty()) {
            return name;
        }
        Matcher m = bracketPat.matcher(name);
        String result = "";
        while (m.find()) {
            result += m.group();
        }
        return result;
    }
}
