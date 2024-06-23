package org.jeecg.modules.system.test;

import org.jeecg.JeecgSystemApplication;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.config.firewall.SqlInjection.IDictTableWhiteListHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description: 系统表白名单测试
 * @Author: sunjianlei
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = JeecgSystemApplication.class)
public class SysTableWhiteCheckTest {

    @Autowired
    IDictTableWhiteListHandler whiteListHandler;
    @Autowired
    ISysBaseAPI sysBaseAPI;

    @Autowired
    JeecgBaseConfig jeecgBaseConfig;

    @Before
    public void before() {
        String lowCodeMode = this.jeecgBaseConfig.getFirewall().getLowCodeMode();
        System.out.println("当前 LowCode 模式为: " + lowCodeMode);
        // 清空缓存，防止影响测试
        whiteListHandler.clear();
    }

    @Test
    public void testSql() {
        System.out.println("=== 开始测试 SQL 方式 ===");
        String[] sqlArr = new String[]{
                "select username from sys_user",
                "select username, CONCAT(realname, SEX) from SYS_USER",
                "select username, CONCAT(realname, sex) from sys_user",
        };
        for (String sql : sqlArr) {
            System.out.println("- 测试Sql: " + sql);
            try {
                sysBaseAPI.dictTableWhiteListCheckBySql(sql);
                System.out.println("-- 测试通过");
            } catch (Exception e) {
                System.out.println("-- 测试未通过: " + e.getMessage());
            }
        }
        System.out.println("=== 结束测试 SQL 方式 ===");
    }

    @Test
    public void testDict() {
        System.out.println("=== 开始测试 DICT 方式 ===");

        String table = "sys_user";
        String code = "username";
        String text = "realname";
        this.testDict(table, code, text);

        table = "sys_user";
        code = "username";
        text = "CONCAT(realname, sex)";
        this.testDict(table, code, text);

        table = "SYS_USER";
        code = "username";
        text = "CONCAT(realname, SEX)";
        this.testDict(table, code, text);

        System.out.println("=== 结束测试 DICT 方式 ===");
    }

    private void testDict(String table, String code, String text) {
        try {
            sysBaseAPI.dictTableWhiteListCheckByDict(table, code, text);
            System.out.println("- 测试通过");
        } catch (Exception e) {
            System.out.println("- 测试未通过: " + e.getMessage());
        }
    }

}
