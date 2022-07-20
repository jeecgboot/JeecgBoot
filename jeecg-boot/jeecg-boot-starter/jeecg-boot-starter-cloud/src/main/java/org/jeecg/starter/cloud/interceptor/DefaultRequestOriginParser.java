package org.jeecg.starter.cloud.interceptor;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.IpUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 【示例】sentinel ip和参数授权规则拦截器(黑名单白名单)
 * 1. 有参数origin的时候走参数拦截规则
 * 2. 当参数为空时走ip拦截模式
 *
 * @author zyf
 */
@Component
public class DefaultRequestOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest request) {
        //基于请求参数,origin对应授权规则中的流控应用名称,也可通过getHeader传参
        String origin = request.getParameter("origin");
        if (StringUtils.isNotEmpty(origin)) {
            return origin;
        } else {
            //当参数为空使用ip拦截模式
            String ip = IpUtils.getIpAddr(request);
            return ip;
        }
    }
}
