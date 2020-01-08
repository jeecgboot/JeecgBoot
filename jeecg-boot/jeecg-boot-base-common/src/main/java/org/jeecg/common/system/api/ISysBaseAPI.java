package org.jeecg.common.system.api;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.jeecg.common.system.vo.ComboModel;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.system.vo.SysDepartModel;

/**
 * @Description: 底层共通业务API，提供其他独立模块调用
 * @Author: scott
 * @Date:2019-4-20 
 * @Version:V1.0
 */
public interface ISysBaseAPI {

	/**
	 * 日志添加
	 * @param LogContent 内容
	 * @param logType 日志类型(0:操作日志;1:登录日志;2:定时任务)
	 * @param operatetype 操作类型(1:添加;2:修改;3:删除;)
	 */
	void addLog(String LogContent, Integer logType, Integer operatetype);
	
	/**
	  * 根据用户账号查询用户信息
	 * @param username
	 * @return
	 */
	public LoginUser getUserByName(String username);
	
	/**
	  * 根据用户id查询用户信息
	 * @param id
	 * @return
	 */
	public LoginUser getUserById(String id);
	
	/**
	 * 通过用户账号查询角色集合
	 * @param username
	 * @return
	 */
	public List<String> getRolesByUsername(String username);

	/**
	 * 通过用户账号查询部门集合
	 * @param username
	 * @return 部门 id
	 */
	List<String> getDepartIdsByUsername(String username);

	/**
	 * 通过用户账号查询部门 name
	 * @param username
	 * @return 部门 name
	 */
	List<String> getDepartNamesByUsername(String username);

	/**
	 * 获取当前数据库类型
	 * @return
	 * @throws Exception 
	 */
	public String getDatabaseType() throws SQLException;
	
	/**
	  * 获取数据字典
	 * @param code
	 * @return
	 */
	public List<DictModel> queryDictItemsByCode(String code);

	/** 查询所有的父级字典，按照create_time排序 */
	public List<DictModel> queryAllDict();

	/**
	  * 获取表数据字典
	 * @param table
	 * @param text
	 * @param code
	 * @return
	 */
    List<DictModel> queryTableDictItemsByCode(String table, String text, String code);
    
    /**
   	 * 查询所有部门 作为字典信息 id -->value,departName -->text
   	 * @return
   	 */
   	public List<DictModel> queryAllDepartBackDictModel();
   	
	/**
	 * 发送系统消息
	 * @param fromUser 发送人(用户登录账户)
	 * @param toUser  发送给(用户登录账户)
	 * @param title  消息主题
	 * @param msgContent  消息内容
	 */
	public void sendSysAnnouncement(String fromUser,String toUser,String title, String msgContent);

	/**
	 * 发送系统消息
	 * @param fromUser 发送人(用户登录账户)
	 * @param toUser   发送给(用户登录账户)
	 * @param title    通知标题
	 * @param map  	   模板参数
	 * @param templateCode  模板编码
	 */
	public void sendSysAnnouncement(String fromUser, String toUser,String title, Map<String, String> map, String templateCode);

	/**
	 * 通过消息中心模板，生成推送内容
	 *
	 * @param templateCode 模板编码
	 * @param map          模板参数
	 * @return
	 */
	public String parseTemplateByCode(String templateCode, Map<String, String> map);


	/**
	 * 发送系统消息
	 * @param fromUser 发送人(用户登录账户)
	 * @param toUser  发送给(用户登录账户)
	 * @param title  消息主题
	 * @param msgContent  消息内容
	 * @param setMsgCategory  消息类型 1:消息2:系统消息
	 */
	public void sendSysAnnouncement(String fromUser, String toUser, String title, String msgContent, String setMsgCategory);

	/**
	 * 查询表字典 支持过滤数据
	 * @param table
	 * @param text
	 * @param code
	 * @param filterSql
	 * @return
	 */
	public List<DictModel> queryFilterTableDictInfo(String table, String text, String code, String filterSql);

	/**
	 * 获取所有有效用户
	 * @return
	 */
	public List<ComboModel> queryAllUser();

    /**
     * 获取所有有效用户 带参
     * userIds 默认选中用户
     * @return
     */
    public List<ComboModel> queryAllUser(String[] userIds);

	/**
	 * 获取所有角色
	 * @return
	 */
	public List<ComboModel> queryAllRole();

	/**
	 * 获取所有角色 带参
     * roleIds 默认选中角色
	 * @return
	 */
	public List<ComboModel> queryAllRole(String[] roleIds );

	/**
	 * 通过用户账号查询角色Id集合
	 * @param username
	 * @return
	 */
	public List<String> getRoleIdsByUsername(String username);

	/**
	 * 通过部门编号查询部门id
	 * @param orgCode
	 * @return
	 */
	public String getDepartIdsByOrgCode(String orgCode);

	/**
	 * 查询上一级部门
	 * @param departId
	 * @return
	 */
	public DictModel getParentDepartId(String departId);

	/**
	 * 查询所有部门
	 * @return
	 */
	public List<SysDepartModel> getAllSysDepart();
	
}
