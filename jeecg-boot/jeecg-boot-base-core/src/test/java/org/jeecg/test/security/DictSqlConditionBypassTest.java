package org.jeecg.test.security;

import org.jeecg.common.exception.JeecgSqlInjectionException;
import org.jeecg.common.util.SensitiveTableCheckUtil;
import org.jeecg.common.util.SqlInjectionUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 字典过滤条件 SQL 注入绕过 — 单元测试
 *
 * GHSA-jgwm-2xxm-mmm4：反斜杠导致 JSqlParser 与 MySQL 对字符串边界理解不一致
 * GHSA-xmw4-rx44-4jcp：MySQL 的 # 注释、password# 字段名绕过敏感字段检查
 * GHSA-g87f-mgx5-wj9c：字面量 null 表名被当成空表名放行
 *
 * @author scott
 * @since 2026-10-27 GHSA-jgwm/GHSA-xmw4/GHSA-g87f 字典条件注入加固
 */
@ExtendWith(PrintTestResultExtension.class)
public class DictSqlConditionBypassTest {

    private static void check(String filterSql) {
        SqlInjectionUtil.filterDictConditionSqlFromRequest("sys_user", filterSql);
    }

    @Test
    @DisplayName("jgwm：反斜杠闭合引号的 IN 列表被拦截")
    void backslashInListBlocked() {
        assertThrows(JeecgSqlInjectionException.class, () -> check("id IN ('a\\',') or 1=1#')"));
        assertThrows(JeecgSqlInjectionException.class,
                () -> check("id IN ('a\\',') or (select ascii(substring(password from 1 for 1)) from sys_user where username=0x61646d696e)>0#')"));
    }

    @Test
    @DisplayName("jgwm：不带 # 的反斜杠变体同样被拦截")
    void backslashWithoutCommentBlocked() {
        assertThrows(JeecgSqlInjectionException.class, () -> check("username = 'a\\'' or username = ''"));
    }

    @Test
    @DisplayName("xmw4：password# 字段名与 # 注释被拦截")
    void hashCommentBlocked() {
        assertThrows(JeecgSqlInjectionException.class, () -> check("username='admin' and password# like '\nlike \"c%\"#'"));
        assertThrows(JeecgSqlInjectionException.class, () -> check("username='admin' # comment"));
    }

    @Test
    @DisplayName("回归：引号内的 # 与正常条件放行")
    void normalConditionAllowed() {
        assertDoesNotThrow(() -> check("username = 'a#b'"));
        assertDoesNotThrow(() -> check("status = '1' and sex in ('1','2') order by create_time desc"));
        assertDoesNotThrow(() -> check("u.username like 'adm%' and `realname` is not null"));
    }

    @Test
    @DisplayName("回归：直接使用敏感字段仍被拦截")
    void sensitiveFieldBlocked() {
        assertThrows(JeecgSqlInjectionException.class, () -> check("username='admin' and password like 'c%'"));
    }

    @Test
    @DisplayName("g87f：字面量 null 表名不再放行")
    void literalNullTableBlocked() {
        assertThrows(JeecgSqlInjectionException.class, () -> SensitiveTableCheckUtil.checkForbiddenFields("null", "password"));
        assertThrows(JeecgSqlInjectionException.class, () -> SensitiveTableCheckUtil.checkForbiddenFields(" NULL ", "password"));
        assertDoesNotThrow(() -> SensitiveTableCheckUtil.checkForbiddenFields("", "password"));
        assertDoesNotThrow(() -> SensitiveTableCheckUtil.checkForbiddenFields("sys_user", "username"));
    }
}
