package org.jeecg.common.handler;

import com.alibaba.fastjson.JSONObject;

/**
 * 填值规则接口
 *
 * @author Yan_东
 * 如需使用填值规则功能，规则实现类必须实现此接口
 */
public interface IFillRuleHandler {

    public Object execute(JSONObject params, JSONObject formData);

}

