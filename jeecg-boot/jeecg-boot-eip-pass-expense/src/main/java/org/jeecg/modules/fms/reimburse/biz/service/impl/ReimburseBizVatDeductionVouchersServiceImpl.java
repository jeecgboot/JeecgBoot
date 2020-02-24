package org.jeecg.modules.fms.reimburse.biz.service.impl;

import java.util.List;

import org.jeecg.modules.fms.reimburse.biz.entity.ReimburseBizVatDeductionVouchers;
import org.jeecg.modules.fms.reimburse.biz.mapper.ReimburseBizVatDeductionVouchersMapper;
import org.jeecg.modules.fms.reimburse.biz.service.IReimburseBizVatDeductionVouchersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 报销单抵扣凭证
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
@Service
@DS("reimbursement")
public class ReimburseBizVatDeductionVouchersServiceImpl extends ServiceImpl<ReimburseBizVatDeductionVouchersMapper, ReimburseBizVatDeductionVouchers> implements IReimburseBizVatDeductionVouchersService {
	
	@Autowired
	private ReimburseBizVatDeductionVouchersMapper reimburseBizVatDeductionVouchersMapper;
	
	@Override
	@DS("reimbursement")
	public List<ReimburseBizVatDeductionVouchers> selectByMainId(String mainId) {
		return reimburseBizVatDeductionVouchersMapper.selectByMainId(mainId);
	}
	
	@Override
	@Transactional
	public boolean save(ReimburseBizVatDeductionVouchers rmbsVatVoucherInfo){
		String maxSeq = this.getDtlMaxSeq(rmbsVatVoucherInfo.getApplyNo());
		rmbsVatVoucherInfo.setSeq(Integer.valueOf(maxSeq));
    	super.save(rmbsVatVoucherInfo);
		return true;
	}
	
	private String getDtlMaxSeq(String applyNo) {
		Integer maxSeq = this.reimburseBizVatDeductionVouchersMapper.selectMaxSeqByApplyNo(applyNo);
		int iMaxSeq = 1;
		if(maxSeq!=null) {
			iMaxSeq = maxSeq.intValue()+1;
		}
		return String.valueOf(iMaxSeq);
	}
}
