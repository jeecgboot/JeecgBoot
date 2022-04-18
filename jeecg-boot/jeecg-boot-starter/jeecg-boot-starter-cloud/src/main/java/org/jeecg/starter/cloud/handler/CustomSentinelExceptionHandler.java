package org.jeecg.starter.cloud.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: 全局Sentinel自定义信息处理(需要启动Sentinel客户端)
 * @author: zyf
 * @date: 2022/02/18
 * @version: V1.0
 */
@Configuration
public class CustomSentinelExceptionHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException ex) throws Exception {

        String msg = null;

        if (ex instanceof FlowException) {
            msg = "访问频繁，请稍候再试";

        } else if (ex instanceof DegradeException) {
            msg = "系统降级";

        } else if (ex instanceof ParamFlowException) {
            msg = "热点参数限流";

        } else if (ex instanceof SystemBlockException) {
            msg = "系统规则限流或降级";

        } else if (ex instanceof AuthorityException) {
            msg = "授权规则不通过";

        } else {
            msg = "未知限流降级";
        }
        // http状态码
        response.setStatus(200);
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", "application/json;charset=utf-8");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write("{\"code\":500,\"message\":"+msg+"}");
    }

}