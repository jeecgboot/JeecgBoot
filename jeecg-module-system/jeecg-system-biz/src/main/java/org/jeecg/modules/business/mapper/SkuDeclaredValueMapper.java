package org.jeecg.modules.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.business.entity.SkuDeclaredValue;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description: SKU申报价格
 * @Author: jeecg-boot
 * @Date:   2021-06-28
 * @Version: V1.0
 */
@Repository
public interface SkuDeclaredValueMapper extends BaseMapper<SkuDeclaredValue> {

	boolean deleteByMainId(@Param("mainId") String mainId);
    
	List<SkuDeclaredValue> selectByMainId(@Param("mainId") String mainId);

	List<SkuDeclaredValue> getLatestSkuDeclaredValues();

	BigDecimal getDeclaredValueForDate(@Param("skuId") String skuId, @Param("date") Date date);

	List<Map<String, BigDecimal>> getLatestDeclaredValues();

	void insertNewDeclaredValues(@Param("items") List<SkuDeclaredValue> items);
}
