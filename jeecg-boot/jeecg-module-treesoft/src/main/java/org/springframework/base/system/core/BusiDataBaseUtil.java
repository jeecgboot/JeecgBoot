package org.springframework.base.system.core;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.base.system.utils.CryptoUtil;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 业务数据库操作工具
 *
 * @author 00fly
 * @version [版本号, 2018年11月22日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Slf4j
public class BusiDataBaseUtil {

    private static String driver;

    private static String url;

    private static String userName;

    private static String passWord;

    private final SysDataBaseUtil sysDataBaseUtil = new SysDataBaseUtil();

    public BusiDataBaseUtil(String dbName, String databaseConfigId) {
        String sql = " select database_type as databaseType, user_name as userName, password, port, ip, url from treesoft_config where id=?";
        Map<String, Object> map = sysDataBaseUtil.queryForMap(sql, databaseConfigId);
        String ip = (String) map.get("ip");
        String port = (String) map.get("port");
        String databaseType = (String) map.get("databaseType");
        switch (databaseType) {
            case "MySql":
            case "MariaDB":
                driver = "com.mysql.cj.jdbc.Driver";
                url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?characterEncoding=utf8&tinyInt1isBit=false&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true";
                break;
            case "Oracle":
                driver = "oracle.jdbc.driver.OracleDriver";
                url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + dbName;
                break;
            case "PostgreSQL":
                driver = "org.postgresql.Driver";
                url = "jdbc:postgresql://" + ip + ":" + port + "/" + dbName;
                break;
            case "MSSQL":
                driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                url = "jdbc:sqlserver://" + ip + ":" + port + ";database=" + dbName;
                break;
            case "Hive2":
                driver = "org.apache.hive.jdbc.HiveDriver";
                url = "jdbc:hive2://" + ip + ":" + port + "/" + dbName;
                break;
            default:
                break;
        }
        userName = (String) map.get("userName");
        passWord = CryptoUtil.decode(map.get("password").toString());
        if (passWord.contains("`")) {
            passWord = StringUtils.substringAfter(passWord, "`");
        }
    }

    private synchronized Connection getConnection() {
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, userName, passWord);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static boolean testConnection(String databaseType, String databaseName, String ip, String port, String user, String password) {
        try {
            String url = "";
            if (databaseType.equals("MySql")) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                url = "jdbc:mysql://" + ip + ":" + port + "/" + databaseName + "?characterEncoding=utf8";
            }
            if (databaseType.equals("MariaDB")) {
                Class.forName("com.mysql.jdbc.Driver");
                url = "jdbc:mysql://" + ip + ":" + port + "/" + databaseName + "?characterEncoding=utf8";
            }
            if (databaseType.equals("Oracle")) {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                url = "jdbc:oracle:thin:@" + ip + ":" + port + ":" + databaseName;
            }
            if (databaseType.equals("PostgreSQL")) {
                Class.forName("org.postgresql.Driver");
                url = "jdbc:postgresql://" + ip + ":" + port + "/" + databaseName;
            }
            if (databaseType.equals("MSSQL")) {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                url = "jdbc:sqlserver://" + ip + ":" + port + ";database=" + databaseName;
            }
            if (databaseType.equals("Hive2")) {
                Class.forName("org.apache.hive.jdbc.HiveDriver");
                url = "jdbc:hive2://" + ip + ":" + port + "/" + databaseName;
            }
            Connection conn = DriverManager.getConnection(url, user, password);
            return (conn != null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    public int setupdateData(String sql) throws Exception {
        @Cleanup
        Connection conn = getConnection();
        @Cleanup
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            return stmt.executeUpdate(sql);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            throw new Exception(e.getMessage());
        }
    }

    public int updateExecuteBatch(List<String> sqlList) throws Exception {
        @Cleanup
        Connection conn = getConnection();
        @Cleanup
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            conn.setAutoCommit(false);
            for (String sql : sqlList) {
                sql = sql.replaceAll(";", "");
                stmt.addBatch(sql);
            }
            int[] updateCounts = stmt.executeBatch();
            conn.commit();
            return updateCounts.length;
        } catch (Exception e) {
            conn.rollback();
            log.error(e.getMessage(), e);
            throw new Exception(e.getMessage());
        }
    }

    public List<Map<String, Object>> queryForList(String sql) throws Exception {
        @Cleanup
        Connection conn = null;
        @Cleanup
        PreparedStatement pstmt = null;
        @Cleanup
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int maxSize = -1;
        String[] fields = null;
        List<String> times = new ArrayList<>();
        List<String> clob = new ArrayList<>();
        List<String> binary = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        conn = getConnection();
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        rsmd = rs.getMetaData();
        maxSize = rsmd.getColumnCount();
        fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++) {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if (("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1))) || ("oracle.sql.TIMESTAMP".equals(rsmd.getColumnClassName(i + 1)))) {
                times.add(fields[i]);
            }
            if (("oracle.jdbc.OracleClob".equals(rsmd.getColumnClassName(i + 1))) || ("oracle.jdbc.OracleBlob".equals(rsmd.getColumnClassName(i + 1)))) {
                clob.add(fields[i]);
            }
            if ("[B".equals(rsmd.getColumnClassName(i + 1))) {
                binary.add(fields[i]);
            }
        }
        Map<String, Object> row = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (rs.next()) {
            row = new LinkedHashMap<>();
            for (int i = 0; i < maxSize; i++) {
                Object value = times.contains(fields[i]) ? rs.getTimestamp(fields[i]) : rs.getObject(fields[i]);
                if ((times.contains(fields[i])) && (value != null)) {
                    value = sdf.format(value);
                }
                if ((clob.contains(fields[i])) && (value != null)) {
                    value = "(Blob)";
                }
                if ((binary.contains(fields[i])) && (value != null)) {
                    value = new String((byte[]) value);
                }
                row.put(fields[i], value);
            }
            rows.add(row);
        }
        return rows;
    }

    public List<Map<String, Object>> queryForListForPostgreSQL(String sql) throws Exception {
        @Cleanup
        Connection conn = null;
        @Cleanup
        PreparedStatement pstmt = null;
        @Cleanup
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int maxSize = -1;
        String[] fields;
        List<String> times = new ArrayList<>();
        List<String> binary = new ArrayList<>();
        List<String> object = new ArrayList<>();
        List<Map<String, Object>> rows = new ArrayList<>();
        conn = getConnection();
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        rsmd = rs.getMetaData();
        maxSize = rsmd.getColumnCount();
        fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++) {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if (("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1))) || ("oracle.sql.TIMESTAMP".equals(rsmd.getColumnClassName(i + 1)))) {
                times.add(fields[i]);
            }
            if ("java.lang.Object".equals(rsmd.getColumnClassName(i + 1))) {
                object.add(fields[i]);
            }
            if ("[B".equals(rsmd.getColumnClassName(i + 1))) {
                binary.add(fields[i]);
            }
        }
        Map<String, Object> row;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (rs.next()) {
            row = new LinkedHashMap<>();
            for (int i = 0; i < maxSize; i++) {
                Object value = times.contains(fields[i]) ? rs.getTimestamp(fields[i]) : rs.getObject(fields[i]);
                if ((times.contains(fields[i])) && (value != null)) {
                    value = sdf.format(value);
                }
                try {
                    if ((binary.contains(fields[i])) && (value != null)) {
                        value = new String((byte[]) value);
                    }
                    if ((object.contains(fields[i])) && (value != null)) {
                        value = value.toString();
                    }
                } catch (Exception e) {
                    value = "(Object)";
                }
                row.put(fields[i], value);
            }
            rows.add(row);
        }
        return rows;
    }

    public List<Map<String, Object>> queryForList2(String sql)
            throws Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int maxSize = -1;
        conn = getConnection();
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        rsmd = rs.getMetaData();
        maxSize = rsmd.getColumnCount();
        List<Map<String, Object>> rows = new ArrayList<>();
        Map<String, Object> row;
        while (rs.next()) {
            row = new HashMap<>();
            for (int i = 0; i < maxSize; i++) {
                row.put(rsmd.getColumnLabel(i + 1), rs.getObject(rsmd.getColumnLabel(i + 1)));
            }
            rows.add(row);
        }
        return rows;
    }

    public List<Map<String, Object>> queryForListPageForMSSQL(String sql, int maxRow, int beginIndex) throws Exception {
        @Cleanup
        Connection conn = null;
        @Cleanup
        PreparedStatement pstmt = null;
        @Cleanup
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int maxSize = -1;
        String[] fields;
        List<String> times = new ArrayList<>();
        conn = getConnection();
        pstmt = conn.prepareStatement(sql, 1005, 1008);
        pstmt.setMaxRows(maxRow);
        rs = pstmt.executeQuery();
        rsmd = rs.getMetaData();
        maxSize = rsmd.getColumnCount();
        fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++) {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if ("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1))) {
                times.add(fields[i]);
            }
        }
        List<Map<String, Object>> rows = new ArrayList<>();
        Map<String, Object> row;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        rs.absolute(beginIndex);
        while (rs.next()) {
            row = new HashMap<>();
            for (int i = 0; i < maxSize; i++) {
                Object value = times.contains(fields[i]) ? rs.getTimestamp(fields[i]) : rs.getObject(fields[i]);
                if ((times.contains(fields[i])) && (value != null)) {
                    value = sdf.format(value);
                }
                row.put(fields[i], value);
            }
            rows.add(row);
        }
        return rows;
    }

    public List<Map<String, Object>> queryForListPageForHive2(String sql, int maxRow, int beginIndex) throws Exception {
        @Cleanup
        Connection conn = null;
        @Cleanup
        PreparedStatement pstmt = null;
        @Cleanup
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int maxSize = -1;
        String[] fields;
        List<String> times = new ArrayList<>();
        conn = getConnection();
        pstmt = conn.prepareStatement(sql);
        pstmt.setMaxRows(maxRow);
        rs = pstmt.executeQuery();
        rsmd = rs.getMetaData();
        maxSize = rsmd.getColumnCount();
        fields = new String[maxSize];
        for (int i = 0; i < maxSize; i++) {
            fields[i] = rsmd.getColumnLabel(i + 1);
            if ("java.sql.Timestamp".equals(rsmd.getColumnClassName(i + 1))) {
                times.add(fields[i]);
            }
        }
        List<Map<String, Object>> rows = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> row = null;
        while (rs.next()) {
            row = new LinkedHashMap<>();
            for (int i = 0; i < maxSize; i++) {
                Object value = times.contains(fields[i]) ? rs.getTimestamp(fields[i]) : rs.getObject(fields[i]);
                if ((times.contains(fields[i])) && (value != null)) {
                    value = sdf.format(value);
                }
                row.put(fields[i], value);
            }
            rows.add(row);
        }
        return rows;
    }

    public List<Map<String, Object>> queryForListWithType(String sql) {

        List<Map<String, Object>> rows2 = new ArrayList<>();
        try {
            @Cleanup
            Connection conn = getConnection();
            @Cleanup
            PreparedStatement pstmt = conn.prepareStatement(sql);
            @Cleanup
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsme = rs.getMetaData();
            int columnCount = rsme.getColumnCount();
            rs.next();
            for (int i = 1; i < columnCount + 1; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("column_name", rsme.getColumnName(i));
                map.put("column_value", rs.getObject(rsme.getColumnName(i)));
                map.put("data_type", rsme.getColumnTypeName(i));
                map.put("precision", Integer.valueOf(rsme.getPrecision(i)));
                map.put("isAutoIncrement", Boolean.valueOf(rsme.isAutoIncrement(i)));
                map.put("is_nullable", Integer.valueOf(rsme.isNullable(i)));
                map.put("isReadOnly", Boolean.valueOf(rsme.isReadOnly(i)));
                rows2.add(map);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return rows2;
    }

    public List<Map<String, Object>> queryForColumnOnly(String sql) {

        List<Map<String, Object>> rows2 = new ArrayList<>();
        try {
            @Cleanup
            Connection conn = getConnection();
            @Cleanup
            PreparedStatement pstmt = conn.prepareStatement(sql);
            @Cleanup
            ResultSet rs = pstmt.executeQuery();
            ResultSetMetaData rsme = rs.getMetaData();
            int columnCount = rsme.getColumnCount();
            for (int i = 1; i < columnCount + 1; i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("column_name", rsme.getColumnName(i));
                map.put("data_type", rsme.getColumnTypeName(i));
                map.put("precision", Integer.valueOf(rsme.getPrecision(i)));
                map.put("isAutoIncrement", Boolean.valueOf(rsme.isAutoIncrement(i)));
                map.put("is_nullable", Integer.valueOf(rsme.isNullable(i)));
                map.put("isReadOnly", Boolean.valueOf(rsme.isReadOnly(i)));
                rows2.add(map);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return rows2;
    }

    public List<Map<String, Object>> executeSqlForColumns(String sql) throws Exception {
        @Cleanup
        Connection conn = null;
        @Cleanup
        PreparedStatement pstmt = null;
        @Cleanup
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        int maxSize = -1;
        conn = getConnection();
        List<Map<String, Object>> rows = new ArrayList<>();
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        rsmd = rs.getMetaData();
        maxSize = rsmd.getColumnCount();
        for (int i = 0; i < maxSize; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("column_name", rsmd.getColumnLabel(i + 1));
            map.put("data_type", rsmd.getColumnTypeName(i + 1));
            rows.add(map);
        }
        return rows;
    }

    public int executeQueryForCount(String sql) {
        int rowCount = 0;
        try {
            @Cleanup
            Connection conn = getConnection();
            @Cleanup
            Statement stmt = conn.createStatement();
            @Cleanup
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Object count = rs.getObject("count(*)");
                rowCount = Integer.parseInt(count.toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return rowCount;
    }

    public int executeQueryForCount2(String sql) {
        int rowCount = 0;
        try {
            String time = String.valueOf(System.currentTimeMillis() / 1000);
            @Cleanup
            Connection conn = getConnection();
            @Cleanup
            Statement stmt = conn.createStatement();
            @Cleanup
            ResultSet rs = stmt.executeQuery("select count(*) from (" + sql + ") tableCount" + time);
            while (rs.next()) {
                Object count = rs.getObject("count(*)");
                rowCount = Integer.parseInt(count.toString());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return rowCount;
    }

    public int executeQueryForCountForOracle(String sql) {
        int rowCount = 0;
        String sql3 = " select count(*) as count from  (" + sql + ")";
        try {
            @Cleanup
            Connection conn = getConnection();
            @Cleanup
            Statement stmt = conn.createStatement();
            @Cleanup
            ResultSet rs = stmt.executeQuery(sql3);
            rs.next();
            rowCount = rs.getInt("count");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return rowCount;
    }

    public int executeQueryForCountForPostgreSQL(String sql) {
        int rowCount = 0;
        String sql3 = " select count(*) as count from  (" + sql + ") t ";
        try {
            @Cleanup
            Connection conn = getConnection();
            @Cleanup
            Statement stmt = conn.createStatement();
            @Cleanup
            ResultSet rs = stmt.executeQuery(sql3);
            rs.next();
            rowCount = rs.getInt("count");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return rowCount;
    }
}
