package com.vone.mq.controller;

import com.vone.mq.dto.CommonRes;
import com.vone.mq.dto.PageRes;
import com.vone.mq.entity.PayQrcode;
import com.vone.mq.service.AdminService;
import com.vone.mq.utils.JWTUtil;
import com.vone.mq.utils.ResUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

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
            String token = JWTUtil.getToken(user,pass);
            session.setAttribute("token",token);
        }
        return r;
    }

    @RequestMapping("/logout")
    public void logout(HttpSession session,String user){
        session.removeAttribute("token");
    }

    @RequestMapping("/admin/getMenu")
    public List<Map<String,Object>> getMenu(HttpSession session){
        List<Map<String,Object>> menu = new ArrayList<>();
        Map<String,Object> node = new HashMap<>();
        node.put("name","系统设置");
        node.put("type","url");
        node.put("url","admin/setting.html?t="+new Date().getTime());
        menu.add(node);

        node = new HashMap<>();
        node.put("name","监控端设置");
        node.put("type","url");
        node.put("url","admin/jk.html?t="+new Date().getTime());
        menu.add(node);


        List<Map<String,Object>> menu1 = new ArrayList<>();

        node = new HashMap<>();
        node.put("name","添加");
        node.put("type","url");
        node.put("url","admin/addwxqrcode.html?t="+new Date().getTime());
        menu1.add(node);

        node = new HashMap<>();
        node.put("name","管理");
        node.put("type","url");
        node.put("url","admin/wxqrcodelist.html?t="+new Date().getTime());
        menu1.add(node);

        node = new HashMap<>();
        node.put("name","微信二维码");
        node.put("type","menu");
        node.put("node",menu1);
        menu.add(node);


        List<Map<String,Object>> menu2 = new ArrayList<>();

        node = new HashMap<>();
        node.put("name","添加");
        node.put("type","url");
        node.put("url","admin/addzfbqrcode.html?t="+new Date().getTime());
        menu2.add(node);

        node = new HashMap<>();
        node.put("name","管理");
        node.put("type","url");
        node.put("url","admin/zfbqrcodelist.html?t="+new Date().getTime());
        menu2.add(node);

        node = new HashMap<>();
        node.put("name","支付宝二维码");
        node.put("type","menu");
        node.put("node",menu2);
        menu.add(node);

        node = new HashMap<>();
        node.put("name","订单列表");
        node.put("type","url");
        node.put("url","admin/orderlist.html?t="+new Date().getTime());
        menu.add(node);

        node = new HashMap<>();
        node.put("name","Api说明");
        node.put("type","url");
        node.put("url","../api.html?t="+new Date().getTime());
        menu.add(node);
        return menu;
    }
    @RequestMapping("/admin/saveSetting")
    public CommonRes saveSetting(HttpSession session,String user,String pass,String notifyUrl,String returnUrl,String key,String wxpay,String zfbpay,String close,String payQf){
        return adminService.saveSetting(user, pass, notifyUrl, returnUrl, key, wxpay, zfbpay, close, payQf);
    }
    @RequestMapping("/admin/getSettings")
    public CommonRes getSettings(HttpSession session){
        return adminService.getSettings();
    }
    @RequestMapping("/admin/getOrders")
    public PageRes getOrders(HttpSession session,Integer page, Integer limit, Integer type, Integer state){
        return adminService.getOrders(page, limit, type,state);
    }
    @RequestMapping("/admin/setBd")
    public CommonRes setBd(HttpSession session,Integer id){
        if (id==null){
            return ResUtil.error();
        }
        return adminService.setBd(id);
    }
    @RequestMapping("/admin/getPayQrcodes")
    public PageRes getPayQrcodes(HttpSession session,Integer page, Integer limit, Integer type){
        return adminService.getPayQrcodes(page, limit, type);
    }
    @RequestMapping("/admin/delPayQrcode")
    public CommonRes delPayQrcode(HttpSession session,Long id){
        return adminService.delPayQrcode(id);
    }
    @RequestMapping("/admin/addPayQrcode")
    public CommonRes addPayQrcode(HttpSession session,PayQrcode payQrcode){
        return adminService.addPayQrcode(payQrcode);
    }
    @RequestMapping("/admin/getMain")
    public CommonRes getMain(HttpSession session){
        return adminService.getMain();
    }

    @RequestMapping("/admin/delOrder")
    public CommonRes delOrder(HttpSession session,Long id){
        return adminService.delOrder(id);
    }

    @RequestMapping("/admin/delGqOrder")
    public CommonRes delGqOrder(HttpSession session){
        return adminService.delGqOrder();
    }
    @RequestMapping("/admin/delLastOrder")
    public CommonRes delLastOrder(HttpSession session){
        return adminService.delLastOrder();
    }
}
