package com.alibaba.csp.sentinel.dashboard.rule.nacos.gateway;


import java.util.List;

import com.alibaba.csp.sentinel.dashboard.constants.SentinelConStants;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.SentinelConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.nacos.api.config.ConfigService;

/**
 * 网关流控规则推送
 *
 * @author zyf
 * @date 2022-04-13
 */
@Component("gateWayFlowRulesNacosPublisher")
public class GateWayFlowRulesNacosPublisher implements DynamicRulePublisher<List<GatewayFlowRuleEntity>> {

    @Autowired
    private ConfigService configService;
    @Autowired
    private Converter<List<GatewayFlowRuleEntity>, String> converter;


    @Override
    public void publish(String app, List<GatewayFlowRuleEntity> rules) throws Exception {
        AssertUtil.notEmpty(app, "app name cannot be empty");
        if (rules == null) {
            return;
        }
        configService.publishConfig(app + SentinelConStants.GETEWAY_FLOW_DATA_ID_POSTFIX,
                SentinelConStants.GROUP_ID, converter.convert(rules));
    }
}
