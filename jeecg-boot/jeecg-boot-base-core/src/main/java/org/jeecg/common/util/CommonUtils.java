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

import jakarta.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        //update-begin---author:wangshuai---date:2026-01-08---for:【QQYUN-14535】ai生成图片的后缀不一致的，导致不展示---
        fileName += "." + PoiPublicUtil.getFileExtendName(data).toLowerCase();
        //update-end---author:wangshuai---date:2026-01-08---for:【QQYUN-14535】ai生成图片的后缀不一致的，导致不展示---
        try {
            if(CommonConstant.UPLOAD_TYPE_LOCAL.equals(uploadType)){
                //update-begin---author:wangshuai---date:2026-03-30---for:【issues/9435】uploadOnlineImage路径遍历漏洞修复---
                // 1. 使用已有的路径遍历检查
                SsrfFileTypeFilter.checkPathTraversal(bizPath);
                // 2. 标准化路径并校验是否在basePath范围内
                Path root = Paths.get(basePath).toAbsolutePath().normalize();
                Path targetDir = root.resolve(bizPath).toAbsolutePath().normalize();
                if (!targetDir.startsWith(root)) {
                    log.error("检测到路径遍历攻击！非法 bizPath: {}", bizPath);
                    throw new SecurityException("Illegal access to path outside of base directory.");
                }
                File file = targetDir.toFile();
                //update-end---author:wangshuai---date:2026-03-30---for:【issues/9435】uploadOnlineImage路径遍历漏洞修复---
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
            // 文件安全校验，防止上传漏洞文件
            SsrfFileTypeFilter.checkUploadFileType(mf, bizPath);
            
            String fileName = null;
            //update-begin---author:liusq ---date:2026-03-30  for：【issues/9428】修复uploadLocal bizPath路径遍历漏洞(CWE-22)-----------
            // 路径遍历校验：规范化后确保目标目录在uploadpath内
            File uploadDir = new File(uploadpath).getCanonicalFile();
            File file = new File(uploadpath + File.separator + bizPath + File.separator).getCanonicalFile();
            if (!file.toPath().startsWith(uploadDir.toPath())) {
                throw new JeecgBootException("非法业务路径，禁止访问上传目录之外的路径: " + bizPath);
            }
            //update-end---author:liusq ---date:2026-03-30  for：【issues/9428】修复uploadLocal bizPath路径遍历漏洞(CWE-22)-----------
            if (!file.exists()) {
                // 创建文件根目录
                file.mkdirs();
            }
            // 获取文件名
            String orgName = mf.getOriginalFilename();
            // 无中文情况下进行转码
            if (orgName != null && !CommonUtils.ifContainChinese(orgName)) {
                orgName = new String(orgName.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            }
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
     * 统一全局上传（支持自定义桶）
     * 根据 uploadType 自动选择 MinIO 或 阿里云OSS 进行文件上传
     *
     * @param file         待上传的文件
     * @param bizPath      业务路径，作为文件存储的目录前缀（如 "upload/images"）
     * @param uploadType   上传方式：{@link CommonConstant#UPLOAD_TYPE_MINIO} 使用MinIO，其他使用阿里云OSS
     * @param customBucket 自定义桶名称，为空则使用各存储服务的默认桶
     * @return 文件访问URL，上传失败返回空字符串
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
            //【采用SQL_SERVER2005引擎】QQYUN-13298 解决升级mybatisPlus后SqlServer分页使用OFFSET，无排序字段报错问题
            if (dbTypeEnum == DbType.SQL_SERVER) {
                dbTypeEnum = DbType.SQL_SERVER2005;
            }
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
            //update-begin---author:wangshuai ---date:20260616  for：【issues/9695】校验X_GATEWAY_BASE_PATH防止SSRF header注入-----------
            String validated = validateGatewayBasePath(xGatewayBasePath);
            if(validated != null){
                log.info("x_gateway_base_path = {}", validated);
                return validated;
            }
            log.warn("X_GATEWAY_BASE_PATH header 校验失败，已忽略: {}", xGatewayBasePath);
            //update-end---author:wangshuai ---date:20260616  for：【issues/9695】校验X_GATEWAY_BASE_PATH防止SSRF header注入-----------
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
        log.debug("-----获取当前服务 BaseUrl----- : " + baseDomainPath);
        return baseDomainPath;
    }

    //update-begin---author:wangshuai ---date:20260616  for：【issues/9695】校验X_GATEWAY_BASE_PATH防止SSRF header注入-----------
    /**
     * 校验 X_GATEWAY_BASE_PATH 请求头：仅允许 http/https 协议，不允许 userInfo，
     * 从解析后的 URI 组件重新拼接，防止注入特殊字符绕过。
     * @return 校验通过返回安全的 baseUrl，否则返回 null
     */
    public static String validateGatewayBasePathForDomain(String headerValue) {
        return validateGatewayBasePath(headerValue);
    }

    private static String validateGatewayBasePath(String headerValue) {
        if (oConvertUtils.isEmpty(headerValue)) {
            return null;
        }
        try {
            java.net.URI uri = new java.net.URI(headerValue.trim());
            String scheme = uri.getScheme();
            if (scheme == null || (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme))) {
                return null;
            }
            if (uri.getUserInfo() != null) {
                return null;
            }
            String host = uri.getHost();
            if (oConvertUtils.isEmpty(host)) {
                return null;
            }
            //update-begin---author:jeecg---for：网关基址头只接受纯路径，拒绝查询串/片段/路径穿越，防止篡改转发目标---
            if (uri.getRawQuery() != null || uri.getRawFragment() != null) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(scheme.toLowerCase()).append("://").append(host);
            if (uri.getPort() != -1) {
                sb.append(":").append(uri.getPort());
            }
            String path = uri.getPath();
            if (path != null && !path.isEmpty()) {
                // 仅允许常规路径字符，拒绝解码后出现的 ? # 及路径穿越 ..
                if (!path.matches("[A-Za-z0-9/_.\\-]*") || path.contains("..")) {
                    return null;
                }
                sb.append(path);
            }
            return sb.toString();
            //update-end---author:jeecg---for：网关基址头只接受纯路径---
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 校验 URL 的 host 是否为内网地址（回环 / 局域网 / 链路本地），
     * 用于 OpenAPI 转发等服务端发起请求的场景，防止 SSRF 到公网。
     * 校验失败抛出 JeecgBootException。
     */
    public static void checkInternalUrl(String url) {
        try {
            java.net.URI uri = new java.net.URI(url);
            String host = uri.getHost();
            if (oConvertUtils.isEmpty(host)) {
                throw new JeecgBootException("URL host 为空: " + url);
            }
            java.net.InetAddress addr = java.net.InetAddress.getByName(host);
            if (addr.isLoopbackAddress() || addr.isSiteLocalAddress() || addr.isLinkLocalAddress()) {
                return;
            }
            throw new JeecgBootException("OpenAPI baseUrl 仅允许内网地址，当前解析到外部地址: " + host + " -> " + addr.getHostAddress());
        } catch (JeecgBootException e) {
            throw e;
        } catch (Exception e) {
            throw new JeecgBootException("OpenAPI baseUrl 校验失败: " + e.getMessage());
        }
    }
    //update-end---author:wangshuai ---date:20260616  for：【issues/9695】校验X_GATEWAY_BASE_PATH防止SSRF header注入-----------

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


    /**
     * 从完整 URL 中提取本系统存储服务（OSS staticDomain/endpoint、MinIO）里的相对路径，非已配置存储的地址返回 null。
     * 按主机名 + 端口精确比对（不区分大小写），带用户信息（user@host）或路径含 .. 的地址一律不认，
     * 防止 http://存储地址@内网IP/、在查询串里拼存储地址等前缀匹配绕过；可用于"只允许引用本系统存储文件"的场景（如匿名附件）
     *
     * @param url 完整地址
     * @return 存储内相对路径（不带开头 /，MinIO 已去掉 bucket），非本系统存储返回 null
     * @author scott
     * @since 2026-10-27 【GHSA-hw5p-65q6-qcxr】由 AiragChatServiceImpl 下沉，供其他模块复用
     */
    public static String extractStorageRelativePath(String url) {
        if (oConvertUtils.isEmpty(url)) {
            return null;
        }
        java.net.URI target = parseUri(url);
        // 带用户信息（user@host）的地址一律不认，防止 http://存储地址@内网IP/ 这类写法
        if (target == null || target.getHost() == null || target.getRawUserInfo() != null) {
            return null;
        }
        String targetPath = oConvertUtils.getString(target.getPath(), "");
        if (targetPath.contains("..")) {
            return null;
        }
        // 匹配 OSS staticDomain（如 https://jeecgdev.oss-cn-beijing.aliyuncs.com）
        java.net.URI ossDomain = parseUri(OssBootUtil.getStaticDomain());
        if (isSameServer(ossDomain, target)) {
            return stripBasePath(ossDomain, targetPath);
        }
        // 匹配 OSS endpoint（如 oss-cn-beijing.aliyuncs.com，访问地址为 bucket.endpoint）
        String ossEndpoint = OssBootUtil.getEndPoint();
        if (oConvertUtils.isNotEmpty(ossEndpoint)) {
            String endpointHost = ossEndpoint.replaceFirst("(?i)^https?://", "").replaceAll("/.*$", "").toLowerCase();
            String host = target.getHost().toLowerCase();
            if (host.equals(endpointHost) || host.endsWith("." + endpointHost)) {
                return targetPath.startsWith("/") ? targetPath.substring(1) : targetPath;
            }
        }
        // 匹配 MinIO URL（如 http://192.168.1.100:9000/）
        java.net.URI minioUrl = parseUri(MinioUtil.getMinioUrl());
        if (isSameServer(minioUrl, target)) {
            String path = stripBasePath(minioUrl, targetPath);
            if (path == null) {
                return null;
            }
            // 跳过 bucket 名称
            int slashIdx = path.indexOf('/');
            return slashIdx >= 0 ? path.substring(slashIdx + 1) : null;
        }
        return null;
    }

    /**
     * 解析地址，格式错误返回 null
     *
     * @author scott
     * @since 2026-10-27 【GHSA-hw5p-65q6-qcxr】存储地址按主机名比对
     */
    private static java.net.URI parseUri(String url) {
        if (oConvertUtils.isEmpty(url)) {
            return null;
        }
        try {
            return new java.net.URI(url.trim());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 两个地址是否指向同一服务：主机名相同（不区分大小写）且端口相同（未写端口按协议默认端口）
     *
     * @author scott
     * @since 2026-10-27 【GHSA-hw5p-65q6-qcxr】存储地址按主机名比对
     */
    private static boolean isSameServer(java.net.URI base, java.net.URI target) {
        if (base == null || base.getHost() == null) {
            return false;
        }
        return base.getHost().equalsIgnoreCase(target.getHost()) && effectivePort(base) == effectivePort(target);
    }

    /**
     * 地址的实际端口，未写端口时按协议取默认值
     *
     * @author scott
     * @since 2026-10-27 【GHSA-hw5p-65q6-qcxr】存储地址按主机名比对
     */
    private static int effectivePort(java.net.URI uri) {
        if (uri.getPort() != -1) {
            return uri.getPort();
        }
        return "https".equalsIgnoreCase(uri.getScheme()) ? 443 : 80;
    }

    /**
     * 去掉存储地址自身的路径前缀，返回不带开头 / 的相对路径；不在该前缀下返回 null
     *
     * @author scott
     * @since 2026-10-27 【GHSA-hw5p-65q6-qcxr】存储地址按主机名比对
     */
    private static String stripBasePath(java.net.URI base, String targetPath) {
        String basePath = oConvertUtils.getString(base.getPath(), "");
        if (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }
        if (!basePath.isEmpty() && !(targetPath.equals(basePath) || targetPath.startsWith(basePath + "/"))) {
            return null;
        }
        String path = targetPath.substring(basePath.length());
        return path.startsWith("/") ? path.substring(1) : path;
    }
}
