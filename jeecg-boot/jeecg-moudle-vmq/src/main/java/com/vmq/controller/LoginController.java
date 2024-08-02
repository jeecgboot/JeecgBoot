package com.vmq.controller;

import com.vmq.config.EmailUtils;
import com.vmq.dao.PayOrderDao;
import com.vmq.dao.TmpPriceDao;
import com.vmq.dto.CommonRes;
import com.vmq.entity.PayInfo;
import com.vmq.entity.PayOrder;
import com.vmq.entity.VmqSetting;
import com.vmq.service.AdminService;
import com.vmq.service.WebService;
import com.vmq.utils.JWTUtil;
import com.vmq.utils.PasswordUtil;
import com.vmq.utils.ResUtil;
import com.vmq.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Slf4j
@Controller
@Api(tags = "登录接口")
public class LoginController {

    private static final Pattern namePatt = Pattern.compile("^\\w{4,20}$");

    /** 审批链接有效时间 */
    @Value("${token.expire.link}")
    private int expireHour;

    /** 邮箱发送者账号 */
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

    @Autowired
    HttpServletRequest request;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static Map<String, String> emailMap = new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry e) {
            return size() > 100;
        }
    };

    @GetMapping({"/","/login"})
    public String login(){
        return "login";
    }

    @GetMapping({"/index","/menu"})
    public String menu() {
        HttpSession session = request.getSession(true);
        request.setAttribute("username", session.getAttribute("username"));
        request.setAttribute("token", session.getAttribute("token"));
        return "menu";
    }

    @ResponseBody
    @RequestMapping(value = "/loginValid", method = {RequestMethod.GET, RequestMethod.POST})
    public CommonRes loginValid(HttpSession session, String user, String pass){
        if (user==null){
            return ResUtil.error("请输入账号");
        }
        if (pass==null){
            return ResUtil.error("请输入密码");
        }
        CommonRes res = adminService.login(user, pass);
        if (res.getCode()==1){
            Map<String,Object> userMap = (Map<String, Object>) res.getData();
            session.setAttribute("username",user);
            session.setAttribute("token", JWTUtil.getToken(user,pass));
        }
        return res;
    }

    @ResponseBody
    @GetMapping("/getUserInfo")
    public CommonRes getUserInfo(){
        HttpSession session = request.getSession(true);
        Map<String,Object> map = new HashMap<>();
        map.put("username",session.getAttribute("username"));
        map.put("token",session.getAttribute("token"));
        return ResUtil.success(map);
    }

    @ResponseBody
    @RequestMapping(value = "/getVerificationCode",method = {RequestMethod.GET,RequestMethod.POST})
    public CommonRes getVerificationCode(String email){
        if (!EmailUtils.checkEmail(email)){
            return ResUtil.error("邮箱格式不正确");
        }
        String temp = redisTemplate.opsForValue().get(email);
        if (StringUtils.isNotBlank(temp)) {
            return ResUtil.error("2分钟内已发送过验证码，请前往邮箱确认");
        }
        String code = PasswordUtil.randomNum(6);
        emailUtils.sendMail(sender, email,"【码支付】- 验证码", "您的验证码为："+code+"，有效期2分钟。");
        //记录缓存
        emailMap.put(email,code);
        redisTemplate.opsForValue().set(email, "added", 2, TimeUnit.MINUTES);
        return ResUtil.success("发送成功");
    }

    @ResponseBody
    @RequestMapping("/regist")
    public CommonRes regist(HttpSession session,String username, String password,String email,String code){
        if (username==null){
            return ResUtil.error("请输入账号");
        }
        if (!namePatt.matcher(username).matches()) {
            return ResUtil.error("用户名只能由数据、字母、下划线组成，且长度不能小于4位");
        }
        if (password==null){
            return ResUtil.error("请输入密码");
        }
        if (password.length() < 4) {
            return ResUtil.error("密码长度不能小于4位");
        }
        String temp = redisTemplate.opsForValue().get(email);
        if (StringUtils.isBlank(temp)) {
            return ResUtil.error("验证码已失效");
        }
        String validCode = emailMap.get(email);
        if (!(StringUtils.isNotBlank(validCode) && validCode.equals(code))) {
            return ResUtil.error("邮箱验证码不正确");
        }
        redisTemplate.opsForValue().getAndDelete(email);
        CommonRes r = adminService.regist(username, password, email);
        return r;
    }

    @ResponseBody
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("token");
        return "success";
    }

    @GetMapping("/payPage")
    public String payPage(String orderId){
        HttpSession session = request.getSession(true);
        session.setAttribute("orderId",orderId);
        return "pay";
    }

    @RequestMapping(value = "/passOrder",method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "订单审核通过")
    public String passOrder(String orderId,String sign,Model model) {
        PayOrder payOrder = auditOrder(orderId, sign, model);
        log.info("{}",model.getAttribute("errorMsg"));
        if (payOrder == null) return "500";
        VmqSetting vmqSetting = webService.getDefaultSetting(payOrder.getUsername());
        if (vmqSetting.getIsNotice() == 1 && EmailUtils.checkEmail(payOrder.getEmail())) {
            PayInfo payInfo = new PayInfo(payOrder);
            if (payOrder.getPayDate() < 1) {
                payInfo.setPayDate(StringUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
            }
            emailUtils.sendTemplateMail(sender, payOrder.getEmail(),"【码支付】支付成功通知","pay-success", payInfo);
        }
        payOrderDao.setState(1,payOrder.getId());
        return "success";
    }
    
    @RequestMapping(value = "/backOrder",method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "订单驳回")
    public String backOrder(String orderId, String sign, Model model) {
        PayOrder payOrder = auditOrder(orderId, sign, model);
        log.info("{}",model.getAttribute("errorMsg"));
        if (payOrder == null) return "500";
        if (payOrder.getState()==-1){
            model.addAttribute("errorMsg","订单已过期");
            return "500";
        }
        VmqSetting vmqSetting = webService.getDefaultSetting(payOrder.getUsername());
        if (vmqSetting.getIsNotice() == 1 && EmailUtils.checkEmail(payOrder.getEmail())) {
            PayInfo payInfo = new PayInfo(payOrder);
            if (payOrder.getPayDate() < 1) {
                payInfo.setPayDate(StringUtils.format(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss"));
            }
            emailUtils.sendTemplateMail(sender, payOrder.getEmail(),"【码支付】支付失败通知", "pay-fail", payInfo);
        }
        payOrderDao.setState(-1,payOrder.getId());
        return "success";
    }

    @RequestMapping(value = "/delOrder",method = {RequestMethod.GET, RequestMethod.POST})
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
