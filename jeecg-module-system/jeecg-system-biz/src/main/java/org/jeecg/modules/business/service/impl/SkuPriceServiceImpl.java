package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.SkuPrice;
import org.jeecg.modules.business.mapper.SkuPriceMapper;
import org.jeecg.modules.business.service.ISkuPriceService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: SKU价格表
 * @Author: jeecg-boot
 * @Date:   2021-04-03
 * @Version: V1.0
 */
@Service
public class SkuPriceServiceImpl extends ServiceImpl<SkuPriceMapper, SkuPrice> implements ISkuPriceService {
	
	@Autowired
	private SkuPriceMapper skuPriceMapper;
	
	@Override
	public List<SkuPrice> selectByMainId(String mainId) {
		return skuPriceMapper.selectByMainId(mainId);
	}
}
