package com.vmq.controller;

import com.vmq.config.EmailUtils;
import com.vmq.dto.CommonRes;
import com.vmq.entity.Setting;
import com.vmq.dto.PageRes;
import com.vmq.entity.PayQrcode;
import com.vmq.service.AdminService;
import com.vmq.utils.ResUtil;
import com.vmq.utils.StringUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@Api(tags = "系统内部接口",description = "用户登录后才能操作")
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
        String email = setting.getEmail();
        if (!StringUtils.isEmpty(email) && !EmailUtils.checkEmail(email)) {
            return ResUtil.error("请填写正确的邮箱地址");
        }
        return adminService.saveSetting(setting);
    }

    @RequestMapping("/getSettings")
    public CommonRes getSettings(HttpServletRequest request, HttpSession session){
        String username = (String) session.getAttribute("username");
        CommonRes res = adminService.getSettings(username);
        Setting setting = (Setting) res.getData();
        if (setting == null) {
            setting = new Setting();
            setting.setUsername(username);
            res.setData(setting);
        }
        String path = request.getRequestURL().toString().replace("/admin/getSettings","");
        if (StringUtils.isBlank(setting.getNotifyUrl())) {
            setting.setNotifyUrl(path + "/notify");
        }
        if (StringUtils.isBlank(setting.getReturnUrl())) {
            setting.setReturnUrl(path + "/notify");
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
        String username = (String) session.getAttribute("username");
        return adminService.delPayQrcode(id,username);
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
