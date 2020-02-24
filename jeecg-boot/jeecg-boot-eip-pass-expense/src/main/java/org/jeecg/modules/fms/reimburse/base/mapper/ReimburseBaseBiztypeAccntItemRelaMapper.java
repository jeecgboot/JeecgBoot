package org.jeecg.modules.fms.reimburse.base.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.fms.reimburse.base.entity.ReimburseBaseBiztypeAccntItemRela;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: reimburse_base_biztype_accnt_item_rela
 * @Author: jeecg-boot
 * @Date:   2020-02-10
 * @Version: V1.0
 */
public interface ReimburseBaseBiztypeAccntItemRelaMapper extends BaseMapper<ReimburseBaseBiztypeAccntItemRela> {

	public List<DictModel> queryBizTypeByCostTypeNow(@Param("costType") String costType,@Param("costcenterType") String costcenterType,@Param("now") String now);
	public List<DictModel> queryFeeItemByBizTypeCostTypeNow(@Param("costType") String costType,@Param("costcenterType") String costcenterType,@Param("bizTypeCode") String bizTypeCode,@Param("now") String now);
	public List<DictModel> queryErpAccntByFeeItemNow(@Param("costType") String costType,@Param("costcenterType") String costcenterType,@Param("bizTypeCode") String bizTypeCode,@Param("feeItemCode") String feeItemCode,@Param("now") String now);
	public List<DictModel> queryTableDictItemsByCodeAndFilter(@Param("table") String table,@Param("text") String text,@Param("code") String code,@Param("filterSql") String filterSql);

	
	public String queryDictTextByKey(@Param("code") String code,@Param("key") String key);

	public String queryTableDictTextByKey(@Param("table") String table,@Param("text") String text,@Param("code") String code,@Param("key") String key);

	public List<DictModel> queryTableDictByKeys(@Param("table") String table, @Param("text") String text, @Param("code") String code, @Param("keyArray") String[] keyArray);

}
