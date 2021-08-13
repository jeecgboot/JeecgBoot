package org.jeecg.modules.system.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.*;
import org.jeecg.common.util.encryption.EncryptedString;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysTenant;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.SysLoginModel;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.util.RandImageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Author scott
 * @since 2018-12-17
 */
@RestController
@RequestMapping("/sys")
@Api(tags="用户登录")
@Slf4j
public class LoginController {
	@Autowired
	private ISysUserService sysUserService;
	@Autowired
	private ISysBaseAPI sysBaseAPI;
	@Autowired
	private ISysLogService logService;
	@Autowired
    private RedisUtil redisUtil;
	@Autowired
    private ISysDepartService sysDepartService;
	@Autowired
	private ISysTenantService sysTenantService;
	@Autowired
    private ISysDictService sysDictService;
	@Resource
	private BaseCommonService baseCommonService;

	private static final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

	@ApiOperation("登录接口")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Result<JSONObject> login(@RequestBody SysLoginModel sysLoginModel){
		Result<JSONObject> result = new Result<JSONObject>();
		String username = sysLoginModel.getUsername();
		String password = sysLoginModel.getPassword();
		//update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题
		//前端密码加密，后端进行密码解密
		//password = AesEncryptUtil.desEncrypt(sysLoginModel.getPassword().replaceAll("%2B", "\\+")).trim();//密码解密
		//update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题

		//update-begin-author:taoyan date:20190828 for:校验验证码
        String captcha = sysLoginModel.getCaptcha();
        if(captcha==null){
            result.error500("验证码无效");
            return result;
        }
        String lowerCaseCaptcha = captcha.toLowerCase();
		String realKey = MD5Util.MD5Encode(lowerCaseCaptcha+sysLoginModel.getCheckKey(), "utf-8");
		Object checkCode = redisUtil.get(realKey);
		//当进入登录页时，有一定几率出现验证码错误 #1714
		if(checkCode==null || !checkCode.toString().equals(lowerCaseCaptcha)) {
			result.error500("验证码错误");
			return result;
		}
		//update-end-author:taoyan date:20190828 for:校验验证码
		
		//1. 校验用户是否有效
		//update-begin-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
		LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(SysUser::getUsername,username);
		SysUser sysUser = sysUserService.getOne(queryWrapper);
		//update-end-author:wangshuai date:20200601 for: 登录代码验证用户是否注销bug，if条件永远为false
		result = sysUserService.checkUserIsEffective(sysUser);
		if(!result.isSuccess()) {
			return result;
		}
		
		//2. 校验用户名或密码是否正确
		String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
		String syspassword = sysUser.getPassword();
		if (!syspassword.equals(userpassword)) {
			result.error500("用户名或密码错误");
			return result;
		}
				
		//用户登录信息
		userInfo(sysUser, result);
		//update-begin--Author:liusq  Date:20210126  for：登录成功，删除redis中的验证码
		redisUtil.del(realKey);
		//update-begin--Author:liusq  Date:20210126  for：登录成功，删除redis中的验证码
		LoginUser loginUser = new LoginUser();
		BeanUtils.copyProperties(sysUser, loginUser);
		baseCommonService.addLog("用户名: " + username + ",登录成功！", CommonConstant.LOG_TYPE_1, null,loginUser);
        //update-end--Author:wangshuai  Date:20200714  for：登录日志没有记录人员
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
		LoginUser sysUser = sysBaseAPI.getUserByName(username);
	    if(sysUser!=null) {
			//update-begin--Author:wangshuai  Date:20200714  for：登出日志没有记录人员
			baseCommonService.addLog("用户名: "+sysUser.getRealname()+",退出成功！", CommonConstant.LOG_TYPE_1, null,sysUser);
			//update-end--Author:wangshuai  Date:20200714  for：登出日志没有记录人员
	    	log.info(" 用户名:  "+sysUser.getRealname()+",退出成功！ ");
	    	//清空用户登录Token缓存
	    	redisUtil.del(CommonConstant.PREFIX_USER_TOKEN + token);
	    	//清空用户登录Shiro权限缓存
			redisUtil.del(CommonConstant.PREFIX_USER_SHIRO_CACHE + sysUser.getId());
			//清空用户的缓存信息（包括部门信息），例如sys:cache:user::<username>
			redisUtil.del(String.format("%s::%s", CacheConstant.SYS_USERS_CACHE, sysUser.getUsername()));
			//调用shiro的logout
			SecurityUtils.getSubject().logout();
	    	return Result.ok("退出登录成功！");
	    }else {
	    	return Result.error("Token无效!");
	    }
	}
	
	/**
	 * 获取访问量
	 * @return
	 */
	@GetMapping("loginfo")
	public Result<JSONObject> loginfo() {
		Result<JSONObject> result = new Result<JSONObject>();
		JSONObject obj = new JSONObject();
		//update-begin--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数
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
		//update-end--Author:zhangweijian  Date:20190428 for：传入开始时间，结束时间参数
		obj.put("todayIp", todayIp);
		result.setResult(obj);
		result.success("登录成功");
		return result;
	}
	
	/**
	 * 获取访问量
	 * @return
	 */
	@GetMapping("visitInfo")
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
		String orgCode= user.getOrgCode();
		this.sysUserService.updateUserDepart(username, orgCode);
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
	public Result<String> sms(@RequestBody JSONObject jsonObject) {
		Result<String> result = new Result<String>();
		String mobile = jsonObject.get("mobile").toString();
		//手机号模式 登录模式: "2"  注册模式: "1"
		String smsmode=jsonObject.get("smsmode").toString();
		log.info(mobile);
		if(oConvertUtils.isEmpty(mobile)){
			result.setMessage("手机号不允许为空！");
			result.setSuccess(false);
			return result;
		}
		Object object = redisUtil.get(mobile);
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
					if("该用户不存在，请注册".equals(message)){
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
				}
			}

			if (b == false) {
				result.setMessage("短信验证码发送失败,请稍后重试");
				result.setSuccess(false);
				return result;
			}
			//验证码10分钟内有效
			redisUtil.set(mobile, captcha, 600);
			//update-begin--Author:scott  Date:20190812 for：issues#391
			//result.setResult(captcha);
			//update-end--Author:scott  Date:20190812 for：issues#391
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
	@ApiOperation("手机号登录接口")
	@PostMapping("/phoneLogin")
	public Result<JSONObject> phoneLogin(@RequestBody JSONObject jsonObject) {
		Result<JSONObject> result = new Result<JSONObject>();
		String phone = jsonObject.getString("mobile");
		
		//校验用户有效性
		SysUser sysUser = sysUserService.getUserByPhone(phone);
		result = sysUserService.checkUserIsEffective(sysUser);
		if(!result.isSuccess()) {
			return result;
		}
		
		String smscode = jsonObject.getString("captcha");
		Object code = redisUtil.get(phone);
		if (!smscode.equals(code)) {
			result.setMessage("手机验证码错误");
			return result;
		}
		//用户信息
		userInfo(sysUser, result);
		//添加日志
		baseCommonService.addLog("用户名: " + sysUser.getUsername() + ",登录成功！", CommonConstant.LOG_TYPE_1, null);

		return result;
	}


	/**
	 * 用户信息
	 *
	 * @param sysUser
	 * @param result
	 * @return
	 */
	private Result<JSONObject> userInfo(SysUser sysUser, Result<JSONObject> result) {
		String syspassword = sysUser.getPassword();
		String username = sysUser.getUsername();
		// 获取用户部门信息
		JSONObject obj = new JSONObject();
		List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
		obj.put("departs", departs);
		if (departs == null || departs.size() == 0) {
			obj.put("multi_depart", 0);
		} else if (departs.size() == 1) {
			sysUserService.updateUserDepart(username, departs.get(0).getOrgCode());
			obj.put("multi_depart", 1);
		} else {
			//查询当前是否有登录部门
			// update-begin--Author:wangshuai Date:20200805 for：如果用戶为选择部门，数据库为存在上一次登录部门，则取一条存进去
			SysUser sysUserById = sysUserService.getById(sysUser.getId());
			if(oConvertUtils.isEmpty(sysUserById.getOrgCode())){
				sysUserService.updateUserDepart(username, departs.get(0).getOrgCode());
			}
			// update-end--Author:wangshuai Date:20200805 for：如果用戶为选择部门，数据库为存在上一次登录部门，则取一条存进去
			obj.put("multi_depart", 2);
		}
		// update-begin--Author:sunjianlei Date:20210802 for：获取用户租户信息
		String tenantIds = sysUser.getRelTenantIds();
		if (oConvertUtils.isNotEmpty(tenantIds)) {
			List<String> tenantIdList = Arrays.asList(tenantIds.split(","));
			// 该方法仅查询有效的租户，如果返回0个就说明所有的租户均无效。
			List<SysTenant> tenantList = sysTenantService.queryEffectiveTenant(tenantIdList);
			if (tenantList.size() == 0) {
				result.error500("与该用户关联的租户均已被冻结，无法登录！");
				return result;
			} else {
				obj.put("tenantList", tenantList);
			}
		}
		// update-end--Author:sunjianlei Date:20210802 for：获取用户租户信息
		// 生成token
		String token = JwtUtil.sign(username, syspassword);
		// 设置token缓存有效时间
		redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
		redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
		obj.put("token", token);
		obj.put("userInfo", sysUser);
		obj.put("sysAllDictItems", sysDictService.queryAllDictItems());
		result.setResult(obj);
		result.success("登录成功");
		return result;
	}

	/**
	 * 获取加密字符串
	 * @return
	 */
	@GetMapping(value = "/getEncryptedString")
	public Result<Map<String,String>> getEncryptedString(){
		Result<Map<String,String>> result = new Result<Map<String,String>>();
		Map<String,String> map = new HashMap<String,String>();
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
	@ApiOperation("获取验证码")
	@GetMapping(value = "/randomImage/{key}")
	public Result<String> randomImage(HttpServletResponse response,@PathVariable String key){
		Result<String> res = new Result<String>();
		try {
			String code = RandomUtil.randomString(BASE_CHECK_CODES,4);
			String lowerCaseCode = code.toLowerCase();
			String realKey = MD5Util.MD5Encode(lowerCaseCode+key, "utf-8");
			redisUtil.set(realKey, lowerCaseCode, 60);
			String base64 = RandImageUtil.generate(code);
			res.setSuccess(true);
			res.setResult(base64);
		} catch (Exception e) {
			res.error500("获取验证码出错"+e.getMessage());
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * app登录
	 * @param sysLoginModel
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mLogin", method = RequestMethod.POST)
	public Result<JSONObject> mLogin(@RequestBody SysLoginModel sysLoginModel) throws Exception {
		Result<JSONObject> result = new Result<JSONObject>();
		String username = sysLoginModel.getUsername();
		String password = sysLoginModel.getPassword();
		
		//1. 校验用户是否有效
		SysUser sysUser = sysUserService.getUserByName(username);
		result = sysUserService.checkUserIsEffective(sysUser);
		if(!result.isSuccess()) {
			return result;
		}
		
		//2. 校验用户名或密码是否正确
		String userpassword = PasswordUtil.encrypt(username, password, sysUser.getSalt());
		String syspassword = sysUser.getPassword();
		if (!syspassword.equals(userpassword)) {
			result.error500("用户名或密码错误");
			return result;
		}
		
		String orgCode = sysUser.getOrgCode();
		if(oConvertUtils.isEmpty(orgCode)) {
			//如果当前用户无选择部门 查看部门关联信息
			List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
			if (departs == null || departs.size() == 0) {
				result.error500("用户暂未归属部门,不可登录!");
				return result;
			}
			orgCode = departs.get(0).getOrgCode();
			sysUser.setOrgCode(orgCode);
			this.sysUserService.updateUserDepart(username, orgCode);
		}
		JSONObject obj = new JSONObject();
		//用户登录信息
		obj.put("userInfo", sysUser);
		
		// 生成token
		String token = JwtUtil.sign(username, syspassword);
		// 设置超时时间
		redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
		redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME*2 / 1000);

		//token 信息
		obj.put("token", token);
		result.setResult(obj);
		result.setSuccess(true);
		result.setCode(200);
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
		String realKey = MD5Util.MD5Encode(lowerCaseCaptcha+checkKey, "utf-8");
		Object checkCode = redisUtil.get(realKey);
		if(checkCode==null || !checkCode.equals(lowerCaseCaptcha)) {
			return Result.error("验证码错误");
		}
		return Result.ok();
	}

}