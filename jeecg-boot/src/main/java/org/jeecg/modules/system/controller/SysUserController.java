package org.jeecg.modules.system.controller;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysUser;
import org.jeecg.modules.system.entity.SysUserRole;
import org.jeecg.modules.system.service.ISysUserRoleService;
import org.jeecg.modules.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author scott
 * @since 2018-12-20
 */
@Slf4j
@RestController
@RequestMapping("/sys/user")
public class SysUserController {
	
	@Autowired
	private ISysUserService sysUserService;
	
	@Autowired
	private ISysUserRoleService sysUserRoleService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<IPage<SysUser>> queryPageList(SysUser user,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
		Result<IPage<SysUser>> result = new Result<IPage<SysUser>>();
		QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>(user);
		Page<SysUser> page = new Page<SysUser>(pageNo,pageSize);
		//排序逻辑 处理
		String column = req.getParameter("column");
		String order = req.getParameter("order");
		if(oConvertUtils.isNotEmpty(column) && oConvertUtils.isNotEmpty(order)) {
			if("asc".equals(order)) {
				queryWrapper.orderByAsc(oConvertUtils.camelToUnderline(column));
			}else {
				queryWrapper.orderByDesc(oConvertUtils.camelToUnderline(column));
			}
		}
		//TODO 多字段排序
		//TODO 过滤逻辑处理
		//TODO begin、end逻辑处理
		//TODO 一个强大的功能，前端传一个字段字符串，后台只返回这些字符串对应的字段
		//创建时间/创建人的赋值
		IPage<SysUser> pageList = sysUserService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@RequiresRoles({"admin"})
	public Result<SysUser> add(@RequestBody JSONObject jsonObject) {
		Result<SysUser> result = new Result<SysUser>();
		String selectedRoles = jsonObject.getString("selectedroles");
		try {
			SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
			user.setCreateTime(new Date());//设置创建时间
			String salt = oConvertUtils.randomGen(8);
			user.setSalt(salt);
			String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), salt);
			user.setPassword(passwordEncode);
			user.setStatus(1);
			user.setDelFlag("0");
			sysUserService.addUserWithRole(user, selectedRoles);
			result.success("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("操作失败");
		}
		return result;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	@RequiresRoles({"admin"})
	public Result<SysUser> eidt(@RequestBody JSONObject jsonObject) {
		Result<SysUser> result = new Result<SysUser>();
		try {
			SysUser sysUser = sysUserService.getById(jsonObject.getString("id"));
			if(sysUser==null) {
				result.error500("未找到对应实体");
			}else {
				SysUser user = JSON.parseObject(jsonObject.toJSONString(), SysUser.class);
				user.setUpdateTime(new Date());
				//String passwordEncode = PasswordUtil.encrypt(user.getUsername(), user.getPassword(), sysUser.getSalt());
				user.setPassword(sysUser.getPassword());
				String roles = jsonObject.getString("selectedroles");
				sysUserService.editUserWithRole(user, roles);
				result.success("修改成功!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("操作失败");
		}
		return result;
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@RequiresRoles({"admin"})
	public Result<SysUser> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysUser> result = new Result<SysUser>();
		SysUser sysUser = sysUserService.getById(id);
		if(sysUser==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysUserService.removeById(id);
			if(ok) {
				result.success("删除成功!");
			}
		}
		
		return result;
	}
	
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	@RequiresRoles({"admin"})
	public Result<SysUser> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysUser> result = new Result<SysUser>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sysUserService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	@RequestMapping(value = "/frozenBatch", method = {RequestMethod.POST,RequestMethod.GET})
	public Result<SysUser> frozenBatch(@RequestParam(name="ids",required=true) String ids,@RequestParam(name="status",required=true) String status) {
		Result<SysUser> result = new Result<SysUser>();
		try {
			String[] arr = ids.split(",");
			for (String id : arr) {
				if(oConvertUtils.isNotEmpty(id)) {
					this.sysUserService.update(new SysUser().setStatus(Integer.parseInt(status)),
							new UpdateWrapper<SysUser>().lambda().eq(SysUser::getId,id));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.error500("操作失败"+e.getMessage());
		}
		result.success("操作成功!");
		return result;
	}
	
	
	
	@RequestMapping(value = "/queryById", method = RequestMethod.GET)
	public Result<SysUser> queryById(@RequestParam(name="id",required=true) String id) {
		Result<SysUser> result = new Result<SysUser>();
		SysUser sysUser = sysUserService.getById(id);
		if(sysUser==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(sysUser);
			result.setSuccess(true);
		}
		return result;
	}
	
	@RequestMapping(value = "/queryUserRole", method = RequestMethod.GET)
	public Result<List<String>> queryUserRole(@RequestParam(name="userid",required=true) String userid) {
		Result<List<String>> result = new Result<>();
		List<String> list = new ArrayList<String>();
		List<SysUserRole> userRole = sysUserRoleService.list(new QueryWrapper<SysUserRole>().lambda().eq(SysUserRole::getUserId,userid));
		if(userRole==null || userRole.size()<=0) {
			result.error500("未找到用户相关角色信息");
		}else {
			for (SysUserRole sysUserRole : userRole) {
				list.add(sysUserRole.getRoleId());
			}
			result.setSuccess(true);
			result.setResult(list);
		}
		return result;
	}
	
	
	/**
	  * 校验用户账号是否唯一<br>
	  * 可以校验其他 需要检验什么就传什么。。。
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/checkOnlyUser", method = RequestMethod.GET)
	public Result<Boolean> checkUsername(SysUser sysUser) {
		Result<Boolean> result = new Result<>();
		result.setResult(true);//如果此参数为false则程序发生异常
		String id = sysUser.getId();
		log.info("--验证用户信息是否唯一---id:"+id);
		try {
			SysUser oldUser = null;
			if(oConvertUtils.isNotEmpty(id)) {
				oldUser = sysUserService.getById(id);
			}else {
				sysUser.setId(null);
			}
			//通过传入信息查询新的用户信息
			SysUser newUser = sysUserService.getOne(new QueryWrapper<SysUser>(sysUser));
			if(newUser!=null) {
				//如果根据传入信息查询到用户了，那么就需要做校验了。
				if(oldUser==null) {
					//oldUser为空=>新增模式=>只要用户信息存在则返回false
					result.setSuccess(false);
					result.setMessage("用户账号已存在");
					return result;
				}else if(!id.equals(newUser.getId())) {
					//否则=>编辑模式=>判断两者ID是否一致-
					result.setSuccess(false);
					result.setMessage("用户账号已存在");
					return result;
				}
			}
			
		} catch (Exception e) {
			result.setSuccess(false);
			result.setResult(false);
			result.setMessage(e.getMessage());
			return result;
		}
		result.setSuccess(true);
		return result;
	}
	
	/**
	  * 修改密码
	 */
	@RequestMapping(value = "/changPassword", method = RequestMethod.PUT)
	@RequiresRoles({"admin"})
	public Result<SysUser> changPassword(@RequestBody SysUser sysUser) {
		Result<SysUser> result = new Result<SysUser>();
		String password = sysUser.getPassword();
		sysUser = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, sysUser.getUsername()));
		if(sysUser==null) {
			result.error500("未找到对应实体");
		}else {
			String salt = oConvertUtils.randomGen(8);
			sysUser.setSalt(salt);
			String passwordEncode = PasswordUtil.encrypt(sysUser.getUsername(),password, salt);
			sysUser.setPassword(passwordEncode);
			this.sysUserService.updateById(sysUser);
			result.setResult(sysUser);
			result.success("密码修改完成！");
		}
		return result;
	}


}
