package org.jeecg.common.util.security;

import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;

/**
 * JDBC连接安全校验工具类
 *
 * 修复说明:
 *   原实现仅检查 URL 中 '?' 之后的参数，且黑名单仅包含 5 个 PostgreSQL 参数。
 *   存在以下安全隐患：
 *     1. MySQL 危险参数 (allowLoadLocalInfile, autoDeserialize 等) 未覆盖
 *     2. H2 使用 ';' 分隔参数，完全绕过 '?' 检查
 *     3. MySQL multi-host 语法 '(host,param=val)' 和 address-block 语法不使用 '?'
 *
 *   修复方案: 对 URL 全文做 toLowerCase() + contains() 匹配，
 *   覆盖所有参数分隔符格式 (?, ;, (), address=)，并扩展黑名单覆盖全部主流驱动。
 *
 * @Author taoYan
 * @Date 2022/8/10 18:15
 */
public class JdbcSecurityUtil {

    /**
     * 全驱动危险参数黑名单 (全小写，用于 contains 匹配)
     *
     * 使用 URL 全文 contains 匹配策略，覆盖所有参数分隔符:
     *   - 标准格式: ?key=value&key=value
     *   - H2 格式:  ;KEY=value
     *   - MySQL multi-host: (host,key=value)
     *   - MySQL address-block: address=(key=value)
     */
    private static final String[] UNSAFE_PARAMS = {
            // === MySQL / MariaDB ===
            // 文件读取相关
            "allowloadlocalinfile",       // LOAD DATA LOCAL INFILE
            "allowurlinlocalinfile",      // 通过 URL 读取远程文件
            "allowloadlocalinfileinpath", // 指定路径文件读取
            // 反序列化相关
            "autodeserialize",            // 启用反序列化
            "queryinterceptors",          // 查询拦截器 (反序列化触发点)
            "statementinterceptors",      // 语句拦截器 (反序列化触发点)
            "detectcustomcollations",     // 自定义排序规则检测 (反序列化触发点)
            // 配合攻击
            "maxallowedpacket",           // 突破数据包大小限制

            // === PostgreSQL ===
            // https://github.com/pgjdbc/pgjdbc/security/advisories/GHSA-v7wg-cpwc-24m4
            "socketfactory",              // 任意类实例化 RCE
            "socketfactoryarg",           // socketFactory 构造参数
            "sslfactory",                 // SSL 工厂类加载
            "sslhostnameverifier",        // SSL 主机名验证器类加载
            "sslpasswordcallback",        // SSL 密码回调类加载
            "authenticationpluginclassname", // 认证插件类加载
            "jaasapplicationname",        // JAAS 认证攻击

            // === H2 ===
            "init=",                      // 连接初始化 SQL (带 '=' 防止匹配到正常单词 'init')
            "runscript",                  // 远程/本地 SQL 脚本加载
            "trace_level_system_out",     // 系统信息泄露
    };

    /**
     * 允许的 JDBC 驱动类名白名单
     */
    private static final String[] ALLOWED_DRIVERS = {
            // MySQL
            "com.mysql.cj.jdbc.Driver",
            "com.mysql.jdbc.Driver",
            // PostgreSQL
            "org.postgresql.Driver",
            // Oracle
            "oracle.jdbc.OracleDriver",
            "oracle.jdbc.driver.OracleDriver",
            // SQL Server
            "com.microsoft.sqlserver.jdbc.SQLServerDriver",
            // MariaDB
            "org.mariadb.jdbc.Driver",
            // 达梦
            "dm.jdbc.driver.DmDriver",
            // 人大金仓
            "com.kingbase8.Driver",
            // DB2
            "com.ibm.db2.jcc.DB2Driver",
    };

    /**
     * 校验 JDBC URL 是否包含危险参数
     *
     * @param jdbcUrl JDBC 连接地址
     * @throws JeecgBootException 包含危险参数时抛出
     */
    public static void validate(String jdbcUrl) {
        if (oConvertUtils.isEmpty(jdbcUrl)) {
            return;
        }

        String lowerUrl = jdbcUrl.toLowerCase();

        for (String unsafeParam : UNSAFE_PARAMS) {
            if (lowerUrl.contains(unsafeParam)) {
                throw new JeecgBootException("连接地址有安全风险，包含不安全参数【" + unsafeParam + "】");
            }
        }
    }

    /**
     * 校验驱动类名是否在白名单中
     *
     * @param driverClassName JDBC 驱动类名
     * @throws JeecgBootException 驱动不在白名单时抛出
     */
    public static void validateDriver(String driverClassName) {
        if (oConvertUtils.isEmpty(driverClassName)) {
            throw new JeecgBootException("数据库驱动类名不能为空");
        }
        for (String allowed : ALLOWED_DRIVERS) {
            if (allowed.equals(driverClassName)) {
                return;
            }
        }
        throw new JeecgBootException("不支持的数据库驱动【" + driverClassName + "】，如需支持请联系管理员");
    }
}
