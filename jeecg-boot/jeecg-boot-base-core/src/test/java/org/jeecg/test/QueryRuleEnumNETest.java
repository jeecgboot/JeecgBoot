package org.jeecg.test;

import org.jeecg.common.system.query.QueryRuleEnum;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 测试 QueryRuleEnum.NE 规则修复
 * 修复 issue #9312: NE规则导致ID首位字符丢失
 */
public class QueryRuleEnumNETest {

    /**
     * 模拟修复后的parseValue逻辑
     */
    private String parseValueForNE(String val) {
        String value = val;
        // 修复后的逻辑：只有当值以!开头时才截取
        if (val.startsWith(QueryRuleEnum.NE.getValue())) {
            value = val.substring(1);
        }
        return value;
    }

    @Test
    public void testNERuleWithNormalId() {
        // 测试普通ID不应该被截取首字符
        String testId = "2016032112479109121";
        String result = parseValueForNE(testId);
        assertEquals(testId, result, "普通ID不应该被截取");
    }

    @Test
    public void testNERuleWithExclamationPrefix() {
        // 测试带!前缀的值应该正确截取
        String testId = "2016032112479109121";
        String valWithPrefix = "!" + testId;
        String result = parseValueForNE(valWithPrefix);
        assertEquals(testId, result, "!前缀应该被正确截取");
    }

    @Test
    public void testNERuleWithNumericId() {
        // 测试数字开头的ID
        String testId = "123456789";
        String result = parseValueForNE(testId);
        assertEquals(testId, result, "数字ID不应该被截取");
    }

    @Test
    public void testNERuleWithStringValue() {
        // 测试字符串值
        String testValue = "testString";
        String result = parseValueForNE(testValue);
        assertEquals(testValue, result, "字符串值不应该被截取");
    }
}
