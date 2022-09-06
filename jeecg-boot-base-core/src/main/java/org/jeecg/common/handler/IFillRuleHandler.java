package org.jeecg.common.handler;

import com.alibaba.fastjson.JSONObject;

/**
 * 填值规则接口
 *
 * @author Yan_东
 * 如需使用填值规则功能，规则实现类必须实现此接口
 */
public interface IFillRuleHandler {

    /**
     * 填值规则
     * @param params 页面配置固定参数
     * @param formData  动态表单参数
     * @return
     */
    public Object execute(JSONObject params, JSONObject formData);

}

