package org.jeecg.common.system.vo;

import java.util.List;

import org.jeecg.common.util.DateUtils;

public class SysUserCacheInfo {
	
	private String sysUserCode;
	
	private String sysUserName;
	
	private String sysOrgCode;
	
	private List<String> sysMultiOrgCode;
	
	private boolean oneDepart;
	
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

}
