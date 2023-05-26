package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.business.controller.UserException;
import org.jeecg.modules.business.entity.LogisticChannelPrice;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import org.jeecg.modules.business.mapper.CountryNameMapper;
import org.jeecg.modules.business.mapper.LogisticChannelPriceMapper;
import org.jeecg.modules.business.mapper.PlatformOrderContentMapper;
import org.jeecg.modules.business.mapper.PopularCountryMapper;
import org.jeecg.modules.business.service.CountryService;
import org.jeecg.modules.business.service.ILogisticChannelPriceService;
import org.jeecg.modules.business.service.IPlatformOrderContentService;
import org.jeecg.modules.business.vo.CountryName;
import org.jeecg.modules.business.vo.PopularCountry;
import org.jeecg.modules.business.vo.SkuWeightDiscountServiceFees;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 物流渠道价格
 * @Author: William
 * @Date: 2021-06-16
 * @Version: V1.0
 */
@Service
public class LogisticChannelPriceServiceImpl extends ServiceImpl<LogisticChannelPriceMapper, LogisticChannelPrice> implements ILogisticChannelPriceService {

    @Autowired
    private LogisticChannelPriceMapper logisticChannelPriceMapper;

    @Autowired
    private CountryNameMapper countryNameMapper;

    @Autowired
    private PopularCountryMapper popularCountryMapper;

    @Autowired
    private CountryService countryService;

    @Autowired
    private IPlatformOrderContentService platformOrderContentService;
    @Autowired
    private PlatformOrderContentMapper platformOrderContentMapper;

    @Override
    public List<LogisticChannelPrice> selectByMainId(String mainId) {
        return logisticChannelPriceMapper.selectByMainId(mainId);
    }

    @Override
    public List<CountryName> getAllCountry() {
        return countryNameMapper.selectList(null);
    }

    @Override
    public List<PopularCountry> getPopularCountryList() {
        return popularCountryMapper.selectList(null);
    }

    @Override
    public LogisticChannelPrice findPriceForPlatformOrder(PlatformOrder order) throws UserException {
        Map<String, BigDecimal> skuRealWeights = new HashMap<>();
        for (SkuWeightDiscountServiceFees skuWeightDiscountServiceFees : platformOrderContentService.getAllSKUWeightsDiscountsServiceFees()) {
            if (skuWeightDiscountServiceFees.getWeight() != null) {
                skuRealWeights.put(skuWeightDiscountServiceFees.getSkuId(),
                        skuWeightDiscountServiceFees.getDiscount().multiply(BigDecimal.valueOf(skuWeightDiscountServiceFees.getWeight())));
            }
        }

        List<PlatformOrderContent> contents = platformOrderContentMapper.selectByMainId(order.getId());
        Map<String, Integer> contentMap = new HashMap<>();
        for (PlatformOrderContent content : contents) {
            contentMap.put(content.getSkuId(), content.getQuantity());
        }

        String logisticChannelName = order.getInvoiceLogisticChannelName() == null ?
                order.getLogisticChannelName() : order.getInvoiceLogisticChannelName();
        BigDecimal weight = platformOrderContentService.calculateWeight(
                contentMap,
                skuRealWeights
        );


        String countryCode = countryService.findByEnName(order.getCountry()).getCode();

        LogisticChannelPrice price = logisticChannelPriceMapper.findBy(
                logisticChannelName,
                order.getShippingTime(),
                weight,
                countryCode
        );

        if (price == null) {
            throw new UserException("Can't find price for channel {}, shipped at {}, weight {}, country {}",
                    logisticChannelName,
                    order.getShippingTime(),
                    weight,
                    countryCode
            );
        }
        return price;
    }

    @Override
    public LogisticChannelPrice findPriceOfSkuByCountryAndChannelAndDate(String skuId, String countryCode, String channelId, Date date) throws UserException {
        Map<String, BigDecimal> skuRealWeights = new HashMap<>();
        for (SkuWeightDiscountServiceFees skuWeightDiscountServiceFees : platformOrderContentService.getAllSKUWeightsDiscountsServiceFees()) {
            if (skuWeightDiscountServiceFees.getWeight() != null) {
                skuRealWeights.put(skuWeightDiscountServiceFees.getSkuId(),
                        skuWeightDiscountServiceFees.getDiscount().multiply(BigDecimal.valueOf(skuWeightDiscountServiceFees.getWeight())));
            }
        }

        BigDecimal weight = skuRealWeights.get(skuId);

        LogisticChannelPrice price = logisticChannelPriceMapper.findByIdDateWeightAndCountry(
                channelId,
                date,
                weight,
                countryCode
        );

        if (price == null) {
            throw new UserException("Can't find price for channel {}, shipped at {}, weight {}, country {}",
                    channelId,
                    date,
                    weight,
                    countryCode
            );
        }
        return price;
    }
}
