package org.jeecg.modules.system.model;

import java.util.List;

import org.jeecg.common.util.DateUtils;

public class SysUserCacheInfo {
	
	private String sysUserCode;
	
	private String sysUserName;
	
	/*private String sysDate;  
	
	private String sysTime;*/
	
	private String companyCode;
	
	private List<String> sysOrgCode;
	
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



	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public List<String> getSysOrgCode() {
		return sysOrgCode;
	}

	public void setSysOrgCode(List<String> sysOrgCode) {
		this.sysOrgCode = sysOrgCode;
	}

}
