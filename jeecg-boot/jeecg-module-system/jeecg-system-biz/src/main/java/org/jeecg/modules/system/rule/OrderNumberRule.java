package org.jeecg.modules.system.rule;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.jeecg.common.handler.IFillRuleHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 填值规则Demo：生成订单号
 * 【测试示例】
 */
public class OrderNumberRule implements IFillRuleHandler {

    @Override
    public Object execute(JSONObject params, JSONObject formData) {
        String prefix = "";
        String suffix = "";
        String dateFormat = "yyMMddHHmmss";
        String randomStr = "100";
        //订单前缀默认为CN 如果规则参数不为空，则取自定义前缀
        if (params != null) {
            if (params.containsKey("prefix")) {
                prefix = params.getString("prefix");
            }
            if (params.containsKey("suffix")) {
                suffix = params.getString("suffix");
            }
            if (params.containsKey("dateFormat")) {
                dateFormat = params.getString("dateFormat");
            }
            if (params.containsKey("random")) {
                randomStr = params.getString("random");
            }
        }
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        int random = RandomUtils.nextInt(Integer.parseInt(randomStr));
        String.format("%0"+random+"d", Integer.parseInt(randomStr)/10);
        String value = format.format(new Date()) + random;
        // 根据formData的值的不同，生成不同的订单号
        String name = formData.getString("name");
        if (!StringUtils.isEmpty(name)) {
            value += name;
        }
        return prefix + value + suffix;
    }

}
