package org.jeecg.modules.quartz.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.DateUtils;
import org.jeecg.config.ComponentUtil;
import org.jeecg.config.Constant;
import org.quartz.JobExecutionException;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class JobUtil {

    //匹配中文
    private static final Pattern  chinesePat = Pattern.compile("[\u4e00-\u9fa5]");
    //匹配英文名
    private static final Pattern englishPat = Pattern.compile("[A-Za-z]+\\s+[A-Za-z]+");
    //匹配script
    private static final Pattern scriptPat = Pattern.compile("]*?>[\\s\\S]*?<\\/script>",Pattern.CASE_INSENSITIVE);
    //匹配style
    private static final Pattern stylePat = Pattern.compile("]*?>[\\s\\S]*?<\\/style>",Pattern.CASE_INSENSITIVE);
    //匹配html
    private static final Pattern htmlPat = Pattern.compile("<[^>]+>",Pattern.CASE_INSENSITIVE);
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
    private static Map<String,String> typeMap = new HashMap<>();

    /**
     * key:驱动类型
     * value:网盘名称
     * eg:<AliyundriveShare,阿里云盘>
     */
    private static Map<String,String> driverMap = new HashMap<>();

    //记录方法耗时时间
    private static Map<String,Long> recordMap = new HashMap<>(200);

    private static OkHttpClient client = new OkHttpClient().newBuilder().build();

    static {
        typeMap.put("pan.quark.cn","夸克网盘");
        typeMap.put("www.alipan.com","阿里云盘");
        typeMap.put("www.aliyundrive.com","阿里云盘");
        typeMap.put("pan.baidu.com","百度网盘");
        typeMap.put("115.com","115");
        typeMap.put("www.123pan.com","123云盘");
        typeMap.put("yun.139.com","移动云盘");
        typeMap.put("cloud.189.cn","天翼云盘");
        typeMap.put("eva.lanzoue.com","蓝奏云");
        typeMap.put("drive.google.com","谷歌硬盘");
    }

    static {
        //driverMap.put("","夸克网盘");
        //driverMap.put("","天翼云盘");
        //driverMap.put("","谷歌硬盘");
        driverMap.put("AliyundriveShare","阿里云盘");
        driverMap.put("AliyundriveOpen","阿里云盘");
        driverMap.put("BaiduShare","百度网盘");
        driverMap.put("BaiduNetdisk","百度网盘");
        driverMap.put("115 Cloud","115");
        driverMap.put("115 Share","115");
        driverMap.put("123PanShare","123云盘");
        driverMap.put("139Yun","移动云盘");
        driverMap.put("Lanzou","蓝奏云");
        driverMap.put("Quqi","曲奇云盘");
    }

    /**
     * 匹配资源类型
     * @param resourceTypeList 数据字典
     * @param name 名称
     * @return
     */
    public static String getResourceTypeName(List<DictModel> resourceTypeList, String name) {
        if (resourceTypeList.isEmpty()) {
            return "";
        }
        for (DictModel dict : resourceTypeList) {
            if (StringUtils.isBlank(dict.getText())) {
                continue;
            }
            for (String index : dict.getText().replaceAll(",","，").split("，")) {
                if (name.contains(index) && StringUtils.isNotBlank(index)) {
                    return dict.getValue();
                }
            }
        }
        return "";
    }

    /**
     * 获取网盘名称（默认存储路径）
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
     * @param name
     * @return
     */
    public static String delHTMLTag(String name) {
        if (name.isEmpty()) {
            return name;
        }
        Matcher m_script=scriptPat.matcher(name);
        name=m_script.replaceAll("");

        Matcher m_style=stylePat.matcher(name);
        name=m_style.replaceAll("");

        Matcher m_html=htmlPat.matcher(name);
        name=m_html.replaceAll("");
        return name.trim();
    }

    /**
     * 获取链接
     * @param name
     * @return
     */
    private static String getUrl(String name) {
        if (name.isEmpty()) {
            return name;
        }
        Matcher m = urlPat.matcher(name);
        if (m.find()) {
            return m.group();
        }
        return "";
    }

    /**
     * 拼接字符窜
     * @param strArray 数组
     * @param split 分隔符
     * @return
     */
    public static String concat(String split,String... strArray) {
        String result = "";
        for (String s : strArray) {
            if (StringUtils.isNotBlank(s)) {
                result += split + s;
            }
        }
        return result;
    }

    /**
     * 根据域名获取网盘名称
     * @param driver
     * @return
     */
    public static String getDriverName(String driver) {
        if (driverMap.containsKey(driver)) {
            return driverMap.get(driver);
        }
        return "其它";
    }

    public static void startTime(String name) {
        recordMap.put(name,System.currentTimeMillis());
        log.info("{} 方法开始执行",name);
    }

    public static void endTime(String name,String msg) {
        Long start = recordMap.get(name);
        if (start != null) {
            log.info("{} -- {} {} 方法调用结束，耗时{}秒",DateUtils.now(),name,msg,(System.currentTimeMillis()-start)/1000);
        } else {
            log.info("{} -- {} {} 方法调用结束",DateUtils.now(),name,msg);
        }
    }

    public static void endTime(String name) {
        endTime(name,"");
    }

    private static boolean isShareLink(String links) {
        for (Map.Entry<String,String> map : typeMap.entrySet()) {
            if (links.contains(map.getKey())) {
                return true;
            }
        }
        return false;
    }

    public static String getShareLink(String str) {
        String url = getUrl(str);
        if (isShareLink(url)) {
            if (url.contains("?pwd=")) {
                url = url.substring(0,url.indexOf("?pwd="));
            } else if (url.contains("?password=")) {
                url = url.substring(0,url.indexOf("?password="));
            } else if (url.endsWith("#")) {
                url = url.substring(0,url.length()-1);
            }
            return url;
        }
        return "";
    }

    public static String getPassword(String text) {
        for (String str : text.split(" ")) {
            str += " ";
            String password = "";
            if ((str.contains("分享码:") || str.contains("分享码：") || str.contains("分享码 ")) && StringUtils.isBlank(password)) {
                if (str.indexOf(" ",str.indexOf("分享码")+5)>0) {
                    password = str.substring(str.indexOf("分享码")+4,str.indexOf(" ",str.indexOf("分享码")+5)).trim();
                }
            } else if ((str.contains("访问码:") || str.contains("访问码：") || str.contains("访问码 ")) && StringUtils.isBlank(password)) {
                if (str.indexOf(" ",str.indexOf("访问码")+5)>0) {
                    password = str.substring(str.indexOf("访问码")+4,str.indexOf(" ",str.indexOf("访问码")+5)).trim();
                }
            } else if ((str.contains("提取码:") || str.contains("提取码：") || str.contains("提取码 ")) && StringUtils.isBlank(password)) {
                if (str.indexOf(" ",str.indexOf("提取码")+5)>0) {
                    password = str.substring(str.indexOf("提取码")+4,str.indexOf(" ",str.indexOf("提取码")+5)).trim();
                }
            } else if ((str.contains("密码:") || str.contains("密码：") || str.contains("密码 ")) && StringUtils.isBlank(password)) {
                if (str.indexOf(" ", str.indexOf("密码") + 4) > 0) {
                    password = str.substring(str.indexOf("密码")+3,str.indexOf(" ",str.indexOf("密码")+4)).trim();
                }
            } else if (str.contains("密码") && StringUtils.isBlank(password)) {
                if (str.indexOf(" ", str.indexOf("密码") + 4) > 0) {
                    password = str.substring(str.indexOf("密码")+2,str.indexOf(" ",str.indexOf("密码")+3)).trim();
                }
            } else if (str.contains("?password=") && StringUtils.isBlank(password)) {
                int endIndex = str.indexOf("&", str.indexOf("?password="), str.indexOf(" "));
                password = str.substring(str.indexOf("?password=") + 10, endIndex > 0 ? endIndex : str.length()).trim();
            } else if (str.contains("?pwd=") && StringUtils.isBlank(password)) {
                int endIndex = str.indexOf("&", str.indexOf("?pwd="), str.indexOf(" "));
                password = str.substring(str.indexOf("?pwd=") + 5, endIndex > 0 ? endIndex : str.length()).trim();
            }
            if (!password.matches("^[a-zA-Z0-9].*")) {
                password = "";
            }
            if (StringUtils.isNotBlank(password)) {
                if (password.endsWith("#") && password.length() > 4) {
                    password = password.substring(0,password.length()-1);
                }
                return password;
            }
        }
        return "";
    }

    public static String getSize(String str) {
        str += " ";
        String size = "";
        if ((str.contains("Size:") || str.startsWith("Size：") || str.startsWith("Size ")) && StringUtils.isBlank(size)) {
            if (str.indexOf(" ",str.indexOf("Size")+6)>0) {
                size = str.substring(str.indexOf("Size")+5,str.indexOf(" ",str.indexOf("Size")+6)).trim();
            }
        } else if ((str.contains("容量:") || str.startsWith("容量：") || str.startsWith("容量 ")) && StringUtils.isBlank(size)) {
            if (str.indexOf(" ",str.indexOf("容量")+4)>0) {
                size = str.substring(str.indexOf("容量")+3,str.indexOf(" ",str.indexOf("容量")+4)).trim();
            }
        } else if ((str.contains("大小:") || str.startsWith("大小：") || str.startsWith("大小 ")) && StringUtils.isBlank(size)) {
            if (str.indexOf(" ",str.indexOf("大小")+4)>0) {
                size = str.substring(str.indexOf("大小")+3,str.indexOf(" ",str.indexOf("大小")+4)).trim();
            }
        }
        if (!size.matches("^\\d+.*")) {
            size = "";
        }
        return size;
    }

    public static boolean validResourceName(String str) {
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

    public static String replaceAll(String text, String... strArray) {
        String result = text;
        for (String s : strArray) {
            result = result.replaceAll(s, "");
        }
        return result;
    }

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

    public static boolean isContains(String str, String... strings) {
        for (String s : strings) {
            if (str.contains(s)) {
                return true;
            }
        }
        return false;
    }

    public static void updateMountPath(List<DictModel> resourceTypeList, List<Map<String, Object>> result, List<Object[]> sharesParams, List<Object[]> deleteStorages) {
        Set<String> pathSet = new HashSet<>();
        for (Map map : result) {
            String path = map.get("mount_path").toString();
            List<String> array = Arrays.stream(path.split("/")).collect(Collectors.toList());
            array.remove("");
            String name = array.get(array.size()-1);
            String resourceType = JobUtil.getResourceTypeName(resourceTypeList,name);
            if (StringUtils.isBlank(resourceType)) { // 处理手动分类数据
                resourceType = StringUtils.trimToEmpty((String) map.get("resource_type"));
            }
            String driverName = JobUtil.getDriverName(map.get("driver").toString());
            String newPath = "/共享/"+(StringUtils.isBlank(resourceType)?driverName:resourceType)+"/"+name;
            if (pathSet.contains(newPath) || path.equals(newPath)) {
                continue;
            }
            sharesParams.add(new Object[]{newPath,resourceType,name,driverName});
            deleteStorages.add(new Object[]{path});
            log.info(String.format("%s -> %s", path,newPath));
        }
    }

    public static JSONObject alistPostRequest(String api, Map<String, Object> param) throws JobExecutionException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(param));
        Request request = new Request.Builder()
                .url(ComponentUtil.getAListDomain() + api)
                .method("POST", body)
                .addHeader("Authorization", ComponentUtil.getAListToken())
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = null;
        String result = "";
        try {
            log.info("api: {} \t param: {}",api,JSON.toJSONString(param));
            response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            log.error("{} connet timeout",api);
        }
        return JSONObject.parseObject(result);
    }

    public static JSONObject updateStorage(Map map) throws JobExecutionException {
        map.remove("status");
        map.remove("remark");
        map.remove("disabled");
        map.remove("proxy_range");
        map.remove("resource_type");
        map.remove("modified");
        map.remove("update_date");
        return alistPostRequest(Constant.ALIST_STORAGE_UPDATE,map);
    }

    public static void main(String args[]) {
        System.out.println(delHTMLTag("链接： <a href=\"https://115.com/s/swzs2p433f7?password=lIIl#\">https://115.com/s/swzs2p433f7?password=lIIl#</a>"));
        System.out.println(getPassword("https://115.com/s/swzs2p433f7?password=lIIl#"));
    }

}
