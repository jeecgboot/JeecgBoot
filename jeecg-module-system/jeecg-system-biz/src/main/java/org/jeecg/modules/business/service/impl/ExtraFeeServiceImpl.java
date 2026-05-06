package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.entity.ExtraFee;
import org.jeecg.modules.business.entity.ExtraFeeOption;
import org.jeecg.modules.business.entity.Shop;
import org.jeecg.modules.business.mapper.ExtraFeeMapper;
import org.jeecg.modules.business.service.IExtraFeeOptionService;
import org.jeecg.modules.business.service.IExtraFeeService;
import org.jeecg.modules.business.service.IShopService;
import org.jeecg.modules.business.vo.ExtraFeeParam;
import org.jeecg.modules.business.vo.ExtraFeeResult;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: extra fees content
 * @Author: jeecg-boot
 * @Date:   2024-11-15
 * @Version: V1.0
 */
@Service
public class ExtraFeeServiceImpl extends ServiceImpl<ExtraFeeMapper, ExtraFee> implements IExtraFeeService {
    private static final String AUTRES_OPTION_NAME = "Autres";

    @Autowired
    private ExtraFeeMapper extraFeeMapper;
    @Autowired
    private IExtraFeeOptionService extraFeeOptionService;
    @Autowired
    private IShopService shopService;

    @Override
    public void createExtraFee(ExtraFeeParam feeParam) {
        if (feeParam == null) {
            throw new IllegalArgumentException("Fee data is missing");
        }
        if (feeParam.getShop() == null || feeParam.getShop().trim().isEmpty()) {
            throw new IllegalArgumentException("Shop is empty");
        }
        if (feeParam.getOptionId() == null || feeParam.getOptionId().trim().isEmpty()) {
            throw new IllegalArgumentException("Option is empty");
        }
        if (feeParam.getQuantity() == null || feeParam.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (feeParam.getUnitPrice() == null) {
            throw new IllegalArgumentException("Unit price is empty");
        }
        if (feeParam.getUnitPrice().signum() < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        String shopCode = getLookupValue(feeParam.getShop());
        String optionId = getLookupValue(feeParam.getOptionId());
        String shopId = shopService.getIdByCode(shopCode);
        if (shopId == null) {
            throw new IllegalArgumentException("Shop not found");
        }
        ExtraFeeOption option = extraFeeOptionService.getById(optionId);
        if (option == null) {
            throw new IllegalArgumentException("Option not found");
        }
        ExtraFeeOption otherOption = extraFeeOptionService.getByName(AUTRES_OPTION_NAME);
        boolean isAutres = otherOption != null && option.getId().equals(otherOption.getId());
        if (isAutres) {
            if (feeParam.getDescription() == null || feeParam.getDescription().trim().isEmpty()) {
                throw new IllegalArgumentException("Description is empty");
            }
            feeParam.setDescription(feeParam.getDescription().trim());
        } else {
            feeParam.setDescription(null);
        }
        ExtraFee fee = new ExtraFee();
        fee.setShop_id(shopId);
        fee.setOptionId(optionId);
        fee.setDescription(feeParam.getDescription());
        fee.setQuantity(feeParam.getQuantity());
        fee.setUnitPrice(feeParam.getUnitPrice());
        save(fee);
    }

    private String getLookupValue(String value) {
        return value == null ? null : value.split("\\|", 2)[0].trim();
    }

    @Override
    public byte[] generateImportTemplate() throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("extra_fee_import");
            Sheet optionsSheet = workbook.createSheet("options");
            String[] headers = {"shop", "optionId", "quantity", "unitPrice", "description"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
                sheet.setColumnWidth(i, 20 * 256);
            }
            Row exampleRow = sheet.createRow(1);
            exampleRow.createCell(2).setCellValue(1);
            exampleRow.createCell(3).setCellValue(0.00);
            exampleRow.createCell(4).setCellValue("Required only for Autres");
            fillTemplateOptions(sheet, optionsSheet);
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void fillTemplateOptions(Sheet sheet, Sheet optionsSheet) {
        Row optionHeader = optionsSheet.createRow(0);
        optionHeader.createCell(0).setCellValue("shop options");
        optionHeader.createCell(1).setCellValue("extra fee optionId options");
        List<Shop> shops = shopService.list();
        for (int i = 0; i < shops.size(); i++) {
            Shop shop = shops.get(i);
            String label = shop.getErpCode();
            if (shop.getName() != null && !shop.getName().isEmpty()) {
                label += " | " + shop.getName();
            }
            optionsSheet.createRow(i + 1).createCell(0).setCellValue(label);
        }
        List<ExtraFeeOption> options = extraFeeOptionService.list();
        for (int i = 0; i < options.size(); i++) {
            ExtraFeeOption option = options.get(i);
            Row row = optionsSheet.getRow(i + 1);
            if (row == null) {
                row = optionsSheet.createRow(i + 1);
            }
            String label = option.getId();
            if (option.getEnName() != null && !option.getEnName().isEmpty()) {
                label += " | " + option.getEnName();
            }
            if (option.getZhName() != null && !option.getZhName().isEmpty()) {
                label += " | " + option.getZhName();
            }
            row.createCell(1).setCellValue(label);
        }
        optionsSheet.setColumnWidth(0, 35 * 256);
        optionsSheet.setColumnWidth(1, 55 * 256);
        addDropdown(sheet, 1, 500, 0, "'options'!$A$2:$A$" + Math.max(2, shops.size() + 1));
        addDropdown(sheet, 1, 500, 1, "'options'!$B$2:$B$" + Math.max(2, options.size() + 1));
    }

    private void addDropdown(Sheet sheet, int firstRow, int lastRow, int column, String formula) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint = helper.createFormulaListConstraint(formula);
        CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, column, column);
        DataValidation validation = helper.createValidation(constraint, addressList);
        validation.setSuppressDropDownArrow(true);
        validation.setShowErrorBox(true);
        sheet.addValidationData(validation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importExtraFees(List<ExtraFeeParam> feeParams) {
        List<ExtraFeeParam> safeFeeParams = feeParams == null ? new ArrayList<>() : feeParams;
        int importedCount = 0;
        for (int i = 0; i < safeFeeParams.size(); i++) {
            if (isEmptyImportRow(safeFeeParams.get(i))) {
                continue;
            }
            try {
                createExtraFee(safeFeeParams.get(i));
                importedCount++;
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Row " + (i + 2) + ": " + e.getMessage());
            }
        }
        return importedCount;
    }

    private boolean isEmptyImportRow(ExtraFeeParam feeParam) {
        return feeParam == null
                || (isBlank(feeParam.getShop())
                && isBlank(feeParam.getOptionId())
                && feeParam.getQuantity() == null
                && feeParam.getUnitPrice() == null
                && isBlank(feeParam.getDescription()));
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Override
    public List<ExtraFeeResult> listWithFilters(String shop, String status, Integer pageNo, Integer pageSize, String column, String order) {
        int offset = (pageNo - 1) * pageSize;
        return extraFeeMapper.listWithFilters(shop, status, offset, pageSize, column, order);
    }
    @Override
    public Integer countAllFees(String shop, String status) {
        return extraFeeMapper.countAllFees(shop, status);
    }

    @Override
    public void updateFee(ExtraFeeParam feeParam) throws Exception {
        ExtraFee fee = getById(feeParam.getId());
        if(fee == null) {
            throw new Exception("Fee not found");
        }
        extraFeeMapper.updateFee(feeParam.getId(), feeParam.getDescription(), feeParam.getQuantity(), feeParam.getUnitPrice());
    }

    @Override
    public List<ExtraFeeResult> findNotInvoicedByShops(List<String> shopIds) {
        return extraFeeMapper.findNotInvoicedByShops(shopIds);
    }

    @Override
    public void updateInvoiceNumberByIds(List<String> feeIds, String invoiceCode) {
        extraFeeMapper.updateInvoiceNumberByIds(feeIds, invoiceCode);
    }

    @Override
    public List<ExtraFeeResult> findByInvoiceNumber(String invoiceCode) {
        return extraFeeMapper.findByInvoiceNumber(invoiceCode);
    }

    @Override
    public void cancelInvoice(String invoiceNumber, String clientId) {
        extraFeeMapper.cancelInvoice(invoiceNumber, clientId);
    }
}
