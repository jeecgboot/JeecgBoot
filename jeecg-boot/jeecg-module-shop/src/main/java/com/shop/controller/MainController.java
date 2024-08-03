package com.shop.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shop.common.core.utils.StringUtil;
import com.shop.common.core.web.BaseController;
import com.shop.common.core.web.JsonResult;
import com.shop.entity.*;
import com.shop.service.*;
import com.wf.captcha.utils.CaptchaUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 首页、登录、验证码等
 * 2018-12-24 16:10
 */
@Controller
public class MainController extends BaseController implements ErrorController {

    private static final Pattern namePatt = Pattern.compile("^\\w{4,20}$");

    @Autowired
    private MenuService menuService;

    @Autowired
    private LoginRecordService loginRecordService;

    @Autowired
    private WebsiteService websiteService;

    @Autowired
    private PaysService paysService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    private static Map<String, String> emailMap = new LinkedHashMap<String, String>(){
        @Override
        protected boolean removeEldestEntry(Map.Entry e) {
            return size()>100;
        }
    };

    /**
     * 用户登录
     */
    @ResponseBody
    @PostMapping("/login")
    public JsonResult login(String username, String password, String code, Boolean remember) {
        if (username == null || username.trim().isEmpty()) return JsonResult.error("请输入账号");
        if (!CaptchaUtil.ver(code, request)) {
            CaptchaUtil.clear(request);  // 清除session中的验证码
            loginRecordService.saveAsync(username, LoginRecord.TYPE_ERROR, "验证码错误", request);
            return JsonResult.error("验证码不正确");
        }
        try {
            if (remember == null) remember = false;
            SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password, remember));
            loginRecordService.saveAsync(username, request);
            return JsonResult.ok("登录成功");
        } catch (IncorrectCredentialsException ice) {
            loginRecordService.saveAsync(username, LoginRecord.TYPE_ERROR, "密码错误", request);
            return JsonResult.error("密码错误");
        } catch (UnknownAccountException uae) {
            loginRecordService.saveAsync(username, LoginRecord.TYPE_ERROR, "账号不存在", request);
            return JsonResult.error("账号不存在");
        } catch (LockedAccountException e) {
            loginRecordService.saveAsync(username, LoginRecord.TYPE_ERROR, "账号被锁定", request);
            return JsonResult.error("账号被锁定");
        } catch (ExcessiveAttemptsException eae) {
            loginRecordService.saveAsync(username, LoginRecord.TYPE_ERROR, "操作频繁", request);
            return JsonResult.error("操作频繁，请稍后再试");
        }
    }

    /**
     * 登录页
     */
    @GetMapping("/login")
    public String login(Model model) {

        Website website = websiteService.getById(1);
        model.addAttribute("website", website);

        if (getLoginUser() != null) return "redirect:admin";
        return "login.html";
    }

    /**
     * 主页
     */
    @RequestMapping("/admin")
    public String index(Model model) {

        String username = getLoginUsername();
        Pays paypal = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "paypal").eq("username",username));
        Pays zlqqpay = paysService.getOne(new QueryWrapper<Pays>().eq("driver", "epay_qqpay").eq("username",username));

        if (ObjectUtils.isEmpty(paypal)) {
            Pays pays1 = new Pays();
            pays1.setName("Paypal");
            pays1.setDriver("paypal");

            Map<String, String> map = new HashMap<>();
            map.put("clientId", "xxx");
            map.put("clientSecret", "xxx");
            map.put("return_url", "xxx");
            String jsonString = JSON.toJSONString(map);
            pays1.setConfig(jsonString);
            pays1.setComment("Paypal 境外支付（默认美元交易）");
            pays1.setIsMobile(0);
            pays1.setIsPc(0);
            pays1.setCreatedAt(new Date());
            pays1.setUpdatedAt(new Date());
            paysService.save(pays1);
        }

        if (ObjectUtils.isEmpty(zlqqpay)) {
            Pays pays1 = new Pays();
            pays1.setName("QQ钱包");
            pays1.setDriver("epay_qqpay");

            Map<String, String> map = new HashMap<>();
            map.put("pid", "xxx");
            map.put("key", "xxx");
            map.put("notify_url", "xxx");
            map.put("create_url", "xxx");

            String jsonString = JSON.toJSONString(map);
            pays1.setConfig(jsonString);
            pays1.setComment("易支付 - QQ钱包");
            pays1.setIsMobile(0);
            pays1.setIsPc(0);
            pays1.setCreatedAt(new Date());
            pays1.setUpdatedAt(new Date());
            paysService.save(pays1);
        }

        Website website = websiteService.getById(1);
        model.addAttribute("website", website);

        // 左侧菜单
        List<Menu> menus = menuService.getUserMenu(getLoginUserId(), Menu.TYPE_MENU);
        model.addAttribute("menus", menuService.toMenuTree(menus, 0));
        return "main.html";
    }

    /**
     * 图形验证码
     */
    @RequestMapping("/assets/captcha")
    public void captcha(HttpServletResponse response) {
        try {
            CaptchaUtil.out(5, request, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 主页弹窗页面
     */
    @RequestMapping("/tpl/{name}")
    public String tpl(@PathVariable("name") String name) {
        return "index/" + name + ".html";
    }

    /**
     * 错误页
     */
    @RequestMapping("/error")
    public String error() {
        return "error/404.html";
    }

    @ResponseBody
    @RequestMapping(value = "/getVerificationCode",method = {RequestMethod.GET,RequestMethod.POST})
    public JsonResult getVerificationCode(String email){
        if (StringUtils.isBlank(email) || !email.contains("@")) {
            return JsonResult.error("请输入正确邮箱");
        }
        String temp = redisTemplate.opsForValue().get(email);
        if (StringUtils.isNotBlank(temp)) {
            return JsonResult.error("2分钟内已发送过验证码，请前往邮箱确认");
        }
        String code = StringUtil.getRandomNumber(6);
        try {
            emailService.sendTextEmail("【码商城】- 验证码", "您的验证码为：" + code + "，有效期2分钟。", new String[]{email});
            //记录缓存
            emailMap.put(email,code);
            redisTemplate.opsForValue().set(email, "added", 2, TimeUnit.MINUTES);
        } catch (Exception e) {
            return JsonResult.error("发送失败");
        }
        return JsonResult.ok("发送成功");
    }

    @ResponseBody
    @RequestMapping(value = "/regist",method = {RequestMethod.GET,RequestMethod.POST})
    public JsonResult regist(String username, String password,String email,String code){
        if (username==null){
            return JsonResult.error("请输入账号");
        }
        if (!namePatt.matcher(username).matches()) {
            return JsonResult.error("用户名只能由数据、字母、下划线组成，且长度不能小于4位");
        }
        if (password==null){
            return JsonResult.error("请输入密码");
        }
        if (password.length() < 4) {
            return JsonResult.error("密码长度不能小于4位");
        }
        String temp = redisTemplate.opsForValue().get(email);
        if (StringUtils.isBlank(temp)) {
            return JsonResult.error("验证码已失效");
        }
        String validCode = emailMap.get(email);
        if (!(StringUtils.isNotBlank(validCode) && validCode.equals(code))) {
            return JsonResult.error("邮箱验证码不正确");
        }
        redisTemplate.opsForValue().getAndDelete(email);

        User user = new User();
        user.setNickName(username);
        user.setUsername(username);
        user.setState(0);
        user.setPassword(userService.encodePsw(password));
        user.setEmail(email);
        List<Role> roles = roleService.list(new QueryWrapper<Role>().eq("role_code", "merchant"));
        user.setRoleIds(roles.stream().map(role -> role.getRoleId()).collect(Collectors.toList()));
        userService.saveUser(user);
        return JsonResult.ok("注册成功");
    }
}
