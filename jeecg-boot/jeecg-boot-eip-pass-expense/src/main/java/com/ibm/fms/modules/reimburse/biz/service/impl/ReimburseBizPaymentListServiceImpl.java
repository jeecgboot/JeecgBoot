package com.ibm.fms.modules.reimburse.biz.service.impl;

import com.ibm.fms.modules.reimburse.biz.entity.ReimburseBizPaymentList;
import com.ibm.fms.modules.reimburse.biz.mapper.ReimburseBizPaymentListMapper;
import com.ibm.fms.modules.reimburse.biz.service.IReimburseBizPaymentListService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 报销单付款清单
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
@Service
public class ReimburseBizPaymentListServiceImpl extends ServiceImpl<ReimburseBizPaymentListMapper, ReimburseBizPaymentList> implements IReimburseBizPaymentListService {
	
	@Autowired
	private ReimburseBizPaymentListMapper reimburseBizPaymentListMapper;
	
	@Override
	public List<ReimburseBizPaymentList> selectByMainId(String mainId) {
		return reimburseBizPaymentListMapper.selectByMainId(mainId);
	}
}
