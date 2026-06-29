package org.jeecg.modules.business.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.jeecg.modules.business.entity.Country;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.Inquiry;
import org.jeecg.modules.business.entity.LogisticChannel;
import org.jeecg.modules.business.entity.LogisticChannelPrice;
import org.jeecg.modules.business.entity.Quotation;
import org.jeecg.modules.business.mapper.CountryMapper;
import org.jeecg.modules.business.mapper.ExchangeRatesMapper;
import org.jeecg.modules.business.mapper.LogisticChannelMapper;
import org.jeecg.modules.business.mapper.LogisticChannelPriceMapper;
import org.jeecg.modules.business.mapper.QuotationMapper;
import org.jeecg.modules.business.service.IClientService;
import org.jeecg.modules.business.service.IInquiryService;
import org.jeecg.modules.business.service.IQuotationService;
import org.jeecg.modules.business.service.ISecurityService;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.enmus.ExcelType;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuotationServiceImpl extends ServiceImpl<QuotationMapper, Quotation> implements IQuotationService {
    private static final String SALES_REMARK_GROSS_WEIGHT = "GROSS_WEIGHT";
    private static final int CUSTOMER_QUOTE_DATA_START_ROW = 2;
    private static final int CUSTOMER_QUOTE_PHOTO_COLUMN = 4;
    private static final int CUSTOMER_QUOTE_PHOTO_COLUMN_WIDTH = 40 * 256;
    private static final short CUSTOMER_QUOTE_PHOTO_ROW_HEIGHT = 90 * 20;

    @Autowired
    private LogisticChannelMapper logisticChannelMapper;
    @Autowired
    private LogisticChannelPriceMapper logisticChannelPriceMapper;
    @Autowired
    private CountryMapper countryMapper;
    @Autowired
    private ExchangeRatesMapper exchangeRateMapper;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private IClientService clientService;
    @Autowired
    private IInquiryService inquiryService;
    @Value("${jeecg.path.upload}")
    private String uploadPath;

    @Override
    public IPage<Quotation> pageByStatus(Page<Quotation> page, Quotation q) {
        normalizeCountryFields(q);
        IPage<Quotation> result = baseMapper.pageByStatus(page, q);
        if (result != null && result.getRecords() != null) {
            for (Quotation record : result.getRecords()) {
                fillComputedFields(record);
            }
        }
        return result;
    }

    @Override
    public IPage<Quotation> pageByStatusForClient(Page<Quotation> page, Quotation q, String clientId) {
        normalizeCountryFields(q);
        IPage<Quotation> result = baseMapper.pageByStatusForClient(page, q, clientId);
        if (result != null && result.getRecords() != null) {
            for (Quotation record : result.getRecords()) {
                sanitizeClientVisibleFields(fillComputedFields(record));
            }
        }
        return result;
    }

    @Override
    public void exportCustomerQuotes(Quotation q, String selections, HttpServletResponse response) throws IOException {
        if (q == null) {
            q = new Quotation();
        }
        String clientId = resolveExportClientId(response);
        if (clientId == null && !securityService.checkIsEmployee()) {
            return;
        }
        List<Quotation> records = listForCustomerQuoteExport(q, parseSelections(selections), clientId);
        writeCustomerQuotesWorkbook(records, response, StringUtils.isBlank(clientId));
    }

    private List<Quotation> listForCustomerQuoteExport(Quotation q, List<String> ids, String clientId) {
        normalizeCountryFields(q);
        List<Quotation> records;
        if (ids != null && !ids.isEmpty()) {
            records = baseMapper.selectBatchIds(ids);
            records.removeIf(record -> record == null
                    || (StringUtils.isNotBlank(q.getInquiryId()) && !q.getInquiryId().equals(record.getInquiryId())));
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
        filterRecordsForClient(records, clientId);
        if (records != null) {
            for (Quotation record : records) {
                if (StringUtils.isBlank(clientId)) {
                    fillComputedFields(record);
                } else {
                    sanitizeClientVisibleFields(fillComputedFields(record));
                }
            }
        }
        return records;
    }

    private String resolveExportClientId(HttpServletResponse response) throws IOException {
        if (securityService.checkIsEmployee()) {
            return null;
        }
        Client client = clientService.getCurrentClient();
        if (client == null || StringUtils.isBlank(client.getId())) {
            response.sendError(403, "Access denied");
            return null;
        }
        return client.getId();
    }

    private void filterRecordsForClient(List<Quotation> records, String clientId) {
        if (records == null || records.isEmpty() || StringUtils.isBlank(clientId)) {
            return;
        }
        Map<String, Inquiry> inquiryMap = loadInquiryMap(records);
        records.removeIf(record -> {
            if (record == null || StringUtils.isBlank(record.getInquiryId())) {
                return true;
            }
            Inquiry inquiry = inquiryMap.get(record.getInquiryId());
            return inquiry == null || !clientId.equals(inquiry.getClientId());
        });
    }

    private Map<String, Inquiry> loadInquiryMap(List<Quotation> records) {
        Set<String> inquiryIds = new HashSet<>();
        for (Quotation record : records) {
            if (record != null && StringUtils.isNotBlank(record.getInquiryId())) {
                inquiryIds.add(record.getInquiryId());
            }
        }
        if (inquiryIds.isEmpty()) {
            return new HashMap<>();
        }
        return inquiryService.listByIds(inquiryIds).stream()
                .filter(inquiry -> inquiry != null && StringUtils.isNotBlank(inquiry.getId()))
                .collect(Collectors.toMap(Inquiry::getId, inquiry -> inquiry));
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

    private void writeCustomerQuotesWorkbook(List<Quotation> records, HttpServletResponse response, boolean includeInternalFields) throws IOException {
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
        if (includeInternalFields) {
            columns.add(new ExcelExportEntity("Sales Remark", "salesRemark"));
        }
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
                if (includeInternalFields) {
                    row.put("salesRemark", getSalesRemarkText(quotation.getSalesRemark()));
                }
                row.put("logisticsFee", quotation.getLogisticsFee());
                row.put("totalFee", quotation.getTotalFee());
                row.put("sizeRange", StringUtils.isNotBlank(quotation.getSizeRange()) ? quotation.getSizeRange() : quotation.getProductSize());
                dataList.add(row);
            }
        }

        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, columns, dataList);
        embedQuotationPhotos(workbook, records);
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
        sheet.setColumnWidth(CUSTOMER_QUOTE_PHOTO_COLUMN, Math.max(sheet.getColumnWidth(CUSTOMER_QUOTE_PHOTO_COLUMN), CUSTOMER_QUOTE_PHOTO_COLUMN_WIDTH));
    }

    private void embedQuotationPhotos(Workbook workbook, List<Quotation> records) {
        if (workbook == null || workbook.getNumberOfSheets() == 0 || records == null || records.isEmpty()) {
            return;
        }
        Sheet sheet = workbook.getSheetAt(0);
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        CreationHelper helper = workbook.getCreationHelper();
        for (int i = 0; i < records.size(); i++) {
            Quotation quotation = records.get(i);
            String photoPath = resolveFirstFilePath(quotation == null ? null : quotation.getPhoto());
            if (StringUtils.isBlank(photoPath)) {
                continue;
            }
            byte[] imageBytes = readPhotoBytes(photoPath);
            if (imageBytes == null || imageBytes.length == 0) {
                continue;
            }
            int pictureType = detectPictureType(photoPath);
            if (pictureType < 0) {
                continue;
            }
            int rowIndex = CUSTOMER_QUOTE_DATA_START_ROW + i;
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }
            row.setHeight(CUSTOMER_QUOTE_PHOTO_ROW_HEIGHT);
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(CUSTOMER_QUOTE_PHOTO_COLUMN);
            anchor.setRow1(rowIndex);
            anchor.setCol2(CUSTOMER_QUOTE_PHOTO_COLUMN + 1);
            anchor.setRow2(rowIndex + 1);
            drawing.createPicture(anchor, workbook.addPicture(imageBytes, pictureType));
            if (row.getCell(CUSTOMER_QUOTE_PHOTO_COLUMN) != null) {
                row.getCell(CUSTOMER_QUOTE_PHOTO_COLUMN).setCellValue("");
            }
        }
    }

    private String resolveFirstFilePath(String rawPath) {
        if (StringUtils.isBlank(rawPath)) {
            return null;
        }
        String[] parts = rawPath.split(",");
        for (String part : parts) {
            if (StringUtils.isNotBlank(part)) {
                return part.trim();
            }
        }
        return null;
    }

    private byte[] readPhotoBytes(String rawPath) {
        try {
            Path filePath = resolveUploadFile(rawPath);
            if (filePath == null || !Files.exists(filePath) || Files.isDirectory(filePath)) {
                log.warn("Quotation export photo not found: {}", rawPath);
                return null;
            }
            try (InputStream inputStream = Files.newInputStream(filePath)) {
                return IOUtils.toByteArray(inputStream);
            }
        } catch (Exception e) {
            log.warn("Read quotation export photo failed, path={}, err={}", rawPath, e.getMessage());
            return null;
        }
    }

    private Path resolveUploadFile(String rawPath) {
        if (StringUtils.isBlank(rawPath)) {
            return null;
        }
        String normalized = rawPath.trim().replace("/", File.separator).replace("\\", File.separator);
        Path candidate = Paths.get(normalized);
        if (candidate.isAbsolute()) {
            return candidate.normalize();
        }
        return Paths.get(uploadPath, normalized).normalize();
    }

    private int detectPictureType(String path) {
        String lower = path == null ? "" : path.toLowerCase();
        if (lower.endsWith(".png")) {
            return Workbook.PICTURE_TYPE_PNG;
        }
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
            return Workbook.PICTURE_TYPE_JPEG;
        }
        if (lower.endsWith(".dib") || lower.endsWith(".bmp")) {
            return Workbook.PICTURE_TYPE_DIB;
        }
        return -1;
    }

    private String getCountryName(String countryValue) {
        if (StringUtils.isBlank(countryValue)) {
            return countryValue;
        }
        try {
            Country country = countryMapper.selectById(countryValue);
            if (country == null) country = countryMapper.findByCode(countryValue);
            if (country == null) country = countryMapper.findByEnName(countryValue);
            if (country == null) country = countryMapper.findByZhName(countryValue);
            return country == null ? countryValue : country.getNameEn();
        } catch (Exception e) {
            log.warn("Resolve export country name failed, value={}, err={}", countryValue, e.getMessage());
            return countryValue;
        }
    }

    @Override
    public Quotation getByIdAndStatus(String id, String status) {
        return fillComputedFields(baseMapper.getByIdAndStatus(id, status));
    }

    @Override
    public Quotation getQuoteById(String id) {
        return fillComputedFields(sanitizeInquiryAssociation(getById(id)));
    }

    @Override
    public Quotation getQuoteByIdForClient(String id, String clientId) {
        return sanitizeClientVisibleFields(fillComputedFields(baseMapper.getByIdForClient(id, clientId)));
    }

    @Override
    public void prepareDirectQuote(Quotation q) {
        if (q == null) {
            throw new IllegalArgumentException("quote payload cannot be empty");
        }
        q.setId(null);
        normalizeCountryFields(q);
        if (StringUtils.isBlank(q.getCountry())) {
            throw new IllegalArgumentException("country cannot be empty");
        }
        estimateQuote(q);
        q.setStatus("0");
    }

    @Override
    public int updateQuoteFields(Quotation q) {
        normalizeCountryFields(q);
        estimateQuote(q);
        return baseMapper.updateQuoteFields(q);
    }

    @Override
    public void normalizeCountryFields(Quotation q) {
        if (q != null) {
            q.setCountry(normalizeCountryValue(q.getCountry()));
            q.setInquiryCountry(normalizeCountryValue(q.getInquiryCountry()));
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

    @Override
    public Quotation estimateQuote(Quotation q) {
        if (q == null) return null;

        Integer grossG = q.getGrossWeightG();
        Integer packG = safeInt(q.getPackWeightG());
        int expressG = isGrossWeightBilling(q.getSalesRemark())
                ? (grossG == null ? 0 : grossG)
                : (grossG == null ? 0 : grossG) + (packG == null ? 0 : packG);
        q.setExpressWeightG(expressG);

        if (q.getPurchasePriceRmb() != null || q.getDomesticShippingRmb() != null) {
            BigDecimal purchase = safeBd(q.getPurchasePriceRmb());
            BigDecimal domestic = safeBd(q.getDomesticShippingRmb());
            q.setCostRmb(purchase.add(domestic).setScale(2, RoundingMode.HALF_UP));
        }

        BigDecimal rmbToEur = getRmbToEurRate();
        boolean salePriceComputedFromMargin = false;
        BigDecimal inputMargin = null;
        if (rmbToEur != null && rmbToEur.compareTo(BigDecimal.ZERO) > 0) {
            if (q.getCostRmb() != null) {
                q.setCostEur(safeBd(q.getCostRmb()).multiply(rmbToEur).setScale(2, RoundingMode.HALF_UP));
            }
            if (shouldComputeSalePriceFromMargin(q)) {
                BigDecimal margin = normalizeMarginInput(q.getMargin());
                BigDecimal denominator = BigDecimal.ONE.subtract(margin);
                if (denominator.compareTo(BigDecimal.ZERO) > 0) {
                    q.setSalePriceRmb(safeBd(q.getCostRmb()).divide(denominator, 2, RoundingMode.UP));
                    q.setMargin(margin);
                    inputMargin = margin;
                    salePriceComputedFromMargin = true;
                }
            }
            if (q.getSalePriceRmb() != null) {
                q.setSalePriceEur(safeBd(q.getSalePriceRmb()).multiply(rmbToEur).setScale(2, RoundingMode.HALF_UP));
            }
        }

        fillLogisticsFee(q);

        if (q.getSalePriceEur() != null) {
            q.setPrixAchat(safeBd(q.getSalePriceEur()).setScale(2, RoundingMode.HALF_UP));
        }
        if (q.getSalePriceEur() != null && q.getLogisticsFee() != null) {
            q.setTotalFee(safeBd(q.getSalePriceEur()).add(safeBd(q.getLogisticsFee())).setScale(2, RoundingMode.HALF_UP));
        }
        if (q.getDeclaredValue() != null && q.getIossRate() != null) {
            q.setIossFee(safeBd(q.getDeclaredValue()).multiply(safeBd(q.getIossRate())).setScale(2, RoundingMode.HALF_UP));
        } else {
            q.setIossFee(null);
        }
        if (q.getSalePriceRmb() != null && q.getCostRmb() != null) {
            q.setProfitRmb(safeBd(q.getSalePriceRmb()).subtract(safeBd(q.getCostRmb())).setScale(2, RoundingMode.HALF_UP));
        }
        if (q.getSalePriceEur() != null && q.getCostEur() != null) {
            q.setProfitEur(safeBd(q.getSalePriceEur()).subtract(safeBd(q.getCostEur())).setScale(2, RoundingMode.HALF_UP));
        }
        if (salePriceComputedFromMargin) {
            q.setMargin(inputMargin);
        } else if (q.getProfitEur() != null && q.getSalePriceEur() != null) {
            BigDecimal saleEur = safeBd(q.getSalePriceEur());
            if (saleEur.compareTo(BigDecimal.ZERO) != 0) {
                q.setMargin(safeBd(q.getProfitEur()).divide(saleEur, 4, RoundingMode.HALF_UP));
            }
        }
        return q;
    }

    private BigDecimal getRmbToEurRate() {
        try {
            BigDecimal eurToRmb = exchangeRateMapper.getLatestExchangeRate("EUR", "RMB");
            if (eurToRmb != null && eurToRmb.compareTo(BigDecimal.ZERO) > 0) {
                return BigDecimal.ONE.divide(eurToRmb, 10, RoundingMode.HALF_UP);
            }
        } catch (Exception ex) {
            log.error("[estimateQuote] FX query failed", ex);
        }
        return null;
    }

    private void fillLogisticsFee(Quotation q) {
        if (StringUtils.isBlank(q.getLogisticChannel()) || StringUtils.isBlank(q.getCountry()) || q.getExpressWeightG() == null) {
            q.setLogisticsFee(null);
            return;
        }
        Integer weightG = q.getExpressWeightG();
        if (weightG <= 0) {
            q.setLogisticsFee(BigDecimal.ZERO);
            return;
        }
        String countryCode = null;
        try {
            Country country = countryMapper.selectById(q.getCountry());
            countryCode = country == null ? null : country.getCode();
        } catch (Exception ex) {
            log.error("[estimateQuote] country resolve failed countryId={}", q.getCountry(), ex);
        }
        try {
            LogisticChannelPrice price = logisticChannelPriceMapper.findByIdDateWeightAndCountry(
                    resolvePricingChannelId(q.getLogisticChannel()),
                    new Date(),
                    BigDecimal.valueOf(weightG),
                    countryCode
            );
            if (price == null) {
                q.setLogisticsFee(null);
                return;
            }
            BigDecimal fee = price.calculateShippingPrice(BigDecimal.valueOf(weightG))
                    .add(safeBd(price.getRegistrationFee()))
                    .add(safeBd(price.getAdditionalCost()))
                    .setScale(2, RoundingMode.UP);
            q.setLogisticsFee(fee);
        } catch (Exception ex) {
            log.error("[estimateQuote] freight query failed", ex);
            q.setLogisticsFee(null);
        }
    }

    private Quotation fillComputedFields(Quotation quotation) {
        return quotation == null ? null : estimateQuote(sanitizeInquiryAssociation(quotation));
    }

    private Quotation sanitizeInquiryAssociation(Quotation quotation) {
        if (quotation == null) {
            return null;
        }
        String inquiryId = quotation.getInquiryId();
        if (StringUtils.isBlank(inquiryId) || inquiryId.equals(quotation.getId())) {
            quotation.setInquiryId(null);
            return quotation;
        }
        Inquiry inquiry = inquiryService.getById(inquiryId);
        if (inquiry == null || StringUtils.isBlank(inquiry.getId())) {
            quotation.setInquiryId(null);
        } else {
            quotation.setInquiryId(inquiry.getId());
        }
        return quotation;
    }

    private Quotation sanitizeClientVisibleFields(Quotation quotation) {
        if (quotation == null) {
            return null;
        }
        quotation.setSalesRemark(null);
        quotation.setGrossWeightG(null);
        quotation.setPackWeightG(null);
        quotation.setExpressWeightG(null);
        quotation.setLogisticChannel(null);
        return quotation;
    }

    private String resolvePricingChannelId(String channelInput) {
        if (StringUtils.isBlank(channelInput)) {
            return null;
        }
        try {
            LogisticChannel channel = logisticChannelMapper.selectById(channelInput);
            if (channel == null) {
                return channelInput;
            }
            if (org.apache.commons.lang3.StringUtils.isNotBlank(channel.getSamePriceChannelId())) {
                return channel.getSamePriceChannelId();
            }
            return channel.getId();
        } catch (Exception e) {
            log.warn("[estimateQuote] resolvePricingChannelId failed, input={}, err={}", channelInput, e.getMessage());
            return channelInput;
        }
    }

    private String normalizeCountryValue(String countryValue) {
        if (StringUtils.isBlank(countryValue)) {
            return countryValue;
        }
        String value = countryValue.trim();
        try {
            Country byId = countryMapper.selectById(value);
            if (byId != null) return byId.getId();
            Country byName = countryMapper.findByEnName(value);
            if (byName != null) return byName.getId();
            Country byZhName = countryMapper.findByZhName(value);
            if (byZhName != null) return byZhName.getId();
            Country byCode = countryMapper.findByCode(value);
            if (byCode != null) return byCode.getId();
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

    private boolean isGrossWeightBilling(String salesRemark) {
        return SALES_REMARK_GROSS_WEIGHT.equals(salesRemark);
    }

    private BigDecimal normalizeMarginInput(BigDecimal margin) {
        if (margin == null) {
            return BigDecimal.ZERO;
        }
        if (margin.compareTo(BigDecimal.ONE) > 0) {
            return margin.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
        }
        return margin;
    }

    private boolean shouldComputeSalePriceFromMargin(Quotation q) {
        if (q == null || q.getMargin() == null || q.getCostRmb() == null) {
            return false;
        }
        if (q.getSalePriceRmb() == null) {
            return true;
        }
        BigDecimal salePriceRmb = safeBd(q.getSalePriceRmb());
        if (salePriceRmb.compareTo(BigDecimal.ZERO) <= 0) {
            return true;
        }
        BigDecimal normalizedMargin = normalizeMarginInput(q.getMargin());
        BigDecimal impliedMargin = salePriceRmb
                .subtract(safeBd(q.getCostRmb()))
                .divide(salePriceRmb, 4, RoundingMode.HALF_UP);
        return impliedMargin.compareTo(normalizedMargin.setScale(4, RoundingMode.HALF_UP)) != 0;
    }

    private String getSalesRemarkText(String salesRemark) {
        return isGrossWeightBilling(salesRemark) ? "Gross weight billing" : "Express weight billing";
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
