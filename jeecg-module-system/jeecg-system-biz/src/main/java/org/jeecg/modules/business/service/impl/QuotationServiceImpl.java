package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.Country;
import org.jeecg.modules.business.entity.LogisticChannel;
import org.jeecg.modules.business.entity.LogisticChannelPrice;
import org.jeecg.modules.business.entity.Quotation;
import org.jeecg.modules.business.mapper.*;
import org.jeecg.modules.business.service.IQuotationService;
import org.jeecg.modules.business.service.ISecurityService;
import org.jeecg.modules.business.service.IUserClientService;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuotationServiceImpl extends ServiceImpl<QuotationMapper, Quotation> implements IQuotationService {

    @Autowired
    private LogisticChannelMapper logisticChannelMapper;
    @Autowired private LogisticChannelPriceMapper logisticChannelPriceMapper;
    @Autowired private CountryMapper countryMapper;
    @Autowired private ExchangeRatesMapper exchangeRateMapper;
    @Autowired private ISecurityService securityService;
    @Autowired private IUserClientService userClientService;
    @Autowired private ClientSalespersonMapper clientSalespersonMapper;

    @Override
    public IPage<Quotation> pageByStatus(Page<Quotation> page, Quotation q) {
        normalizeCountryFields(q);
        IPage<Quotation> result = baseMapper.pageByStatus(page, q);
        if (result == null || result.getRecords() == null || result.getRecords().isEmpty()) {
            return result;
        }
        for (Quotation record : result.getRecords()) {
            fillComputedFields(record);
        }
        return result;
    }

    @Override
    public void exportCustomerQuotes(Quotation q, String selections, HttpServletResponse response) throws IOException {
        if (q == null) {
            q = new Quotation();
        }
        Result<String> clientScopeError = applyClientScope(q);
        if (clientScopeError != null) {
            response.sendError(clientScopeError.getCode(), clientScopeError.getMessage());
            return;
        }
        List<String> ids = parseSelections(selections);
        List<Quotation> records = listForCustomerQuoteExport(q, ids);
        writeCustomerQuotesWorkbook(records, response);
    }

    private List<Quotation> listForCustomerQuoteExport(Quotation q, List<String> ids) {
        normalizeCountryFields(q);
        List<Quotation> records;
        if (ids != null && !ids.isEmpty()) {
            records = baseMapper.selectBatchIds(ids);
            records.removeIf(record -> !matchesCustomerQuoteExportSelectionScope(record, q));
            records.sort((a, b) -> {
                Date aTime = a == null ? null : a.getCreateTime();
                Date bTime = b == null ? null : b.getCreateTime();
                if (aTime == null && bTime == null) return 0;
                if (aTime == null) return 1;
                if (bTime == null) return -1;
                return bTime.compareTo(aTime);
            });
        } else {
            IPage<Quotation> page = baseMapper.pageByStatus(new Page<>(1, Integer.MAX_VALUE), q);
            records = page == null ? new ArrayList<>() : page.getRecords();
        }
        if (records != null) {
            for (Quotation record : records) {
                fillComputedFields(record);
            }
        }
        return records;
    }

    private List<String> parseSelections(String selections) {
        if (StringUtils.isBlank(selections)) {
            return new ArrayList<>();
        }
        return Arrays.stream(selections.split(","))
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
    }

    private void writeCustomerQuotesWorkbook(List<Quotation> records, HttpServletResponse response) throws IOException {
        ExportParams exportParams = new ExportParams("Customer Quotes", "Customer Quotes");
        exportParams.setType(ExcelType.XSSF);

        List<ExcelExportEntity> columns = new ArrayList<>();
        columns.add(new ExcelExportEntity("Product Name", "productName"));
        columns.add(new ExcelExportEntity("Supplier SKU", "supplierSku"));
        columns.add(new ExcelExportEntity("MOQ", "moq"));
        columns.add(new ExcelExportEntity("Customer Price (EUR)", "customerPrice"));
        columns.add(new ExcelExportEntity("Photo", "photo"));
        columns.add(new ExcelExportEntity("Customer Link", "customerUrl"));
        columns.add(new ExcelExportEntity("Size Range", "sizeRange"));
        columns.add(new ExcelExportEntity("Country", "country"));
        columns.add(new ExcelExportEntity("Delivery Time", "livraison"));
        columns.add(new ExcelExportEntity("Purchase Price (EUR)", "purchasePrice"));
        columns.add(new ExcelExportEntity("Shipping Fee (EUR)", "logisticsFee"));
        columns.add(new ExcelExportEntity("Total Fee (EUR)", "totalFee"));

        List<Map<String, Object>> dataList = new ArrayList<>();
        if (records != null) {
            for (Quotation quotation : records) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("productName", quotation.getProductName());
                row.put("supplierSku", quotation.getSupplierSku());
                row.put("moq", quotation.getMoq());
                row.put("customerPrice", quotation.getCustomerPrice());
                row.put("photo", quotation.getPhoto());
                row.put("customerUrl", quotation.getCustomerUrl());
                row.put("country", getCountryName(quotation.getCountry()));
                row.put("livraison", quotation.getLivraison());
                row.put("purchasePrice", quotation.getPrixAchat() != null ? quotation.getPrixAchat() : quotation.getSalePriceEur());
                row.put("logisticsFee", quotation.getLogisticsFee());
                row.put("totalFee", quotation.getTotalFee());
                row.put("sizeRange", StringUtils.isNotBlank(quotation.getSizeRange()) ? quotation.getSizeRange() : quotation.getProductSize());
                dataList.add(row);
            }
        }

        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, columns, dataList);
        autoSizeColumns(workbook, columns.size());
        String exportDate = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename="
                + URLEncoder.encode("customer_quotes_" + exportDate + ".xlsx", "UTF-8"));
        try (ServletOutputStream out = response.getOutputStream()) {
            workbook.write(out);
            out.flush();
        }
    }

    private void autoSizeColumns(Workbook workbook, int columnCount) {
        if (workbook == null || workbook.getNumberOfSheets() == 0) {
            return;
        }
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
            int width = Math.min(sheet.getColumnWidth(i) + 512, 80 * 256);
            sheet.setColumnWidth(i, Math.max(width, 14 * 256));
        }
    }

    private String getCountryName(String countryValue) {
        if (StringUtils.isBlank(countryValue)) {
            return countryValue;
        }
        try {
            Country country = countryMapper.selectById(countryValue);
            if (country == null) {
                country = countryMapper.findByCode(countryValue);
            }
            if (country == null) {
                country = countryMapper.findByEnName(countryValue);
            }
            if (country == null) {
                country = countryMapper.findByZhName(countryValue);
            }
            return country == null ? countryValue : country.getNameEn();
        } catch (Exception e) {
            log.warn("Resolve export country name failed, value={}, err={}", countryValue, e.getMessage());
            return countryValue;
        }
    }

    private boolean matchesCustomerQuoteExportSelectionScope(Quotation record, Quotation q) {
        if (record == null) {
            return false;
        }
        if (q == null) {
            return true;
        }
        return StringUtils.isBlank(q.getInquiryClient()) || q.getInquiryClient().equals(record.getInquiryClient());
    }

    @Override
    public Quotation getByIdAndStatus(String id, String status) {
        return fillComputedFields(baseMapper.getByIdAndStatus(id, status));
    }

    @Override
    public int updateInquiryFields(Quotation q) {
        normalizeCountryFields(q);
        fillInquirySalesByClient(q);
        return baseMapper.updateInquiryFields(q);
    }

    @Override
    public int addQuoteBasedOnInquiry(Quotation q) {
        normalizeCountryFields(q);
        return baseMapper.addQuoteBasedOnInquiry(q);
    }

    @Override
    public int updateQuoteFields(Quotation q) {
        normalizeCountryFields(q);
        return baseMapper.updateQuoteFields(q);
    }

    @Override
    public void normalizeCountryFields(Quotation q) {
        if (q == null) {
            return;
        }
        q.setCountry(normalizeCountryValue(q.getCountry()));
        q.setInquiryCountry(normalizeCountryList(q.getInquiryCountry()));
    }

    @Override
    public void fillInquirySalesByClient(Quotation q) {
        if (q == null || StringUtils.isBlank(q.getInquiryClient()) || StringUtils.isNotBlank(q.getInquirySales())) {
            return;
        }
        List<String> salespersonIds = clientSalespersonMapper.getSalespersonIdsByClientId(q.getInquiryClient());
        if (salespersonIds == null || salespersonIds.isEmpty()) {
            return;
        }
        String sales = salespersonIds.stream()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(","));
        if (StringUtils.isNotBlank(sales)) {
            q.setInquirySales(sales);
        }
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

    // For inquiry creation, set the inquiryClient to current user's client id
    @Override
    public Result<String> applyClientScope(Quotation quotation) {
        if (securityService.checkIsEmployee()) {
            return null;
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Client userClient = userClientService.getClientByUserId(sysUser.getId());
        if (userClient == null) {
            log.error("User {} has no bound client record.", sysUser.getUsername());
            return Result.error(403, "Access denied");
        }
        // For inquiry creation, set the inquiryClient to current user's client id;
        quotation.setInquiryClient(userClient.getId());
        return null;
    }

    // For inquiry edit/delete/queryById, check if the record belongs to current user's client
    @Override
    public Result<String> checkInquiryOwnership(String quotationId) {
        if (securityService.checkIsEmployee()) {
            return null;
        }
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        Client userClient = userClientService.getClientByUserId(sysUser.getId());
        if (userClient == null) {
            log.error("User {} has no bound client record.", sysUser.getUsername());
            return Result.error(403, "Access denied");
        }
        Quotation quotation = getById(quotationId);
        if (quotation == null) {
            return Result.error("Record not found");
        }
        if (!userClient.getId().equals(quotation.getInquiryClient())) {
            log.error("User {} tried to edit quotation {} which belongs to client {}.", sysUser.getUsername(), quotationId, quotation.getInquiryClient());
            return Result.error(403, "Access denied");
        }
        return null;
    }

    @Override
    public Quotation estimateQuote(Quotation q) {
        if (q == null) return null;
        log.debug("[estimateQuote] START id={}, country={}, channel={}",
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
            log.debug("[estimateQuote] costRmb skipped (purchase/domestic both null)");
        }
        // 3) rate: RMB->EUR = 1 / (EUR->RMB) the most recent one
        BigDecimal rmbToEur = null;
        try {
            BigDecimal eurToRmb = exchangeRateMapper.getLatestExchangeRate("EUR", "RMB");
            log.debug("[estimateQuote] FX query EUR->RMB rate={}", eurToRmb);
            if (eurToRmb != null && eurToRmb.compareTo(BigDecimal.ZERO) > 0) {
                // RMB->EUR = 1 / (EUR->RMB)
                rmbToEur = BigDecimal.ONE.divide(eurToRmb, 10, RoundingMode.HALF_UP);
                log.debug("[estimateQuote] FX computed RMB->EUR = 1/{} = {}", eurToRmb, rmbToEur);
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
                log.debug("[estimateQuote] costEur computed costRmb={}, rmbToEur={}, costEur={}", q.getCostRmb(), rmbToEur, costEur);
            } else {
                log.debug("[estimateQuote] costEur skipped (costRmb null)");
            }

            // sale price EUR
            if (q.getSalePriceRmb() != null) {
                BigDecimal saleEur = safeBd(q.getSalePriceRmb()).multiply(rmbToEur).setScale(2, RoundingMode.HALF_UP);
                q.setSalePriceEur(saleEur);
                log.debug("[estimateQuote] salePriceEur computed saleRmb={}, rmbToEur={}, saleEur={}", q.getSalePriceRmb(), rmbToEur, saleEur);
            } else {
                log.debug("[estimateQuote] salePriceEur skipped (salePriceRmb null)");
            }
        } else {
            log.debug("[estimateQuote] RMB->EUR rate is null/zero => costEur & salePriceEur stay null. rmbToEur={}", rmbToEur);
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
            log.debug("[estimateQuote] freight resolve channelInput={}, pricingChannelId={}, countryId={}, countryCode={}, weightG={}, date={}",
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
                log.debug("[estimateQuote] logisticsFee NO ROW (pricingChannelId={}, countryCode={}, weightG={}, date={})",
                        pricingChannelId, countryCode, weightG, now);
            } else {
                log.debug("[estimateQuote] hit priceRow: pricingChannelId={}, rowId={}, effDate={}, country={}, range=[{},{}], minW={}, minPrice={}, unit={}, unitPrice={}, addCost={}, regFee={}",
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
                log.debug("[estimateQuote] logisticsFee computed by entityMethod weightG={}, shipping={}, addCost={}, regFee={}, fee={}",
                        q.getExpressWeightG(), shipping, addCost, regFee, fee);
            }
        } else {
            q.setLogisticsFee(null);
            log.debug("[estimateQuote] logisticsFee skipped (missing channel/country/weight)");
        }
        // 5) Prix d’achat = purchase price (EUR) = purchase price (RMB) * exchange rate (RMB->EUR)
        if (q.getSalePriceEur() != null) {
            BigDecimal prixAchat = safeBd(q.getSalePriceEur()).setScale(2, RoundingMode.HALF_UP);
            q.setPrixAchat(prixAchat);
            log.debug("[estimateQuote] prixAchat set from salePriceEur={}, prixAchat={}",
                    q.getSalePriceEur(), prixAchat);
        } else {
            log.debug("[estimateQuote] prixAchat skipped (salePriceEur null)");
        }
        // 6) Total Fee = Prix d’achat + Logistics Fee
        if (q.getSalePriceEur() != null && q.getLogisticsFee() != null) {
            q.setTotalFee(safeBd(q.getSalePriceEur()).add(safeBd(q.getLogisticsFee())).setScale(2, RoundingMode.HALF_UP));
            log.debug("[estimateQuote] totalFee computed salePriceEur={}, logisticsFee={}, totalFee={}",
                    q.getSalePriceEur(), q.getLogisticsFee(), q.getTotalFee());
        } else {
            log.debug("[estimateQuote] totalFee skipped (salePriceEur/logisticsFee null)");
        }
        // 7) profitRmb = salePriceRmb - costRmb
        if (q.getSalePriceRmb() != null && q.getCostRmb() != null) {
            q.setProfitRmb(safeBd(q.getSalePriceRmb()).subtract(safeBd(q.getCostRmb())).setScale(2, RoundingMode.HALF_UP));
            log.debug("[estimateQuote] profitRmb computed saleRmb={}, costRmb={}, profitRmb={}",
                    q.getSalePriceRmb(), q.getCostRmb(), q.getProfitRmb());
        } else {
            log.debug("[estimateQuote] profitRmb skipped (salePriceRmb/costRmb null)");
        }
        if (q.getSalePriceEur() != null && q.getCostEur() != null) {
            q.setProfitEur(safeBd(q.getSalePriceEur()).subtract(safeBd(q.getCostEur())).setScale(2, RoundingMode.HALF_UP));
            log.debug("[estimateQuote] profitEur computed saleEur={}, costEur={}, profitEur={}",
                    q.getSalePriceEur(), q.getCostEur(), q.getProfitEur());
        } else {
            log.debug("[estimateQuote] profitEur skipped (salePriceEur/costEur null)");
        }
        if (q.getProfitEur() != null && q.getSalePriceEur() != null) {
            BigDecimal saleEur = safeBd(q.getSalePriceEur());
            if (saleEur.compareTo(BigDecimal.ZERO) != 0) {
                q.setMargin(safeBd(q.getProfitEur()).divide(saleEur, 4, RoundingMode.HALF_UP));
                log.debug("[estimateQuote] margin computed profitEur={}, saleEur={}, margin={}",
                        q.getProfitEur(), q.getSalePriceEur(), q.getMargin());
            }
        }
        log.debug("[estimateQuote] END id={} costEur={} salePriceEur={} logisticsFee={}",
                q.getId(), q.getCostEur(), q.getSalePriceEur(), q.getLogisticsFee());
        return q;
    }

    private Quotation fillComputedFields(Quotation quotation) {
        if (quotation == null) {
            return null;
        }
        return estimateQuote(quotation);
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

    private String normalizeCountryList(String countryList) {
        if (StringUtils.isBlank(countryList)) {
            return countryList;
        }
        String[] values = countryList.split(",");
        List<String> normalized = new ArrayList<>(values.length);
        for (String value : values) {
            String countryId = normalizeCountryValue(value);
            if (StringUtils.isNotBlank(countryId)) {
                normalized.add(countryId);
            }
        }
        return normalized.isEmpty() ? null : String.join(",", normalized);
    }

    private String normalizeCountryValue(String countryValue) {
        if (StringUtils.isBlank(countryValue)) {
            return countryValue;
        }
        String value = countryValue.trim();
        try {
            Country byId = countryMapper.selectById(value);
            if (byId != null) {
                return byId.getId();
            }
            Country byName = countryMapper.findByEnName(value);
            if (byName != null) {
                return byName.getId();
            }
            Country byZhName = countryMapper.findByZhName(value);
            if (byZhName != null) {
                return byZhName.getId();
            }
            Country byCode = countryMapper.findByCode(value);
            if (byCode != null) {
                return byCode.getId();
            }
        } catch (Exception e) {
            log.warn("Normalize quotation country failed, value={}, err={}", value, e.getMessage());
        }
        return value;
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
