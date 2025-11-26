package org.jeecg.modules.system.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.constant.enums.DySmsEnum;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.*;
import org.jeecg.common.util.encryption.AesEncryptUtil;
import org.jeecg.common.util.encryption.EncryptedString;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.config.shiro.IgnoreAuth;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysRoleIndex;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.SysLoginModel;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.service.impl.SysBaseApiImpl;
import org.jeecg.modules.system.util.RandImageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/sys")
@Tag(name="用户登录")
@Slf4j
public class LoginController {
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private ISysPermissionService sysPermissionService;
	@Autowired
	private SysBaseApiImpl sysBaseApi;
	@Autowired
	private ISysLogService logService;
	@Autowired
    private RedisUtil redisUtil;
	@Autowired
    private ISysDepartService sysDepartService;
	@Autowired
    private ISysDictService sysDictService;
	@Resource
	private BaseCommonService baseCommonService;
	@Autowired
	private JeecgBaseConfig jeecgBaseConfig;
	
	private final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";
	/**
	 * 线程池用于异步发送纪要
	 */
	public static ExecutorService cachedThreadPool = new ShiroThreadPoolExecutor(0, 1024, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());

	

	@Operation(summary="登录接口")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Result<JSONObject> login(@RequestBody SysLoginModel sysLoginModel, HttpServletRequest request){
		Result<JSONObject> result = new Result<>();
		String username = sysLoginModel.getUsername();
		// 密码加密传输(尝试 AES解密，失败视为明文)
		String password  = AesEncryptUtil.resolvePassword(sysLoginModel.getPassword());
		log.debug("登录密码，原始密码:{}，解密密码:{}" , sysLoginModel.getPassword(), password);

		//step.1 登录失败超出次数5次锁定用户10分钟
		if(isLoginFailOvertimes(username)){
			return result.error500("该用户登录失败次数过多，请于10分钟后再次登录！");
		}

		// step.2 验证码check
        String realKey = validateCaptcha(sysLoginModel, result);
        if (realKey == null) {
            return result;
        }
		
		// step.3 校验用户是否存在且有效
		LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SysUser::getUsername,username);
		SysUser sysUser = sysUserService.getOne(queryWrapper);
		result = sysUserService.checkUserIsEffective(sysUser);
		if(!result.isSuccess()) {
			return result;
		}

		// step.4 校验用户名或密码是否正确
		String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
		String syspassword = sysUser.getPassword();
		if (!syspassword.equals(userpassword)) {
			addLoginFailOvertimes(username);
			result.error500("用户名或密码错误");
			return result;
		}

		// step.5 登录成功获取用户信息
		String loginOrgCode = sysLoginModel.getLoginOrgCode();
		sysUser.setLoginOrgCode(loginOrgCode);
		userInfo(sysUser, result, request, CommonConstant.CLIENT_TYPE_PC);

		// step.6  登录成功删除验证码
		redisUtil.del(realKey);
		redisUtil.del(CommonConstant.LOGIN_FAIL + username);

		// step.7 记录用户登录日志
		LoginUser loginUser = new LoginUser();
		BeanUtils.copyProperties(sysUser, loginUser);
		baseCommonService.addLog("用户名: " + username + ",登录成功！", CommonConstant.LOG_TYPE_1, null,loginUser);
		return result;
	}


	/**
	 * 【vue3专用】获取用户信息
	 */
	@GetMapping("/user/getUserInfo")
	public Result<JSONObject> getUserInfo(HttpServletRequest request){
		long start = System.currentTimeMillis();
		Result<JSONObject> result = new Result<JSONObject>();
		String  username = JwtUtil.getUserNameByToken(request);
		if(oConvertUtils.isNotEmpty(username)) {
			// 根据用户名查询用户信息
			SysUser sysUser = sysUserService.getUserByName(username);
			JSONObject obj=new JSONObject();
			log.debug("1 获取用户信息耗时（用户基础信息）" + (System.currentTimeMillis() - start) + "毫秒");

			// 代码逻辑说明: vue3前端，支持自定义首页-----------
			String vue3Version = request.getHeader(CommonConstant.VERSION);
			SysRoleIndex roleIndex = sysUserService.getDynamicIndexByUserRole(username, vue3Version);
			if (oConvertUtils.isNotEmpty(vue3Version) && roleIndex != null && oConvertUtils.isNotEmpty(roleIndex.getUrl())) {
				String homePath = roleIndex.getUrl();
				if (!homePath.startsWith(SymbolConstant.SINGLE_SLASH)) {
					homePath = SymbolConstant.SINGLE_SLASH + homePath;
				}
				sysUser.setHomePath(homePath);
			}
			log.debug("2 获取用户信息耗时 (首页面配置)" + (System.currentTimeMillis() - start) + "毫秒");
			
			obj.put("userInfo",sysUser);
			obj.put("sysAllDictItems", sysDictService.queryAllDictItems());
			log.debug("3 获取用户信息耗时 (字典数据)" + (System.currentTimeMillis() - start) + "毫秒");
			
			result.setResult(obj);
			result.success("");
		}
		log.debug("end 获取用户信息耗时 " + (System.currentTimeMillis() - start) + "毫秒");
		return result;

	}
	
	/**
	 * 退出登录
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/logout")
	public Result<Object> logout(HttpServletRequest request,HttpServletResponse response) {
		//用户退出逻辑
	    String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
	    if(oConvertUtils.isEmpty(token)) {
	    	return Result.error("退出登录失败！");
	    }
	    String username = JwtUtil.getUsername(token);
		LoginUser sysUser = sysBaseApi.getUserByName(username);
	    if(sysUser!=null) {
			asyncClearLogoutCache(token, sysUser); // 异步清理
			SecurityUtils.getSubject().logout();
	    	return Result.ok("退出登录成功！");
	    }else {
	    	return Result.error("Token无效!");
	    }
	}

	/**
	 * 清理用户缓存
	 * 
	 * @param token
	 * @param sysUser
	 */
	private void asyncClearLogoutCache(String token, LoginUser sysUser) {
		cachedThreadPool.execute(()->{
			//清空用户登录Token缓存
			redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
			//清空用户登录Shiro权限缓存
			redisUtil.del(CommonConstant.PREFIX_USER_SHIRO_CACHE + sysUser.getId());
			//清空用户的缓存信息（包括部门信息），例如sys:cache:user::<username>
			redisUtil.del(String.format("%s::%s", CacheConstant.SYS_USERS_CACHE, sysUser.getUsername()));
			//清空是否允许同一账号多地同时登录缓存（PC端和APP端）
			redisUtil.del(CommonConstant.PREFIX_USER_TOKEN_PC + sysUser.getUsername());
			redisUtil.del(CommonConstant.PREFIX_USER_TOKEN_APP + sysUser.getUsername());
			redisUtil.del(CommonConstant.PREFIX_USER_TOKEN_PHONE + sysUser.getUsername());
			baseCommonService.addLog("用户名: "+sysUser.getRealname()+",退出成功！", CommonConstant.LOG_TYPE_1, null, sysUser);
			log.debug("【退出成功操作】异步处理，退出后，清理用户缓存： "+sysUser.getRealname());
		});
	}
	
	/**
	 * 获取访问量
	 * @return
	 */
	@GetMapping("loginfo")
	public Result<JSONObject> loginfo() {
		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();
		// 获取一天的开始和结束时间
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date dayStart = calendar.getTime();
		calendar.add(Calendar.DATE, 1);
		Date dayEnd = calendar.getTime();
		// 获取系统访问记录
		Long totalVisitCount = logService.findTotalVisitCount();
		obj.put("totalVisitCount", totalVisitCount);
		Long todayVisitCount = logService.findTodayVisitCount(dayStart,dayEnd);
		obj.put("todayVisitCount", todayVisitCount);
		Long todayIp = logService.findTodayIp(dayStart,dayEnd);
		obj.put("todayIp", todayIp);
		result.setResult(obj);
		result.success("登录成功");
		return result;
	}
	
	/**
	 * 获取访问量
	 * @return
	 */
	@GetMapping("/visitInfo")
	public Result<List<Map<String,Object>>> visitInfo() {
		Result<List<Map<String,Object>>> result = new Result<List<Map<String,Object>>>();
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date dayEnd = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        Date dayStart = calendar.getTime();
        List<Map<String,Object>> list = logService.findVisitCount(dayStart, dayEnd);
		result.setResult(oConvertUtils.toLowerCasePageList(list));
		return result;
	}
	
	
	/**
	 * 登陆成功选择用户当前部门
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/selectDepart", method = RequestMethod.PUT)
	public Result<JSONObject> selectDepart(@RequestBody SysUser user) {
		Result<JSONObject> result = new Result<JSONObject>();
		String username = user.getUsername();
		if(oConvertUtils.isEmpty(username)) {
			LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
			username = sysUser.getUsername();
		}
		
		//获取登录部门
		String orgCode= user.getOrgCode();
		//获取登录租户
		Integer tenantId = user.getLoginTenantId();
		//设置用户登录部门和登录租户
		this.sysUserService.updateUserDepart(username, orgCode,tenantId);
		SysUser sysUser = sysUserService.getUserByName(username);
		JSONObject obj = new JSONObject();
		obj.put("userInfo", sysUser);
		result.setResult(obj);
		return result;
	}

	/**
	 * 短信登录接口
	 * 
	 * @param jsonObject
	 * @return
	 */
	@PostMapping(value = "/sms")
	public Result<String> sms(@RequestBody JSONObject jsonObject,HttpServletRequest request) {
		Result<String> result = new Result<String>();
		String clientIp = IpUtils.getIpAddr(request);
		String mobile = jsonObject.get("mobile").toString();
		//手机号模式 登录模式: "2"  注册模式: "1"
		String smsmode=jsonObject.get("smsmode").toString();
		log.info("-------- IP:{}, 手机号：{}，获取绑定验证码", clientIp, mobile);
		
		if(oConvertUtils.isEmpty(mobile)){
			result.setMessage("手机号不允许为空！");
			result.setSuccess(false);
			return result;
		}
		
		// VUEN-2245【漏洞】发现新漏洞待处理20220906
		String redisKey = CommonConstant.PHONE_REDIS_KEY_PRE+mobile;
		Object object = redisUtil.get(redisKey);
		
		if (object != null) {
			result.setMessage("验证码10分钟内，仍然有效！");
			result.setSuccess(false);
			return result;
		}

		//-------------------------------------------------------------------------------------
		//增加 check防止恶意刷短信接口
		if(!DySmsLimit.canSendSms(clientIp)){
			log.warn("--------[警告] IP地址:{}, 短信接口请求太多-------", clientIp);
			result.setMessage("短信接口请求太多，请稍后再试！");
			result.setCode(CommonConstant.PHONE_SMS_FAIL_CODE);
			result.setSuccess(false);
			return result;
		}
		//-------------------------------------------------------------------------------------

		//随机数
		String captcha = RandomUtil.randomNumbers(6);
		JSONObject obj = new JSONObject();
    	obj.put("code", captcha);
		try {
			boolean b = false;
			//注册模板
			if (CommonConstant.SMS_TPL_TYPE_1.equals(smsmode)) {
				SysUser sysUser = sysUserService.getUserByPhone(mobile);
				if(sysUser!=null) {
					result.error500(" 手机号已经注册，请直接登录！");
					baseCommonService.addLog("手机号已经注册，请直接登录！", CommonConstant.LOG_TYPE_1, null);
					return result;
				}
				b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.REGISTER_TEMPLATE_CODE);
			}else {
				//登录模式，校验用户有效性
				SysUser sysUser = sysUserService.getUserByPhone(mobile);
				result = sysUserService.checkUserIsEffective(sysUser);
				if(!result.isSuccess()) {
					String message = result.getMessage();
					String userNotExist="该用户不存在，请注册";
					if(userNotExist.equals(message)){
						result.error500("该用户不存在或未绑定手机号");
					}
					return result;
				}
				
				/**
				 * smsmode 短信模板方式  0 .登录模板、1.注册模板、2.忘记密码模板
				 */
				if (CommonConstant.SMS_TPL_TYPE_0.equals(smsmode)) {
					//登录模板
					b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.LOGIN_TEMPLATE_CODE);
				} else if(CommonConstant.SMS_TPL_TYPE_2.equals(smsmode)) {
					//忘记密码模板
					b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.FORGET_PASSWORD_TEMPLATE_CODE);
                    // 代码逻辑说明: 【issues/8567】严重：修改密码存在水平越权问题。---
                    if(b){
                        String username = sysUser.getUsername();
                        obj.put("username",username);
                        redisUtil.set(redisKey, obj.toJSONString(), 600);
                        result.setSuccess(true);
                        return result;
                    }
                }
			}

			if (b == false) {
				result.setMessage("短信验证码发送失败,请稍后重试");
				result.setSuccess(false);
				return result;
			}
			
			//验证码10分钟内有效
			redisUtil.set(redisKey, captcha, 600);
			result.setSuccess(true);

		} catch (ClientException e) {
			e.printStackTrace();
			result.error500(" 短信接口未配置，请联系管理员！");
			return result;
		}
		return result;
	}
	

	/**
	 * 手机号登录接口
	 * 
	 * @param jsonObject
	 * @return
	 */
	@Operation(summary="手机号登录接口")
	@PostMapping("/phoneLogin")
	public Result<JSONObject> phoneLogin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
		Result<JSONObject> result = new Result<JSONObject>();
		String phone = jsonObject.getString("mobile");
		// 平台用户登录失败锁定用户
		if(isLoginFailOvertimes(phone)){
			return result.error500("该用户登录失败次数过多，请于10分钟后再次登录！");
		}
		
		//校验用户有效性
		SysUser sysUser = sysUserService.getUserByPhone(phone);
		result = sysUserService.checkUserIsEffective(sysUser);
		if(!result.isSuccess()) {
			return result;
		}
		
		String smscode = jsonObject.getString("captcha");

		// 代码逻辑说明: VUEN-2245 【漏洞】发现新漏洞待处理20220906
		String redisKey = CommonConstant.PHONE_REDIS_KEY_PRE+phone;
		Object code = redisUtil.get(redisKey);

		if (!smscode.equals(code)) {
			addLoginFailOvertimes(phone);
			return Result.error("手机验证码错误");
		}
		//用户信息
		String loginOrgCode = jsonObject.getString("loginOrgCode");
		sysUser.setLoginOrgCode(loginOrgCode);
		userInfo(sysUser, result, request, CommonConstant.CLIENT_TYPE_PHONE);
		//添加日志
		baseCommonService.addLog("用户名: " + sysUser.getUsername() + ",登录成功！", CommonConstant.LOG_TYPE_1, null);
        redisUtil.removeAll(redisKey);
		return result;
	}


	/**
	 * 用户信息
	 *
	 * @param sysUser
	 * @param result
	 * @return
	 */
	private Result<JSONObject> userInfo(SysUser sysUser, Result<JSONObject> result, HttpServletRequest request, String clientType) {
		String username = sysUser.getUsername();
		String syspassword = sysUser.getPassword();
		JSONObject obj = new JSONObject(new LinkedHashMap<>());

		//1.生成token，并设置超时时间
		String token = JwtUtil.sign(username, syspassword, clientType);
		redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
		// 根据客户端类型设置对应的过期时间
		long expireTime = CommonConstant.CLIENT_TYPE_APP.equalsIgnoreCase(clientType) 
			? JwtUtil.APP_EXPIRE_TIME * 2 / 1000 
			: JwtUtil.EXPIRE_TIME * 2 / 1000;
		redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, expireTime);
		obj.put("token", token);

		// 是否允许同一账号多地同时登录，踢掉之前的登录
		handleSingleSignOn(username, token, clientType);

		//2.设置登录租户
		Result<JSONObject> loginTenantError = sysUserService.setLoginTenant(sysUser, obj, username,result);
		if (loginTenantError != null) {
			return loginTenantError;
		}

		//3.设置登录用户信息
		obj.put("userInfo", sysUser);
		
		//4.设置登录部门
		List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
		obj.put("departs", departs);
		if (departs == null || departs.size() == 0) {
			obj.put("multi_depart", 0);
			sysUserService.updateUserDepart(username, null, null);
		} else if (departs.size() == 1) {
			sysUserService.updateUserDepart(username, departs.get(0).getOrgCode(),null);
			obj.put("multi_depart", 1);
		} else {
			//查询当前是否有登录部门
			SysUser sysUserById = sysUserService.getById(sysUser.getId());
			//【部门切换】支持登录页面选择部门
			String loginOrgCode = sysUser.getLoginOrgCode();

			// 判断上次登录部门orgCode是否在departs中
			boolean orgCodeInDeparts = departs.stream().anyMatch(d -> sysUserById.getOrgCode() != null && sysUserById.getOrgCode().equalsIgnoreCase(d.getOrgCode()));
			if (!orgCodeInDeparts) {
				sysUserById.setOrgCode(null);
			}

			// 如果未设置登录部门，则将登录部门设置为用户选择的 loginOrgCode（优先），否则设置为默认的第一个部门
			if(oConvertUtils.isEmpty(sysUserById.getOrgCode())){
				String orgCode = oConvertUtils.isNotEmpty(loginOrgCode) ? loginOrgCode : departs.get(0).getOrgCode();
				sysUserService.updateUserDepart(username, orgCode, null);
			} else {
				// 已设置登录部门，若用户本次登录选择了不同的部门，则优先使用用户选择的 loginOrgCode 更新登录部门
				String orgCode = sysUserById.getOrgCode();
				if(oConvertUtils.isNotEmpty(loginOrgCode) && !orgCode.equalsIgnoreCase(loginOrgCode)){
					sysUserService.updateUserDepart(username, loginOrgCode, null);
				}
			}
			obj.put("multi_depart", 2);
		}

		// 5.vue3版本不加载字典数据，vue2下加载字典
		String vue3Version = request.getHeader(CommonConstant.VERSION);
		if(oConvertUtils.isEmpty(vue3Version)){
			obj.put("sysAllDictItems", sysDictService.queryAllDictItems());
		}
		
		result.setResult(obj);
		result.success("登录成功");
		return result;
	}

	/**
	 * 同一账号在同一客户端类型只能登录一次
	 * 
	 * @author scott
	 * @date 2025-10-31
	 * PC端、APP端、手机号登录分别独立，互不影响
	 * 
	 * @param username 用户名
	 * @param newToken 新生成的token
	 * @param clientType 客户端类型（PC、APP、PHONE）
	 */
	private void handleSingleSignOn(String username, String newToken, String clientType) {
		// 检查是否允许并发登录
		if (jeecgBaseConfig.getFirewall() == null || jeecgBaseConfig.getFirewall().getIsConcurrent()==null || Boolean.TRUE.equals(jeecgBaseConfig.getFirewall().getIsConcurrent())) {
			// 允许并发登录，只设置当前用户的token缓存，不踢掉之前的登录
			log.debug("并发登录已启用：用户[{}]在{}端允许多地同时登录", username, clientType);
			return;
		}
		
		log.info("【并发登录限制已开启】 用户[{}]在{}端不允许多地同时登录", username, clientType);
		// 根据客户端类型选择对应的Redis key前缀
		String redisKeyPrefix;
		if (CommonConstant.CLIENT_TYPE_APP.equalsIgnoreCase(clientType)) {
			redisKeyPrefix = CommonConstant.PREFIX_USER_TOKEN_APP;
		} else if (CommonConstant.CLIENT_TYPE_PHONE.equalsIgnoreCase(clientType)) {
			redisKeyPrefix = CommonConstant.PREFIX_USER_TOKEN_PHONE;
		} else {
			redisKeyPrefix = CommonConstant.PREFIX_USER_TOKEN_PC;
		}
		
		String userTokenKey = redisKeyPrefix + username;
		
		// 获取该用户在当前客户端类型下之前的token
		Object oldTokenObj = redisUtil.get(userTokenKey);
		if (oldTokenObj != null && !oldTokenObj.equals(newToken)) {
			String oldToken = oldTokenObj.toString();
			// 清除旧登录token的缓存（设置 1 小时过期时间）
			redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + oldToken);
			redisUtil.set(CommonConstant.PREFIX_USER_TOKEN_ERROR_MSG + oldToken, "不允许同一账号多地同时登录，当前登录被踢掉！", 60 * 1 * 60);
			log.info("【并发登录限制已开启】用户[{}]在{}端的旧登录已被踢下线！", username, clientType);
			log.info("【并发登录限制已开启】用户被踢下线，新token: {}，旧token：{}", newToken, oldToken);
		}
		
		// 保存新的token到单点登录缓存
		redisUtil.set(userTokenKey, newToken);
		redisUtil.expire(userTokenKey, JwtUtil.EXPIRE_TIME * 2 / 1000);
	}

	/**
	 * 获取加密字符串
	 * @return
	 */
	@GetMapping(value = "/getEncryptedString")
	public Result<Map<String,String>> getEncryptedString(){
		Result<Map<String,String>> result = new Result<Map<String,String>>();
		Map<String,String> map = new HashMap(5);
		map.put("key", EncryptedString.key);
		map.put("iv",EncryptedString.iv);
		result.setResult(map);
		return result;
	}

	/**
	 * 后台生成图形验证码 ：有效
	 * @param response
	 * @param key
	 */
	@Operation(summary="获取验证码")
	@GetMapping(value = "/randomImage/{key}")
	public Result<String> randomImage(HttpServletResponse response,@PathVariable("key") String key){
		Result<String> res = new Result<String>();
		try {
			//生成验证码，存到redis中
			String code = RandomUtil.randomString(BASE_CHECK_CODES,4);
			String lowerCaseCode = code.toLowerCase();
			String keyPrefix = Md5Util.md5Encode(key + jeecgBaseConfig.getSignatureSecret(), "utf-8");
			String realKey = keyPrefix + lowerCaseCode;
			redisUtil.removeAll(keyPrefix);
			redisUtil.set(realKey, lowerCaseCode, 60);
			log.debug("获取验证码，Redis key = {}，checkCode = {}", realKey, code);
			String base64 = RandImageUtil.generate(code);
			res.setSuccess(true);
			res.setResult(base64);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			res.error500("获取验证码失败,请检查redis配置!");
			return res;
		}
		return res;
	}

//	/**
//	 * 切换菜单表为vue3的表
//	 */
//	@RequiresRoles({"admin"})
//	@GetMapping(value = "/switchVue3Menu")
//	public Result<String> switchVue3Menu(HttpServletResponse response) {
//		Result<String> res = new Result<String>();	
//		sysPermissionService.switchVue3Menu();
//		return res;
//	}
	
	/**
	 * app登录
	 * @param sysLoginModel
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mLogin", method = RequestMethod.POST)
	public Result<JSONObject> mLogin(@RequestBody SysLoginModel sysLoginModel, HttpServletRequest request) throws Exception {
		Result<JSONObject> result = new Result<JSONObject>();
		String username = sysLoginModel.getUsername();
		// 密码加密传输(尝试 AES解密，失败视为明文)
		String password  = AesEncryptUtil.resolvePassword(sysLoginModel.getPassword());
		log.debug("登录密码，原始密码:{}，解密密码:{}" , sysLoginModel.getPassword(), password);

		JSONObject obj = new JSONObject();
		
		// 1.平台用户登录失败锁定用户
		if(isLoginFailOvertimes(username)){
			return result.error500("该用户登录失败次数过多，请于10分钟后再次登录！");
		}
		// 2.校验用户是否有效
		SysUser sysUser = sysUserService.getUserByName(username);
		result = sysUserService.checkUserIsEffective(sysUser);
		if(!result.isSuccess()) {
			return result;
		}
		
		// 3.校验用户名或密码是否正确
		String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
		String syspassword = sysUser.getPassword();
		if (!syspassword.equals(userpassword)) {
			addLoginFailOvertimes(username);
			result.error500("用户名或密码错误");
			return result;
		}
		
		//4.设置登录部门
		String orgCode = sysUser.getOrgCode();
		//登录设置的组织
		String loginOrgCode = sysLoginModel.getLoginOrgCode();
		if(oConvertUtils.isEmpty(orgCode)) {
			//如果当前用户无选择部门 查看部门关联信息
			if(oConvertUtils.isNotEmpty(loginOrgCode)){
				sysUser.setOrgCode(loginOrgCode);
				this.sysUserService.updateUserDepart(username, loginOrgCode,null);
			}else{
				List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
				if (departs != null && !departs.isEmpty()) {
					orgCode = departs.get(0).getOrgCode();
					sysUser.setOrgCode(orgCode);
					this.sysUserService.updateUserDepart(username, orgCode,null);
				}
			}
		}else{
			if(oConvertUtils.isNotEmpty(loginOrgCode) && !orgCode.equalsIgnoreCase(loginOrgCode)){
				sysUser.setOrgCode(loginOrgCode);
				sysUserService.updateUserDepart(username, loginOrgCode,null);
			}
		}

		//5. 设置登录租户
		Result<JSONObject> loginTenantError = sysUserService.setLoginTenant(sysUser, obj, username, result);
		if (loginTenantError != null) {
			return loginTenantError;
		}
		// 设置登录用户信息
		obj.put("userInfo", sysUser);

		//6. 生成token，并设置超时时间
		String token = JwtUtil.sign(username, syspassword, CommonConstant.CLIENT_TYPE_APP);
		redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
		redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.APP_EXPIRE_TIME*2 / 1000);
		obj.put("token", token);
		result.setResult(obj);
		result.setSuccess(true);
		result.setCode(200);

		// 7.是否允许同一账号多地同时登录(APP端登录，踢掉之前的APP端登录)
		handleSingleSignOn(username, token, CommonConstant.CLIENT_TYPE_APP);
		
		// 8.登录成功记录日志
		baseCommonService.addLog("用户名: " + username + ",登录成功[移动端]！", CommonConstant.LOG_TYPE_1, null);
		return result;
	}

	/**
	 * 图形验证码
	 * @param sysLoginModel
	 * @return
	 */
	@RequestMapping(value = "/checkCaptcha", method = RequestMethod.POST)
	public Result<?> checkCaptcha(@RequestBody SysLoginModel sysLoginModel){
		String captcha = sysLoginModel.getCaptcha();
		String checkKey = sysLoginModel.getCheckKey();
		if(captcha==null){
			return Result.error("验证码无效");
		}
		String lowerCaseCaptcha = captcha.toLowerCase();
		String realKey = Md5Util.md5Encode(lowerCaseCaptcha+checkKey, "utf-8");
		Object checkCode = redisUtil.get(realKey);
		if(checkCode==null || !checkCode.equals(lowerCaseCaptcha)) {
			return Result.error("验证码错误");
		}
		return Result.ok();
	}
	/**
	 * 登录二维码
	 */
	@Operation(summary = "登录二维码")
	@GetMapping("/getLoginQrcode")
	public Result<?>  getLoginQrcode() {
		String qrcodeId = CommonConstant.LOGIN_QRCODE_PRE+IdWorker.getIdStr();
		//定义二维码参数
		Map params = new HashMap(5);
		params.put("qrcodeId", qrcodeId);
		//存放二维码唯一标识30秒有效
		redisUtil.set(CommonConstant.LOGIN_QRCODE + qrcodeId, qrcodeId, 30);
		return Result.OK(params);
	}
	/**
	 * 扫码二维码
	 */
	@Operation(summary = "扫码登录二维码")
	@PostMapping("/scanLoginQrcode")
	public Result<?> scanLoginQrcode(@RequestParam String qrcodeId, @RequestParam String token) {
		Object check = redisUtil.get(CommonConstant.LOGIN_QRCODE + qrcodeId);
		if (oConvertUtils.isNotEmpty(check)) {
			//存放token给前台读取
			redisUtil.set(CommonConstant.LOGIN_QRCODE_TOKEN+qrcodeId, token, 60);
		} else {
			return Result.error("二维码已过期,请刷新后重试");
		}
		return Result.OK("扫码成功");
	}


	/**
	 * 获取用户扫码后保存的token
	 */
	@Operation(summary = "获取用户扫码后保存的token")
	@GetMapping("/getQrcodeToken")
	public Result getQrcodeToken(@RequestParam String qrcodeId) {
		Object token = redisUtil.get(CommonConstant.LOGIN_QRCODE_TOKEN + qrcodeId);
		Map result = new HashMap(5);
		Object qrcodeIdExpire = redisUtil.get(CommonConstant.LOGIN_QRCODE + qrcodeId);
		if (oConvertUtils.isEmpty(qrcodeIdExpire)) {
			//二维码过期通知前台刷新
			result.put("token", "-2");
			return Result.OK(result);
		}
		if (oConvertUtils.isNotEmpty(token)) {
			result.put("success", true);
			result.put("token", token);
		} else {
			result.put("token", "-1");
		}
		return Result.OK(result);
	}

	/**
	 * 登录失败超出次数5 返回true
	 * @param username
	 * @return
	 */
	private boolean isLoginFailOvertimes(String username){
		String key = CommonConstant.LOGIN_FAIL + username;
		Object failTime = redisUtil.get(key);
		if(failTime!=null){
			Integer val = Integer.parseInt(failTime.toString());
			if(val>5){
				return true;
			}
		}
		return false;
	}

	/**
	 * 记录登录失败次数
	 * @param username
	 */
	private void addLoginFailOvertimes(String username){
		String key = CommonConstant.LOGIN_FAIL + username;
		Object failTime = redisUtil.get(key);
		Integer val = 0;
		if(failTime!=null){
			val = Integer.parseInt(failTime.toString());
		}
		// 10分钟，一分钟为60s
		redisUtil.set(key, ++val, 600);
	}

	/**
	 * 发送短信验证码接口(修改密码)
	 *
	 * @param jsonObject
	 * @return
	 */
	@PostMapping(value = "/sendChangePwdSms")
	public Result<String> sendSms(@RequestBody JSONObject jsonObject) {
		Result<String> result = new Result<>();
		String mobile = jsonObject.get("mobile").toString();
		if (oConvertUtils.isEmpty(mobile)) {
			result.setMessage("手机号不允许为空！");
			result.setSuccess(false);
			return result;
		}
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String username = sysUser.getUsername();
		LambdaQueryWrapper<SysUser> query = new LambdaQueryWrapper<>();
		query.eq(SysUser::getUsername, username).eq(SysUser::getPhone, mobile);
		SysUser user = sysUserService.getOne(query);
		if (null == user) {
			return Result.error("当前登录用户和绑定的手机号不匹配，无法修改密码！");
		}
		String redisKey = CommonConstant.PHONE_REDIS_KEY_PRE + mobile;
		Object object = redisUtil.get(redisKey);
		if (object != null) {
			result.setMessage("验证码10分钟内，仍然有效！");
			result.setSuccess(false);
			return result;
		}
		//随机数
		String captcha = RandomUtil.randomNumbers(6);
		JSONObject obj = new JSONObject();
		obj.put("code", captcha);
		try {
			boolean b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.CHANGE_PASSWORD_TEMPLATE_CODE);
			if (!b) {
				result.setMessage("短信验证码发送失败,请稍后重试");
				result.setSuccess(false);
				return result;
			}
            //【issues/8567】严重：修改密码存在水平越权问题
            obj.put("username",username);
            redisUtil.set(redisKey, obj.toJSONString(), 300);
			result.setSuccess(true);
		} catch (ClientException e) {
			e.printStackTrace();
			result.error500(" 短信接口未配置，请联系管理员！");
			return result;
		}
		return result;
	}

	
	/**
	 * 图形验证码
	 * @param sysLoginModel
	 * @return
	 */
	@RequestMapping(value = "/smsCheckCaptcha", method = RequestMethod.POST)
	public Result<?> smsCheckCaptcha(@RequestBody SysLoginModel sysLoginModel, HttpServletRequest request){
		String captcha = sysLoginModel.getCaptcha();
		String checkKey = sysLoginModel.getCheckKey();
		if(captcha==null){
			return Result.error("验证码无效");
		}
		String lowerCaseCaptcha = captcha.toLowerCase();
		String realKey = Md5Util.md5Encode(lowerCaseCaptcha+checkKey+jeecgBaseConfig.getSignatureSecret(), "utf-8");
		Object checkCode = redisUtil.get(realKey);
		if(checkCode==null || !checkCode.equals(lowerCaseCaptcha)) {
			return Result.error("验证码错误");
		}
		String clientIp = IpUtils.getIpAddr(request);
		//清空短信记录数量
		DySmsLimit.clearSendSmsCount(clientIp);
		redisUtil.removeAll(realKey);
		return Result.ok();
	}
	/**
	 * 登录获取用户部门信息
	 *
	 * @param jsonObject
	 * @return
	 */
	@IgnoreAuth
	@RequestMapping(value = "/loginGetUserDeparts", method = RequestMethod.POST)
	public Result<JSONObject> loginGetUserDeparts(@RequestBody JSONObject jsonObject, HttpServletRequest request){
		return sysUserService.loginGetUserDeparts(jsonObject);
	}
	
	/**
     * 校验验证码工具方法，校验失败直接返回Result，校验通过返回realKey
     */
    private String validateCaptcha(SysLoginModel sysLoginModel, Result<JSONObject> result) {
		// 判断是否启用登录验证码校验
		if (jeecgBaseConfig.getFirewall() != null && Boolean.FALSE.equals(jeecgBaseConfig.getFirewall().getEnableLoginCaptcha())) {
			log.warn("关闭了登录验证码校验，跳过验证码校验！");
			return "LoginWithoutVerifyCode";
		}
		
        String captcha = sysLoginModel.getCaptcha();
        if (captcha == null) {
            result.error500("验证码无效");
            return null;
        }
        String lowerCaseCaptcha = captcha.toLowerCase();
        String keyPrefix = Md5Util.md5Encode(sysLoginModel.getCheckKey() + jeecgBaseConfig.getSignatureSecret(), "utf-8");
        String realKey = keyPrefix + lowerCaseCaptcha;
        Object checkCode = redisUtil.get(realKey);
        if (checkCode == null || !checkCode.toString().equals(lowerCaseCaptcha)) {
            log.warn("验证码错误，key= {} , Ui checkCode= {}, Redis checkCode = {}", sysLoginModel.getCheckKey(), lowerCaseCaptcha, checkCode);
            result.error500("验证码错误");
            result.setCode(HttpStatus.PRECONDITION_FAILED.value());
            return null;
        }
        return realKey;
    }
}