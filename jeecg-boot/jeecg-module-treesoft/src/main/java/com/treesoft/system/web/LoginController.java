package com.treesoft.system.web;

import com.treesoft.system.utils.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Controller
@RequestMapping({"treesoft"})
public class LoginController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    HttpServletRequest request;


    static Map<String, String> loginUserMap = new HashMap<>();

    @RequestMapping(value = {"login"}, method = RequestMethod.GET)
    public String login() {
        return "system/login";
    }

    @RequestMapping("index")
    public String treesoft(HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        String username = (String) session.getAttribute("LOGIN_USER_NAME");
        String permission = (String) session.getAttribute("LOGIN_USER_PERMISSION");
        request.setAttribute("username", username);
        request.setAttribute("permission", permission);
        return "system/index";
    }

    @RequestMapping(value = {"loginVaildate"}, method = RequestMethod.POST)
    public String loginVaildate(String username1, String password1, String captcha, HttpServletResponse response) {
        String username = username1.toLowerCase();
        String password = password1.toLowerCase();
        username = StringEscapeUtils.escapeHtml4(username.trim());
        HttpSession session = request.getSession(true);
        String message;
        String identifyCodeTemp = "1234";//(String) session.getAttribute("KAPTCHA_SESSION_KEY");
        if (!captcha.equals(identifyCodeTemp)) {
            message = "验证码错误！！";
            request.setAttribute("message", message);
            return "system/login";
        }
        if (StringUtils.isEmpty(username)) {
            message = "请输入帐号！";
            request.setAttribute("message", message);
            return "system/login";
        }
        String sql = " select u.username,u.realname,u.password,u.salt,u.status,r.* from sys_user u left join treesoft_user_role r on u.id=r.user_id where u.del_flag=0 and username=?";
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql, username);
        if (mapList.isEmpty()) {
            message = "您输入的帐号有误！";
            request.setAttribute("message", message);
            return "system/login";
        }
        Map<String, Object> map = mapList.get(0);
        String salt = (String) map.get("salt");
        String pas = (String) map.get("password");
        Boolean status = (Boolean) map.get("status");
        String expiration = (String) map.get("expiration");
        String permission = (String) map.get("permission");
        if (!status) {
            message = "当前用户已冻结！";
            request.setAttribute("message", message);
            return "system/login";
        }
        if (!pas.equals(PasswordUtil.encrypt(username, password, salt))) {
            message = "您输入的密码有误！";
            request.setAttribute("message", message);
            return "system/login";
        }
        if (StringUtils.isNotEmpty(expiration)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date bt = sdf.parse(expiration);
                Date nowDate = new Date();
                if (bt.before(nowDate)) {
                    message = "当前用户已过期！";
                    request.setAttribute("message", message);
                    return "system/login";
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        session.setAttribute("LOGIN_USER_NAME", username);
        session.setAttribute("LOGIN_USER_PERMISSION", permission);
        loginUserMap.put(username, session.getId());
        request.setAttribute("username", username);
        return "redirect:/treesoft/index";
    }

    @RequestMapping("logout")
    public String logout() {
        Enumeration<String> em = request.getSession().getAttributeNames();
        while (em.hasMoreElements()) {
            request.getSession().removeAttribute(em.nextElement());
        }
        request.getSession().invalidate();
        return "system/login";
    }
}
