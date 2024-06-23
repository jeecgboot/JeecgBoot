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
public class TestSqlInjectForDict {
    /**
     * 注入测试
     *
     * @param sql
     * @return
     */
    private boolean isExistSqlInject(String sql) {
        try {
            SqlInjectionUtil.specialFilterContentForDictSql(sql);
            return false;
        } catch (Exception e) {
            log.info("===================================================");
            return true;
        }
    }


    @Test
    public void test() throws JSQLParserException {
        //不存在sql注入
        assertFalse(isExistSqlInject("sys_user,realname,id"));
        assertFalse(isExistSqlInject("oa_officialdoc_organcode,organ_name,id"));
        assertFalse(isExistSqlInject("onl_cgform_head where table_type!=3 and copy_type=0,table_txt,table_name"));
        assertFalse(isExistSqlInject("onl_cgform_head where copy_type = 0,table_txt,table_name"));

        //存在sql注入
        assertTrue(isExistSqlInject("or 1= 1 --"));
        assertTrue(isExistSqlInject("select * from test where sleep(%23)"));
    }

}

