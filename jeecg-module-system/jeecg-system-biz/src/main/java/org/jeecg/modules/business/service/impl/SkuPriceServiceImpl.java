package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.SkuPrice;
import org.jeecg.modules.business.mapper.SkuPriceMapper;
import org.jeecg.modules.business.service.ICurrencyService;
import org.jeecg.modules.business.service.ISkuPriceService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

	@Autowired
	private ICurrencyService currencyService;

    @Override
	public List<SkuPrice> selectByMainId(String mainId) {
		return skuPriceMapper.selectByMainId(mainId);
	}

	@Override
	public SkuPrice getLatestBySkuId(String sku_id) {
		return skuPriceMapper.getLatestBySkuId(sku_id);
	}


	/**
	 * The price of a sku depends on its quantity, Given a quantity here, return the correspondent price.
	 * @param skuPrice SKU price object
	 * @param quantity Quantity of the SKU
	 * @param eurToRmb Exchange rate from EUR to RMB
	 * @return The price based on the quantity
	 */
	@Override
	public BigDecimal getPrice(SkuPrice skuPrice, int quantity, BigDecimal eurToRmb) {
        String RMB_CURRENCY_CODE = "RMB";
        String rmbId = currencyService.getIdByCode(RMB_CURRENCY_CODE);
		BigDecimal priceCandidate = skuPrice.getPrice();
		BigDecimal discountPriceCandidate = skuPrice.getDiscountedPrice() == null ? priceCandidate : skuPrice.getDiscountedPrice();
		// Convert the price to EURO if the currency is RMB
		boolean isRmb = rmbId.equals(skuPrice.getCurrencyId());
		if (isRmb) {
			priceCandidate = priceCandidate.divide(eurToRmb, 2, RoundingMode.HALF_UP);
			discountPriceCandidate = discountPriceCandidate.divide(eurToRmb, 2, RoundingMode.HALF_UP);
		}
		// Get the price based on the quantity
		if (skuPrice.getThreshold() != null && quantity >= skuPrice.getThreshold()) {
			return discountPriceCandidate;
		} else {
			return priceCandidate;
		}
	}
}
