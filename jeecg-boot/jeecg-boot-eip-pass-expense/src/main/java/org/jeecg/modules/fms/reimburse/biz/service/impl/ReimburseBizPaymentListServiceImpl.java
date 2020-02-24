package org.jeecg.modules.fms.reimburse.biz.service.impl;

import org.jeecg.modules.fms.reimburse.biz.entity.ReimburseBizPaymentList;
import org.jeecg.modules.fms.reimburse.biz.mapper.ReimburseBizPaymentListMapper;
import org.jeecg.modules.fms.reimburse.biz.service.IReimburseBizPaymentListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 报销单付款清单
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
@Service
@DS("reimbursement")
public class ReimburseBizPaymentListServiceImpl extends ServiceImpl<ReimburseBizPaymentListMapper, ReimburseBizPaymentList> implements IReimburseBizPaymentListService {
	
	@Autowired
	private ReimburseBizPaymentListMapper reimburseBizPaymentListMapper;
	
	@Override
	@DS("reimbursement")
	public List<ReimburseBizPaymentList> selectByMainId(String mainId) {
		return reimburseBizPaymentListMapper.selectByMainId(mainId);
	}
	
	@Override
	@Transactional
	public boolean save(ReimburseBizPaymentList paymentList) {
		String maxSeq = this.getDtlMaxSeq(paymentList.getApplyNo());
		paymentList.setSeq(Integer.valueOf(maxSeq));
		paymentList.setPaymentStatus("0");
		paymentList.setPaymentStatusDesc("未付款");
		paymentList.setErpAccountComNo("");//ERP 付款账户 从业务字典表读取
		paymentList.setErpAccountComRemark("");//ERP 付款账户说明 从业务字典表读取
    	super.save(paymentList);
		return true;
	}
	
	private String getDtlMaxSeq(String applyNo) {
		Integer maxSeq = this.reimburseBizPaymentListMapper.selectMaxSeqByApplyNo(applyNo);
		int iMaxSeq = 1;
		if(maxSeq!=null) {
			iMaxSeq = maxSeq.intValue()+1;
		}
		return String.valueOf(iMaxSeq);
	}
}
