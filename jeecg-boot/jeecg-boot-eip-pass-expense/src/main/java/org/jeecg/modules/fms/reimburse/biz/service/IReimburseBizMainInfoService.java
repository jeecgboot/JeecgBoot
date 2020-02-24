package org.jeecg.modules.fms.reimburse.biz.service;

import java.io.Serializable;
import java.util.Collection;

import com.baomidou.mybatisplus.extension.service.IService;

import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.fms.reimburse.biz.entity.ReimburseBizMainInfo;

/**
 * @Description: 报销单基本信息
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
public interface IReimburseBizMainInfoService extends IService<ReimburseBizMainInfo> {

	/**
	 * 删除一对多
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
	/**
	 * 生成报销单编号
	 * @param orgCode 经办人所在组织
	 * @return 报销单编号：两位组织简称+两位年+两位月+两位日+三位随机数+三位序号
	 */
	public String genReimburseNum(String orgCode,String rbsTemplate);

	
	/**
	 * 初始化报账单信息
	 * @param sysUser
	 * @param orgCode
	 * @param rbsTemplateCode
	 * @return
	 */
	public ReimburseBizMainInfo initRbsBizMainInfo(LoginUser sysUser,String orgCode,String rbsTemplateCode);
	
	
	/**
	 * 初始化报账单信息
	 * @param sysUser
	 * @param orgCode
	 * @param rbsTemplateCode
	 * @return
	 */
	public ReimburseBizMainInfo getReimburseBizMainInfoByApplyNo(String applyNo);
}
