package com.vmq.controller;

import com.vmq.config.EmailUtils;
import com.vmq.dto.CommonRes;
import com.vmq.entity.OtherSetting;
import com.vmq.entity.VmqSetting;
import com.vmq.dto.PageRes;
import com.vmq.entity.PayQrcode;
import com.vmq.service.AdminService;
import com.vmq.utils.ResUtil;
import com.vmq.utils.StringUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${server.url}")
    private String url;

    @Autowired
    private AdminService adminService;

    @RequestMapping("/getMenu")
    public List<Map<String,Object>> getMenu(){
        List<Map<String,Object>> menu = new ArrayList<>();
        return menu;
    }

    @RequestMapping("/saveSetting")
    public CommonRes saveSetting(@RequestBody VmqSetting vmqSetting){
        String email = vmqSetting.getEmail();
        if (!StringUtils.isEmpty(email) && !EmailUtils.checkEmail(email)) {
            return ResUtil.error("请填写正确的邮箱地址");
        }
        return adminService.saveSetting(vmqSetting);
    }

    @RequestMapping("/saveOtherSetting")
    public CommonRes saveOtherSetting(@RequestBody OtherSetting setting){
        String email = setting.getEmail();
        if (!StringUtils.isEmpty(email) && !EmailUtils.checkEmail(email)) {
            return ResUtil.error("请填写正确的邮箱地址");
        }
        return adminService.saveOtherSetting(setting);
    }

    @RequestMapping("/getSettings")
    public CommonRes getSettings(HttpServletRequest request, HttpSession session){
        String username = (String) session.getAttribute("username");
        CommonRes res = adminService.getSettings(username);
        VmqSetting vmqSetting = (VmqSetting) res.getData();
        if (vmqSetting == null) {
            vmqSetting = new VmqSetting();
            vmqSetting.setUsername(username);
            res.setData(vmqSetting);
        }
        if (StringUtils.isBlank(vmqSetting.getNotifyUrl())) {
            vmqSetting.setNotifyUrl(url + "/notify");
        }
        if (StringUtils.isBlank(vmqSetting.getReturnUrl())) {
            vmqSetting.setReturnUrl(url + "/return");
        }
        if (StringUtils.isBlank(vmqSetting.getSecret())) {
            vmqSetting.setSecret(vmqSetting.getMd5key());
        }
        return res;
    }

    @RequestMapping("/getOtherSettings")
    public CommonRes getOtherSettings(HttpServletRequest request, HttpSession session){
        String username = (String) session.getAttribute("username");
        String type = request.getParameter("type");
        CommonRes res = adminService.getOtherSettings(username,type);
        OtherSetting otherSetting  = (OtherSetting) res.getData();
        if (otherSetting == null) {
            otherSetting = new OtherSetting();
            if ("epusdt".equals(type)) {
                otherSetting.setNotifyUrl("http://127.0.0.1:8090/epusdt");
                otherSetting.setCreateUrl("http://127.0.0.1:8090/epusdt/api/v1/order/create-transaction");
            } else if ("epay".equals(type)) {
                otherSetting.setNotifyUrl("https://yi-pay.com");
                otherSetting.setCreateUrl("https://yi-pay.com");
            }
            otherSetting.setUsername(username);
            res.setData(otherSetting);
        }
        return res;
    }

    @RequestMapping("/getOrders")
    public PageRes getOrders(HttpSession session,Integer page, Integer limit, Integer type, Integer state){
        String username = (String) session.getAttribute("username");
        return adminService.getOrders(username,page, limit, type,state);
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
