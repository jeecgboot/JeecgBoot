package com.shop.common.core.web;

import com.shop.entity.User;
import com.shop.entity.Website;
import com.shop.service.WebsiteService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller基类
 * 2017-06-10 10:10
 */
public class BaseController {

    @Autowired
    public HttpServletRequest request;

    @Autowired
    private WebsiteService websiteService;

    /**
     * 获取当前登录的user
     */
    public static User getLoginUser() {
        Subject subject = SecurityUtils.getSubject();
        if (subject == null) return null;
        Object object = subject.getPrincipal();
        if (object != null) return (User) object;
        return null;
    }

    /**
     * 获取当前登录的userId
     */
    public static Integer getLoginUserId() {
        User loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getUserId();
    }

    public static String getLoginUsername() {
        User loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getUsername();
    }

    public String getWebName() {
        Website website = websiteService.getById(1);
        return website.getWebsiteName();
    }

}
