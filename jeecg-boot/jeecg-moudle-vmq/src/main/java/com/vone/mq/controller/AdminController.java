package com.vone.mq.controller;

import com.vone.mq.dto.CommonRes;
import com.vone.mq.dto.PageRes;
import com.vone.mq.entity.PayQrcode;
import com.vone.mq.entity.Setting;
import com.vone.mq.service.AdminService;
import com.vone.mq.utils.ResUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @RequestMapping("/getMenu")
    public List<Map<String,Object>> getMenu(){
        List<Map<String,Object>> menu = new ArrayList<>();
        return menu;
    }

    @RequestMapping("/saveSetting")
    public CommonRes saveSetting(@RequestBody Setting setting){
        return adminService.saveSetting(setting);
    }

    @RequestMapping("/getSettings")
    public CommonRes getSettings(HttpSession session){
        String username = (String) session.getAttribute("username");
        CommonRes res = adminService.getSettings(username);
        if (res.getData() == null) {
            Setting setting = new Setting();
            setting.setUsername(username);
            res.setData(setting);
        }
        return res;
    }

    @RequestMapping("/getOrders")
    public PageRes getOrders(HttpSession session,Integer page, Integer limit, Integer type, Integer state){
        String username = (String) session.getAttribute("username");
        return adminService.getOrders(username,page, limit, type,state);
    }

    @RequestMapping("/getGoods")
    public PageRes getGoods(HttpSession session,Integer page, Integer limit, String name, Integer state){
        String username = (String) session.getAttribute("username");
        return adminService.getGoods(username, page, limit, name, state);
    }

    @RequestMapping("/setBd")
    public CommonRes setBd(HttpSession session,Integer id){
        if (id==null){
            return ResUtil.error();
        }
        String username = (String) session.getAttribute("username");
        if (StringUtils.isEmpty(username)) {
            return ResUtil.error("请先登录系统，再进行操作");
        }
        return adminService.setBd(id,username);
    }

    @RequestMapping("/getPayQrcodes")
    public PageRes getPayQrcodes(HttpSession session,Integer page, Integer limit, Integer type){
        String username = (String) session.getAttribute("username");
        return adminService.getPayQrcodes(username,page, limit, type);
    }

    @RequestMapping("/delPayQrcode")
    public CommonRes delPayQrcode(HttpSession session,Long id){
        return adminService.delPayQrcode(id);
    }

    @RequestMapping("/addPayQrcode")
    public CommonRes addPayQrcode(HttpSession session,PayQrcode payQrcode){
        String username = (String) session.getAttribute("username");
        payQrcode.setUsername(username);
        return adminService.addPayQrcode(payQrcode);
    }

    @RequestMapping("/getMain")
    public CommonRes getMain(HttpSession session){
        String username = (String) session.getAttribute("username");
        return adminService.getMain(username);
    }

    @RequestMapping("/delOrder")
    public CommonRes delOrder(HttpSession session,Long id){
        String username = (String) session.getAttribute("username");
        return adminService.delOrder(id,username);
    }

    @RequestMapping("/delGqOrder")
    public CommonRes delGqOrder(HttpSession session){
        String username = (String) session.getAttribute("username");
        return adminService.delGqOrder(username);
    }

    @RequestMapping("/delLastOrder")
    public CommonRes delLastOrder(HttpSession session){
        String username = (String) session.getAttribute("username");
        return adminService.delLastOrder(username);
    }
}
