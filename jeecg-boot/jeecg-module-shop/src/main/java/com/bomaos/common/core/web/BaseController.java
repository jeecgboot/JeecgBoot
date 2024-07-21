package com.bomaos.common.core.web;

import com.bomaos.common.system.entity.User;
import com.bomaos.website.entity.Website;
import com.bomaos.website.service.WebsiteService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Controller基类
 * Created by Panyoujie on 2017-06-10 10:10
 */
public class BaseController {

    @Autowired
    private WebsiteService websiteService;

    /**
     * 获取当前登录的user
     */
    public User getLoginUser() {
        Subject subject = SecurityUtils.getSubject();
        if (subject == null) return null;
        Object object = subject.getPrincipal();
        if (object != null) return (User) object;
        return null;
    }

    /**
     * 获取当前登录的userId
     */
    public Integer getLoginUserId() {
        User loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getUserId();
    }

    public String getWebName() {
        Website website = websiteService.getById(1);
        return website.getWebsiteName();
    }

}
