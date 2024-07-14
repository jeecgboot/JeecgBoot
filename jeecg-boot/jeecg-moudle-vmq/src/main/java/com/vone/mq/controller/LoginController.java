package com.vone.mq.controller;

import com.vone.mq.dto.CommonRes;
import com.vone.mq.service.AdminService;
import com.vone.mq.utils.JWTUtil;
import com.vone.mq.utils.ResUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private AdminService adminService;

    @RequestMapping("/index")
    public String index() {
        return "forward:/index.html";
    }

    @ResponseBody
    @RequestMapping("/login")
    public CommonRes login(HttpSession session,String user, String pass){
        if (user==null){
            return ResUtil.error("请输入账号");
        }
        if (pass==null){
            return ResUtil.error("请输入密码");
        }
        CommonRes r = adminService.login(user, pass);
        if (r.getCode()==1){
            Map<String,Object> userMap = (Map<String, Object>) r.getData();
            session.setAttribute("username",user);
            session.setAttribute("token", JWTUtil.getToken(user,pass));
            r.setData(userMap.get("realname"));
        }
        return r;
    }

    @ResponseBody
    @RequestMapping("/regist")
    public CommonRes regist(HttpSession session,String username, String password){
        if (username==null){
            return ResUtil.error("请输入账号");
        }
        if (username.length() < 4) {
            return ResUtil.error("用户名长度不能小于4位");
        }
        if (password==null){
            return ResUtil.error("请输入密码");
        }
        if (password.length() < 4) {
            return ResUtil.error("密码长度不能小于4位");
        }
        CommonRes r = adminService.regist(username, password);
        return r;
    }

    @RequestMapping("/logout")
    public void logout(HttpSession session){
        session.removeAttribute("token");
    }
}
