package org.jeecg.modules.fms.reimburse.base.service.impl;

import java.util.List;
import java.util.Map;

import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.fms.reimburse.base.entity.ReimburseBaseBiztypeAccntItemRela;
import org.jeecg.modules.fms.reimburse.base.mapper.ReimburseBaseBiztypeAccntItemRelaMapper;
import org.jeecg.modules.fms.reimburse.base.service.IReimburseBaseBiztypeAccntItemRelaService;
import org.jeecg.modules.fms.reimburse.biz.utils.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: reimburse_base_biztype_accnt_item_rela
 * @Author: jeecg-boot
 * @Date:   2020-02-10
 * @Version: V1.0
 */
@Service
@DS("reimbursement")
public class ReimburseBaseBiztypeAccntItemRelaServiceImpl extends ServiceImpl<ReimburseBaseBiztypeAccntItemRelaMapper, ReimburseBaseBiztypeAccntItemRela> implements IReimburseBaseBiztypeAccntItemRelaService {

	@Autowired
	private ReimburseBaseBiztypeAccntItemRelaMapper bizTypeAccntItemRelaMapper;
	
	@Override
	public List<DictModel> queryBizTypeByCostTypeNow(String costType, String costcenterType) {
		 String now = DateTimeUtils.getTimeNowStr();
		 return bizTypeAccntItemRelaMapper.queryBizTypeByCostTypeNow(costType, costcenterType, now);
	}

	@Override
	public List<DictModel> queryFeeItemByBizTypeCostTypeNow(String costType, String costcenterType,String bizTypeCode) {
		String now = DateTimeUtils.getTimeNowStr();
		return bizTypeAccntItemRelaMapper.queryFeeItemByBizTypeCostTypeNow(costType, costcenterType, bizTypeCode, now);
	}
	
	@Override
	public List<DictModel> queryErpAccntByFeeItemNow(String costType,String costcenterType,String bizTypeCode,String feeItemCode){
		String now = DateTimeUtils.getTimeNowStr();
		return bizTypeAccntItemRelaMapper.queryErpAccntByFeeItemNow(costType, costcenterType, bizTypeCode, feeItemCode, now);
	}
	
	
	@Override
	public List<DictModel> queryTableDictItemsByCode(String table, String text, String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DictModel> queryTableDictItemsByCodeAndFilter(String table, String text, String code,
			String filterSql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String queryTableDictTextByKey(String table, String text, String code, String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> queryTableDictByKeys(String table, String text, String code, String[] keyArray) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
