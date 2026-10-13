package org.jeecg.modules.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xkcoding.justauth.AuthRequestFactory;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import me.zhyd.oauth.utils.StringUtils;
import me.zhyd.oauth.config.AuthConfig;
import com.xkcoding.justauth.autoconfigure.JustAuthProperties;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.enums.MessageTypeEnum;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.*;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysThirdAccount;
import org.jeecg.modules.system.entity.SysThirdAppConfig;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.ThirdLoginModel;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.service.ISysThirdAccountService;
import org.jeecg.modules.system.service.ISysThirdAppConfigService;
import org.jeecg.modules.system.service.ISysUserService;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.impl.ThirdAppDingtalkServiceImpl;
import org.jeecg.modules.system.service.impl.ThirdAppFeishuServiceImpl;
import org.jeecg.modules.system.service.impl.ThirdAppWechatEnterpriseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.config.vo.DomainUrl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * @Author scott
 * @since 2018-12-17
 */
@Controller
@RequestMapping("/sys/thirdLogin")
@Slf4j
public class ThirdLoginController {
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private ISysThirdAccountService sysThirdAccountService;
	@Autowired
	private ISysDictService sysDictService;
	@Autowired
	private BaseCommonService baseCommonService;
	@Autowired
    private RedisUtil redisUtil;
	@Autowired
	private AuthRequestFactory factory;
	@Autowired
	private ISysDepartService sysDepartService;

	@Autowired
	private ThirdAppWechatEnterpriseServiceImpl thirdAppWechatEnterpriseService;
	@Autowired
	private ThirdAppDingtalkServiceImpl thirdAppDingtalkService;

	@Autowired
	private ISysThirdAppConfigService appConfigService;

	@Autowired
	private ThirdAppFeishuServiceImpl thirdAppFeishuService;

	@Autowired
	private JustAuthProperties justAuthProperties;

	@Autowired
	public ISysBaseAPI sysBaseAPI;

	@Autowired
	private JeecgBaseConfig jeecgBaseConfig;

	@RequestMapping("/render/{source}")
    public void render(@PathVariable("source") String source, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("第三方登录进入render：" + source);
        //update-begin---author:liusq ---date:2026-05-13  for：【QQYUN-12767】飞书集成，手动调用飞书新版 OAuth2 API（绕过 JustAuth 废弃接口）-----------
        if (CommonConstant.FEISHU.equalsIgnoreCase(source) || ThirdAppFeishuServiceImpl.THIRD_TYPE.equalsIgnoreCase(source)) {
            SysThirdAppConfig feishuConfig = buildFeishuConfigFromYaml();
            if (feishuConfig == null) {
                feishuConfig = appConfigService.getThirdConfigByThirdType(CommonConstant.TENANT_ID_DEFAULT_VALUE, ThirdAppFeishuServiceImpl.THIRD_TYPE);
            }
            if (feishuConfig == null) {
                response.setContentType("text/html;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("飞书应用尚未配置，请联系管理员");
                return;
            }
            // 将租户ID编码进state，格式：{tenantId}_{uuid}，callback时解析租户ID实现多租户精确查询
            //update-begin---author:liusq ---date:2026-05-15  for：【QQYUN-12767】飞书扫码登录改用yml配置，兼容tenantId为null的情况-----------
            Integer renderTenantId = feishuConfig.getTenantId() != null ? feishuConfig.getTenantId() : CommonConstant.TENANT_ID_DEFAULT_VALUE;
            String state = renderTenantId + "_" + AuthStateUtils.createState();
            //update-end---author:liusq ---date:2026-05-15  for：【QQYUN-12767】飞书扫码登录改用yml配置，兼容tenantId为null的情况-----------
            String redirectUri = CommonUtils.getBaseUrl(request) + "/sys/thirdLogin/feishu/callback";
            String authorizeUrl = thirdAppFeishuService.buildAuthorizeUrl(feishuConfig, redirectUri, state);
            log.info("飞书登录认证地址：{}", authorizeUrl);
            response.sendRedirect(authorizeUrl);
            return;
        }
        //update-end---author:liusq ---date:2026-05-13  for：【QQYUN-12767】飞书集成，手动调用飞书新版 OAuth2 API（绕过 JustAuth 废弃接口）-----------
        //update-begin---author:scott ---date:2026-06-04  for：【第三方登录NPE加固】source未在justauth.type中配置时直接拦截，避免JustAuth-starter1.4.0的get()未判空getExtend()抛出空指针-----------
        if (justAuthProperties.getType() == null || !justAuthProperties.getType().containsKey(source.toUpperCase())) {
            log.warn("第三方登录方式[{}]未配置（justauth.type 中不存在），已拦截 render 请求", source);
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            //update-begin---author:scott ---date:20261013  for：【安全 GHSA-4c5v-rq3g-j3pc】回显source前做HTML转义，防止反射型XSS-----------
            response.getWriter().write("第三方登录方式[" + HtmlUtils.htmlEscape(source) + "]尚未配置，请联系管理员");
            //update-end---author:scott ---date:20261013  for：【安全 GHSA-4c5v-rq3g-j3pc】回显source前做HTML转义，防止反射型XSS-----------
            return;
        }
        //update-end---author:scott ---date:2026-06-04  for：【第三方登录NPE加固】source未在justauth.type中配置时直接拦截，避免JustAuth-starter1.4.0的get()未判空getExtend()抛出空指针-----------
        AuthRequest authRequest = factory.get(source);
        String authorizeUrl = authRequest.authorize(AuthStateUtils.createState());
        log.info("第三方登录认证地址：" + authorizeUrl);
        response.sendRedirect(authorizeUrl);
    }

	@RequestMapping("/{source}/callback")
    public String loginThird(@PathVariable("source") String source, AuthCallback callback,ModelMap modelMap, HttpServletRequest request) {
		log.info("第三方登录进入callback：" + source + " params：" + JSONObject.toJSONString(callback));
        //update-begin---author:jeecg ---date:2026-05-13  for：【QQYUN-12767】飞书集成，手动调用飞书新版 OAuth2 API（绕过 JustAuth 废弃接口）-----------
        if (CommonConstant.FEISHU.equalsIgnoreCase(source) || ThirdAppFeishuServiceImpl.THIRD_TYPE.equalsIgnoreCase(source)) {
            String code = request.getParameter("code");
            log.info("【飞书】扫码登录回调 code={}", code);
            try {
                // 从state中解析租户ID（render时编码为 {tenantId}_{uuid} 格式）
                Integer feishuTenantId = CommonConstant.TENANT_ID_DEFAULT_VALUE;
                String stateParam = request.getParameter("state");
                if (stateParam != null && stateParam.contains("_")) {
                    try {
                        feishuTenantId = Integer.parseInt(stateParam.split("_", 2)[0]);
                    } catch (NumberFormatException ex) {
                        log.warn("【飞书】state中租户ID解析失败，使用默认租户，state={}", stateParam);
                    }
                }
                log.info("【飞书】扫码登录回调 tenantId={}", feishuTenantId);
                // 直接调用飞书新版 API 完成 code → token → 用户信息 全流程
                String redirectUri = CommonUtils.getBaseUrl(request) + "/sys/thirdLogin/feishu/callback";
                SysThirdAppConfig feishuYamlConfig = buildFeishuConfigFromYaml();
                SysUser loginUser = thirdAppFeishuService.oauth2Login(code, feishuTenantId, redirectUri, feishuYamlConfig);
                String token = saveToken(loginUser);
                modelMap.addAttribute("token", token);
            } catch (Exception e) {
                log.error("飞书登录回调异常", e);
                if (e.getMessage() != null && e.getMessage().contains("openId=")) {
                    String openId = e.getMessage().substring(e.getMessage().indexOf("openId=") + 7);
                    modelMap.addAttribute("token", "绑定手机号," + openId);
                } else {
                    modelMap.addAttribute("token", "登录失败");
                }
            }
            return "thirdLogin";
        }
        //update-end---author:liusq ---date:2026-05-13  for：【QQYUN-12767】飞书集成，手动调用飞书新版 OAuth2 API（绕过 JustAuth 废弃接口）-----------
        AuthRequest authRequest = factory.get(source);
        AuthResponse response = authRequest.login(callback);
        log.info(JSONObject.toJSONString(response));
        Result<JSONObject> result = new Result<JSONObject>();
        if(response.getCode()==2000) {

        	JSONObject data = JSONObject.parseObject(JSONObject.toJSONString(response.getData()));
        	String username = data.getString("username");
        	String avatar = data.getString("avatar");
        	String uuid = data.getString("uuid");
        	//构造第三方登录信息存储对象
			ThirdLoginModel tlm = new ThirdLoginModel(source, uuid, username, avatar);
        	//判断有没有这个人
			// 代码逻辑说明: 修改成查询第三方账户表
        	LambdaQueryWrapper<SysThirdAccount> query = new LambdaQueryWrapper<SysThirdAccount>();
        	query.eq(SysThirdAccount::getThirdType, source);
			// 代码逻辑说明: 【QQYUN-6667】敲敲云，线上解绑重新绑定一直提示这个---
        	query.eq(SysThirdAccount::getTenantId, CommonConstant.TENANT_ID_DEFAULT_VALUE);
			query.and(q -> q.eq(SysThirdAccount::getThirdUserUuid, uuid).or().eq(SysThirdAccount::getThirdUserId, uuid));
        	List<SysThirdAccount> thridList = sysThirdAccountService.list(query);
			SysThirdAccount user = null;
        	if(thridList==null || thridList.size()==0) {
				//否则直接创建新账号
				user = sysThirdAccountService.saveThirdUser(tlm,CommonConstant.TENANT_ID_DEFAULT_VALUE);
        	}else {
        		//已存在 只设置用户名 不设置头像
        		user = thridList.get(0);
        	}
        	// 生成token
			// 代码逻辑说明: 从第三方登录查询是否存在用户id，不存在绑定手机号
			if(oConvertUtils.isNotEmpty(user.getSysUserId())) {
				String sysUserId = user.getSysUserId();
				SysUser sysUser = sysUserService.getById(sysUserId);
				String token = saveToken(sysUser);
    			modelMap.addAttribute("token", token);
			}else{
				modelMap.addAttribute("token", "绑定手机号,"+""+uuid);
			}
        }else{
			modelMap.addAttribute("token", "登录失败");
		}
        result.setSuccess(false);
        result.setMessage("第三方登录异常,请联系管理员");
        return "thirdLogin";
    }

	/**
	 * 创建新账号
	 * @param model
	 * @return
	 */
	@PostMapping("/user/create")
	@ResponseBody
	public Result<String> thirdUserCreate(@RequestBody ThirdLoginModel model) {
		log.info("第三方登录创建新账号：" );
		Result<String> res = new Result<>();
		Object operateCode = redisUtil.get(CommonConstant.THIRD_LOGIN_CODE);
		if(operateCode==null || !operateCode.toString().equals(model.getOperateCode())){
			res.setSuccess(false);
			res.setMessage("校验失败");
			return res;
		}
		//创建新账号
		// 代码逻辑说明: 修改成从第三方登录查出来的user_id，在查询用户表尽行token
		SysThirdAccount user = sysThirdAccountService.saveThirdUser(model,CommonConstant.TENANT_ID_DEFAULT_VALUE);
		if(oConvertUtils.isNotEmpty(user.getSysUserId())){
			String sysUserId = user.getSysUserId();
			SysUser sysUser = sysUserService.getById(sysUserId);
			// 生成token
			String token = saveToken(sysUser);
			res.setResult(token);
			res.setSuccess(true);
		}
		return res;
	}

	/**
	 * 绑定账号 需要设置密码 需要走一遍校验
	 * @param json
	 * @return
	 */
	@PostMapping("/user/checkPassword")
	@ResponseBody
	public Result<String> checkPassword(@RequestBody JSONObject json) {
		Result<String> result = new Result<>();
		Object operateCode = redisUtil.get(CommonConstant.THIRD_LOGIN_CODE);
		if(operateCode==null || !operateCode.toString().equals(json.getString("operateCode"))){
			result.setSuccess(false);
			result.setMessage("校验失败");
			return result;
		}
		String username = json.getString("uuid");
		SysUser user = this.sysUserService.getUserByName(username);
		if(user==null){
			result.setMessage("用户未找到");
			result.setSuccess(false);
			return result;
		}
		String password = json.getString("password");
		String salt = user.getSalt();
		String passwordEncode = PasswordUtil.encrypt(user.getUsername(), password, salt);
		if(!passwordEncode.equals(user.getPassword())){
			result.setMessage("密码不正确");
			result.setSuccess(false);
			return result;
		}

		sysUserService.updateById(user);
		result.setSuccess(true);
		// 生成token
		String token = saveToken(user);
		result.setResult(token);
		return result;
	}

	/**
	 * 从 justauth yml 配置中读取飞书 app_id/app_secret，构建临时 SysThirdAppConfig。
	 * client-id 为空或为占位符 "??" 时返回 null，由调用方回退到数据库配置。
	 */
	private SysThirdAppConfig buildFeishuConfigFromYaml() {
		if (justAuthProperties == null || justAuthProperties.getType() == null) {
			return null;
		}
		AuthConfig authConfig = justAuthProperties.getType().get(CommonConstant.FEISHU);
		if (authConfig == null || authConfig.getClientId() == null || "??".equals(authConfig.getClientId().trim())) {
			return null;
		}
		SysThirdAppConfig config = new SysThirdAppConfig();
		config.setClientId(authConfig.getClientId());
		config.setClientSecret(authConfig.getClientSecret());
		config.setTenantId(CommonConstant.TENANT_ID_DEFAULT_VALUE);
		return config;
	}

	private String saveToken(SysUser user) {
		// 生成token
		String token = JwtUtil.sign(user.getUsername(), user.getPassword(), CommonConstant.CLIENT_TYPE_PC);
		redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
		// 设置超时时间
		redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
		return token;
	}

	/**
	 * 第三方登录回调接口
	 * @param token
	 * @param thirdType
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getLoginUser/{token}/{thirdType}/{tenantId}", method = RequestMethod.GET)
	@ResponseBody
	public Result<JSONObject> getThirdLoginUser(@PathVariable("token") String token,@PathVariable("thirdType") String thirdType,@PathVariable("tenantId") String tenantId) throws Exception {
		Result<JSONObject> result = new Result<JSONObject>();
		// 代码逻辑说明: 快速拦截非 JWT 格式的 token（如飞书 SDK 发出的 [tea-sdk]ready），避免 JWTDecodeException 触发 ERROR 日志
		if (token == null || token.split("\\.").length != 3) {
			log.debug("【第三方登录】收到非 JWT 格式 token，忽略: {}", token);
			return Result.noauth("token格式无效");
		}
		String username = JwtUtil.getUsername(token);
		// 代码逻辑说明: [QQYUN-11021]三方登录接口通过token获取用户信息漏洞修复------------
		if (!TokenUtils.verifyToken(token, sysBaseAPI, redisUtil)) {
			return Result.noauth("token验证失败");
		}
		//1. 校验用户是否有效
		SysUser sysUser = sysUserService.getUserByName(username);
		result = sysUserService.checkUserIsEffective(sysUser);
		if(!result.isSuccess()) {
			return result;
		}
		// 代码逻辑说明: 如果真实姓名和头像不存在就取第三方登录的
		LambdaQueryWrapper<SysThirdAccount> query = new LambdaQueryWrapper<>();
		query.eq(SysThirdAccount::getSysUserId,sysUser.getId());
		query.eq(SysThirdAccount::getThirdType,thirdType);
		query.eq(SysThirdAccount::getTenantId,oConvertUtils.getInt(tenantId,CommonConstant.TENANT_ID_DEFAULT_VALUE));
		// 代码逻辑说明: [QQYUN-4883]钉钉auth登录同一个租户下有同一个用户id------------
		List<SysThirdAccount> accountList = sysThirdAccountService.list(query);
		SysThirdAccount account = new SysThirdAccount();
		if(CollectionUtil.isNotEmpty(accountList)){
			account = accountList.get(0);
		}
		if(oConvertUtils.isEmpty(sysUser.getRealname())){
			sysUser.setRealname(account.getRealname());
		}
		if(oConvertUtils.isEmpty(sysUser.getAvatar())){
			sysUser.setAvatar(account.getAvatar());
		}
		JSONObject obj = new JSONObject();
		//第三方登确定登录租户和部门逻辑
		this.setUserTenantAndDepart(sysUser,obj,result);		
		//用户登录信息
		obj.put("userInfo", sysUser);
		//获取字典缓存【解决 #jeecg-boot/issues/3998】
		//update-begin---author:scott ---date:20261013  for：【issues/9838】返回字典版本号；先取版本号再查字典，避免期间字典变更导致前端拿到新版本号+旧数据-----------
		obj.put("dictVersion", sysDictService.getDictVersion());
		//update-end---author:scott ---date:20261013  for：【issues/9838】返回字典版本号；先取版本号再查字典，避免期间字典变更导致前端拿到新版本号+旧数据-----------
		obj.put("sysAllDictItems", sysDictService.queryAllDictItems());
		//token 信息
		obj.put("token", token);
		result.setResult(obj);
		result.setSuccess(true);
		result.setCode(200);
		baseCommonService.addLog("用户名: " + username + ",登录成功[第三方用户]！", CommonConstant.LOG_TYPE_1, null);
		return result;
	}
	/**
	 * 第三方绑定手机号返回token
	 *
	 * @param jsonObject
	 * @return
	 */
	@Operation(summary="手机号登录接口")
	@PostMapping("/bindingThirdPhone")
	@ResponseBody
	public Result<String> bindingThirdPhone(@RequestBody JSONObject jsonObject) {
		Result<String> result = new Result<String>();
		String phone = jsonObject.getString("mobile");
		String thirdUserUuid = jsonObject.getString("thirdUserUuid");
		// 校验验证码
		String captcha = jsonObject.getString("captcha");
		// 代码逻辑说明: VUEN-2245 【漏洞】发现新漏洞待处理20220906
		String redisKey = CommonConstant.PHONE_REDIS_KEY_PRE+phone;
		Object captchaCache = redisUtil.get(redisKey);
		if (oConvertUtils.isEmpty(captcha) || !captcha.equals(captchaCache)) {
			result.setMessage("验证码错误");
			result.setSuccess(false);
			return result;
		}
		//校验用户有效性
		SysUser sysUser = sysUserService.getUserByPhone(phone);
		if(sysUser != null){
			// 存在用户，直接绑定
			sysThirdAccountService.updateThirdUserId(sysUser,thirdUserUuid);
		}else{
			// 不存在手机号，创建用户
			sysUser = sysThirdAccountService.createUser(phone,thirdUserUuid,CommonConstant.TENANT_ID_DEFAULT_VALUE);
		}
		String token = saveToken(sysUser);
		result.setSuccess(true);
		result.setResult(token);
		return result;
	}

	/**
	 * 企业微信/钉钉 OAuth2登录
	 *
	 * @param source
	 * @param state
	 * @return
	 */
	@ResponseBody
	@GetMapping("/oauth2/{source}/login")
	public String oauth2LoginCallback(@PathVariable("source") String source, @RequestParam("state") String state, HttpServletRequest request, HttpServletResponse response,
									  @RequestParam(value = "tenantId",required = false,defaultValue = "0") String tenantId) throws Exception {
		String url;
		//应用id为空，说明没有配置lowAppId
		if(oConvertUtils.isEmpty(tenantId)){
			return "租户编码未配置";
		}
		if (CommonConstant.WECHAT_ENTERPRISE.equalsIgnoreCase(source)) {
			//换成第三方app配置表
			SysThirdAppConfig config = appConfigService.getThirdConfigByThirdType(Integer.valueOf(tenantId), MessageTypeEnum.QYWX.getType());
			if(null == config){
				return "还未配置企业微信应用，请配置企业微信应用";
			}
			StringBuilder builder = new StringBuilder();
			// 构造企业微信OAuth2登录授权地址
			builder.append("https://open.weixin.qq.com/connect/oauth2/authorize");
			// 企业的CorpID
			builder.append("?appid=").append(config.getClientId());
			// 授权后重定向的回调链接地址，请使用urlencode对链接进行处理
			String redirectUri = CommonUtils.getBaseUrl(request)  + "/sys/thirdLogin/oauth2/wechat_enterprise/callback?tenantId="+tenantId;;
			builder.append("&redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8"));
			// 返回类型，此时固定为：code
			builder.append("&response_type=code");
			// 应用授权作用域。
			// snsapi_base：静默授权，可获取成员的的基础信息（UserId与DeviceId）；
			builder.append("&scope=snsapi_base");
			// 重定向后会带上state参数，长度不可超过128个字节
			builder.append("&state=").append(state);
			// 终端使用此参数判断是否需要带上身份信息
			builder.append("#wechat_redirect");
			url = builder.toString();
		} else if (CommonConstant.DINGTALK.equalsIgnoreCase(source)) {
			//换成第三方app配置表
			SysThirdAppConfig appConfig = appConfigService.getThirdConfigByThirdType(Integer.valueOf(tenantId), MessageTypeEnum.DD.getType());
			if(null == appConfig){
				return "还未配置钉钉应用，请配置钉钉应用";
			}
			StringBuilder builder = new StringBuilder();
			// 构造钉钉OAuth2登录授权地址
			builder.append("https://login.dingtalk.com/oauth2/auth");
			// 授权通过/拒绝后回调地址。
			// 注意 需要与注册应用时登记的域名保持一致。
			String redirectUri = CommonUtils.getBaseUrl(request) + "/sys/thirdLogin/oauth2/dingtalk/callback?tenantId="+tenantId;
			builder.append("?redirect_uri=").append(URLEncoder.encode(redirectUri, "UTF-8"));
			// 固定值为code。
			// 授权通过后返回authCode。
			builder.append("&response_type=code");
			// 步骤一中创建的应用详情中获取。
			// 企业内部应用：client_id为应用的AppKey。
			builder.append("&client_id=").append(appConfig.getClientId());
			// 授权范围，授权页面显示的授权信息以应用注册时配置的为准。
			// openid：授权后可获得用户userid
			builder.append("&scope=openid");
			// 跟随authCode原样返回。
			builder.append("&state=").append(state);
            // 代码逻辑说明: [issues/I5BOUF]oauth2 钉钉无法登录------------
            builder.append("&prompt=").append("consent");
            url = builder.toString();
		}
		else if (CommonConstant.FEISHU.equalsIgnoreCase(source) || "feishu".equalsIgnoreCase(source)) {
			SysThirdAppConfig feishuConfig = appConfigService.getThirdConfigByThirdType(Integer.valueOf(tenantId), MessageTypeEnum.FS.getType());
			if (null == feishuConfig) {
				return "还未配置飞书应用，请配置飞书应用";
			}
			String redirectUri = CommonUtils.getBaseUrl(request) + "/sys/thirdLogin/oauth2/feishu/callback?tenantId=" + tenantId;
			url = thirdAppFeishuService.buildAuthorizeUrl(feishuConfig, redirectUri, state);
		}
		else {
			return "不支持的source";
		}
		log.info("oauth2 login url:" + url);
		response.sendRedirect(url);
		return "login…";
	}

    /**
     * 企业微信/钉钉 OAuth2登录回调
     *
     * @param code
     * @param state
     * @param response
     * @return
     */
	@ResponseBody
	@GetMapping("/oauth2/{source}/callback")
	public String oauth2LoginCallback(
			@PathVariable("source") String source,
			// 企业微信返回的code
			@RequestParam(value = "code", required = false) String code,
			// 钉钉返回的code
			@RequestParam(value = "authCode", required = false) String authCode,
			@RequestParam("state") String state,
			@RequestParam(name = "tenantId",defaultValue = "0") String tenantId,
			HttpServletRequest request,
			HttpServletResponse response) {
        SysUser loginUser;
        if (CommonConstant.WECHAT_ENTERPRISE.equalsIgnoreCase(source)) {
            log.info("【企业微信】OAuth2登录进入callback：code=" + code + ", state=" + state);
            loginUser = thirdAppWechatEnterpriseService.oauth2Login(code,Integer.valueOf(tenantId));
            if (loginUser == null) {
                return "登录失败";
            }
        } else if (CommonConstant.DINGTALK.equalsIgnoreCase(source)) {
			log.info("【钉钉】OAuth2登录进入callback：authCode=" + authCode + ", state=" + state);
			loginUser = thirdAppDingtalkService.oauth2Login(authCode,Integer.valueOf(tenantId));
			if (loginUser == null) {
				return "登录失败";
			}
        }
        else if (CommonConstant.FEISHU.equalsIgnoreCase(source) || "feishu".equalsIgnoreCase(source)) {
			log.info("【飞书】OAuth2登录进入callback：code={}, state={}", code, state);
			String redirectUri = CommonUtils.getBaseUrl(request) + "/sys/thirdLogin/oauth2/feishu/callback?tenantId=" + tenantId;
			loginUser = thirdAppFeishuService.oauth2Login(code, Integer.valueOf(tenantId), redirectUri);
			if (loginUser == null) {
				return "登录失败";
			}
		}
        else {
            return "不支持的source";
        }
        try {
			// 代码逻辑说明: 工作流发送消息 点击消息链接跳转办理页面
			String redirect = "";
			if (state.indexOf("?") > 0) {
				String[] arr = state.split("\\?");
				state = arr[0];
				if(arr.length>1){
					redirect = arr[1];
				}
			}
			//update-begin---author:scott ---date:20261013  for：【安全 GHSA-2x3m-cj6f-5894/GHSA-ffqp-583g-3c3w/GHSA-jj4x-xwpm-5vx8】跳转地址须在白名单内，防止登录token被带到外部站点-----------
			if (!isSafeRedirect(state, request)) {
				return "非法的跳转地址";
			}
			//update-end---author:scott ---date:20261013  for：【安全 GHSA-2x3m-cj6f-5894/GHSA-ffqp-583g-3c3w/GHSA-jj4x-xwpm-5vx8】跳转地址须在白名单内，防止登录token被带到外部站点-----------

			String token = saveToken(loginUser);
			state += "/oauth2-app/login?oauth2LoginToken=" + URLEncoder.encode(token, "UTF-8") + "&tenantId=" + URLEncoder.encode(tenantId, "UTF-8");
			// 代码逻辑说明: [issues/I5BOUF]oauth2 钉钉无法登录------------
			state += "&thirdType=" + source;
			//state += "&thirdType=" + "wechat_enterprise";
			if (redirect != null && redirect.length() > 0) {
				state += "&" + redirect;
			}

			log.info("OAuth2登录重定向地址: " + state);
            try {
                response.sendRedirect(state);
                return "ok";
            } catch (IOException e) {
                e.printStackTrace();
                return "重定向失败";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "解码失败";
        }
    }

	/**
	 * 校验OAuth2登录成功后的跳转地址(state)：只允许相对路径，或域名为当前后端、jeecg.domainUrl.pc/app/back 中配置的域名
	 *
	 * @author scott
	 * @since 2026-10-13 【安全 GHSA-2x3m-cj6f-5894/GHSA-ffqp-583g-3c3w/GHSA-jj4x-xwpm-5vx8】防止登录token被带到外部站点
	 */
	private boolean isSafeRedirect(String state, HttpServletRequest request) {
		if (state.startsWith("/")) {
			return !state.startsWith("//") && !state.startsWith("/\\");
		}
		DomainUrl domainUrl = jeecgBaseConfig.getDomainUrl();
		String[] allowUrls = domainUrl == null ? new String[]{CommonUtils.getBaseUrl(request)}
				: new String[]{CommonUtils.getBaseUrl(request), domainUrl.getPc(), domainUrl.getApp(), domainUrl.getBack()};
		try {
			URI uri = new URI(state);
			if (uri.getHost() == null || !uri.getScheme().matches("(?i)https?")) {
				return false;
			}
			for (String url : allowUrls) {
				if (oConvertUtils.isNotEmpty(url) && uri.getHost().equalsIgnoreCase(new URI(url).getHost())) {
					return true;
				}
			}
		} catch (URISyntaxException e) {
			log.warn("OAuth2登录跳转地址非法: {}", state);
		}
		return false;
	}

	/**
	 * 注册账号并绑定第三方账号 【低代码应用专用接口】
	 * @param jsonObject
	 * @param user
	 * @return
	 */
	@ResponseBody
	@PutMapping("/registerBindThirdAccount")
	public Result<String> registerBindThirdAccount(@RequestBody JSONObject jsonObject, SysUser user) {
		//手机号
		String phone = jsonObject.getString("phone");
		//验证码
		String smscode = jsonObject.getString("smscode");
		String redisKey = CommonConstant.PHONE_REDIS_KEY_PRE + phone;
		Object code = redisUtil.get(redisKey);
		//第三方uuid
		String thirdUserUuid = jsonObject.getString("thirdUserUuid");
		String username = jsonObject.getString("username");
		//未设置用户名，则用手机号作为用户名
		if (oConvertUtils.isEmpty(username)) {
			username = phone;
		}
		//未设置密码，则随机生成一个密码
		String password = jsonObject.getString("password");
		if (oConvertUtils.isEmpty(password)) {
			password = RandomUtil.randomString(8);
		}
		String email = jsonObject.getString("email");
		SysUser sysUser1 = sysUserService.getUserByName(username);
		if (sysUser1 != null) {
			return Result.error("用户名已注册");
		}
		SysUser sysUser2 = sysUserService.getUserByPhone(phone);
		if (sysUser2 != null) {
			return Result.error("该手机号已注册");
		}
		if (oConvertUtils.isNotEmpty(email)) {
			SysUser sysUser3 = sysUserService.getUserByEmail(email);
			if (sysUser3 != null) {
				return Result.error("邮箱已被注册");
			}
		}
		if (null == code) {
			return Result.error("手机验证码失效，请重新获取");
		}
		if (!smscode.equals(code.toString())) {
			return Result.error("手机验证码错误");
		}
		String realname = jsonObject.getString("realname");
		if (oConvertUtils.isEmpty(realname)) {
			realname = username;
		}
		try {
			//保存用户表
			user.setCreateTime(new Date());
			String salt = oConvertUtils.randomGen(8);
			String passwordEncode = PasswordUtil.encrypt(username, password, salt);
			user.setSalt(salt);
			user.setUsername(username);
			user.setRealname(realname);
			user.setPassword(passwordEncode);
			user.setEmail(email);
			user.setPhone(phone);
			user.setStatus(CommonConstant.USER_UNFREEZE);
			user.setDelFlag(CommonConstant.DEL_FLAG_0);
			user.setActivitiSync(CommonConstant.ACT_SYNC_1);
			sysUserService.addUserWithRole(user, "");
			//保存第三方用户表
			sysThirdAccountService.updateThirdUserId(user, thirdUserUuid);
			String token = saveToken(user);
			return Result.ok(token);
		} catch (Exception e) {
			return Result.error("注册失败");
		}
	}

	/**
	 * 设置用户租户和部门信息
	 *
	 * @param sysUser
	 * @param obj
	 * @param result
	 */
	private void setUserTenantAndDepart(SysUser sysUser, JSONObject obj, Result<JSONObject> result) {
		//1.设置登录租户
		sysUserService.setLoginTenant(sysUser, obj, sysUser.getUsername(), result);
		//2.设置登录部门
		String orgCode = sysUser.getOrgCode();
		//部门不为空还是用原来的部门code
		if(StringUtils.isEmpty(orgCode)){
			List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
			//部门不为空取第一个作为当前登录部门
			if(CollectionUtil.isNotEmpty(departs)){
				orgCode = departs.get(0).getOrgCode();
				sysUser.setOrgCode(orgCode);
				this.sysUserService.updateUserDepart(sysUser.getUsername(), orgCode,null);
			}
		}
	}

	/**
	 * 新版钉钉登录
	 *
	 * @param authCode
	 * @param state
	 * @param tenantId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@GetMapping("/oauth2/dingding/login")
	public String OauthDingDingLogin(@RequestParam(value = "authCode", required = false) String authCode,
									 @RequestParam("state") String state,
									 @RequestParam(name = "tenantId",defaultValue = "0") String tenantId,
									 HttpServletRequest request,
									 HttpServletResponse response) {
		SysUser loginUser = thirdAppDingtalkService.oauthDingDingLogin(authCode,Integer.valueOf(tenantId));
		try {
			String redirect = "";
			if (state.indexOf("?") > 0) {
				String[] arr = state.split("\\?");
				state = arr[0];
				if(arr.length>1){
					redirect = arr[1];
				}
			}
			//update-begin---author:scott ---date:20261013  for：【安全 GHSA-2x3m-cj6f-5894/GHSA-jj4x-xwpm-5vx8】跳转地址须在白名单内，防止登录token被带到外部站点-----------
			if (!isSafeRedirect(state, request)) {
				return "非法的跳转地址";
			}
			//update-end---author:scott ---date:20261013  for：【安全 GHSA-2x3m-cj6f-5894/GHSA-jj4x-xwpm-5vx8】跳转地址须在白名单内，防止登录token被带到外部站点-----------
			String token = saveToken(loginUser);
			state += "/oauth2-app/login?oauth2LoginToken=" + URLEncoder.encode(token, "UTF-8") + "&tenantId=" + URLEncoder.encode(tenantId, "UTF-8");
			state += "&thirdType=DINGTALK";
			if (redirect != null && redirect.length() > 0) {
				state += "&" + redirect;
			}
			log.info("OAuth2登录重定向地址: " + state);
			try {
				response.sendRedirect(state);
				return "ok";
			} catch (IOException e) {
				log.error(e.getMessage(),e);
				return "重定向失败";
			}
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(),e);
			return "解码失败";
		}
	}

	/**
	 * 获取企业id和应用id
	 * @param tenantId
	 * @return
	 */
	@ResponseBody
	@GetMapping("/get/corpId/clientId")
	public Result<SysThirdAppConfig> getCorpIdClientId(@RequestParam(value = "tenantId", defaultValue = "0") String tenantId){
		Result<SysThirdAppConfig> result = new Result<>();
		SysThirdAppConfig sysThirdAppConfig = thirdAppDingtalkService.getCorpIdClientId(Integer.valueOf(tenantId));
		result.setSuccess(true);
		result.setResult(sysThirdAppConfig);
		return result;
	}
}