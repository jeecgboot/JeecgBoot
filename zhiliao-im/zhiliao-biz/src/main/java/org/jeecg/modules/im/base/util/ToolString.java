package org.jeecg.modules.im.base.util;

import com.sigpwned.emoji4j.core.GraphemeMatcher;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.EmojiParser;
import emoji4j.EmojiUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理常用方法
 */
@Slf4j
public abstract class ToolString {

    public final static Pattern pattern_mac = Pattern.compile("^(([a-f0-9]{2}:)|([a-f0-9]{2}-)){5}[a-f0-9]{2}$");

    public final static Pattern pattern_hour_minute = Pattern.compile("^(([0-1]?\\d)|(2[0-3])):[0-5]?\\d$");
    /**
     * 常用正则表达式：匹配非负整数（正整数 + 0）
     */
    public final static Pattern pattern_integer_1 = Pattern.compile("^\\d+$");

    /**
     * 常用正则表达式：匹配正整数
     */
    public final static Pattern pattern_integer_2 = Pattern.compile("^[0-9]*[1-9][0-9]*$");

    /**
     * 常用正则表达式：匹配非正整数（负整数  + 0）
     */
    public final static Pattern pattern_integer_3 = Pattern.compile("^((-\\d+) ?(0+))$");

    /**
     * 常用正则表达式：匹配负整数
     */
    public final static Pattern pattern_integer_4 = Pattern.compile("^-[0-9]*[1-9][0-9]*$");

    /**
     * 常用正则表达式：匹配整数
     */
    public final static Pattern pattern_integer_5 = Pattern.compile("^-?\\d+$");

    /**
     * 常用正则表达式：匹配非负浮点数（正浮点数 + 0）
     */
    public final static Pattern pattern_float_1 = Pattern.compile("^\\d+(\\.\\d+)?$");

    /**
     * 常用正则表达式：匹配正浮点数
     */
    public final static Pattern pattern_float_2 = Pattern.compile("^(([0-9]+\\.[0-9]*[1-9][0-9]*) ?([0-9]*[1-9][0-9]*\\.[0-9]+) ?([0-9]*[1-9][0-9]*))$");

    /**
     * 常用正则表达式：匹配非正浮点数（负浮点数 + 0）
     */
    public final static Pattern pattern_float_3 = Pattern.compile("^((-\\d+(\\.\\d+)?) ?(0+(\\.0+)?))$");

    /**
     * 常用正则表达式：匹配负浮点数
     */
    public final static Pattern pattern_float_4 = Pattern.compile("^(-(([0-9]+\\.[0-9]*[1-9][0-9]*) ?([0-9]*[1-9][0-9]*\\.[0-9]+) ?([0-9]*[1-9][0-9]*)))$");

    /**
     * 常用正则表达式：匹配浮点数
     */
    public final static Pattern pattern_float_5 = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");

    public final static Pattern pattern_money = Pattern.compile("(^[1-9]([0-9]+)?(\\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\\.[0-9]([0-9])?$)");

    /**
     * 常用正则表达式：匹配由26个英文字母组成的字符串
     */
    public final static Pattern pattern_letter_1 = Pattern.compile("^[A-Za-z]+$");

    /**
     * 常用正则表达式：匹配由26个英文字母的大写组成的字符串
     */
    public final static Pattern pattern_letter_2 = Pattern.compile("^[A-Z]+$");

    /**
     * 常用正则表达式：匹配由26个英文字母的小写组成的字符串
     */
    public final static Pattern pattern_letter_3 = Pattern.compile("^[a-z]+$");

    /**
     * 常用正则表达式：匹配由数字、26个英文字母组成的字符串
     */
    public final static Pattern pattern_letter_4 = Pattern.compile("^[A-Za-z0-9]+$");

    /**
     * 常用正则表达式：匹配由数字、26个英文字母、下划线组成的字符串
     */
    public final static Pattern pattern_letter_5 = Pattern.compile("^\\w+$");

    /**
     * 常用正则表达式：匹配由数字、26个英文字母、下划线、中划线、点组成的字符串
     */
    public final static Pattern pattern_letter_6 = Pattern.compile("^([a-z_A-Z-.+0-9]+)$");

    /**
     * 常用正则表达式：匹配email地址
     */
    public final static Pattern pattern_email = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");

    /**
     * 常用正则表达式：匹配url
     */
    public final static Pattern pattern_url_1 = Pattern.compile("^[a-zA-z]+://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$");

    /**
     * 常用正则表达式：匹配url
     */
    public final static Pattern pattern_url_2 = Pattern.compile("[a-zA-z]+://[^\\s]*");

    /**
     * 常用正则表达式：匹配中文字符
     */
    public final static Pattern pattern_chinese_1 = Pattern.compile("[\\u4e00-\\u9fa5]");

    /**
     * 常用正则表达式：匹配双字节字符(包括汉字在内)
     */
    public final static Pattern pattern_chinese_2 = Pattern.compile("[^\\x00-\\xff]");

    /**
     * 常用正则表达式：匹配空行
     */
    public final static Pattern pattern_line = Pattern.compile("\\n[\\s ? ]*\\r");

    /**
     * 常用正则表达式：匹配HTML标记
     */
    public final static Pattern pattern_html_1 = Pattern.compile("/ <(.*)>.* <\\/\\1> ? <(.*) \\/>/");

    /**
     * 常用正则表达式：匹配首尾空格
     */
    public final static Pattern pattern_startEndEmpty = Pattern.compile("(^\\s*) ?(\\s*$)");

    /**
     * 常用正则表达式：匹配用户名是否合法(字母开头，允许5-20字节，允许字母数字下划线)
     */
    public final static Pattern pattern_username = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{4,19}$");

    /**
     * 常用正则表达式：匹配国内电话号码，匹配形式如 0511-4405222 或 021-87888822
     */
    public final static Pattern pattern_telephone = Pattern.compile("\\d{3}-\\d{8} ?\\d{4}-\\d{7}");

    /**
     * 常用正则表达式：腾讯QQ号, 腾讯QQ号从10000开始
     */
    public final static Pattern pattern_qq = Pattern.compile("[1-9][0-9]{4,}");

    /**
     * 常用正则表达式：匹配中国邮政编码
     */
    public final static Pattern pattern_postbody = Pattern.compile("[1-9]\\d{5}(?!\\d)");

    /**
     * 常用正则表达式：匹配身份证, 中国的身份证为15位或18位
     */
    public final static Pattern pattern_idCard = Pattern.compile("\\d{15} ?\\d{18}");

    /**
     * 常用正则表达式：IP
     */
    public final static Pattern pattern_ip = Pattern.compile("\\d+\\.\\d+\\.\\d+\\.\\d+");

    /**
     * 常用正则表达式：手机号
     */
    public final static Pattern pattern_mobile = Pattern.compile("^((13[0-9])|(14[57])|(15[0-35-9])|(16[2567])|(17[01235-8])|(18[0-9])|(19[1589]))\\\\d{8}\\$");//^0?(13[0-9]|15[012356789]|18[01236789]|14[57])[0-9]{8}$
    public final static Pattern pattern_mobile_global = Pattern.compile("^(\\\\+|00){0,2}(9[976]\\\\d|8[987530]\\\\d|6[987]\\\\d|5[90]\\\\d|42\\\\d|3[875]\\\\d|2[98654321]\\\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\\\d{1,14}\\$");//^0?(13[0-9]|15[012356789]|18[01236789]|14[57])[0-9]{8}$

    /**
     * 字符编码
     */
    public final static String encoding = "UTF-8";

    public static void main(String[] args) {
        System.out.println(nicknameTuoMin("小日本"));
    }

    public static String nicknameTuoMin(String str){
        if (str.length() == 2) {
            return str.substring(0, 1) + '*'; //截取name 字符串截取第一个字符，
        } else if (str.length() == 3) {
            return str.charAt(0) + "*" + str.charAt(2); //截取第一个和第三个字符
        } else if (str.length() > 3) {
            return str.charAt(0) + "*" + '*' + str.substring(3); //截取第一个和大于第4个字符
        }
        return "";
    }

    /**
     * 验证字符串是否匹配指定正则表达式
     * @param pattern
     * @param content
     * @return
     */
    public static boolean regExpVali(Pattern pattern, String content){
        Matcher matcher = pattern.matcher(content);
        return matcher.matches();
    }
    public static boolean regExpValiMobile(String mobile){
        if (mobile.startsWith("+86")) {
            return regExpVali(pattern_mobile, mobile.split(" ")[1]);
        }else return regExpVali(pattern_mobile_global, mobile);
    }

//    由大、小写字母、数字，6~20个字符组成 或者 由大、小写字母、特殊字符，6~20个字符组成
    public static boolean valiPassword(String str) {
        return (str.matches("^.*[a-zA-Z]+.*$") && str.matches("^.*[0-9]+.*$") && str.matches("^.{6,20}$"))
                || (str.matches("^.*[a-zA-Z]+.*$") && str.matches("^.*[/^/$/.//,;:'!@#%&/*/|/?/+/(/)/[/]/{/}]+.*$") && str.matches("^.{6,20}$"));
    }
//    同时包含大、小写字母、数字、特殊字符，长度为6~20等
    public static boolean valiPassword2(String str) {
        return (str.matches("^.*[a-z]+.*$") &&str.matches("^.*[A-Z]+.*$")&& str.matches("^.*[0-9]+.*$")&& str.matches("^.*[/^/$/.//,;:'!@#%&/*/|/?/+/(/)/[/]/{/}]+.*$") && str.matches("^.{6,20}$"));
    }
    //由6~20位数字、字母或符号组成，至少含有2种以上字符
    public static boolean valiPasswordQQ(String str) {
        return (
                str.matches("^.*[a-zA-Z]+.*$") && str.matches("^.*[0-9]+.*$")//字母数字组成
                        || (str.matches("^.*[a-zA-Z]+.*$") && str.matches("^.*[/^/$/.//,;:'!@#%&/*/|/?/+/(/)/[/]/{/}]+.*$"))//字母符号组成
                        || (str.matches("^.*[0-9]+.*$") && str.matches("^.*[/^/$/.//,;:'!@#%&/*/|/?/+/(/)/[/]/{/}]+.*$")) //数字符号组成
        )&& str.matches("^.{6,20}$");
    }
    public static boolean valiUserName(String str){
        return str.matches("^[a-z][a-zA-Z0-9_]{3,15}$");
    }
    public static boolean valiNickname(String str){
        return str.matches("^[a-zA-Z0-9_-\\u4e00-\\u9fa5]{2,16}$");
    }
    //一个emoji算4个字符
    public static int getNicknameLength(String nickname){
        int len = 0;

        GraphemeMatcher m=new GraphemeMatcher(nickname);
        while(m.find()) {
            len+=4;
        }
        nickname = EmojiUtils.removeAllEmojis(nickname);
        //一个中文占用2个字符
        for(int i=0;i<nickname.length();i++){
            if(126>=nickname.charAt(i)&&nickname.charAt(i)>=32){
                len+=1;
            }else{
                len+=2;
            }
        }
        return len;
    }


    //    同时包含大、小写字母、数字长度为4~15等
    public static boolean regExpAccountNumber(String str) {
        return (str.matches("^.*[a-z]+.*$") &&str.matches("^.*[A-Z]+.*$")&& str.matches("^.*[0-9]+.*$")&& str.matches("^.{4,15}$"));
    }
    public static String jsonStringConvert(String s){
        char[] temp = s.toCharArray();
        int n = temp.length;
        for(int i =0;i<n;i++){
            if(temp[i]==':'&&temp[i+1]=='"'){
                for(int j =i+2;j<n;j++){
                    if(temp[j]=='"'){
                        if(temp[j+1]!=',' &&  temp[j+1]!='}'){
                            temp[j]='”';
                        }else if(temp[j+1]==',' ||  temp[j+1]=='}'){
                            break ;
                        }
                    }
                }
            }
        }
        return new String(temp);
    }

    /**
     * double精度调整
     *
     * @param doubleValue 需要调整的值123.454
     * @param format      目标样式".##"
     * @return
     */
    public static String decimalFormat(double doubleValue, String format) {
        DecimalFormat myFormatter = new DecimalFormat(format);
        String formatValue = myFormatter.format(doubleValue);
        return formatValue;
    }

    /**
     * URL编码（utf-8）
     *
     * @param source
     * @return
     */
    public static String urlEncode(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据内容类型判断文件扩展名
     *
     * @param contentType 内容类型
     * @return
     */
    public static String getFileExt(String contentType) {
        String fileExt = "";
        if ("image/jpeg".equals(contentType)) {
            fileExt = ".jpg";

        } else if ("audio/mpeg".equals(contentType)) {
            fileExt = ".mp3";

        } else if ("audio/amr".equals(contentType)) {
            fileExt = ".amr";

        } else if ("video/mp4".equals(contentType)) {
            fileExt = ".mp4";

        } else if ("video/mpeg4".equals(contentType)) {
            fileExt = ".mp4";
        }

        return fileExt;
    }

    /**
     * 获取bean名称
     *
     * @param bean
     * @return
     */
    public static String beanName(Object bean) {
        String fullClassName = bean.getClass().getName();
        String classNameTemp = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        return classNameTemp.charAt(0) + classNameTemp.substring(1);
    }

    public final static Pattern referer_pattern = Pattern.compile("@([^@^\\s^:]{1,})([\\s\\:\\,\\;]{0,1})");//@.+?[\\s:]


    /**
     * 首字母转小写
     *
     * @param s
     * @return
     */
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);
        }
    }

    /**
     * 首字母转大写
     *
     * @param s
     * @return
     */
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toUpperCase(s.charAt(0)) + s.substring(1);
        }
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    private static String TruncateUrlPage(String strURL) {
        String strAllParam = null;
        String[] arrSplit = null;

        strURL = strURL.trim();

        arrSplit = strURL.split("[?]");
        if (strURL.length() > 1) {
            if (arrSplit.length > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1];
                }
            }
        }

        return strAllParam;
    }

    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     *
     * @param URL url地址
     * @return url请求参数部分
     */
    public static Map<String, String> URLRequest(String URL) {
        Map<String, String> mapRequest = new HashMap<>();

        String[] arrSplit = null;

        String strUrlParam = TruncateUrlPage(URL);
        if (strUrlParam == null) {
            return mapRequest;
        }
        //每个键值为一组
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");

            //解析出键值
            if (arrSplitEqual.length > 1) {
                //正确解析
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            } else {
                if (arrSplitEqual[0] != "") {
                    //只有参数没有值，不加入
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    public static String rejoin(String ids) {
        String[] arr = StringUtils.split(ids,",");
        if(arr==null||arr.length==0){
            return null;
        }
        StringBuilder str = new StringBuilder();
        for (String s : arr) {
            str.append("'").append(s).append("'").append(",");
        }
        return StringUtils.substring(str.toString(),1,str.length()-2);
    }
    public static String rejoin(List<Integer> ids,String s) {
        if(ids==null||ids.size()==0){
            return "";
        }
        StringBuilder str = new StringBuilder();
        for (Integer id : ids) {
            str.append(id).append(s);
        }
        return StringUtils.substring(str.toString(),0,str.length()-1);
    }


    /**
     * 用户名匿名
     * @param username
     * @return
     */
    public static String anonymize(String username) {
        if(StringUtils.isEmpty(username)){
            return null;
        }
        char[] arr =  username.toCharArray();
        if(arr.length ==1){
            return arr[0]+"****";
        }
        if(arr.length ==2){
            return arr[0]+"****"+arr[1];
        }
        if (arr.length >2) {
            return username.replaceFirst(username.substring(1,arr.length-1) ,"****");
        }
        return "";
    }
    public static String encryptMobile(String mobile){
        if(StringUtils.isBlank(mobile)){
            return null;
        }
        return mobile.substring(0,3)+"****"+mobile.substring(7);
    }

    //左侧补零
    public static String leftAdd(int total,int count){
        StringBuilder result = new StringBuilder(String.valueOf(count));
        while (result.length() < (String.valueOf(total)).length()){
            result.insert(0, "0");
        }
        return result.toString();
    }
    public static String leftAdd(int total,String count){
        StringBuilder result = new StringBuilder(count);
        while (result.length() < total){
            result.insert(0, "0");
        }
        return result.toString();
    }

    /**
     * 去除HTML代码
     *
     * @param inputString
     * @return
     */
    public static String HtmltoText(String inputString) {
        String htmlStr = inputString; // 含HTML标签的字符串
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;
        Pattern p_ba;
        Matcher m_ba;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            // }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            // }
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            String patternStr = "\\s+";

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签

            p_ba = Pattern.compile(patternStr, Pattern.CASE_INSENSITIVE);
            m_ba = p_ba.matcher(htmlStr);
            htmlStr = m_ba.replaceAll(""); // 过滤空格

            textStr = htmlStr;

        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }
        return textStr;// 返回文本字符串
    }

    /**
     * 把页面的信息替换成我们想要的信息存放数据库里面
     *
     * @param sourcestr 页面得到的信息
     * @return
     */
    public static String getHTMLToString(String sourcestr) {
        if (sourcestr == null) {
            return "";
        }
        sourcestr = sourcestr.replaceAll("\\x26", "&amp;");// &
        sourcestr = sourcestr.replaceAll("\\x3c", "&lt;");// <
        sourcestr = sourcestr.replaceAll("\\x3e", "&gt;");// >
        sourcestr = sourcestr.replaceAll("\\x09", "&nbsp;&nbsp;&nbsp;&nbsp;");// tab键
        sourcestr = sourcestr.replaceAll("\\x20", "&nbsp;");// 空格
        sourcestr = sourcestr.replaceAll("\\x22", "&quot;");// "

        sourcestr = sourcestr.replaceAll("\r\n", "<br>");// 回车换行
        sourcestr = sourcestr.replaceAll("\r", "<br>");// 回车
        sourcestr = sourcestr.replaceAll("\n", "<br>");// 换行
        return sourcestr;
    }

    /**
     * 把数据库里面的信息回显到页面上
     *
     * @param sourcestr 数据库取得的信息
     * @return
     */
    public static String getStringToHTML(String sourcestr) {
        if (sourcestr == null) {
            return "";
        }
        sourcestr = sourcestr.replaceAll("&amp;", "\\x26");// &
        sourcestr = sourcestr.replaceAll("&lt;", "\\x3c");// <
        sourcestr = sourcestr.replaceAll("&gt;", "\\x3e");// >
        sourcestr = sourcestr.replaceAll("&nbsp;&nbsp;&nbsp;&nbsp;", "\\x09");// tab键
        sourcestr = sourcestr.replaceAll("&nbsp;", "\\x20");// 空格
        sourcestr = sourcestr.replaceAll("&quot;", "\\x22");// "

        sourcestr = sourcestr.replaceAll("<br>", "\r\n");// 回车换行
        sourcestr = sourcestr.replaceAll("<br>", "\r");// 回车
        sourcestr = sourcestr.replaceAll("<br>", "\n");// 换行
        return sourcestr;
    }


}
