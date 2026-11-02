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
            // 【安全 GHSA-jq87-2rf2-wvm3】pgjdbc 日志参数会初始化 FileHandler 向指定路径写文件（连接失败也写），需拦截
            "loggerlevel",                // 日志级别（触发 FileHandler 初始化）
            "loggerfile",                 // 日志文件路径（任意文件写入）

            // === H2 ===
            "init=",                      // 连接初始化 SQL (带 '=' 防止匹配到正常单词 'init')
            "runscript",                  // 远程/本地 SQL 脚本加载
            "trace_level_system_out",     // 系统信息泄露

            // === Apache Calcite ===
            "jdbc:calcite:",              // Calcite 连接协议：可经 model=inline 的 functions 注册任意类 UDF 实现 RCE
    };

    /**
     * 危险 JDBC 驱动黑名单（小写比较）。仅拦截已知可被用于任意代码执行的驱动，
     * 其余驱动（含各类国产/大数据数据库自定义驱动）一律放行，避免误伤正常业务。
     */
    private static final String[] FORBIDDEN_DRIVERS = {
            // Apache Calcite：可经 model=inline 的 functions 注册任意类为 UDF 实现 RCE
            "org.apache.calcite.jdbc.driver",
            "org.apache.calcite.jdbc.calcitedriver",
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
     * 校验驱动类名是否为已知危险驱动（黑名单）。
     * 驱动为空时放行（由 DriverManager 按 URL 自动识别，URL 已由 {@link #validate(String)} 拦掉危险协议）；
     * 命中黑名单（如 Calcite）时抛出异常。
     *
     * @param driverClassName JDBC 驱动类名
     * @throws JeecgBootException 命中危险驱动黑名单时抛出
     */
    public static void validateDriver(String driverClassName) {
        if (oConvertUtils.isEmpty(driverClassName)) {
            return;
        }
        String lower = driverClassName.trim().toLowerCase();
        for (String forbidden : FORBIDDEN_DRIVERS) {
            if (forbidden.equals(lower)) {
                throw new JeecgBootException("不支持的数据库驱动【" + driverClassName + "】，存在安全风险，已拒绝");
            }
        }
    }
}