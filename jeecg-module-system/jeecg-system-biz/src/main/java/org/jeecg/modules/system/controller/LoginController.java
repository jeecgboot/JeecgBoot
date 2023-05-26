package org.jeecg.modules.system.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.*;
import org.jeecg.common.util.encryption.EncryptedString;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.modules.base.service.BaseCommonService;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysRoleIndex;
import org.jeecg.modules.system.entity.SysTenant;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.model.SysLoginModel;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.service.impl.SysBaseApiImpl;
import org.jeecg.modules.system.util.RandImageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	private ISysTenantService sysTenantService;
	@Autowired
    private ISysDictService sysDictService;
	@Resource
	private BaseCommonService baseCommonService;

	@Autowired
	private JeecgBaseConfig jeecgBaseConfig;

	private final String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

	@ApiOperation("登录接口")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Result<JSONObject> login(@RequestBody SysLoginModel sysLoginModel){
		Result<JSONObject> result = new Result<JSONObject>();
		String username = sysLoginModel.getUsername();
		String password = sysLoginModel.getPassword();
		//update-begin-author:taoyan date:2022-11-7 for: issues/4109 平台用户登录失败锁定用户
		if(isLoginFailOvertimes(username)){
			return result.error500("sys.login.loginFailOvertimes");
		}
		//update-end-author:taoyan date:2022-11-7 for: issues/4109 平台用户登录失败锁定用户
		//update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题
		//前端密码加密，后端进行密码解密
		//password = AesEncryptUtil.desEncrypt(sysLoginModel.getPassword().replaceAll("%2B", "\\+")).trim();//密码解密
		//update-begin--Author:scott  Date:20190805 for：暂时注释掉密码加密逻辑，有点问题

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
			//update-begin-author:taoyan date:2022-11-7 for: issues/4109 平台用户登录失败锁定用户
			addLoginFailOvertimes(username);
			//update-end-author:taoyan date:2022-11-7 for: issues/4109 平台用户登录失败锁定用户
			result.error500("sys.login.wrongCredentials");
			return result;
		}
				
		//用户登录信息
		userInfo(sysUser, result);
		//update-begin--Author:liusq  Date:20210126  for：登录成功，删除redis中的验证码
		//update-begin--Author:liusq  Date:20210126  for：登录成功，删除redis中的验证码
		redisUtil.del(CommonConstant.LOGIN_FAIL + username);
		LoginUser loginUser = new LoginUser();
		BeanUtils.copyProperties(sysUser, loginUser);
		baseCommonService.addLog("用户名: " + username + ",登录成功！", CommonConstant.LOG_TYPE_1, null,loginUser);
        //update-end--Author:wangshuai  Date:20200714  for：登录日志没有记录人员
		return result;
	}


	/**
	 * 【vue3专用】获取用户信息
	 */
	@GetMapping("/user/getUserInfo")
	public Result<JSONObject> getUserInfo(HttpServletRequest request){
		Result<JSONObject> result = new Result<JSONObject>();
		String  username = JwtUtil.getUserNameByToken(request);
		if(oConvertUtils.isNotEmpty(username)) {
			// 根据用户名查询用户信息
			SysUser sysUser = sysUserService.getUserByName(username);
			JSONObject obj=new JSONObject();

			//update-begin---author:scott ---date:2022-06-20  for：vue3前端，支持自定义首页-----------
			String version = request.getHeader(CommonConstant.VERSION);
			//update-begin---author:liusq ---date:2022-06-29  for：接口返回值修改，同步修改这里的判断逻辑-----------
			SysRoleIndex roleIndex = sysUserService.getDynamicIndexByUserRole(username, version);
			if (oConvertUtils.isNotEmpty(version) && roleIndex != null && oConvertUtils.isNotEmpty(roleIndex.getUrl())) {
				String homePath = roleIndex.getUrl();
				if (!homePath.startsWith(SymbolConstant.SINGLE_SLASH)) {
					homePath = SymbolConstant.SINGLE_SLASH + homePath;
				}
				sysUser.setHomePath(homePath);
			}
			//update-begin---author:liusq ---date:2022-06-29  for：接口返回值修改，同步修改这里的判断逻辑-----------
			//update-end---author:scott ---date::2022-06-20  for：vue3前端，支持自定义首页--------------
			
			obj.put("userInfo",sysUser);
			obj.put("sysAllDictItems", sysDictService.queryAllDictItems());
			result.setResult(obj);
			result.success("");
		}
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
	    	return Result.error("sys.login.errorLogout");
	    }
	    String username = JwtUtil.getUsername(token);
		LoginUser sysUser = sysBaseApi.getUserByName(username);
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
	    	return Result.ok("sys.login.logoutSuccess");
	    }else {
	    	return Result.error("sys.login.invalidToken");
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
		result.success("sys.login.loginSuccessTitle");
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
		
		//获取登录部门
		String orgCode= user.getOrgCode();
		//获取登录租户
		Integer tenantId = user.getLoginTenantId();
		this.sysUserService.updateUserDepart(username, orgCode,tenantId);
		SysUser sysUser = sysUserService.getUserByName(username);
		JSONObject obj = new JSONObject();
		obj.put("userInfo", sysUser);
		result.setResult(obj);
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
		String username = sysUser.getUsername();
		String syspassword = sysUser.getPassword();
		// 获取用户部门信息
		JSONObject obj = new JSONObject(new LinkedHashMap<>());

		//1.生成token
		String token = JwtUtil.sign(username, syspassword);
		// 设置token缓存有效时间
		redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
		redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);
		obj.put("token", token);

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
		} else if (departs.size() == 1) {
			sysUserService.updateUserDepart(username, departs.get(0).getOrgCode(),null);
			obj.put("multi_depart", 1);
		} else {
			//查询当前是否有登录部门
			// update-begin--Author:wangshuai Date:20200805 for：如果用戶为选择部门，数据库为存在上一次登录部门，则取一条存进去
			SysUser sysUserById = sysUserService.getById(sysUser.getId());
			if(oConvertUtils.isEmpty(sysUserById.getOrgCode())){
				sysUserService.updateUserDepart(username, departs.get(0).getOrgCode(),null);
			}
			// update-end--Author:wangshuai Date:20200805 for：如果用戶为选择部门，数据库为存在上一次登录部门，则取一条存进去
			obj.put("multi_depart", 2);
		}
		obj.put("sysAllDictItems", sysDictService.queryAllDictItems());
		result.setResult(obj);
		result.success("sys.login.loginSuccessTitle");
		return result;
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
	 * 切换菜单表为vue3的表
	 */
	@RequiresRoles({"admin"})
	@GetMapping(value = "/switchVue3Menu")
	public Result<String> switchVue3Menu(HttpServletResponse response) {
		Result<String> res = new Result<String>();
		sysPermissionService.switchVue3Menu();
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
		JSONObject obj = new JSONObject();
		
		//update-begin-author:taoyan date:2022-11-7 for: issues/4109 平台用户登录失败锁定用户
		if(isLoginFailOvertimes(username)){
			return result.error500("sys.login.loginFailOvertimes");
		}
		//update-end-author:taoyan date:2022-11-7 for: issues/4109 平台用户登录失败锁定用户
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
			//update-begin-author:taoyan date:2022-11-7 for: issues/4109 平台用户登录失败锁定用户
			addLoginFailOvertimes(username);
			//update-end-author:taoyan date:2022-11-7 for: issues/4109 平台用户登录失败锁定用户
			result.error500("sys.login.wrongCredentials");
			return result;
		}
		
		//3.设置登录部门
		String orgCode = sysUser.getOrgCode();
		if(oConvertUtils.isEmpty(orgCode)) {
			//如果当前用户无选择部门 查看部门关联信息
			List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
			//update-begin-author:taoyan date:20220117 for: JTC-1068【app】新建用户，没有设置部门及角色，点击登录提示暂未归属部，一直在登录页面 使用手机号登录 可正常
			if (departs == null || departs.size() == 0) {
				/*result.error500("用户暂未归属部门,不可登录!");
				return result;*/
			}else{
				orgCode = departs.get(0).getOrgCode();
				sysUser.setOrgCode(orgCode);
				this.sysUserService.updateUserDepart(username, orgCode,null);
			}
			//update-end-author:taoyan date:20220117 for: JTC-1068【app】新建用户，没有设置部门及角色，点击登录提示暂未归属部，一直在登录页面 使用手机号登录 可正常
		}

		//4. 设置登录租户
		Result<JSONObject> loginTenantError = sysUserService.setLoginTenant(sysUser, obj, username, result);
		if (loginTenantError != null) {
			return loginTenantError;
		}

		//5. 设置登录用户信息
		obj.put("userInfo", sysUser);
		
		//6. 生成token
		String token = JwtUtil.sign(username, syspassword);
		// 设置超时时间
		redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
		redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME*2 / 1000);

		//token 信息
		obj.put("token", token);
		result.setResult(obj);
		result.setSuccess(true);
		result.setCode(200);
		baseCommonService.addLog("User : " + username + ",Logged In [移动端]！", CommonConstant.LOG_TYPE_1, null);
		return result;
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
		// 1小时
		redisUtil.set(key, ++val, 3600);
	}

}