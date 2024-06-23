package org.jeecg.test.sqlparse;

import net.sf.jsqlparser.JSQLParserException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.common.util.sqlparse.JSqlParserUtils;
import org.jeecg.common.util.sqlparse.vo.SelectSqlInfo;
import org.junit.Test;

import java.util.Map;

/**
 * 针对 JSqlParserUtils 的单元测试
 */
public class JSqlParserUtilsTest {

    private static final String[] sqlList = new String[]{
            "select * from sys_user",
            "select u.* from sys_user u",
            "select u.*, c.name from sys_user u, demo c",
            "select u.age, c.name from sys_user u, demo c",
            "select sex, age, c.name from sys_user, demo c",
            // 别名测试
            "select username as realname from sys_user",
            "select username as realname, u.realname as aaa, u.id bbb from sys_user u",
            // 不存在真实地查询字段
            "select count(1) from sys_user",
            // 函数式字段
            "select max(sex), id from sys_user",
            // 复杂嵌套函数式字段
            "select CONCAT(CONCAT(' _ ', sex), ' - ' , birthday) as info, id from sys_user",
            // 更复杂的嵌套函数式字段
            "select CONCAT(CONCAT(101,'_',NULL, DATE(create_time),'_',sex),' - ',birthday) as info, id from sys_user",
            // 子查询SQL
            "select u.name1 as name2 from (select username as name1 from sys_user) u",
            // 多层嵌套子查询SQL
            "select u2.name2 as name3 from (select u1.name1 as name2 from (select username as name1 from sys_user) u1) u2",
            // 字段子查询SQL
            "select id, (select username as name1 from sys_user u2 where u1.id = u2.id) as name2 from sys_user u1",
            // 带条件的SQL（不解析where条件里的字段，但不影响解析查询字段）
            "select username as name1 from sys_user where realname LIKE '%张%'",
            // 多重复杂关联表查询解析，包含的表为：sys_user, sys_depart, sys_dict_item, demo
            "" +
                    "SELECT " +
                    "    u.*, d.age, sd.item_text AS sex, (SELECT count(sd.id) FROM sys_depart sd) AS count " +
                    "FROM " +
                    "    (SELECT sd.username AS foo, sd.realname FROM sys_user sd) u, " +
                    "    demo d " +
                    "LEFT JOIN sys_dict_item AS sd ON d.sex = sd.item_value " +
                    "WHERE sd.dict_id = '3d9a351be3436fbefb1307d4cfb49bf2'",
    };

    @Test
    public void testParseSelectSql() {
        System.out.println("-----------------------------------------");
        for (String sql : sqlList) {
            System.out.println("待测试的sql：" + sql);
            try {
                // 解析所有的表名，key=表名，value=解析后的sql信息
                Map<String, SelectSqlInfo> parsedMap = JSqlParserUtils.parseAllSelectTable(sql);
                assert parsedMap != null;
                for (Map.Entry<String, SelectSqlInfo> entry : parsedMap.entrySet()) {
                    System.out.println("表名：" + entry.getKey());
                    this.printSqlInfo(entry.getValue(), 1);
                }
            } catch (JSQLParserException e) {
                System.out.println("SQL解析出现异常：" + e.getMessage());
            }
            System.out.println("-----------------------------------------");
        }
    }

    private void printSqlInfo(SelectSqlInfo sqlInfo, int level) {
        String beforeStr = this.getBeforeStr(level);
        if (sqlInfo.getFromTableName() == null) {
            // 子查询
            System.out.println(beforeStr + "子查询：" + sqlInfo.getFromSubSelect().getParsedSql());
            this.printSqlInfo(sqlInfo.getFromSubSelect(), level + 1);
        } else {
            // 非子查询
            System.out.println(beforeStr + "查询的表名：" + sqlInfo.getFromTableName());
        }
        if (oConvertUtils.isNotEmpty(sqlInfo.getFromTableAliasName())) {
            System.out.println(beforeStr + "查询的表别名：" + sqlInfo.getFromTableAliasName());
        }
        if (sqlInfo.isSelectAll()) {
            System.out.println(beforeStr + "查询的字段：*");
        } else {
            System.out.println(beforeStr + "查询的字段：" + sqlInfo.getSelectFields());
            System.out.println(beforeStr + "真实的字段：" + sqlInfo.getRealSelectFields());
            if (sqlInfo.getFromTableName() == null) {
                System.out.println(beforeStr + "所有的字段（包括子查询）：" + sqlInfo.getAllRealSelectFields());
            }
        }
    }

    // 打印前缀，根据层级来打印
    private String getBeforeStr(int level) {
        if (level == 0) {
            return "";
        }
        StringBuilder beforeStr = new StringBuilder();
        for (int i = 0; i < level; i++) {
            beforeStr.append("  ");
        }
        beforeStr.append("- ");
        return beforeStr.toString();
    }

}
