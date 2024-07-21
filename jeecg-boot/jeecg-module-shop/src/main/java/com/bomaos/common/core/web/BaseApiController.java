package com.bomaos.common.core.web;

import org.wf.jwtp.provider.Token;
import org.wf.jwtp.util.SubjectUtil;

import javax.servlet.http.HttpServletRequest;

public class BaseApiController extends BaseController {

    /**
     * 获取当前登录的token
     */
    public Token getLoginToken(HttpServletRequest request) {
        return SubjectUtil.getToken(request);
    }

    /**
     * 获取当前登录的userId
     *
     * @param request
     * @return
     */
    public Long getLoginUserId(HttpServletRequest request) {
        Token token = getLoginToken(request);
        return token == null ? null : Long.parseLong(token.getUserId());
    }
}
