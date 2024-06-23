package org.jeecg.common.util;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.DataBaseConstant;
import org.jeecg.common.constant.ServiceNameConstants;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.filter.SsrfFileTypeFilter;
import org.jeecg.common.util.oss.OssBootUtil;
import org.jeecgframework.poi.util.PoiPublicUtil;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 通用工具
 * @author: jeecg-boot
 */
@Slf4j
public class CommonUtils {

    /**
     * 中文正则
     */
    private static Pattern ZHONGWEN_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");

    /**
     * 文件名 正则字符串
     * 文件名支持的字符串：字母数字中文.-_()（） 除此之外的字符将被删除
     */
    private static String FILE_NAME_REGEX = "[^A-Za-z\\.\\(\\)\\-（）\\_0-9\\u4e00-\\u9fa5]";

    public static String uploadOnlineImage(byte[] data,String basePath,String bizPath,String uploadType){
        String dbPath = null;
        String fileName = "image" + Math.round(Math.random() * 100000000000L);
        fileName += "." + PoiPublicUtil.getFileExtendName(data);
        try {
            if(CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)){
                File file = new File(basePath + File.separator + bizPath + File.separator );
                if (!file.exists()) {
                    file.mkdirs();// 创建文件根目录
                }
                String savePath = file.getPath() + File.separator + fileName;
                File savefile = new File(savePath);
                FileCopyUtils.copy(data, savefile);
                dbPath = bizPath + File.separator + fileName;
            }else {
                InputStream in = new ByteArrayInputStream(data);
                String relativePath = bizPath+"/"+fileName;
                if(CommonConstant.UPLOAD_TYPE_MINIO.equals(uploadType)){
                    dbPath = MinioUtil.upload(in,relativePath);
                }else if(CommonConstant.UPLOAD_TYPE_OSS.equals(uploadType)){
                    dbPath = OssBootUtil.upload(in,relativePath);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbPath;
    }

    /**
     * 判断文件名是否带盘符，重新处理
     * @param fileName
     * @return
     */
    public static String getFileName(String fileName){
        //判断是否带有盘符信息
        // Check for Unix-style path
        int unixSep = fileName.lastIndexOf('/');
        // Check for Windows-style path
        int winSep = fileName.lastIndexOf('\\');
        // Cut off at latest possible point
        int pos = (winSep > unixSep ? winSep : unixSep);
        if (pos != -1)  {
            // Any sort of path separator found...
            fileName = fileName.substring(pos + 1);
        }
        //替换上传文件名字的特殊字符
        fileName = fileName.replace("=","").replace(",","").replace("&","")
                .replace("#", "").replace("“", "").replace("”", "");
        //替换上传文件名字中的空格
        fileName=fileName.replaceAll("\\s","");
        //update-beign-author:taoyan date:20220302 for: /issues/3381 online 在线表单 使用文件组件时，上传文件名中含%，下载异常
        fileName = fileName.replaceAll(FILE_NAME_REGEX, "");
        //update-end-author:taoyan date:20220302 for: /issues/3381 online 在线表单 使用文件组件时，上传文件名中含%，下载异常
        return fileName;
    }

    /**
     * java 判断字符串里是否包含中文字符
     * @param str
     * @return
     */
    public static boolean ifContainChinese(String str) {
        if(str.getBytes().length == str.length()){
            return false;
        }else{
            Matcher m = ZHONGWEN_PATTERN.matcher(str);
            if (m.find()) {
                return true;
            }
            return false;
        }
    }

    /**
     * 统一全局上传
     * @Return: java.lang.String
     */
    public static String upload(MultipartFile file, String bizPath, String uploadType) {
        String url = "";
        try {
            if (CommonConstant.UPLOAD_TYPE_MINIO.equals(uploadType)) {
                url = MinioUtil.upload(file, bizPath);
            } else {
                url = OssBootUtil.upload(file, bizPath);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new JeecgBootException(e.getMessage());
        }
        return url;
    }
    /**
     * 本地文件上传
     * @param mf 文件
     * @param bizPath  自定义路径
     * @return
     */
    public static String uploadLocal(MultipartFile mf,String bizPath,String uploadpath){
        try {
            //update-begin-author:liusq date:20210809 for: 过滤上传文件类型
            SsrfFileTypeFilter.checkUploadFileType(mf);
            //update-end-author:liusq date:20210809 for: 过滤上传文件类型
            String fileName = null;
            File file = new File(uploadpath + File.separator + bizPath + File.separator );
            if (!file.exists()) {
                // 创建文件根目录
                file.mkdirs();
            }
            // 获取文件名
            String orgName = mf.getOriginalFilename();
            orgName = CommonUtils.getFileName(orgName);
            if(orgName.indexOf(SymbolConstant.SPOT)!=-1){
                fileName = orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.lastIndexOf("."));
            }else{
                fileName = orgName+ "_" + System.currentTimeMillis();
            }
            String savePath = file.getPath() + File.separator + fileName;
            File savefile = new File(savePath);
            FileCopyUtils.copy(mf.getBytes(), savefile);
            String dbpath = null;
            if(oConvertUtils.isNotEmpty(bizPath)){
                dbpath = bizPath + File.separator + fileName;
            }else{
                dbpath = fileName;
            }
            if (dbpath.contains(SymbolConstant.DOUBLE_BACKSLASH)) {
                dbpath = dbpath.replace("\\", "/");
            }
            return dbpath;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    /**
     * 统一全局上传 带桶
     * @Return: java.lang.String
     */
    public static String upload(MultipartFile file, String bizPath, String uploadType, String customBucket) {
        String url = "";
        try {
            if (CommonConstant.UPLOAD_TYPE_MINIO.equals(uploadType)) {
                url = MinioUtil.upload(file, bizPath, customBucket);
            } else {
                url = OssBootUtil.upload(file, bizPath, customBucket);
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return url;
    }

    /** 当前系统数据库类型 */
    private static String DB_TYPE = "";
    private static DbType dbTypeEnum = null;

    /**
     * 全局获取平台数据库类型（作废了）
     * @return
     */
    @Deprecated
    public static String getDatabaseType() {
        if(oConvertUtils.isNotEmpty(DB_TYPE)){
            return DB_TYPE;
        }
        DataSource dataSource = SpringContextUtils.getApplicationContext().getBean(DataSource.class);
        try {
            return getDatabaseTypeByDataSource(dataSource);
        } catch (SQLException e) {
            //e.printStackTrace();
            log.warn(e.getMessage(),e);
            return "";
        }
    }

    /**
     * 全局获取平台数据库类型（对应mybaisPlus枚举）
     * @return
     */
    public static DbType getDatabaseTypeEnum() {
        if (oConvertUtils.isNotEmpty(dbTypeEnum)) {
            return dbTypeEnum;
        }
        try {
            DataSource dataSource = SpringContextUtils.getApplicationContext().getBean(DataSource.class);
            dbTypeEnum = JdbcUtils.getDbType(dataSource.getConnection().getMetaData().getURL());
            return dbTypeEnum;
        } catch (SQLException e) {
            log.warn(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据数据源key获取DataSourceProperty
     * @param sourceKey
     * @return
     */
    public static DataSourceProperty getDataSourceProperty(String sourceKey){
        DynamicDataSourceProperties prop = SpringContextUtils.getApplicationContext().getBean(DynamicDataSourceProperties.class);
        Map<String, DataSourceProperty> map = prop.getDatasource();
        DataSourceProperty db = (DataSourceProperty)map.get(sourceKey);
        return db;
    }

    /**
     * 根据sourceKey 获取数据源连接
     * @param sourceKey
     * @return
     * @throws SQLException
     */
    public static Connection getDataSourceConnect(String sourceKey) throws SQLException {
        if (oConvertUtils.isEmpty(sourceKey)) {
            sourceKey = "master";
        }
        DynamicDataSourceProperties prop = SpringContextUtils.getApplicationContext().getBean(DynamicDataSourceProperties.class);
        Map<String, DataSourceProperty> map = prop.getDatasource();
        DataSourceProperty db = (DataSourceProperty)map.get(sourceKey);
        if(db==null){
            return null;
        }
        DriverManagerDataSource ds = new DriverManagerDataSource ();
        ds.setDriverClassName(db.getDriverClassName());
        ds.setUrl(db.getUrl());
        ds.setUsername(db.getUsername());
        ds.setPassword(db.getPassword());
        return ds.getConnection();
    }

    /**
     * 获取数据库类型
     * @param dataSource
     * @return
     * @throws SQLException
     */
    private static String getDatabaseTypeByDataSource(DataSource dataSource) throws SQLException{
        if("".equals(DB_TYPE)) {
            Connection connection = dataSource.getConnection();
            try {
                DatabaseMetaData md = connection.getMetaData();
                String dbType = md.getDatabaseProductName().toUpperCase();
                String sqlserver= "SQL SERVER";
                if(dbType.indexOf(DataBaseConstant.DB_TYPE_MYSQL)>=0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_MYSQL;
                }else if(dbType.indexOf(DataBaseConstant.DB_TYPE_ORACLE)>=0 ||dbType.indexOf(DataBaseConstant.DB_TYPE_DM)>=0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_ORACLE;
                }else if(dbType.indexOf(DataBaseConstant.DB_TYPE_SQLSERVER)>=0||dbType.indexOf(sqlserver)>=0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_SQLSERVER;
                }else if(dbType.indexOf(DataBaseConstant.DB_TYPE_POSTGRESQL)>=0 || dbType.indexOf(DataBaseConstant.DB_TYPE_KINGBASEES)>=0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_POSTGRESQL;
                }else if(dbType.indexOf(DataBaseConstant.DB_TYPE_MARIADB)>=0) {
                    DB_TYPE = DataBaseConstant.DB_TYPE_MARIADB;
                }else {
                    log.error("数据库类型:[" + dbType + "]不识别!");
                    //throw new JeecgBootException("数据库类型:["+dbType+"]不识别!");
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }finally {
                connection.close();
            }
        }
        return DB_TYPE;

    }
    /**
     * 获取服务器地址
     *
     * @param request
     * @return
     */
    public static String getBaseUrl(HttpServletRequest request) {
        //1.【兼容】兼容微服务下的 base path-------
        String xGatewayBasePath = request.getHeader(ServiceNameConstants.X_GATEWAY_BASE_PATH);
        if(oConvertUtils.isNotEmpty(xGatewayBasePath)){
            log.info("x_gateway_base_path = "+ xGatewayBasePath);
            return  xGatewayBasePath;
        }
        //2.【兼容】SSL认证之后，request.getScheme()获取不到https的问题
        // https://blog.csdn.net/weixin_34376986/article/details/89767950
        String scheme = request.getHeader(CommonConstant.X_FORWARDED_SCHEME);
        if(oConvertUtils.isEmpty(scheme)){
            scheme = request.getScheme();
        }

        //3.常规操作
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();

        //返回 host domain
        String baseDomainPath = null;
        //update-begin---author:wangshuai---date:2024-03-15---for:【QQYUN-8561】企业微信登陆请求接口设置上下文不一致，导致接口404---
        int httpPort = 80;
        int httpsPort = 443;
        if(httpPort == serverPort || httpsPort == serverPort){
        //update-end---author:wangshuai---date:2024-03-15---for:【QQYUN-8561】企业微信登陆请求接口设置上下文不一致，导致接口404---~
            baseDomainPath = scheme + "://" + serverName  + contextPath ;
        }else{
            baseDomainPath = scheme + "://" + serverName + ":" + serverPort + contextPath ;
        }
        log.debug("-----Common getBaseUrl----- : " + baseDomainPath);
        return baseDomainPath;
    }

    /**
     * 递归合并 fastJSON 对象
     *
     * @param target  目标对象
     * @param sources 来源对象，允许多个，优先级从左到右，最右侧的优先级最高
     */
    public static JSONObject mergeJSON(JSONObject target, JSONObject... sources) {
        for (JSONObject source : sources) {
            CommonUtils.mergeJSON(target, source);
        }
        return target;
    }

    /**
     * 递归合并 fastJSON 对象
     *
     * @param target 目标对象
     * @param source 来源对象
     */
    public static JSONObject mergeJSON(JSONObject target, JSONObject source) {
        for (String key : source.keySet()) {
            Object sourceItem = source.get(key);
            // 是否是 JSONObject
            if (sourceItem instanceof Map) {
                // target中存在此key
                if (target.containsKey(key)) {
                    // 两个都是 JSONObject，继续合并
                    if (target.get(key) instanceof Map) {
                        CommonUtils.mergeJSON(target.getJSONObject(key), source.getJSONObject(key));
                        continue;
                    }
                }
            }
            // target不存在此key，或不是 JSONObject，则覆盖
            target.put(key, sourceItem);
        }
        return target;
    }

    /**
     * 将list集合以分割符的方式进行分割
     * @param list      String类型的集合文本
     * @param separator 分隔符
     * @return
     */
    public static String getSplitText(List<String> list, String separator) {
        if (null != list && list.size() > 0) {
            return StringUtils.join(list, separator);
        }
        return "";
    }
 
    /**
     * 通过table的条件SQL
     *
     * @param tableSql sys_user where name = '1212'
     * @return name = '1212'
     */
    public static String getFilterSqlByTableSql(String tableSql) {
        if(oConvertUtils.isEmpty(tableSql)){
            return null;
        }
        
        if (tableSql.toLowerCase().indexOf(DataBaseConstant.SQL_WHERE) > 0) {
            String[] arr = tableSql.split(" (?i)where ");
            if (arr != null && oConvertUtils.isNotEmpty(arr[1])) {
                return arr[1];
            }
        }
        return "";
    }

    /**
     * 通过table获取表名
     *
     * @param tableSql sys_user where name = '1212'
     * @return sys_user
     */
    public static String getTableNameByTableSql(String tableSql) {
        if(oConvertUtils.isEmpty(tableSql)){
            return null;
        }
        
        if (tableSql.toLowerCase().indexOf(DataBaseConstant.SQL_WHERE) > 0) {
            String[] arr = tableSql.split(" (?i)where ");
            return arr[0].trim();
        } else {
            return tableSql;
        }
    }

    /**
     * 判断两个数组是否存在交集
     * @param set1
     * @param arr2
     * @return
     */
    public static boolean hasIntersection(Set<String> set1, String[] arr2) {
        if (set1 == null) {
            return false;
        }
        
        if(set1.size()>0){
            for (String str : arr2) {
                if (set1.contains(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 输出info日志，会捕获异常，防止因为日志问题导致程序异常
     *
     * @param msg
     * @param objects
     */
    public static void logInfo(String msg, Object... objects) {
        try {
            log.info(msg, objects);
        } catch (Exception e) {
            log.warn("{} —— {}", msg, e.getMessage());
        }
    }

}