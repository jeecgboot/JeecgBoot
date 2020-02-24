package org.jeecg.modules.fms.reimburse.base.service.impl;

import java.util.List;
import java.util.Map;

import org.jeecg.modules.fms.reimburse.base.entity.ReimburseBaseErpCostcenter;
import org.jeecg.modules.fms.reimburse.base.mapper.ReimburseBaseErpCostcenterMapper;
import org.jeecg.modules.fms.reimburse.base.service.IReimburseBaseErpCostcenterService;
import org.jeecg.modules.fms.reimburse.base.vo.CostcenterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: reimburse_base_erp_costcenter
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Service
@DS("reimbursement")
public class ReimburseBaseErpCostcenterServiceImpl extends ServiceImpl<ReimburseBaseErpCostcenterMapper, ReimburseBaseErpCostcenter> implements IReimburseBaseErpCostcenterService {
	
	@Autowired
	private ReimburseBaseErpCostcenterMapper rmbsErpCostcenterMapper;
	@Override
	public CostcenterVO initCostCenterInfo(String orgCode) {
		List<CostcenterVO> costCenterList= rmbsErpCostcenterMapper.queryCurCostCenterInfo(orgCode);
		if(costCenterList!=null && costCenterList.size()>0) {
			return costCenterList.get(0);
		}else {
			return null;
		}
		
		
	}
}
