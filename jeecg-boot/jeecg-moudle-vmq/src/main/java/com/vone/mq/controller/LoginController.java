package com.vone.mq.controller;

import com.vone.mq.dao.PayOrderDao;
import com.vone.mq.dao.TmpPriceDao;
import com.vone.mq.dto.CommonRes;
import com.vone.mq.entity.PayInfo;
import com.vone.mq.entity.PayOrder;
import com.vone.mq.entity.Setting;
import com.vone.mq.service.AdminService;
import com.vone.mq.service.WebService;
import com.vone.mq.config.EmailUtils;
import com.vone.mq.utils.JWTUtil;
import com.vone.mq.utils.ResUtil;
import com.vone.mq.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@Controller
@Api(tags = "登录接口")
public class LoginController {

    @Value("${token.expire.link}")
    private int expireHour;

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    private AdminService adminService;

    @Autowired
    private PayOrderDao payOrderDao;

    @Autowired
    private TmpPriceDao tmpPriceDao;

    @Autowired
    private WebService webService;

    @Autowired
    private EmailUtils emailUtils;

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

    @ResponseBody
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("token");
        return "success";
    }

    @GetMapping("/passOrder")
    @ApiOperation(value = "订单审核通过")
    public String passOrder(String orderId,String sign,Model model) {
        PayOrder payOrder = auditOrder(orderId, sign, model);
        log.info("{}",model.getAttribute("errorMsg"));
        if (payOrder == null) return "500";
        Setting setting = webService.getDefaultSetting(payOrder.getUsername());
        if (setting.getIsNotice() == 1 && EmailUtils.checkEmail(payOrder.getEmail())) {
            PayInfo payInfo = new PayInfo(payOrder);
            if (payOrder.getPayDate() < 1) {
                payInfo.setPayDate(StringUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
            }
            emailUtils.sendTemplateMail(sender, payOrder.getEmail(),"【码支付】支付成功通知","pay-success", payInfo);
        }
        payOrderDao.setState(1,payOrder.getId());
        return "success";
    }
    
    @GetMapping("/backOrder")
    @ApiOperation(value = "订单驳回")
    public String backOrder(String orderId, String sign, Model model) {
        PayOrder payOrder = auditOrder(orderId, sign, model);
        log.info("{}",model.getAttribute("errorMsg"));
        if (payOrder == null) return "500";
        if (payOrder.getState()==-1){
            model.addAttribute("errorMsg","订单已过期");
            return "500";
        }
        Setting setting = webService.getDefaultSetting(payOrder.getUsername());
        if (setting.getIsNotice() == 1 && EmailUtils.checkEmail(payOrder.getEmail())) {
            PayInfo payInfo = new PayInfo(payOrder);
            if (payOrder.getPayDate() < 1) {
                payInfo.setPayDate(StringUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
            }
            emailUtils.sendTemplateMail(sender, payOrder.getEmail(),"【码支付】支付失败通知", "pay-fail", payInfo);
        }
        payOrderDao.setState(-1,payOrder.getId());
        return "success";
    }

    @GetMapping("/delOrder")
    @ApiOperation(value = "订单删除")
    public String delOrder(String orderId,String sign,Model model) {
        PayOrder payOrder = auditOrder(orderId, sign, model);
        log.info("{}",model.getAttribute("errorMsg"));
        if (payOrder == null) return "500";
        payOrderDao.delete(payOrder);
        return "success";
    }

    private PayOrder auditOrder(String orderId, String sign, Model model) {
        if (orderId == null) {
            model.addAttribute("errorMsg","请传入云端订单号");
            return null;
        }
        if (sign == null) {
            model.addAttribute("errorMsg","请传入签名");
            return null;
        }
        PayOrder payOrder = payOrderDao.findByOrderId(orderId);
        if (payOrder == null) {
            model.addAttribute("errorMsg","订单号不存在或已删除");
            return null;
        }
        if (System.currentTimeMillis()-payOrder.getCreateDate()>expireHour*60*60*1000) {
            model.addAttribute("errorMsg","链接已失效，请在前往系统确认支付状态");
            return null;
        }
        if (payOrder.getState()==1){
            model.addAttribute("errorMsg","订单已完成支付");
            return null;
        }
        String userSign = webService.getMd5(payOrder.getUsername(),payOrder.getOrderId());
        if (!userSign.equals(sign)) {
            model.addAttribute("errorMsg","签名校验失败");
            return null;
        }
        if (payOrder.getState()==0){
            tmpPriceDao.delprice(payOrder.getUsername(),payOrder.getType()+"-"+payOrder.getReallyPrice());
        }
        return payOrder;
    }

}
