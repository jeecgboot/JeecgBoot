package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.business.entity.Country;
import org.jeecg.modules.business.entity.LogisticChannel;
import org.jeecg.modules.business.entity.LogisticChannelPrice;
import org.jeecg.modules.business.entity.Quotation;
import org.jeecg.modules.business.mapper.*;
import org.jeecg.modules.business.service.IQuotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class QuotationServiceImpl extends ServiceImpl<QuotationMapper, Quotation> implements IQuotationService {

    @Autowired
    private LogisticChannelMapper logisticChannelMapper;
    @Autowired private LogisticChannelPriceMapper logisticChannelPriceMapper;
    @Autowired private CountryMapper countryMapper;
    @Autowired private ExchangeRatesMapper exchangeRateMapper;

    @Override
    public IPage<Quotation> pageByStatus(Page<Quotation> page, Quotation q) {
        return baseMapper.pageByStatus(page, q);
    }

    @Override
    public Quotation getByIdAndStatus(String id, String status) {
        return baseMapper.getByIdAndStatus(id, status);
    }

    @Override
    public int updateInquiryFields(Quotation q) {
        return baseMapper.updateInquiryFields(q);
    }

    @Override
    public int addQuoteBasedOnInquiry(Quotation q) {
        return baseMapper.addQuoteBasedOnInquiry(q);
    }

    @Override
    public int updateQuoteFields(Quotation q) {
        return baseMapper.updateQuoteFields(q);
    }

    @Override
    public int revokeQuoteById(String id) {
        return baseMapper.revokeQuoteById(id);
    }

    @Override
    public int revokeQuoteBatch(List<String> ids) {
        if (ids == null || ids.isEmpty()) return 0;
        return baseMapper.revokeQuoteBatch(ids);
    }

    @Override
    public Quotation estimateQuote(Quotation q) {
        if (q == null) return null;
        log.info("[estimateQuote] START id={}, country={}, channel={}",
                q.getId(), q.getCountry(), q.getLogisticChannel());
        // 1) expressWeightG = grossWeightG + packWeightG
        Integer grossG = q.getGrossWeightG();
        Integer packG = safeInt(q.getPackWeightG());
        int expressG = (grossG == null ? 0 : grossG) + (packG == null ? 0 : packG);
        q.setExpressWeightG(expressG);
        // 2) costRmb = purchasePriceRmb + domesticShippingRmb
        if (q.getPurchasePriceRmb() != null || q.getDomesticShippingRmb() != null) {
            BigDecimal purchase = safeBd(q.getPurchasePriceRmb());
            BigDecimal domestic = safeBd(q.getDomesticShippingRmb());
            BigDecimal costRmb = purchase.add(domestic).setScale(2, RoundingMode.HALF_UP);
            q.setCostRmb(costRmb);
        } else {
            log.info("[estimateQuote] costRmb skipped (purchase/domestic both null)");
        }
        // 3) rate: RMB->EUR = 1 / (EUR->RMB) the most recent one
        BigDecimal rmbToEur = null;
        try {
            BigDecimal eurToRmb = exchangeRateMapper.getLatestExchangeRate("EUR", "RMB");
            log.info("[estimateQuote] FX query EUR->RMB rate={}", eurToRmb);
            if (eurToRmb != null && eurToRmb.compareTo(BigDecimal.ZERO) > 0) {
                // RMB->EUR = 1 / (EUR->RMB)
                rmbToEur = BigDecimal.ONE.divide(eurToRmb, 10, RoundingMode.HALF_UP);
                log.info("[estimateQuote] FX computed RMB->EUR = 1/{} = {}", eurToRmb, rmbToEur);
            } else {
                log.warn("[estimateQuote] FX EUR->RMB is null/zero, cannot compute RMB->EUR. eurToRmb={}", eurToRmb);
            }
        } catch (Exception ex) {
            log.error("[estimateQuote] FX query FAILED err={}", ex.getMessage(), ex);
        }

        if (rmbToEur != null && rmbToEur.compareTo(BigDecimal.ZERO) > 0) {
            // cost EUR
            if (q.getCostRmb() != null) {
                BigDecimal costEur = safeBd(q.getCostRmb()).multiply(rmbToEur).setScale(2, RoundingMode.HALF_UP);
                q.setCostEur(costEur);
                log.info("[estimateQuote] costEur computed costRmb={}, rmbToEur={}, costEur={}", q.getCostRmb(), rmbToEur, costEur);
            } else {
                log.info("[estimateQuote] costEur skipped (costRmb null)");
            }

            // sale price EUR
            if (q.getSalePriceRmb() != null) {
                BigDecimal saleEur = safeBd(q.getSalePriceRmb()).multiply(rmbToEur).setScale(2, RoundingMode.HALF_UP);
                q.setSalePriceEur(saleEur);
                log.info("[estimateQuote] salePriceEur computed saleRmb={}, rmbToEur={}, saleEur={}", q.getSalePriceRmb(), rmbToEur, saleEur);
            } else {
                log.info("[estimateQuote] salePriceEur skipped (salePriceRmb null)");
            }
        } else {
            log.warn("[estimateQuote] RMB->EUR rate is null/zero => costEur & salePriceEur stay null. rmbToEur={}", rmbToEur);
        }
        // 4) shipping fee: determined by "pricing channel" + country + weight(g) + effective date
        if (StringUtils.isNotBlank(q.getLogisticChannel())
                && StringUtils.isNotBlank(q.getCountry())
                && q.getExpressWeightG() != null) {
            String channelInput = q.getLogisticChannel();
            String pricingChannelId = resolvePricingChannelId(channelInput);
            Date now = new Date();
            Integer weightG = q.getExpressWeightG();
            Country c = null;
            String countryCode = null;
            try {
                c = countryMapper.selectById(q.getCountry());
                countryCode = (c == null ? null : c.getCode());
            } catch (Exception ex) {
                log.error("[estimateQuote] country resolve FAILED countryId={}, err={}", q.getCountry(), ex.getMessage(), ex);
            }
            log.info("[estimateQuote] freight resolve channelInput={}, pricingChannelId={}, countryId={}, countryCode={}, weightG={}, date={}",
                    channelInput, pricingChannelId, q.getCountry(), countryCode, weightG, now);
            LogisticChannelPrice p = null;
            try {
                p = logisticChannelPriceMapper.findByIdDateWeightAndCountry(
                        pricingChannelId,
                        now,
                        BigDecimal.valueOf(weightG),
                        countryCode
                );
            } catch (Exception ex) {
                log.error("[estimateQuote] freight query FAILED pricingChannelId={}, countryCode={}, weightG={}, err={}",
                        pricingChannelId, countryCode, weightG, ex.getMessage(), ex);
            }
            if (p == null) {
                q.setLogisticsFee(null);
                log.warn("[estimateQuote] logisticsFee NO ROW (pricingChannelId={}, countryCode={}, weightG={}, date={})",
                        pricingChannelId, countryCode, weightG, now);
            } else {
                log.info("[estimateQuote] hit priceRow: pricingChannelId={}, rowId={}, effDate={}, country={}, range=[{},{}], minW={}, minPrice={}, unit={}, unitPrice={}, addCost={}, regFee={}",
                        pricingChannelId,
                        p.getId(), p.getEffectiveDate(), p.getEffectiveCountry(),
                        p.getWeightRangeStart(), p.getWeightRangeEnd(),
                        p.getMinimumWeight(), p.getMinimumWeightPrice(),
                        p.getCalUnit(), p.getCalUnitPrice(),
                        p.getAdditionalCost(), p.getRegistrationFee()
                );
                BigDecimal shipping = p.calculateShippingPrice(BigDecimal.valueOf(q.getExpressWeightG()));
                BigDecimal regFee = safeBd(p.getRegistrationFee());
                BigDecimal addCost = safeBd(p.getAdditionalCost());
                BigDecimal fee = shipping.add(regFee).add(addCost).setScale(2, RoundingMode.UP);
                q.setLogisticsFee(fee);
                log.info("[estimateQuote] logisticsFee computed by entityMethod weightG={}, shipping={}, addCost={}, regFee={}, fee={}",
                        q.getExpressWeightG(), shipping, addCost, regFee, fee);
            }
        } else {
            q.setLogisticsFee(null);
            log.info("[estimateQuote] logisticsFee skipped (missing channel/country/weight)");
        }
        // 5) Prix d’achat = purchase price (EUR) = purchase price (RMB) * exchange rate (RMB->EUR)
        if (q.getPurchasePriceRmb() != null && rmbToEur != null && rmbToEur.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal prixAchat = safeBd(q.getPurchasePriceRmb()).multiply(rmbToEur).setScale(2, RoundingMode.HALF_UP);
            q.setPrixAchat(prixAchat);
            log.info("[estimateQuote] prixAchat computed purchasePriceRmb={}, rmbToEur={}, prixAchat={}",
                    q.getPurchasePriceRmb(), rmbToEur, prixAchat);
        } else {
            log.info("[estimateQuote] prixAchat skipped (purchasePriceRmb or rmbToEur null/zero)");
        }
        // 6) Total Fee = Prix d’achat + Logistics Fee
        if (q.getPrixAchat() != null && q.getLogisticsFee() != null) {
            q.setTotalFee(safeBd(q.getPrixAchat()).add(safeBd(q.getLogisticsFee())).setScale(2, RoundingMode.HALF_UP));
            log.info("[estimateQuote] totalFee computed prixAchat={}, logisticsFee={}, totalFee={}",
                    q.getPrixAchat(), q.getLogisticsFee(), q.getTotalFee());
        } else {
            log.info("[estimateQuote] totalFee skipped (prixAchat/logisticsFee null)");
        }
        // 7) profitRmb = salePriceRmb - costRmb
        if (q.getSalePriceRmb() != null && q.getCostRmb() != null) {
            q.setProfitRmb(safeBd(q.getSalePriceRmb()).subtract(safeBd(q.getCostRmb())).setScale(2, RoundingMode.HALF_UP));
            log.info("[estimateQuote] profitRmb computed saleRmb={}, costRmb={}, profitRmb={}",
                    q.getSalePriceRmb(), q.getCostRmb(), q.getProfitRmb());
        } else {
            log.info("[estimateQuote] profitRmb skipped (salePriceRmb/costRmb null)");
        }
        if (q.getSalePriceEur() != null && q.getCostEur() != null) {
            q.setProfitEur(safeBd(q.getSalePriceEur()).subtract(safeBd(q.getCostEur())).setScale(2, RoundingMode.HALF_UP));
            log.info("[estimateQuote] profitEur computed saleEur={}, costEur={}, profitEur={}",
                    q.getSalePriceEur(), q.getCostEur(), q.getProfitEur());
        } else {
            log.info("[estimateQuote] profitEur skipped (salePriceEur/costEur null)");
        }
        if (q.getProfitEur() != null && q.getSalePriceEur() != null) {
            BigDecimal saleEur = safeBd(q.getSalePriceEur());
            if (saleEur.compareTo(BigDecimal.ZERO) != 0) {
                q.setMargin(safeBd(q.getProfitEur()).divide(saleEur, 4, RoundingMode.HALF_UP));
                log.info("[estimateQuote] margin computed profitEur={}, saleEur={}, margin={}",
                        q.getProfitEur(), q.getSalePriceEur(), q.getMargin());
            }
        }
        log.info("[estimateQuote] END id={} costEur={} salePriceEur={} logisticsFee={}",
                q.getId(), q.getCostEur(), q.getSalePriceEur(), q.getLogisticsFee());
        return q;
    }

    /** if same_price_channel_id is configured for the input channel, use it;
     * otherwise use the input itself as the pricing channel id.
    * */
    private String resolvePricingChannelId(String channelInput) {
        if (StringUtils.isBlank(channelInput)) {
            return null;
        }
        try {
            LogisticChannel lc = logisticChannelMapper.selectById(channelInput);
            if (lc == null) {
                return channelInput;
            }
            if (org.apache.commons.lang3.StringUtils.isNotBlank(lc.getSamePriceChannelId())) {
                return lc.getSamePriceChannelId();
            }
            return lc.getId();
        } catch (Exception e) {
            log.warn("[estimateQuote] resolvePricingChannelId failed, input={}, err={}", channelInput, e.getMessage());
            return channelInput;
        }
    }

    private BigDecimal safeBd(Object v) {
        if (v == null) return BigDecimal.ZERO;
        if (v instanceof BigDecimal) return (BigDecimal) v;
        if (v instanceof Number) return BigDecimal.valueOf(((Number) v).doubleValue());
        try {
            return new BigDecimal(v.toString().trim());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
    private Integer safeInt(Object v) {
        if (v == null) return null;
        try {
            String s = v.toString().trim();
            if (s.isEmpty()) return null;
            return Integer.parseInt(s);
        } catch (Exception e) {
            return null;
        }
    }
}
