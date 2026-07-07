package com.xxl.job.admin.framework.model;

/**
 * xxl job user
 *
 * @author xuxueli 2019-05-04 16:43:12
 */
public class XxlJobUser {
	
	private int id;
	private String username;		// 账号
	private String password;		// 密码
	private String token;			// 登录token
	private int role;				// 角色：0-普通用户、1-管理员
	private String permission;		// 权限：执行器ID列表，多个逗号分割

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

}
