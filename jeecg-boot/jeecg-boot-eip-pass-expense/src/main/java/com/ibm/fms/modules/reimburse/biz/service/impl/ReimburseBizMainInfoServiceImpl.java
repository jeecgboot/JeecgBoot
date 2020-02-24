package com.ibm.fms.modules.reimburse.biz.service.impl;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ibm.fms.modules.reimburse.biz.entity.ReimburseBizMainInfo;
import com.ibm.fms.modules.reimburse.biz.mapper.ReimburseBizBaseDetailInfoMapper;
import com.ibm.fms.modules.reimburse.biz.mapper.ReimburseBizMainInfoMapper;
import com.ibm.fms.modules.reimburse.biz.mapper.ReimburseBizPaymentListMapper;
import com.ibm.fms.modules.reimburse.biz.mapper.ReimburseBizVatDeductionVouchersMapper;
import com.ibm.fms.modules.reimburse.biz.service.IReimburseBizMainInfoService;

/**
 * @Description: 报销单基本信息
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
@Service
public class ReimburseBizMainInfoServiceImpl extends ServiceImpl<ReimburseBizMainInfoMapper, ReimburseBizMainInfo> implements IReimburseBizMainInfoService {

	@Autowired
	private ReimburseBizMainInfoMapper reimburseBizMainInfoMapper;
	@Autowired
	private ReimburseBizBaseDetailInfoMapper reimburseBizBaseDetailInfoMapper;
	@Autowired
	private ReimburseBizVatDeductionVouchersMapper reimburseBizVatDeductionVouchersMapper;
	@Autowired
	private ReimburseBizPaymentListMapper reimburseBizPaymentListMapper;
	
	@Override
	@DS("reimbursement")
	@Transactional
	public void delMain(String id) {
		reimburseBizBaseDetailInfoMapper.deleteByMainId(id);
		reimburseBizVatDeductionVouchersMapper.deleteByMainId(id);
		reimburseBizPaymentListMapper.deleteByMainId(id);
		reimburseBizMainInfoMapper.deleteById(id);
	}

	@Override
	@DS("reimbursement")
	@Transactional
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			reimburseBizBaseDetailInfoMapper.deleteByMainId(id.toString());
			reimburseBizVatDeductionVouchersMapper.deleteByMainId(id.toString());
			reimburseBizPaymentListMapper.deleteByMainId(id.toString());
			reimburseBizMainInfoMapper.deleteById(id);
		}
	}
	
}
