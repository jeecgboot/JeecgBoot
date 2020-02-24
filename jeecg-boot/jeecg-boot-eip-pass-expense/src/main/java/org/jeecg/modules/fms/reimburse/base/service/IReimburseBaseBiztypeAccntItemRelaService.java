package org.jeecg.modules.fms.reimburse.base.service;

import java.util.List;
import java.util.Map;

import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.fms.reimburse.base.entity.ReimburseBaseBiztypeAccntItemRela;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: reimburse_base_biztype_accnt_item_rela
 * @Author: jeecg-boot
 * @Date:   2020-02-10
 * @Version: V1.0
 */
public interface IReimburseBaseBiztypeAccntItemRelaService extends IService<ReimburseBaseBiztypeAccntItemRela> {

	
	public List<DictModel> queryBizTypeByCostTypeNow(String costType,String costcenterType);
	
	public List<DictModel> queryFeeItemByBizTypeCostTypeNow(String costType,String costcenterType,String bizTypeCode);
	
	public List<DictModel> queryErpAccntByFeeItemNow(String costType,String costcenterType,String bizTypeCode,String feeItemCode);


    List<DictModel> queryTableDictItemsByCode(String table, String text, String code);
    
	public List<DictModel> queryTableDictItemsByCodeAndFilter(String table, String text, String code, String filterSql);

	String queryTableDictTextByKey(String table, String text, String code, String key);

	List<String> queryTableDictByKeys(String table, String text, String code, String[] keyArray);
	
	
}
