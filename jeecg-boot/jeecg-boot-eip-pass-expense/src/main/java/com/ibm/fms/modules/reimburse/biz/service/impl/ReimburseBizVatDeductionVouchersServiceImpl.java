package com.ibm.fms.modules.reimburse.biz.service.impl;

import com.ibm.fms.modules.reimburse.biz.entity.ReimburseBizVatDeductionVouchers;
import com.ibm.fms.modules.reimburse.biz.mapper.ReimburseBizVatDeductionVouchersMapper;
import com.ibm.fms.modules.reimburse.biz.service.IReimburseBizVatDeductionVouchersService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 报销单抵扣凭证
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
@Service
public class ReimburseBizVatDeductionVouchersServiceImpl extends ServiceImpl<ReimburseBizVatDeductionVouchersMapper, ReimburseBizVatDeductionVouchers> implements IReimburseBizVatDeductionVouchersService {
	
	@Autowired
	private ReimburseBizVatDeductionVouchersMapper reimburseBizVatDeductionVouchersMapper;
	
	@Override
	public List<ReimburseBizVatDeductionVouchers> selectByMainId(String mainId) {
		return reimburseBizVatDeductionVouchersMapper.selectByMainId(mainId);
	}
}
