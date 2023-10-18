package org.jeecg.test.sqlinjection;

import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import org.jeecg.common.util.SqlInjectionUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Description: SQL注入测试类
 * @author: scott
 * @date: 2023年08月14日 9:55
 */
public class TestSqlInjection {

    
    /**
     * 表名带别名，同时有html编码字符
     */
    @Test
    public void testSpecialSQL() {
        String tableName = "sys_user    t";
        //解决使用参数tableName=sys_user t&复测，漏洞仍然存在
        if (tableName.contains(" ")) {
            tableName = tableName.substring(0, tableName.indexOf(" "));
        }
        //【issues/4393】 sys_user , (sys_user), sys_user%20, %60sys_user%60
        String reg = "\\s+|\\(|\\)|`";
        tableName = tableName.replaceAll(reg, "");
        System.out.println(tableName);
    }


    /**
     * 测试sql是否含sql注入风险
     * <p>
     * mybatis plus的方法
     */
    @Test
    public void sqlInjectionCheck() {
        String sql = "select * from sys_user";
        System.out.println(SqlInjectionUtils.check(sql));
    }


    /**
     * 测试sql是否有SLEEP风险
     * <p>
     *  mybatisPlus的方法
     */
    @Test
    public void sqlSleepCheck() {
        SqlInjectionUtil.checkSqlAnnotation("(SELECT 6240 FROM (SELECT(SLEEP(5))and 1=2)vidl)");
    }

    /**
     * 测试sql是否含sql注入风险
     * <p>
     * 自定义方法
     */
    @Test
    public void sqlInjectionCheck2() {
        String sql = "select * from sys_user";
        SqlInjectionUtil.specialFilterContentForOnlineReport(sql);
    }

    /**
     * 字段定义只能是是字母 数字 下划线的组合（不允许有空格、转义字符串等）
     * <p>
     * 判断字段名是否符合规范
     */
    @Test
    public void testFieldSpecification() {
        List<String> list = new ArrayList();
        list.add("Hello World!");
        list.add("Hello%20World!");
        list.add("HelloWorld!");
        list.add("Hello World");
        list.add("age");
        list.add("user_name");
        list.add("user_name%20");
        list.add("user_name%20 ");

        for (String input : list) {
            boolean containsSpecialChars = isValidString(input);
            System.out.println("input:" + input + " ,包含空格和特殊字符: " + containsSpecialChars);
        }
    }

    /**
     * 字段定义只能是是字母 数字 下划线的组合（不允许有空格、转义字符串等）
     *
     * @param input
     * @return
     */
    private static boolean isValidString(String input) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_]+$");
        return pattern.matcher(input).matches();
    }

}
