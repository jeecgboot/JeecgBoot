package org.jeecg.common.system.vo;

import java.util.List;

import org.jeecg.common.util.DateUtils;

/**
 * @Description: 用户缓存信息
 * @author: jeecg-boot
 */
public class SysUserCacheInfo {

	private String sysUserId;

	private String sysUserCode;
	
	private String sysUserName;
	
	private String sysOrgCode;

	/**
	 * 当前用户部门ID
	 */
	private String sysOrgId;

	private List<String> sysMultiOrgCode;
	
	private boolean oneDepart;

	/**
	 * 当前用户角色code（多个逗号分割）
	 */
	private String sysRoleCode;

	public boolean isOneDepart() {
		return oneDepart;
	}

	public void setOneDepart(boolean oneDepart) {
		this.oneDepart = oneDepart;
	}

	public String getSysDate() {
		return DateUtils.formatDate();
	}

	public String getSysTime() {
		return DateUtils.now();
	}

	public String getSysUserCode() {
		return sysUserCode;
	}

	public void setSysUserCode(String sysUserCode) {
		this.sysUserCode = sysUserCode;
	}

	public String getSysUserName() {
		return sysUserName;
	}

	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}

	public String getSysOrgCode() {
		return sysOrgCode;
	}

	public void setSysOrgCode(String sysOrgCode) {
		this.sysOrgCode = sysOrgCode;
	}

	public List<String> getSysMultiOrgCode() {
		return sysMultiOrgCode;
	}

	public void setSysMultiOrgCode(List<String> sysMultiOrgCode) {
		this.sysMultiOrgCode = sysMultiOrgCode;
	}

	public String getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(String sysUserId) {
		this.sysUserId = sysUserId;
	}

	public String getSysOrgId() {
		return sysOrgId;
	}

	public void setSysOrgId(String sysOrgId) {
		this.sysOrgId = sysOrgId;
	}

	public String getSysRoleCode() {
		return sysRoleCode;
	}

	public void setSysRoleCode(String sysRoleCode) {
		this.sysRoleCode = sysRoleCode;
	}
}
