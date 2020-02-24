package org.jeecg.modules.fms.reimburse.biz.service.impl;

import org.jeecg.modules.fms.reimburse.biz.entity.ReimburseBizBaseDetailInfo;
import org.jeecg.modules.fms.reimburse.biz.entity.ReimburseBizMainInfo;
import org.jeecg.modules.fms.reimburse.biz.mapper.ReimburseBizBaseDetailInfoMapper;
import org.jeecg.modules.fms.reimburse.biz.service.IReimburseBizBaseDetailInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 报销单基本明细
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
@Service
@DS("reimbursement")
public class ReimburseBizBaseDetailInfoServiceImpl extends ServiceImpl<ReimburseBizBaseDetailInfoMapper, ReimburseBizBaseDetailInfo> implements IReimburseBizBaseDetailInfoService {
	
	@Autowired
	private ReimburseBizBaseDetailInfoMapper reimburseBizBaseDetailInfoMapper;
	
	@Override
	@DS("reimbursement")
	public List<ReimburseBizBaseDetailInfo> selectByMainId(String mainId) {
		return reimburseBizBaseDetailInfoMapper.selectByMainId(mainId);
	}
	
	@Override
	@Transactional
	public boolean save(ReimburseBizBaseDetailInfo rmbsDtlInfo) {
    	
		String maxSeq = this.getDtlMaxSeq(rmbsDtlInfo.getApplyNo());
		rmbsDtlInfo.setSeq(Integer.valueOf(maxSeq));
    	super.save(rmbsDtlInfo);
		return true;
	}
	
	private String getDtlMaxSeq(String applyNo) {
		Integer maxSeq = this.reimburseBizBaseDetailInfoMapper.selectMaxSeqByApplyNo(applyNo);
		int iMaxSeq = 1;
		if(maxSeq!=null) {
			iMaxSeq = maxSeq.intValue()+1;
		}
		return String.valueOf(iMaxSeq);
	}
}
