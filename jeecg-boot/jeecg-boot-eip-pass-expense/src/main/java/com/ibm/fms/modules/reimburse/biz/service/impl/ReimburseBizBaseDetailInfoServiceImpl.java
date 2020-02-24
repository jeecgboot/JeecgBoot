package com.ibm.fms.modules.reimburse.biz.service.impl;

import com.ibm.fms.modules.reimburse.biz.entity.ReimburseBizBaseDetailInfo;
import com.ibm.fms.modules.reimburse.biz.mapper.ReimburseBizBaseDetailInfoMapper;
import com.ibm.fms.modules.reimburse.biz.service.IReimburseBizBaseDetailInfoService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 报销单基本明细
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
@Service
public class ReimburseBizBaseDetailInfoServiceImpl extends ServiceImpl<ReimburseBizBaseDetailInfoMapper, ReimburseBizBaseDetailInfo> implements IReimburseBizBaseDetailInfoService {
	
	@Autowired
	private ReimburseBizBaseDetailInfoMapper reimburseBizBaseDetailInfoMapper;
	
	@Override
	public List<ReimburseBizBaseDetailInfo> selectByMainId(String mainId) {
		return reimburseBizBaseDetailInfoMapper.selectByMainId(mainId);
	}
}
