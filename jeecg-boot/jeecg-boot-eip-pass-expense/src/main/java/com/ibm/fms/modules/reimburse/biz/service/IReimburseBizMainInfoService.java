package com.ibm.fms.modules.reimburse.biz.service;

import com.ibm.fms.modules.reimburse.biz.entity.ReimburseBizBaseDetailInfo;
import com.ibm.fms.modules.reimburse.biz.entity.ReimburseBizVatDeductionVouchers;
import com.ibm.fms.modules.reimburse.biz.entity.ReimburseBizPaymentList;
import com.ibm.fms.modules.reimburse.biz.entity.ReimburseBizMainInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

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


}
