package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.ShippingFeesWaiverProduct;
import org.jeecg.modules.business.vo.SkuShippingFeesWaiver;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 采购运费免除产品
 * @Author: jeecg-boot
 * @Date:   2021-06-02
 * @Version: V1.0
 */
@Repository
public interface ShippingFeesWaiverProductMapper extends BaseMapper<ShippingFeesWaiverProduct> {

	public boolean deleteByMainId(@Param("mainId") String mainId);
    
	public List<ShippingFeesWaiverProduct> selectByMainId(@Param("mainId") String mainId);

	public List<SkuShippingFeesWaiver> searchWaiversBySkuIds(List<String> skuIds);
}
