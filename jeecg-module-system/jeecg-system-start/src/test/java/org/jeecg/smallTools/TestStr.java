package org.jeecg.smallTools;

import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Base64;

/**
 * 字符串处理测试
 *
 * @author: scott
 * @date: 2023年03月30日 15:27
 */
public class TestStr {

    /**
     * 测试参数格式化的问题，数字值有问题
     */
    @Test
    public void testParameterFormat() {
        String url = "/pages/lowApp/process/taskDetail?tenantId={0}&procInsId={1}&taskId={2}&taskDefKey={3}";
        String cc = MessageFormat.format(url, "6364", "111", "22", "333");
        System.out.println("参数是字符串：" + cc);

        String cc2 = MessageFormat.format(url, 6364, 111, 22, 333);
        System.out.println("参数是数字（出问题）：" + cc2);
    }


    @Test
    public void testStringSplitError() {
        String conditionValue = "qweqwe";
        String[] conditionValueArray = conditionValue.split(",");
        System.out.println("length = "+ conditionValueArray.length);
        Arrays.stream(conditionValueArray).forEach(System.out::println);
    }

    @Test
    public void testJSONArrayJoin() {
        JSONArray valArray = new JSONArray();
        valArray.add("123");
        valArray.add("qwe");
        System.out.println("值: " + StringUtils.join(valArray, ","));
    }
    
    @Test
    public void testSql() {
        String sql = "select * from sys_user where sex = ${sex}";
        sql = sql.replaceAll("'?\\$\\{sex}'?","1");
        System.out.println(sql);
    }

    @Test
    public void base64(){
        String encodedString = "5L+d5a2Y5aSx6LSl77yM5YWN6LS554mI5pyA5aSa5Yib5bu6ezB95p2h6L+e5o6l77yM6K+35Y2H57qn5ZWG5Lia54mI77yB";
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String decodedString = new String(decodedBytes);
        String tipMsg = MessageFormat.format(decodedString, 10);
        System.out.println(tipMsg);
    }

    /**
     * 正则测试字符串只保存中文和数字和字母
     */
    @Test
    public void testSpecialChar() {
        String str = "Hello, World! 你好！这是一段特殊符号的测试，This is a test string with special characters: @#$%^&*";
        // 使用正则表达式替换特殊字符
        String replacedStr = str.replaceAll("[^a-zA-Z0-9\\u4e00-\\u9fa5]", "");
        System.out.println("Replaced String: " + replacedStr);
    }

}
