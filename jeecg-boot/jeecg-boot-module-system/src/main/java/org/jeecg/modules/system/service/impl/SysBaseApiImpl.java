package org.jeecg.modules.system.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.dto.OnlineAuthDTO;
import org.jeecg.common.api.dto.message.*;
import org.jeecg.common.aspect.UrlMatchEnum;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.DataBaseConstant;
import org.jeecg.common.constant.WebsocketConst;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.*;
import org.jeecg.common.util.SysAnnmentTypeEnum;
import org.jeecg.common.util.YouBianCodeUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.message.entity.SysMessageTemplate;
import org.jeecg.modules.message.handle.impl.EmailSendMsgHandle;
import org.jeecg.modules.message.service.ISysMessageTemplateService;
import org.jeecg.modules.message.websocket.WebSocket;
import org.jeecg.modules.system.entity.*;
import org.jeecg.modules.system.mapper.*;
import org.jeecg.modules.system.service.*;
import org.jeecg.modules.system.util.SecurityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * @Description: 底层共通业务API，提供其他独立模块调用
 * @Author: scott
 * @Date:2019-4-20 
 * @Version:V1.0
 */
@Slf4j
@Service
public class SysBaseApiImpl implements ISysBaseAPI {
	/** 当前系统数据库类型 */
	private static String DB_TYPE = "";
	@Autowired
	private ISysMessageTemplateService sysMessageTemplateService;
	@Resource
	private SysLogMapper sysLogMapper;
	@Resource
	private SysUserMapper userMapper;
	@Resource
	private SysUserRoleMapper sysUserRoleMapper;
	@Autowired
	private ISysDepartService sysDepartService;
	@Autowired
	private ISysDictService sysDictService;
	@Resource
	private SysAnnouncementMapper sysAnnouncementMapper;
	@Resource
	private SysAnnouncementSendMapper sysAnnouncementSendMapper;
	@Resource
    private WebSocket webSocket;
	@Resource
	private SysRoleMapper roleMapper;
	@Resource
	private SysDepartMapper departMapper;
	@Resource
	private SysCategoryMapper categoryMapper;

	@Autowired
	private ISysDataSourceService dataSourceService;
	@Autowired
	private ISysUserDepartService sysUserDepartService;
	@Resource
	private SysPermissionMapper sysPermissionMapper;
	@Autowired
	private ISysPermissionDataRuleService sysPermissionDataRuleService;

	@Autowired
	private ThirdAppWechatEnterpriseServiceImpl wechatEnterpriseService;
	@Autowired
	private ThirdAppDingtalkServiceImpl dingtalkService;

	@Autowired
	ISysCategoryService sysCategoryService;

	@Override
	@Cacheable(cacheNames=CacheConstant.SYS_USERS_CACHE, key="#username")
	public LoginUser getUserByName(String username) {
		if(oConvertUtils.isEmpty(username)) {
			return null;
		}
		LoginUser loginUser = new LoginUser();
		SysUser sysUser = userMapper.getUserByName(username);
		if(sysUser==null) {
			return null;
		}
		BeanUtils.copyProperties(sysUser, loginUser);
		return loginUser;
	}

	@Override
	public String translateDictFromTable(String table, String text, String code, String key) {
		return sysDictService.queryTableDictTextByKey(table, text, code, key);
	}

	@Override
	public String translateDict(String code, String key) {
		return sysDictService.queryDictTextByKey(code, key);
	}

	@Override
	public List<SysPermissionDataRuleModel> queryPermissionDataRule(String component, String requestPath, String username) {
		List<SysPermission> currentSyspermission = null;
		if(oConvertUtils.isNotEmpty(component)) {
			//1.通过注解属性pageComponent 获取菜单
			LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
			query.eq(SysPermission::getDelFlag,0);
			query.eq(SysPermission::getComponent, component);
			currentSyspermission = sysPermissionMapper.selectList(query);
		}else {
			//1.直接通过前端请求地址查询菜单
			LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
			query.eq(SysPermission::getMenuType,2);
			query.eq(SysPermission::getDelFlag,0);
			query.eq(SysPermission::getUrl, requestPath);
			currentSyspermission = sysPermissionMapper.selectList(query);
			//2.未找到 再通过自定义匹配URL 获取菜单
			if(currentSyspermission==null || currentSyspermission.size()==0) {
				//通过自定义URL匹配规则 获取菜单（实现通过菜单配置数据权限规则，实际上针对获取数据接口进行数据规则控制）
				String userMatchUrl = UrlMatchEnum.getMatchResultByUrl(requestPath);
				LambdaQueryWrapper<SysPermission> queryQserMatch = new LambdaQueryWrapper<SysPermission>();
				queryQserMatch.eq(SysPermission::getMenuType, 1);
				queryQserMatch.eq(SysPermission::getDelFlag, 0);
				queryQserMatch.eq(SysPermission::getUrl, userMatchUrl);
				if(oConvertUtils.isNotEmpty(userMatchUrl)){
					currentSyspermission = sysPermissionMapper.selectList(queryQserMatch);
				}
			}
			//3.未找到 再通过正则匹配获取菜单
			if(currentSyspermission==null || currentSyspermission.size()==0) {
				//通过正则匹配权限配置
				String regUrl = getRegexpUrl(requestPath);
				if(regUrl!=null) {
					currentSyspermission = sysPermissionMapper.selectList(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getMenuType,2).eq(SysPermission::getUrl, regUrl).eq(SysPermission::getDelFlag,0));
				}
			}
		}
		if(currentSyspermission!=null && currentSyspermission.size()>0){
			List<SysPermissionDataRuleModel> dataRules = new ArrayList<SysPermissionDataRuleModel>();
			for (SysPermission sysPermission : currentSyspermission) {
				// update-begin--Author:scott Date:20191119 for：数据权限规则编码不规范，项目存在相同包名和类名 #722
				List<SysPermissionDataRule> temp = sysPermissionDataRuleService.queryPermissionDataRules(username, sysPermission.getId());
				if(temp!=null && temp.size()>0) {
					//dataRules.addAll(temp);
					dataRules = oConvertUtils.entityListToModelList(temp,SysPermissionDataRuleModel.class);
				}
				// update-end--Author:scott Date:20191119 for：数据权限规则编码不规范，项目存在相同包名和类名 #722
			}
			return dataRules;
		}
		return null;
	}

	/**
	 * 匹配前端传过来的地址 匹配成功返回正则地址
	 * AntPathMatcher匹配地址
	 *()* 匹配0个或多个字符
	 *()**匹配0个或多个目录
	 */
	private String getRegexpUrl(String url) {
		List<String> list = sysPermissionMapper.queryPermissionUrlWithStar();
		if(list!=null && list.size()>0) {
			for (String p : list) {
				PathMatcher matcher = new AntPathMatcher();
				if(matcher.match(p, url)) {
					return p;
				}
			}
		}
		return null;
	}

	@Override
	public SysUserCacheInfo getCacheUser(String username) {
		SysUserCacheInfo info = new SysUserCacheInfo();
		info.setOneDepart(true);
		LoginUser user = this.getUserByName(username);
		if(user!=null) {
			info.setSysUserCode(user.getUsername());
			info.setSysUserName(user.getRealname());
			info.setSysOrgCode(user.getOrgCode());
		}else{
			return null;
		}
		//多部门支持in查询
		List<SysDepart> list = departMapper.queryUserDeparts(user.getId());
		List<String> sysMultiOrgCode = new ArrayList<String>();
		if(list==null || list.size()==0) {
			//当前用户无部门
			//sysMultiOrgCode.add("0");
		}else if(list.size()==1) {
			sysMultiOrgCode.add(list.get(0).getOrgCode());
		}else {
			info.setOneDepart(false);
			for (SysDepart dpt : list) {
				sysMultiOrgCode.add(dpt.getOrgCode());
			}
		}
		info.setSysMultiOrgCode(sysMultiOrgCode);
		return info;
	}

	@Override
	public LoginUser getUserById(String id) {
		if(oConvertUtils.isEmpty(id)) {
			return null;
		}
		LoginUser loginUser = new LoginUser();
		SysUser sysUser = userMapper.selectById(id);
		if(sysUser==null) {
			return null;
		}
		BeanUtils.copyProperties(sysUser, loginUser);
		return loginUser;
	}

	@Override
	public List<String> getRolesByUsername(String username) {
		return sysUserRoleMapper.getRoleByUserName(username);
	}

	@Override
	public List<String> getDepartIdsByUsername(String username) {
		List<SysDepart> list = sysDepartService.queryDepartsByUsername(username);
		List<String> result = new ArrayList<>(list.size());
		for (SysDepart depart : list) {
			result.add(depart.getId());
		}
		return result;
	}

	@Override
	public List<String> getDepartNamesByUsername(String username) {
		List<SysDepart> list = sysDepartService.queryDepartsByUsername(username);
		List<String> result = new ArrayList<>(list.size());
		for (SysDepart depart : list) {
			result.add(depart.getDepartName());
		}
		return result;
	}

	@Override
	public DictModel getParentDepartId(String departId) {
		SysDepart depart = departMapper.getParentDepartId(departId);
		DictModel model = new DictModel(depart.getId(),depart.getParentId());
		return model;
	}

	@Override
	@Cacheable(value = CacheConstant.SYS_DICT_CACHE,key = "#code", unless = "#result == null ")
	public List<DictModel> queryDictItemsByCode(String code) {
		return sysDictService.queryDictItemsByCode(code);
	}

	@Override
	@Cacheable(value = CacheConstant.SYS_ENABLE_DICT_CACHE,key = "#code", unless = "#result == null ")
	public List<DictModel> queryEnableDictItemsByCode(String code) {
		return sysDictService.queryEnableDictItemsByCode(code);
	}

	@Override
	public List<DictModel> queryTableDictItemsByCode(String table, String text, String code) {
		//update-begin-author:taoyan date:20200820 for:【Online+系统】字典表加权限控制机制逻辑，想法不错 LOWCOD-799
		if(table.indexOf("#{")>=0){
			table = QueryGenerator.getSqlRuleValue(table);
		}
		//update-end-author:taoyan date:20200820 for:【Online+系统】字典表加权限控制机制逻辑，想法不错 LOWCOD-799
		return sysDictService.queryTableDictItemsByCode(table, text, code);
	}

	@Override
	public List<DictModel> queryAllDepartBackDictModel() {
		return sysDictService.queryAllDepartBackDictModel();
	}

	@Override
	public void sendSysAnnouncement(MessageDTO message) {
		this.sendSysAnnouncement(message.getFromUser(),
				message.getToUser(),
				message.getTitle(),
				message.getContent(),
				message.getCategory());
		try {
			// 同步发送第三方APP消息
			wechatEnterpriseService.sendMessage(message, true);
			dingtalkService.sendMessage(message, true);
		} catch (Exception e) {
			log.error("同步发送第三方APP消息失败！", e);
		}
	}

	@Override
	public void sendBusAnnouncement(BusMessageDTO message) {
		sendBusAnnouncement(message.getFromUser(),
				message.getToUser(),
				message.getTitle(),
				message.getContent(),
				message.getCategory(),
				message.getBusType(),
				message.getBusId());
		try {
			// 同步发送第三方APP消息
			wechatEnterpriseService.sendMessage(message, true);
			dingtalkService.sendMessage(message, true);
		} catch (Exception e) {
			log.error("同步发送第三方APP消息失败！", e);
		}
	}

	@Override
	public void sendTemplateAnnouncement(TemplateMessageDTO message) {
		String templateCode = message.getTemplateCode();
		String title = message.getTitle();
		Map<String,String> map = message.getTemplateParam();
		String fromUser = message.getFromUser();
		String toUser = message.getToUser();

		List<SysMessageTemplate> sysSmsTemplates = sysMessageTemplateService.selectByCode(templateCode);
		if(sysSmsTemplates==null||sysSmsTemplates.size()==0){
			throw new JeecgBootException("消息模板不存在，模板编码："+templateCode);
		}
		SysMessageTemplate sysSmsTemplate = sysSmsTemplates.get(0);
		//模板标题
		title = title==null?sysSmsTemplate.getTemplateName():title;
		//模板内容
		String content = sysSmsTemplate.getTemplateContent();
		if(map!=null) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String str = "${" + entry.getKey() + "}";
				if(oConvertUtils.isNotEmpty(title)){
					title = title.replace(str, entry.getValue());
				}
				content = content.replace(str, entry.getValue());
			}
		}

		SysAnnouncement announcement = new SysAnnouncement();
		announcement.setTitile(title);
		announcement.setMsgContent(content);
		announcement.setSender(fromUser);
		announcement.setPriority(CommonConstant.PRIORITY_M);
		announcement.setMsgType(CommonConstant.MSG_TYPE_UESR);
		announcement.setSendStatus(CommonConstant.HAS_SEND);
		announcement.setSendTime(new Date());
		announcement.setMsgCategory(CommonConstant.MSG_CATEGORY_2);
		announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
		sysAnnouncementMapper.insert(announcement);
		// 2.插入用户通告阅读标记表记录
		String userId = toUser;
		String[] userIds = userId.split(",");
		String anntId = announcement.getId();
		for(int i=0;i<userIds.length;i++) {
			if(oConvertUtils.isNotEmpty(userIds[i])) {
				SysUser sysUser = userMapper.getUserByName(userIds[i]);
				if(sysUser==null) {
					continue;
				}
				SysAnnouncementSend announcementSend = new SysAnnouncementSend();
				announcementSend.setAnntId(anntId);
				announcementSend.setUserId(sysUser.getId());
				announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
				sysAnnouncementSendMapper.insert(announcementSend);
				JSONObject obj = new JSONObject();
				obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_USER);
				obj.put(WebsocketConst.MSG_USER_ID, sysUser.getId());
				obj.put(WebsocketConst.MSG_ID, announcement.getId());
				obj.put(WebsocketConst.MSG_TXT, announcement.getTitile());
				webSocket.sendMessage(sysUser.getId(), obj.toJSONString());
			}
		}
		try {
			// 同步企业微信、钉钉的消息通知
			dingtalkService.sendActionCardMessage(announcement, true);
			wechatEnterpriseService.sendTextCardMessage(announcement, true);
		} catch (Exception e) {
			log.error("同步发送第三方APP消息失败！", e);
		}

	}

	@Override
	public void sendBusTemplateAnnouncement(BusTemplateMessageDTO message) {
		String templateCode = message.getTemplateCode();
		String title = message.getTitle();
		Map<String,String> map = message.getTemplateParam();
		String fromUser = message.getFromUser();
		String toUser = message.getToUser();
		String busId = message.getBusId();
		String busType = message.getBusType();

		List<SysMessageTemplate> sysSmsTemplates = sysMessageTemplateService.selectByCode(templateCode);
		if(sysSmsTemplates==null||sysSmsTemplates.size()==0){
			throw new JeecgBootException("消息模板不存在，模板编码："+templateCode);
		}
		SysMessageTemplate sysSmsTemplate = sysSmsTemplates.get(0);
		//模板标题
		title = title==null?sysSmsTemplate.getTemplateName():title;
		//模板内容
		String content = sysSmsTemplate.getTemplateContent();
		if(map!=null) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String str = "${" + entry.getKey() + "}";
				title = title.replace(str, entry.getValue());
				content = content.replace(str, entry.getValue());
			}
		}
		SysAnnouncement announcement = new SysAnnouncement();
		announcement.setTitile(title);
		announcement.setMsgContent(content);
		announcement.setSender(fromUser);
		announcement.setPriority(CommonConstant.PRIORITY_M);
		announcement.setMsgType(CommonConstant.MSG_TYPE_UESR);
		announcement.setSendStatus(CommonConstant.HAS_SEND);
		announcement.setSendTime(new Date());
		announcement.setMsgCategory(CommonConstant.MSG_CATEGORY_2);
		announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
		announcement.setBusId(busId);
		announcement.setBusType(busType);
		announcement.setOpenType(SysAnnmentTypeEnum.getByType(busType).getOpenType());
		announcement.setOpenPage(SysAnnmentTypeEnum.getByType(busType).getOpenPage());
		sysAnnouncementMapper.insert(announcement);
		// 2.插入用户通告阅读标记表记录
		String userId = toUser;
		String[] userIds = userId.split(",");
		String anntId = announcement.getId();
		for(int i=0;i<userIds.length;i++) {
			if(oConvertUtils.isNotEmpty(userIds[i])) {
				SysUser sysUser = userMapper.getUserByName(userIds[i]);
				if(sysUser==null) {
					continue;
				}
				SysAnnouncementSend announcementSend = new SysAnnouncementSend();
				announcementSend.setAnntId(anntId);
				announcementSend.setUserId(sysUser.getId());
				announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
				sysAnnouncementSendMapper.insert(announcementSend);
				JSONObject obj = new JSONObject();
				obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_USER);
				obj.put(WebsocketConst.MSG_USER_ID, sysUser.getId());
				obj.put(WebsocketConst.MSG_ID, announcement.getId());
				obj.put(WebsocketConst.MSG_TXT, announcement.getTitile());
				webSocket.sendMessage(sysUser.getId(), obj.toJSONString());
			}
		}
		try {
			// 同步企业微信、钉钉的消息通知
			dingtalkService.sendActionCardMessage(announcement, true);
			wechatEnterpriseService.sendTextCardMessage(announcement, true);
		} catch (Exception e) {
			log.error("同步发送第三方APP消息失败！", e);
		}

	}

	@Override
	public String parseTemplateByCode(TemplateDTO templateDTO) {
		String templateCode = templateDTO.getTemplateCode();
		Map<String, String> map = templateDTO.getTemplateParam();
		List<SysMessageTemplate> sysSmsTemplates = sysMessageTemplateService.selectByCode(templateCode);
		if(sysSmsTemplates==null||sysSmsTemplates.size()==0){
			throw new JeecgBootException("消息模板不存在，模板编码："+templateCode);
		}
		SysMessageTemplate sysSmsTemplate = sysSmsTemplates.get(0);
		//模板内容
		String content = sysSmsTemplate.getTemplateContent();
		if(map!=null) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				String str = "${" + entry.getKey() + "}";
				content = content.replace(str, entry.getValue());
			}
		}
		return content;
	}

	@Override
	public void updateSysAnnounReadFlag(String busType, String busId) {
		SysAnnouncement announcement = sysAnnouncementMapper.selectOne(new QueryWrapper<SysAnnouncement>().eq("bus_type",busType).eq("bus_id",busId));
		if(announcement != null){
			LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
			String userId = sysUser.getId();
			LambdaUpdateWrapper<SysAnnouncementSend> updateWrapper = new UpdateWrapper().lambda();
			updateWrapper.set(SysAnnouncementSend::getReadFlag, CommonConstant.HAS_READ_FLAG);
			updateWrapper.set(SysAnnouncementSend::getReadTime, new Date());
			updateWrapper.last("where annt_id ='"+announcement.getId()+"' and user_id ='"+userId+"'");
			SysAnnouncementSend announcementSend = new SysAnnouncementSend();
			sysAnnouncementSendMapper.update(announcementSend, updateWrapper);
		}
	}

	/**
	 * 获取数据库类型
	 * @param dataSource
	 * @return
	 * @throws SQLException
	 */
	private String getDatabaseTypeByDataSource(DataSource dataSource) throws SQLException{
		if("".equals(DB_TYPE)) {
			Connection connection = dataSource.getConnection();
			try {
				DatabaseMetaData md = connection.getMetaData();
				String dbType = md.getDatabaseProductName().toLowerCase();
				if(dbType.indexOf("mysql")>=0) {
					DB_TYPE = DataBaseConstant.DB_TYPE_MYSQL;
				}else if(dbType.indexOf("oracle")>=0) {
					DB_TYPE = DataBaseConstant.DB_TYPE_ORACLE;
				}else if(dbType.indexOf("sqlserver")>=0||dbType.indexOf("sql server")>=0) {
					DB_TYPE = DataBaseConstant.DB_TYPE_SQLSERVER;
				}else if(dbType.indexOf("postgresql")>=0) {
					DB_TYPE = DataBaseConstant.DB_TYPE_POSTGRESQL;
				}else if(dbType.indexOf("mariadb")>=0) {
					DB_TYPE = DataBaseConstant.DB_TYPE_MARIADB;
				}else {
					log.error("数据库类型:[" + dbType + "]不识别!");
					//throw new JeecgBootException("数据库类型:["+dbType+"]不识别!");
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}finally {
				connection.close();
			}
		}
		return DB_TYPE;

	}

	@Override
	public List<DictModel> queryAllDict() {
		// 查询并排序
		QueryWrapper<SysDict> queryWrapper = new QueryWrapper<SysDict>();
		queryWrapper.orderByAsc("create_time");
		List<SysDict> dicts = sysDictService.list(queryWrapper);
		// 封装成 model
		List<DictModel> list = new ArrayList<DictModel>();
		for (SysDict dict : dicts) {
			list.add(new DictModel(dict.getDictCode(), dict.getDictName()));
		}

		return list;
	}

	@Override
	public List<SysCategoryModel> queryAllDSysCategory() {
		List<SysCategory> ls = categoryMapper.selectList(null);
		List<SysCategoryModel> res = oConvertUtils.entityListToModelList(ls,SysCategoryModel.class);
		return res;
	}

	@Override
	public List<DictModel> queryFilterTableDictInfo(String table, String text, String code, String filterSql) {
		return sysDictService.queryTableDictItemsByCodeAndFilter(table,text,code,filterSql);
	}

	@Override
	public List<String> queryTableDictByKeys(String table, String text, String code, String[] keyArray) {
		return sysDictService.queryTableDictByKeys(table,text,code,Joiner.on(",").join(keyArray));
	}

	@Override
	public List<ComboModel> queryAllUserBackCombo() {
		List<ComboModel> list = new ArrayList<ComboModel>();
		List<SysUser> userList = userMapper.selectList(new QueryWrapper<SysUser>().eq("status",1).eq("del_flag",0));
		for(SysUser user : userList){
			ComboModel model = new ComboModel();
			model.setTitle(user.getRealname());
			model.setId(user.getId());
			model.setUsername(user.getUsername());
			list.add(model);
		}
		return list;
	}

	@Override
	public JSONObject queryAllUser(String userIds, Integer pageNo, Integer pageSize) {
		JSONObject json = new JSONObject();
		QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>().eq("status",1).eq("del_flag",0);
		List<ComboModel> list = new ArrayList<ComboModel>();
		Page<SysUser> page = new Page<SysUser>(pageNo, pageSize);
		IPage<SysUser> pageList = userMapper.selectPage(page, queryWrapper);
		for(SysUser user : pageList.getRecords()){
			ComboModel model = new ComboModel();
			model.setUsername(user.getUsername());
			model.setTitle(user.getRealname());
			model.setId(user.getId());
			model.setEmail(user.getEmail());
			if(oConvertUtils.isNotEmpty(userIds)){
				String[] temp = userIds.split(",");
				for(int i = 0; i<temp.length;i++){
					if(temp[i].equals(user.getId())){
						model.setChecked(true);
					}
				}
			}
			list.add(model);
		}
		json.put("list",list);
		json.put("total",pageList.getTotal());
		return json;
	}

	@Override
	public List<ComboModel> queryAllRole() {
		List<ComboModel> list = new ArrayList<ComboModel>();
		List<SysRole> roleList = roleMapper.selectList(new QueryWrapper<SysRole>());
		for(SysRole role : roleList){
			ComboModel model = new ComboModel();
			model.setTitle(role.getRoleName());
			model.setId(role.getId());
			list.add(model);
		}
		return list;
	}

    @Override
    public List<ComboModel> queryAllRole(String[] roleIds) {
        List<ComboModel> list = new ArrayList<ComboModel>();
        List<SysRole> roleList = roleMapper.selectList(new QueryWrapper<SysRole>());
        for(SysRole role : roleList){
            ComboModel model = new ComboModel();
            model.setTitle(role.getRoleName());
            model.setId(role.getId());
            model.setRoleCode(role.getRoleCode());
            if(oConvertUtils.isNotEmpty(roleIds)) {
                for (int i = 0; i < roleIds.length; i++) {
                    if (roleIds[i].equals(role.getId())) {
                        model.setChecked(true);
                    }
                }
            }
            list.add(model);
        }
        return list;
    }

	@Override
	public List<String> getRoleIdsByUsername(String username) {
		return sysUserRoleMapper.getRoleIdByUserName(username);
	}

	@Override
	public String getDepartIdsByOrgCode(String orgCode) {
		return departMapper.queryDepartIdByOrgCode(orgCode);
	}

	@Override
	public List<SysDepartModel> getAllSysDepart() {
		List<SysDepartModel> departModelList = new ArrayList<SysDepartModel>();
		List<SysDepart> departList = departMapper.selectList(new QueryWrapper<SysDepart>().eq("del_flag","0"));
		for(SysDepart depart : departList){
			SysDepartModel model = new SysDepartModel();
			BeanUtils.copyProperties(depart,model);
			departModelList.add(model);
		}
		return departModelList;
	}

	@Override
	public DynamicDataSourceModel getDynamicDbSourceById(String dbSourceId) {
		SysDataSource dbSource = dataSourceService.getById(dbSourceId);
		if(dbSource!=null && StringUtils.isNotBlank(dbSource.getDbPassword())){
			String dbPassword = dbSource.getDbPassword();
			String decodedStr = SecurityUtil.jiemi(dbPassword);
			dbSource.setDbPassword(decodedStr);
		}
		return new DynamicDataSourceModel(dbSource);
	}

	@Override
	public DynamicDataSourceModel getDynamicDbSourceByCode(String dbSourceCode) {
		SysDataSource dbSource = dataSourceService.getOne(new LambdaQueryWrapper<SysDataSource>().eq(SysDataSource::getCode, dbSourceCode));
		if(dbSource!=null && StringUtils.isNotBlank(dbSource.getDbPassword())){
			String dbPassword = dbSource.getDbPassword();
			String decodedStr = SecurityUtil.jiemi(dbPassword);
			dbSource.setDbPassword(decodedStr);
		}
		return new DynamicDataSourceModel(dbSource);
	}

	@Override
	public List<String> getDeptHeadByDepId(String deptId) {
		List<SysUser> userList = userMapper.selectList(new QueryWrapper<SysUser>().like("depart_ids",deptId).eq("status",1).eq("del_flag",0));
		List<String> list = new ArrayList<>();
		for(SysUser user : userList){
			list.add(user.getUsername());
		}
		return list;
	}

	@Override
	public void sendWebSocketMsg(String[] userIds, String cmd) {
		JSONObject obj = new JSONObject();
		obj.put(WebsocketConst.MSG_CMD, cmd);
		webSocket.sendMessage(userIds, obj.toJSONString());
	}

	@Override
	public List<LoginUser> queryAllUserByIds(String[] userIds) {
		QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>().eq("status",1).eq("del_flag",0);
		queryWrapper.in("id",userIds);
		List<LoginUser> loginUsers = new ArrayList<>();
		List<SysUser> sysUsers = userMapper.selectList(queryWrapper);
		for (SysUser user:sysUsers) {
			LoginUser loginUser=new LoginUser();
			BeanUtils.copyProperties(user, loginUser);
			loginUsers.add(loginUser);
		}
		return loginUsers;
	}

	/**
	 * 推送签到人员信息
	 * @param userId
	 */
	@Override
	public void meetingSignWebsocket(String userId) {
		JSONObject obj = new JSONObject();
		obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_SIGN);
		obj.put(WebsocketConst.MSG_USER_ID,userId);
		//TODO 目前全部推送，后面修改
		webSocket.sendMessage(obj.toJSONString());
	}

	@Override
	public List<LoginUser> queryUserByNames(String[] userNames) {
		QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>().eq("status",1).eq("del_flag",0);
		queryWrapper.in("username",userNames);
		List<LoginUser> loginUsers = new ArrayList<>();
		List<SysUser> sysUsers = userMapper.selectList(queryWrapper);
		for (SysUser user:sysUsers) {
			LoginUser loginUser=new LoginUser();
			BeanUtils.copyProperties(user, loginUser);
			loginUsers.add(loginUser);
		}
		return loginUsers;
	}

	@Override
	public SysDepartModel selectAllById(String id) {
		SysDepart sysDepart = sysDepartService.getById(id);
		SysDepartModel sysDepartModel = new SysDepartModel();
		BeanUtils.copyProperties(sysDepart,sysDepartModel);
		return sysDepartModel;
	}

	@Override
	public List<String> queryDeptUsersByUserId(String userId) {
		List<String> userIds = new ArrayList<>();
		List<SysUserDepart> userDepartList = sysUserDepartService.list(new QueryWrapper<SysUserDepart>().eq("user_id",userId));
		if(userDepartList != null){
			//查找所属公司
			String orgCodes = "";
			for(SysUserDepart userDepart : userDepartList){
				//查询所属公司编码
				SysDepart depart = sysDepartService.getById(userDepart.getDepId());
				int length = YouBianCodeUtil.zhanweiLength;
				String compyOrgCode = "";
				if(depart != null && depart.getOrgCode() != null){
					compyOrgCode = depart.getOrgCode().substring(0,length);
					if(orgCodes.indexOf(compyOrgCode) == -1){
						orgCodes = orgCodes + "," + compyOrgCode;
					}
				}
			}
			if(oConvertUtils.isNotEmpty(orgCodes)){
				orgCodes = orgCodes.substring(1);
				List<String> listIds = departMapper.getSubDepIdsByOrgCodes(orgCodes.split(","));
				List<SysUserDepart> userList = sysUserDepartService.list(new QueryWrapper<SysUserDepart>().in("dep_id",listIds));
				for(SysUserDepart userDepart : userList){
					if(!userIds.contains(userDepart.getUserId())){
						userIds.add(userDepart.getUserId());
					}
				}
			}
		}
		return userIds;
	}

	/**
	 * 查询用户拥有的角色集合
	 * @param username
	 * @return
	 */
	@Override
	public Set<String> getUserRoleSet(String username) {
		// 查询用户拥有的角色集合
		List<String> roles = sysUserRoleMapper.getRoleByUserName(username);
		log.info("-------通过数据库读取用户拥有的角色Rules------username： " + username + ",Roles size: " + (roles == null ? 0 : roles.size()));
		return new HashSet<>(roles);
	}

	/**
	 * 查询用户拥有的权限集合
	 * @param username
	 * @return
	 */
	@Override
	public Set<String> getUserPermissionSet(String username) {
		Set<String> permissionSet = new HashSet<>();
		List<SysPermission> permissionList = sysPermissionMapper.queryByUser(username);
		for (SysPermission po : permissionList) {
//			// TODO URL规则有问题？
//			if (oConvertUtils.isNotEmpty(po.getUrl())) {
//				permissionSet.add(po.getUrl());
//			}
			if (oConvertUtils.isNotEmpty(po.getPerms())) {
				permissionSet.add(po.getPerms());
			}
		}
		log.info("-------通过数据库读取用户拥有的权限Perms------username： "+ username+",Perms size: "+ (permissionSet==null?0:permissionSet.size()) );
		return permissionSet;
	}

	/**
	 * 判断online菜单是否有权限
	 * @param onlineAuthDTO
	 * @return
	 */
	@Override
	public boolean hasOnlineAuth(OnlineAuthDTO onlineAuthDTO) {
		String username = onlineAuthDTO.getUsername();
		List<String> possibleUrl = onlineAuthDTO.getPossibleUrl();
		String onlineFormUrl = onlineAuthDTO.getOnlineFormUrl();
		//查询菜单
		LambdaQueryWrapper<SysPermission> query = new LambdaQueryWrapper<SysPermission>();
		query.eq(SysPermission::getDelFlag, 0);
		query.in(SysPermission::getUrl, possibleUrl);
		List<SysPermission> permissionList = sysPermissionMapper.selectList(query);
		if (permissionList == null || permissionList.size() == 0) {
			//没有配置菜单 找online表单菜单地址
			SysPermission sysPermission = new SysPermission();
			sysPermission.setUrl(onlineFormUrl);
			int count = sysPermissionMapper.queryCountByUsername(username, sysPermission);
			if(count<=0){
				return false;
			}
		} else {
			//找到菜单了
			boolean has = false;
			for (SysPermission p : permissionList) {
				int count = sysPermissionMapper.queryCountByUsername(username, p);
				has = has || (count>0);
			}
			return has;
		}
		return true;
	}

	/**
	 * 查询用户拥有的角色集合 common api 里面的接口实现
	 * @param username
	 * @return
	 */
	@Override
	public Set<String> queryUserRoles(String username) {
		return getUserRoleSet(username);
	}

	/**
	 * 查询用户拥有的权限集合 common api 里面的接口实现
	 * @param username
	 * @return
	 */
	@Override
	public Set<String> queryUserAuths(String username) {
		return getUserPermissionSet(username);
	}

	/**
	 * 36根据多个用户账号(逗号分隔)，查询返回多个用户信息
	 * @param usernames
	 * @return
	 */
	@Override
	public List<JSONObject> queryUsersByUsernames(String usernames) {
		LambdaQueryWrapper<SysUser> queryWrapper =  new LambdaQueryWrapper<>();
		queryWrapper.in(SysUser::getUsername,usernames.split(","));
		return JSON.parseArray(JSON.toJSONString(userMapper.selectList(queryWrapper))).toJavaList(JSONObject.class);
	}

	@Override
	public List<JSONObject> queryUsersByIds(String ids) {
		LambdaQueryWrapper<SysUser> queryWrapper =  new LambdaQueryWrapper<>();
		queryWrapper.in(SysUser::getId,ids.split(","));
		return JSON.parseArray(JSON.toJSONString(userMapper.selectList(queryWrapper))).toJavaList(JSONObject.class);
	}

	/**
	 * 37根据多个部门编码(逗号分隔)，查询返回多个部门信息
	 * @param orgCodes
	 * @return
	 */
	@Override
	public List<JSONObject> queryDepartsByOrgcodes(String orgCodes) {
		LambdaQueryWrapper<SysDepart> queryWrapper =  new LambdaQueryWrapper<>();
		queryWrapper.in(SysDepart::getOrgCode,orgCodes.split(","));
		return JSON.parseArray(JSON.toJSONString(sysDepartService.list(queryWrapper))).toJavaList(JSONObject.class);
	}

	@Override
	public List<JSONObject> queryDepartsByIds(String ids) {
		LambdaQueryWrapper<SysDepart> queryWrapper =  new LambdaQueryWrapper<>();
		queryWrapper.in(SysDepart::getId,ids.split(","));
		return JSON.parseArray(JSON.toJSONString(sysDepartService.list(queryWrapper))).toJavaList(JSONObject.class);
	}

	/**
	 * 发消息
	 * @param fromUser
	 * @param toUser
	 * @param title
	 * @param msgContent
	 * @param setMsgCategory
	 */
	private void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent, String setMsgCategory) {
		SysAnnouncement announcement = new SysAnnouncement();
		announcement.setTitile(title);
		announcement.setMsgContent(msgContent);
		announcement.setSender(fromUser);
		announcement.setPriority(CommonConstant.PRIORITY_M);
		announcement.setMsgType(CommonConstant.MSG_TYPE_UESR);
		announcement.setSendStatus(CommonConstant.HAS_SEND);
		announcement.setSendTime(new Date());
		announcement.setMsgCategory(setMsgCategory);
		announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
		sysAnnouncementMapper.insert(announcement);
		// 2.插入用户通告阅读标记表记录
		String userId = toUser;
		String[] userIds = userId.split(",");
		String anntId = announcement.getId();
		for(int i=0;i<userIds.length;i++) {
			if(oConvertUtils.isNotEmpty(userIds[i])) {
				SysUser sysUser = userMapper.getUserByName(userIds[i]);
				if(sysUser==null) {
					continue;
				}
				SysAnnouncementSend announcementSend = new SysAnnouncementSend();
				announcementSend.setAnntId(anntId);
				announcementSend.setUserId(sysUser.getId());
				announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
				sysAnnouncementSendMapper.insert(announcementSend);
				JSONObject obj = new JSONObject();
				obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_USER);
				obj.put(WebsocketConst.MSG_USER_ID, sysUser.getId());
				obj.put(WebsocketConst.MSG_ID, announcement.getId());
				obj.put(WebsocketConst.MSG_TXT, announcement.getTitile());
				webSocket.sendMessage(sysUser.getId(), obj.toJSONString());
			}
		}

	}

	/**
	 * 发消息 带业务参数
	 * @param fromUser
	 * @param toUser
	 * @param title
	 * @param msgContent
	 * @param setMsgCategory
	 * @param busType
	 * @param busId
	 */
	private void sendBusAnnouncement(String fromUser, String toUser, String title, String msgContent, String setMsgCategory, String busType, String busId) {
		SysAnnouncement announcement = new SysAnnouncement();
		announcement.setTitile(title);
		announcement.setMsgContent(msgContent);
		announcement.setSender(fromUser);
		announcement.setPriority(CommonConstant.PRIORITY_M);
		announcement.setMsgType(CommonConstant.MSG_TYPE_UESR);
		announcement.setSendStatus(CommonConstant.HAS_SEND);
		announcement.setSendTime(new Date());
		announcement.setMsgCategory(setMsgCategory);
		announcement.setDelFlag(String.valueOf(CommonConstant.DEL_FLAG_0));
		announcement.setBusId(busId);
		announcement.setBusType(busType);
		announcement.setOpenType(SysAnnmentTypeEnum.getByType(busType).getOpenType());
		announcement.setOpenPage(SysAnnmentTypeEnum.getByType(busType).getOpenPage());
		sysAnnouncementMapper.insert(announcement);
		// 2.插入用户通告阅读标记表记录
		String userId = toUser;
		String[] userIds = userId.split(",");
		String anntId = announcement.getId();
		for(int i=0;i<userIds.length;i++) {
			if(oConvertUtils.isNotEmpty(userIds[i])) {
				SysUser sysUser = userMapper.getUserByName(userIds[i]);
				if(sysUser==null) {
					continue;
				}
				SysAnnouncementSend announcementSend = new SysAnnouncementSend();
				announcementSend.setAnntId(anntId);
				announcementSend.setUserId(sysUser.getId());
				announcementSend.setReadFlag(CommonConstant.NO_READ_FLAG);
				sysAnnouncementSendMapper.insert(announcementSend);
				JSONObject obj = new JSONObject();
				obj.put(WebsocketConst.MSG_CMD, WebsocketConst.CMD_USER);
				obj.put(WebsocketConst.MSG_USER_ID, sysUser.getId());
				obj.put(WebsocketConst.MSG_ID, announcement.getId());
				obj.put(WebsocketConst.MSG_TXT, announcement.getTitile());
				webSocket.sendMessage(sysUser.getId(), obj.toJSONString());
			}
		}
	}

	/**
	 * 发送邮件消息
	 * @param email
	 * @param title
	 * @param content
	 */
	@Override
	public void sendEmailMsg(String email, String title, String content) {
			EmailSendMsgHandle emailHandle=new EmailSendMsgHandle();
			emailHandle.SendMsg(email, title, content);
	}

	/**
	 * 获取公司下级部门和所有用户id信息
	 * @param orgCode
	 * @return
	 */
	@Override
	public List<Map> getDeptUserByOrgCode(String orgCode) {
		//1.获取公司信息
		SysDepart comp=sysDepartService.queryCompByOrgCode(orgCode);
		if(comp!=null){
			//2.获取公司下级部门
			List<SysDepart> departs=sysDepartService.queryDeptByPid(comp.getId());
			//3.获取部门下的人员信息
			 List<Map> list=new ArrayList();
			 //4.处理部门和下级用户数据
			for (SysDepart dept:departs) {
				Map map=new HashMap();
				//部门名称
				String departName = dept.getDepartName();
				//根据部门编码获取下级部门id
				List<String> listIds = departMapper.getSubDepIdsByDepId(dept.getId());
				//根据下级部门ids获取下级部门的所有用户
				List<SysUserDepart> userList = sysUserDepartService.list(new QueryWrapper<SysUserDepart>().in("dep_id",listIds));
				List<String> userIds = new ArrayList<>();
				for(SysUserDepart userDepart : userList){
					if(!userIds.contains(userDepart.getUserId())){
						userIds.add(userDepart.getUserId());
					}
				}
				map.put("name",departName);
				map.put("ids",userIds);
				list.add(map);
			}
			return list;
		}
		return null;
	}

	/**
	 * 查询分类字典翻译
	 *
	 * @param ids 分类字典表id
	 * @return
	 */
	@Override
	public List<String> loadCategoryDictItem(String ids) {
		return sysCategoryService.loadDictItem(ids, false);
	}

	/**
	 * 根据字典code加载字典text
	 *
	 * @param dictCode 顺序：tableName,text,code
	 * @param keys     要查询的key
	 * @return
	 */
	@Override
	public List<String> loadDictItem(String dictCode, String keys) {
		String[] params = dictCode.split(",");
		return sysDictService.queryTableDictByKeys(params[0], params[1], params[2], keys, false);
	}

	/**
	 * 根据字典code查询字典项
	 *
	 * @param dictCode 顺序：tableName,text,code
	 * @param dictCode 要查询的key
	 * @return
	 */
	@Override
	public List<DictModel> getDictItems(String dictCode) {
		List<DictModel> ls = sysDictService.getDictItems(dictCode);
		if (ls == null) {
			ls = new ArrayList<>();
		}
		return ls;
	}

	/**
	 * 根据多个字典code查询多个字典项
	 *
	 * @param dictCodeList
	 * @return key = dictCode ； value=对应的字典项
	 */
	@Override
	public Map<String, List<DictModel>> getManyDictItems(List<String> dictCodeList) {
		return sysDictService.queryDictItemsByCodeList(dictCodeList);
	}

	/**
	 * 【下拉搜索】
	 * 大数据量的字典表 走异步加载，即前端输入内容过滤数据
	 *
	 * @param dictCode 字典code格式：table,text,code
	 * @param keyword  过滤关键字
	 * @return
	 */
	@Override
	public List<DictModel> loadDictItemByKeyword(String dictCode, String keyword, Integer pageSize) {
		return sysDictService.loadDict(dictCode, keyword, pageSize);
	}

	@Override
	public Map<String, List<DictModel>> translateManyDict(String dictCodes, String keys) {
		List<String> dictCodeList = Arrays.asList(dictCodes.split(","));
		List<String> values = Arrays.asList(keys.split(","));
		return sysDictService.queryManyDictByKeys(dictCodeList, values);
	}

	@Override
	public List<DictModel> translateDictFromTableByKeys(String table, String text, String code, String keys) {
		return sysDictService.queryTableDictTextByKeys(table, text, code, Arrays.asList(keys.split(",")));
	}

}