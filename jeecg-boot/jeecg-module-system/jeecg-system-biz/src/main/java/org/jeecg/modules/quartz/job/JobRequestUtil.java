package org.jeecg.modules.quartz.job;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.config.Constant;
import org.jeecg.config.RequestUtil;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class JobRequestUtil {

    /**
     * key:驱动类型
     * value:网盘名称
     * eg:<AliyundriveShare,阿里云盘>
     */
    private static final Map<String, String> driverMap = new HashMap<>();

    static {
        //driverMap.put("","夸克网盘");
        //driverMap.put("","天翼云盘");
        //driverMap.put("","谷歌硬盘");
        driverMap.put("AliyundriveShare", "阿里云盘");
        driverMap.put("AliyundriveOpen", "阿里云盘");
        driverMap.put("BaiduShare", "百度网盘");
        driverMap.put("BaiduNetdisk", "百度网盘");
        driverMap.put("115 Cloud", "115");
        driverMap.put("115 Share", "115");
        driverMap.put("123PanShare", "123云盘");
        driverMap.put("139Yun", "移动云盘");
        driverMap.put("Lanzou", "蓝奏云");
        driverMap.put("Quqi", "曲奇云盘");
    }

    /**
     * 匹配资源类型
     *
     * @param resourceTypeList 数据字典
     * @param name             名称
     * @return
     */
    public static String getResourceTypeName(List<DictModel> resourceTypeList, String name) {
        if (resourceTypeList.isEmpty()) {
            return "";
        }
        for (DictModel dict : resourceTypeList) {
            if (StringUtils.isNotBlank(dict.getText())) {
                for (String index : dict.getText().replaceAll(",", "，").split("，")) {
                    if (name.contains(index) && StringUtils.isNotBlank(index)) {
                        return dict.getValue();
                    }
                }
            }
            if (StringUtils.isNotBlank(dict.getDescription())) {
                for (String index : dict.getDescription().replaceAll(",", "，").split("，")) {
                    if (name.contains(index) && StringUtils.isNotBlank(index)) {
                        return dict.getValue();
                    }
                }
            }
        }
        return "";
    }

    /**
     * 根据域名获取网盘名称
     *
     * @param driver
     * @return
     */
    public static String getDriverName(String driver) {
        if (driverMap.containsKey(driver)) {
            return driverMap.get(driver);
        }
        return "其它";
    }

    public static String getPassword(String text) {
        for (String str : text.split(" ")) {
            str += " ";
            String password = "";
            if ((str.contains("分享码:") || str.contains("分享码：") || str.contains("分享码 ")) && StringUtils.isBlank(password)) {
                if (str.indexOf(" ", str.indexOf("分享码") + 5) > 0) {
                    password = str.substring(str.indexOf("分享码") + 4, str.indexOf(" ", str.indexOf("分享码") + 5)).trim();
                }
            } else if ((str.contains("访问码:") || str.contains("访问码：") || str.contains("访问码 ")) && StringUtils.isBlank(password)) {
                if (str.indexOf(" ", str.indexOf("访问码") + 5) > 0) {
                    password = str.substring(str.indexOf("访问码") + 4, str.indexOf(" ", str.indexOf("访问码") + 5)).trim();
                }
            } else if ((str.contains("提取码:") || str.contains("提取码：") || str.contains("提取码 ")) && StringUtils.isBlank(password)) {
                if (str.indexOf(" ", str.indexOf("提取码") + 5) > 0) {
                    password = str.substring(str.indexOf("提取码") + 4, str.indexOf(" ", str.indexOf("提取码") + 5)).trim();
                }
            } else if ((str.contains("密码:") || str.contains("密码：") || str.contains("密码 ")) && StringUtils.isBlank(password)) {
                if (str.indexOf(" ", str.indexOf("密码") + 4) > 0) {
                    password = str.substring(str.indexOf("密码") + 3, str.indexOf(" ", str.indexOf("密码") + 4)).trim();
                }
            } else if (str.contains("密码") && StringUtils.isBlank(password)) {
                if (str.indexOf(" ", str.indexOf("密码") + 4) > 0) {
                    password = str.substring(str.indexOf("密码") + 2, str.indexOf(" ", str.indexOf("密码") + 3)).trim();
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
                    password = password.substring(0, password.length() - 1);
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
            if (str.indexOf(" ", str.indexOf("Size") + 6) > 0) {
                size = str.substring(str.indexOf("Size") + 5, str.indexOf(" ", str.indexOf("Size") + 6)).trim();
            }
        } else if ((str.contains("容量:") || str.startsWith("容量：") || str.startsWith("容量 ")) && StringUtils.isBlank(size)) {
            if (str.indexOf(" ", str.indexOf("容量") + 4) > 0) {
                size = str.substring(str.indexOf("容量") + 3, str.indexOf(" ", str.indexOf("容量") + 4)).trim();
            }
        } else if ((str.contains("大小:") || str.startsWith("大小：") || str.startsWith("大小 ")) && StringUtils.isBlank(size)) {
            if (str.indexOf(" ", str.indexOf("大小") + 4) > 0) {
                size = str.substring(str.indexOf("大小") + 3, str.indexOf(" ", str.indexOf("大小") + 4)).trim();
            }
        }
        if (!size.matches("^\\d+.*")) {
            size = "";
        }
        return size;
    }


    public static String replaceAll(String text, String... strArray) {
        String result = text;
        for (String s : strArray) {
            result = result.replaceAll(s, "");
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
            String name = array.get(array.size() - 1);
            String resourceType = JobRequestUtil.getResourceTypeName(resourceTypeList, name);
            if (StringUtils.isBlank(resourceType)) { // 处理手动分类数据
                resourceType = StringUtils.trimToEmpty((String) map.get("resource_type"));
            }
            String driverName = JobRequestUtil.getDriverName(map.get("driver").toString());
            String newPath = "/共享/" + (StringUtils.isBlank(resourceType) ? driverName : resourceType) + "/" + name;
            if (pathSet.contains(newPath) || path.equals(newPath)) {
                continue;
            }
            sharesParams.add(new Object[]{newPath, resourceType, name, driverName});
            deleteStorages.add(new Object[]{path});
            log.info(String.format("%s -> %s", path, newPath));
        }
    }

    public static JSONObject updateStorage(Map map) {
        map.remove("status");
        map.remove("remark");
        map.remove("disabled");
        map.remove("proxy_range");
        map.remove("resource_type");
        map.remove("modified");
        map.remove("update_date");
        return RequestUtil.alistPostRequest(Constant.ALIST_STORAGE_UPDATE, map);
    }

    public static int deleteStorage(List<Map<String, Object>> maps) {
        int success = 0;
        JSONObject object = null;
        Map<String, Object> newMap = new HashMap<>();
        for (Map map : maps) {
            Long id = (Long) map.get("id");
            if (id != null) {
                newMap.put("id", id);
                object = RequestUtil.alistPostRequest(Constant.ALIST_STORAGE_DELETE, map);
                if (object.getIntValue("code") == 200) {
                    success++;
                }
            }
        }
        return success;
    }

    public static void main(String[] args) {
        System.out.println(getPassword("https://115.com/s/swzs2p433f7?password=lIIl#"));
    }

}
