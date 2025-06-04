package org.jeecg.modules.business.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.entity.SkuPrice;
import org.jeecg.modules.business.model.SkuDocument;
import org.jeecg.modules.business.mongoService.SkuMongoService;
import org.jeecg.modules.business.service.ICurrencyService;
import org.jeecg.modules.business.service.ISecurityService;
import org.jeecg.modules.business.service.ISkuPriceService;
import org.jeecg.modules.business.service.ISkuService;
import org.jeecg.modules.business.util.DateUtils;
import org.jeecg.modules.business.vo.ResponsesWithMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @Description: sku_price
 * @Author: jeecg-boot
 * @Date:   2025-05-12
 * @Version: V1.0
 */
@Api(tags="sku_price")
@RestController
@RequestMapping("/skuprice")
@Slf4j
public class SkuPriceController extends JeecgController<SkuPrice, ISkuPriceService> {
    @Autowired
    private ISkuPriceService skuPriceService;
    @Autowired
    private ISecurityService securityService;
    @Autowired
    private ISkuService skuService;
    @Autowired
    private ICurrencyService currencyService;
    @Autowired
    private SkuMongoService skuMongoService;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 分页列表查询
     *
     * @param skuPrice
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "SKU售价-分页列表查询")
    @ApiOperation(value="SKU售价-分页列表查询", notes="SKU售价-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<SkuPrice>> queryPageList(SkuPrice skuPrice,
                                                 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                 HttpServletRequest req) {
        QueryWrapper<SkuPrice> queryWrapper = QueryGenerator.initQueryWrapper(skuPrice, req.getParameterMap());
        Page<SkuPrice> page = new Page<>(pageNo, pageSize);
        IPage<SkuPrice> pageList = skuPriceService.page(page, queryWrapper);
        return Result.OK(pageList);
    }


    /**
     *   添加
     *
     * @param skuPrice
     * @return
     */
//    @AutoLog(value = "SKU售价-添加")
    @ApiOperation(value="SKU售价-添加", notes="SKU售价-添加")
//    @RequiresPermissions("skuprice:sku_price:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody SkuPrice skuPrice) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        boolean isEmployee = securityService.checkIsEmployee();
        if (!isEmployee) {
            log.warn("Unauthorized add attempt by user: {}", sysUser.getUsername());
            return Result.error(403, "无权限添加 SKU 售价数据");
        }
        //unique index on sku_id and date
        skuPrice.setDate(DateUtils.setToEuropeMorning8(skuPrice.getDate()));
        QueryWrapper<SkuPrice> wrapper = new QueryWrapper<>();
        wrapper.eq("sku_id", skuPrice.getSkuId());
        wrapper.eq("date", skuPrice.getDate());
        boolean exists = skuPriceService.count(wrapper) > 0;
        if (exists) {
            return Result.error("已存在相同 SKU 和日期的记录，不能重复添加。");
        }
        //duplicate content check(except date)
        List<SkuPrice> existingList = skuPriceService.list(new QueryWrapper<SkuPrice>()
                .eq("sku_id", skuPrice.getSkuId()));

        boolean sameContentExists = existingList.stream().anyMatch(existing ->
                existing.getPrice().compareTo(skuPrice.getPrice()) == 0 &&
                        existing.getDiscountedPrice().compareTo(skuPrice.getDiscountedPrice()) == 0 &&
                        existing.getThreshold().equals(skuPrice.getThreshold()) &&
                        existing.getCurrencyId().equals(skuPrice.getCurrencyId())
        );
        if (sameContentExists) {
            return Result.error("相同内容的售价记录已存在，仅日期不同，请勿重复添加。");
        }

        if (skuPrice.getPrice() != null) {
            skuPrice.setPrice(skuPrice.getPrice().setScale(2, RoundingMode.HALF_UP));
        }
        if (skuPrice.getDiscountedPrice() != null) {
            skuPrice.setDiscountedPrice(skuPrice.getDiscountedPrice().setScale(2, RoundingMode.HALF_UP));
        }
        skuPrice.setCreateBy(sysUser.getUsername());
        skuPrice.setCreateTime(new Date());
        skuPriceService.save(skuPrice);
        skuMongoService.upsertSkuPrice(skuPrice);
        return Result.OK("添加成功！");
    }


    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "SKU售价-批量删除")
    @ApiOperation(value="SKU售价-批量删除", notes="SKU售价-批量删除")
//    @RequiresPermissions("skuprice:sku_price:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        boolean isEmployee = securityService.checkIsEmployee();
        if (!isEmployee) {
            log.warn("Unauthorized import attempt by user: {}", sysUser.getUsername());
            return Result.error(403, "无权限执行导入操作");
        }
        List<String> idList = Arrays.asList(ids.split(","));
        List<SkuPrice> pricesToDelete = skuPriceService.listByIds(idList);
        // delete from MySQL
        skuPriceService.removeByIds(idList);
        // check if the deleted prices are the latest ones in MongoDB
        for (SkuPrice deleted : pricesToDelete) {
            String skuId = deleted.getSkuId();
            Date deletedDate = deleted.getDate();
            Query query = new Query(Criteria.where("skuId").is(skuId));
            SkuDocument doc = mongoTemplate.findOne(query, SkuDocument.class);
            boolean isLatestDeleted = doc != null
                    && doc.getLatestSkuPrice() != null
                    && deletedDate.equals(doc.getLatestSkuPrice().getDate());
            if (isLatestDeleted) {
                skuMongoService.deleteSkuPriceBySkuId(skuId);
                List<SkuPrice> remainingPrices = skuPriceService.list(
                        new QueryWrapper<SkuPrice>()
                                .eq("sku_id", skuId)
                                .orderByDesc("date")
                );
                // if there are remaining prices, update the latest price in MongoDB
                if (!remainingPrices.isEmpty()) {
                    log.info("Updating latest SKU price for SKU {} to {}", skuId, remainingPrices.get(0));
                    skuMongoService.upsertSkuPrice(remainingPrices.get(0));
                }
            }
        }
        log.info("Deleted SKU prices: {}", pricesToDelete);
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "SKU售价-通过id查询")
    @ApiOperation(value="SKU售价-通过id查询", notes="SKU售价-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<SkuPrice> queryById(@RequestParam(name="id",required=true) String id) {
        SkuPrice skuPrice = skuPriceService.getById(id);
        if(skuPrice==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(skuPrice);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param skuPrice
     */
//    @RequiresPermissions("skuprice:sku_price:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SkuPrice skuPrice) {
        return super.exportXls(request, skuPrice, SkuPrice.class, "SKU售价");
    }


    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        boolean isEmployee = securityService.checkIsEmployee();
        if (!isEmployee) {
            log.warn("Unauthorized import attempt by user: {}", sysUser.getUsername());
            return Result.error(403, "无权限执行导入操作");
        }
        log.info("Importing Sku Prices from Excel...");
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        ResponsesWithMsg<String> responses = new ResponsesWithMsg<>();
        List<SkuPrice> skuPrices = new ArrayList<>();
        //support multiple files
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            MultipartFile file = entry.getValue();
            try (InputStream inputStream = file.getInputStream()) {
                Workbook workbook = file.getOriginalFilename().endsWith(".xlsx")
                        ? new XSSFWorkbook(inputStream)
                        : new HSSFWorkbook(inputStream);
                Sheet sheet = workbook.getSheetAt(0);
                int startRow = sheet.getFirstRowNum();
                int lastRow = sheet.getLastRowNum();

                // find the first row with valid SKU
                for (; startRow <= lastRow; startRow++) {
                    Row testRow = sheet.getRow(startRow);
                    if (testRow == null) continue;
                    Cell skuCell = testRow.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    String skuCode = skuCell.getStringCellValue().trim();
                    if (!skuCode.isEmpty() && skuService.getByErpCode(skuCode) != null) {
                        break;
                    }
                }
                for (int rowIndex = startRow; rowIndex <= lastRow; rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    if (row == null) continue;
                    SkuPrice skuPrice = new SkuPrice();
                    boolean hasError = false;
                    String erpCode = null;

                    for (int cellIndex = 0; cellIndex <= 5; cellIndex++) {
                        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                        try {
                            switch (cellIndex) {
                                case 0: // SKU
                                    erpCode = cell.getStringCellValue().trim();
                                    Sku sku = skuService.getByErpCode(erpCode);
                                    if (sku == null) {
                                        responses.addFailure("Row " + rowIndex + ": Invalid SKU: " + erpCode);
                                        hasError = true;
                                    } else {
                                        skuPrice.setSkuId(sku.getId());
                                    }
                                    break;
                                case 1: // Price
                                    BigDecimal price;
                                    if (cell.getCellType() == CellType.BLANK) {
                                        price = null;
                                    } else if (cell.getCellType() == CellType.NUMERIC) {
                                        price = BigDecimal.valueOf(cell.getNumericCellValue()).setScale(2, RoundingMode.UP);
                                    } else {
                                        price = new BigDecimal(cell.getStringCellValue().trim()).setScale(2, RoundingMode.UP);
                                    }
                                    skuPrice.setPrice(price);
                                    break;
                                case 2: // Threshold
                                    Integer threshold;
                                    if (cell.getCellType() == CellType.BLANK) {
                                        threshold = null;
                                    } else if (cell.getCellType() == CellType.NUMERIC) {
                                        threshold = (int) cell.getNumericCellValue();
                                    } else {
                                        threshold = Integer.parseInt(cell.getStringCellValue().trim());
                                    }
                                    skuPrice.setThreshold(threshold);
                                    break;
                                case 3: // Discounted Price
                                    BigDecimal discountedPrice;
                                    if (cell.getCellType() == CellType.BLANK) {
                                        discountedPrice = null;
                                    } else if (cell.getCellType() == CellType.NUMERIC) {
                                        discountedPrice = BigDecimal.valueOf(cell.getNumericCellValue()).setScale(2, RoundingMode.UP);
                                    } else {
                                        discountedPrice = new BigDecimal(cell.getStringCellValue().trim()).setScale(2, RoundingMode.UP);
                                    }
                                    skuPrice.setDiscountedPrice(discountedPrice);
                                    break;
                                case 4: // Date
                                    Date effectiveDate;
                                    if (cell.getCellType() == CellType.NUMERIC) {
                                        effectiveDate = cell.getDateCellValue();
                                        effectiveDate = DateUtils.setToEuropeMorning8(effectiveDate);
                                        log.info("Effective date: {}", effectiveDate);
                                    } else {
                                        effectiveDate = new SimpleDateFormat("yyyy-MM-dd").parse(cell.getStringCellValue().trim());
                                    }
                                    skuPrice.setDate(effectiveDate);
                                    break;
                                case 5: // Currency
                                    String currencyCode = cell.getStringCellValue().trim();
                                    String currencyId = currencyService.getIdByCode(currencyCode);
                                    if (currencyId == null) {
                                        responses.addFailure("Row " + (rowIndex+1), ": 无效币种代码: " + currencyCode);
                                        hasError = true;
                                    } else {
                                        skuPrice.setCurrencyId(currencyId);
                                    }
                                    break;
                            }
                        } catch (Exception ex) {
                            responses.addFailure("Row " + (rowIndex+1), " Failure at column " + cellIndex + ": " + ex.getMessage());
                            hasError = true;
                            break;
                        }
                    }
                    if (!hasError) {
                        QueryWrapper<SkuPrice> wrapper = new QueryWrapper<>();
                        wrapper.eq("sku_id", skuPrice.getSkuId());
                        List<SkuPrice> existingPrices = skuPriceService.list(wrapper);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String newDateStr = sdf.format(skuPrice.getDate());
                        boolean sameSkuAndDateExists = existingPrices.stream()
                                .anyMatch(existing -> sdf.format(existing.getDate()).equals(newDateStr));
                        if (sameSkuAndDateExists) {
                            responses.addSuccess("Row " + (rowIndex+1), ": 已存在相同 SKU + 日期，跳过导入");
                            continue;
                        }
                        boolean sameContentExists = existingPrices.stream().anyMatch(existing ->
                                existing.getPrice().compareTo(skuPrice.getPrice()) == 0 &&
                                        existing.getDiscountedPrice().compareTo(skuPrice.getDiscountedPrice()) == 0 &&
                                        existing.getThreshold().equals(skuPrice.getThreshold()) &&
                                        existing.getCurrencyId().equals(skuPrice.getCurrencyId())
                        );
                        if (sameContentExists) {
                            responses.addSuccess("Row " + (rowIndex+1), ": 与已有记录内容相同，仅日期不同，跳过导入");
                            continue;
                        }
                        skuPrices.add(skuPrice);
                        skuMongoService.upsertSkuPrice(skuPrice);
                        responses.addSuccess("Row " + (rowIndex+1), ": 导入成功。ERP: " + erpCode);
                    }
                }
                if (skuPrices.isEmpty()) {
                    Result<Object> result = Result.error("导入失败：未找到任何有效数据行。");
                    result.setResult(responses);
                    return result;
                }
                skuPriceService.saveBatch(skuPrices);
            } catch (Exception e) {
                log.error("导入失败", e);
                return Result.error("导入失败：" + e.getMessage());
            }
        }
        return Result.OK(responses);
    }
}



