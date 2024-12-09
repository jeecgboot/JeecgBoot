package org.jeecg.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.handler.IFillRuleHandler;
import org.jeecg.common.system.query.QueryGenerator;

import javax.servlet.http.HttpServletRequest;


/**
 * 规则值自动生成工具类
 *
 * @author qinfeng
 * @举例： 自动生成订单号；自动生成当前日期
 */
@Slf4j
public class FillRuleUtil {

    /**
     * @param ruleCode ruleCode
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Object executeRule(String ruleCode, JSONObject formData) {
        if (!StringUtils.isEmpty(ruleCode)) {
            try {
                // 获取 Service
                ServiceImpl impl = (ServiceImpl) SpringContextUtils.getBean("sysFillRuleServiceImpl");
                // 根据 ruleCode 查询出实体
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("rule_code", ruleCode);
                JSONObject entity = JSON.parseObject(JSON.toJSONString(impl.getOne(queryWrapper)));
                if (entity == null) {
                    log.warn("填值规则：" + ruleCode + " 不存在");
                    return null;
                }
                // 获取必要的参数
                String ruleClass = entity.getString("ruleClass");
                JSONObject params = entity.getJSONObject("ruleParams");
                if (params == null) {
                    params = new JSONObject();
                }

                HttpServletRequest request = SpringContextUtils.getHttpServletRequest();

                // 解析 params 中的变量
                // 优先级：queryString > 系统变量 > 默认值
                for (String key : params.keySet()) {
                    // 1. 判断 queryString 中是否有该参数，如果有就优先取值
                    //noinspection ConstantValue
                    if (request != null) {
                        String parameter = request.getParameter(key);
                        if (oConvertUtils.isNotEmpty(parameter)) {
                            params.put(key, parameter);
                            continue;
                        }
                    }

                    String value = params.getString(key);
                    // 2. 用于替换 系统变量的值 #{sys_user_code}
                    if (value != null && value.contains(SymbolConstant.SYS_VAR_PREFIX)) {
                        value = QueryGenerator.getSqlRuleValue(value);
                        params.put(key, value);
                    }
                }

                if (formData == null) {
                    formData = new JSONObject();
                }
                // 通过反射执行配置的类里的方法
                IFillRuleHandler ruleHandler = (IFillRuleHandler) Class.forName(ruleClass).newInstance();
                return ruleHandler.execute(params, formData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
