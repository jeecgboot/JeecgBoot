package org.jeecg.starter.cloud.interceptor;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * sentinel 授权规则拦截器
 * @author zyf
 */
@Component
public class DefaultRequestOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest request) {
        //基于请求参数,origin对应授权规则中的流控应用名称,也可通过getHeader传参
        String origin = request.getParameter("origin");

        //TODO 此处做个通过IP做白名单的例子
        return origin;
    }
}
