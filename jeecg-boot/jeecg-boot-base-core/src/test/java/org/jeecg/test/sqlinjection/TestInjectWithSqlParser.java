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
public class TestInjectWithSqlParser {
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
        assertFalse(isExistSqlInject("WITH SUB1 AS (SELECT user FROM t1) SELECT * FROM T2 WHERE id > 123 "));

        //存在sql注入
        assertTrue(isExistSqlInject("or 1= 1 --"));
        assertTrue(isExistSqlInject("select * from test where sleep(%23)"));
        assertTrue(isExistSqlInject("select * from test where id=1 and multipoint((select * from(select * from(select user())a)b));"));
        assertTrue(isExistSqlInject("select * from users;show databases;"));
        assertTrue(isExistSqlInject("select * from dc_device where id=1 and length((select group_concat(table_name) from information_schema.tables where table_schema=database()))>13"));
        assertTrue(isExistSqlInject("update user set name = '123'"));
        assertTrue(isExistSqlInject("SELECT * FROM users WHERE username = 'admin' AND password = '123456' OR 1=1;--"));
        assertTrue(isExistSqlInject("select * from users where id=1 and (select count(*) from information_schema.tables where table_schema='数据库名')>4 %23"));
        assertTrue(isExistSqlInject("select * from dc_device where sleep(5) %23"));
        assertTrue(isExistSqlInject("select * from dc_device where id in (select id from other)"));
        assertTrue(isExistSqlInject("select * from dc_device where id in (select id from other)"));
        assertTrue(isExistSqlInject("select * from dc_device where 2=2.0 or 2 != 4"));
        assertTrue(isExistSqlInject("select * from dc_device where 1!=2.0"));
        assertTrue(isExistSqlInject("select * from dc_device where id=floor(2.0)"));
        assertTrue(isExistSqlInject("select * from dc_device where not true"));
        assertTrue(isExistSqlInject("select * from dc_device where 1 or id > 0"));
        assertTrue(isExistSqlInject("select * from dc_device where 'tom' or id > 0"));
        assertTrue(isExistSqlInject("select * from dc_device where '-2.3' "));
        assertTrue(isExistSqlInject("select * from dc_device where 2 "));
        assertTrue(isExistSqlInject("select * from dc_device where (3+2) "));
        assertTrue(isExistSqlInject("select * from dc_device where  -1 IS TRUE"));
        assertTrue(isExistSqlInject("select * from dc_device where 'hello' is null "));
        assertTrue(isExistSqlInject("select * from dc_device where '2022-10-31' and id > 0"));
        assertTrue(isExistSqlInject("select * from dc_device where id > 0 or 1!=2.0 "));
        assertTrue(isExistSqlInject("select * from dc_device where id > 0 or 1 in (1,3,4) "));
        assertTrue(isExistSqlInject("select * from dc_device  UNION select name from other"));
        assertTrue(isExistSqlInject("(SELECT 6240 FROM (SELECT(SLEEP(5))and 1=2)vidl)"));
    }

}

