package org.jeecg.test.sqlinjection;

import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import org.jeecg.common.util.SqlInjectionUtil;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * SQL注入攻击检查测试
 * @author: liusq
 * @date: 2023年09月08日
 */
@Slf4j
public class TestSqlInjectForOnlineReport {
    /**
     * 注入测试
     *
     * @param sql
     * @return
     */
    private boolean isExistSqlInject(String sql) {
        try {
            SqlInjectionUtil.specialFilterContentForOnlineReport(sql);
            return false;
        } catch (Exception e) {
            log.info("===================================================");
            return true;
        }
    }


    @Test
    public void test() throws JSQLParserException {
        //不存在sql注入
        assertFalse(isExistSqlInject("select * from fm_time where dept_id=:sqlparamsmap.id and time=:sqlparamsmap.time"));
        assertFalse(isExistSqlInject("select * from test"));
        assertFalse(isExistSqlInject("select load_file(\"C:\\\\benben.txt\")"));
        assertFalse(isExistSqlInject("select * from dc_device where id in (select id from other)"));
        assertFalse(isExistSqlInject("select * from dc_device  UNION select name from other"));

        //存在sql注入
        assertTrue(isExistSqlInject("(SELECT 6240 FROM (SELECT(SLEEP(5))and 1=2)vidl)"));
        assertTrue(isExistSqlInject("or 1= 1 --"));
        assertTrue(isExistSqlInject("select * from test where sleep(%23)"));
        assertTrue(isExistSqlInject("select * from test where SLEEP(3)"));
        assertTrue(isExistSqlInject("select * from test where id=1 and multipoint((select * from(select * from(select user())a)b));"));
        assertTrue(isExistSqlInject("select * from users;show databases;"));
        assertTrue(isExistSqlInject("select * from dc_device where id=1 and length((select group_concat(table_name) from information_schema.tables where table_schema=database()))>13"));
        assertTrue(isExistSqlInject("update user set name = '123'"));
        assertTrue(isExistSqlInject("SELECT * FROM users WHERE username = 'admin' AND password = '123456' OR 1=1;--"));
        assertTrue(isExistSqlInject("select * from users where id=1 and (select count(*) from information_schema.tables where table_schema='数据库名')>4 %23"));
        assertTrue(isExistSqlInject("select * from dc_device where sleep(5) %23"));
    }

}

