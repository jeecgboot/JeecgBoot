package org.jeecg.modules.cas.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.cas.util.CasServiceUtil;
import org.jeecg.modules.cas.util.XmlUtils;
import org.jeecg.modules.system.entity.SysDepart;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * CAS单点登录客户端登录认证
 * </p>
 *
 * @Author zhoujf
 * @since 2018-12-20
 */
@Slf4j
@RestController
@RequestMapping("/sys/cas/client")
public class CasClientController {

	@Autowired
	private ISysUserService sysUserService;
	@Autowired
    private ISysDepartService sysDepartService;
	@Autowired
    private RedisUtil redisUtil;
	
	@Value("${cas.prefixUrl}")
    private String prefixUrl;
	
	
	@GetMapping("/validateLogin")
	public Object validateLogin(@RequestParam(name="ticket") String ticket,
								@RequestParam(name="service") String service,
								HttpServletRequest request,
								HttpServletResponse response) throws Exception {
		Result<JSONObject> result = new Result<JSONObject>();
		log.info("Rest api login.");
		try {
			String validateUrl = prefixUrl+"/p3/serviceValidate";
			String res = CasServiceUtil.getStValidate(validateUrl, ticket, service);
			log.info("res."+res);
			final String error = XmlUtils.getTextForElement(res, "authenticationFailure");
			if(StringUtils.isNotEmpty(error)) {
				throw new Exception(error);
			}
			final String principal = XmlUtils.getTextForElement(res, "user");
			if (StringUtils.isEmpty(principal)) {
	            throw new Exception("No principal was found in the response from the CAS server.");
	        }
			log.info("-------token----username---"+principal);
		    //1. 校验用户是否有效
	  		SysUser sysUser = sysUserService.getUserByName(principal);
	  		result = sysUserService.checkUserIsEffective(sysUser);
	  		if(!result.isSuccess()) {
	  			return result;
	  		}
	 		String token = JwtUtil.sign(sysUser.getUsername(), sysUser.getPassword());
	 		// 设置超时时间
	 		redisUtil.set(CommonConstant.PREFIX_USER_TOKEN + token, token);
	 		redisUtil.expire(CommonConstant.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME*2 / 1000);

	 		//获取用户部门信息
			JSONObject obj = new JSONObject();
			List<SysDepart> departs = sysDepartService.queryUserDeparts(sysUser.getId());
			obj.put("departs", departs);
			if (departs == null || departs.size() == 0) {
				obj.put("multi_depart", 0);
			} else if (departs.size() == 1) {
				sysUserService.updateUserDepart(principal, departs.get(0).getOrgCode(),null);
				obj.put("multi_depart", 1);
			} else {
				obj.put("multi_depart", 2);
			}
			obj.put("token", token);
			obj.put("userInfo", sysUser);
			result.setResult(obj);
			result.success("登录成功");
	  		
		} catch (Exception e) {
			//e.printStackTrace();
			result.error500(e.getMessage());
		}
		return new HttpEntity<>(result);
	}

	
}
