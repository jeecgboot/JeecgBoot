package com.ibm.fms.modules.reimburse.biz.service;

import com.ibm.fms.modules.reimburse.biz.entity.ReimburseBizVatDeductionVouchers;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 报销单抵扣凭证
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
public interface IReimburseBizVatDeductionVouchersService extends IService<ReimburseBizVatDeductionVouchers> {

	public List<ReimburseBizVatDeductionVouchers> selectByMainId(String mainId);
}
