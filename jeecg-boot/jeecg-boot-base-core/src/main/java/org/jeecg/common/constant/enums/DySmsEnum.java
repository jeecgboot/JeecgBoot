package org.jeecg.common.constant.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description: 短信枚举类
 * @author: jeecg-boot
 */
public enum DySmsEnum {

    /**登录短信模板编码*/
	LOGIN_TEMPLATE_CODE("SMS_175435174","敲敲云","code"),
    /**忘记密码短信模板编码*/
	FORGET_PASSWORD_TEMPLATE_CODE("SMS_175435174","敲敲云","code"),
	/**修改密码短信模板编码*/
	CHANGE_PASSWORD_TEMPLATE_CODE("SMS_465391221","敲敲云","code"),
	/**注册账号短信模板编码*/
	REGISTER_TEMPLATE_CODE("SMS_175430166","敲敲云","code");

	/**
	 * 短信模板编码
	 */
	private String templateCode;
	/**
	 * 签名
	 */
	private String signName;
	/**
	 * 短信模板必需的数据名称，多个key以逗号分隔，此处配置作为校验
	 */
	private String keys;
	
	private DySmsEnum(String templateCode,String signName,String keys) {
		this.templateCode = templateCode;
		this.signName = signName;
		this.keys = keys;
	}
	
	public String getTemplateCode() {
		return templateCode;
	}
	
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	
	public String getSignName() {
		return signName;
	}
	
	public void setSignName(String signName) {
		this.signName = signName;
	}
	
	public String getKeys() {
		return keys;
	}

	public void setKeys(String keys) {
		this.keys = keys;
	}

	public static DySmsEnum toEnum(String templateCode) {
		if(StringUtils.isEmpty(templateCode)){
			return null;
		}
		for(DySmsEnum item : DySmsEnum.values()) {
			if(item.getTemplateCode().equals(templateCode)) {
				return item;
			}
		}
		return null;
	}
}

