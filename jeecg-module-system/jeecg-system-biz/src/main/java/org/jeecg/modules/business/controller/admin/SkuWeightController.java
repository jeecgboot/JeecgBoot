package org.jeecg.modules.business.controller.admin;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.entity.SkuWeight;
import org.jeecg.modules.business.mongoService.SkuMongoService;
import org.jeecg.modules.business.service.ISecurityService;
import org.jeecg.modules.business.service.ISkuListMabangService;
import org.jeecg.modules.business.service.ISkuService;
import org.jeecg.modules.business.service.ISkuWeightService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.vo.ResponsesWithMsg;
import org.jeecg.modules.business.vo.SkuWeightPage;
import org.jeecg.modules.business.vo.SkuWeightParam;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: sku_weight
 * @Author: jeecg-boot
 * @Date:   2024-08-19
 * @Version: V1.0
 */
@Api(tags="sku_weight")
@RestController
@RequestMapping("/skuWeight")
@Slf4j
public class SkuWeightController extends JeecgController<SkuWeight, ISkuWeightService> {
	@Autowired
	private ISkuListMabangService skuListMabangService;
	@Autowired
	private ISkuService skuService;
	@Autowired
	private ISkuWeightService skuWeightService;
	@Autowired
	private ISecurityService securityService;
	@Autowired
	private SkuMongoService skuMongoService;
	@Resource
	private JeecgBaseConfig jeecgBaseConfig;

	private final static Integer NUMBER_OF_SKU_EXCEL_COLUMNS = 3;
	private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
	/**
	 * 分页列表查询
	 *
	 * @param skuWeight
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "sku_weight-分页列表查询")
	@ApiOperation(value="sku_weight-分页列表查询", notes="sku_weight-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SkuWeight>> queryPageList(SkuWeight skuWeight,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SkuWeight> queryWrapper = QueryGenerator.initQueryWrapper(skuWeight, req.getParameterMap());
		Page<SkuWeight> page = new Page<SkuWeight>(pageNo, pageSize);
		IPage<SkuWeight> pageList = skuWeightService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param skuWeight
	 * @return
	 */
	@AutoLog(value = "sku_weight-添加")
	@ApiOperation(value="sku_weight-添加", notes="sku_weight-添加")
	@RequiresPermissions("business:sku_weight:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SkuWeight skuWeight) {
		skuWeightService.save(skuWeight);
		return Result.OK("添加成功！");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sku_weight-通过id删除")
	@ApiOperation(value="sku_weight-通过id删除", notes="sku_weight-通过id删除")
	@RequiresPermissions("business:sku_weight:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		skuWeightService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sku_weight-批量删除")
	@ApiOperation(value="sku_weight-批量删除", notes="sku_weight-批量删除")
	@RequiresPermissions("business:sku_weight:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.skuWeightService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "sku_weight-通过id查询")
	@ApiOperation(value="sku_weight-通过id查询", notes="sku_weight-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SkuWeight> queryById(@RequestParam(name="id",required=true) String id) {
		SkuWeight skuWeight = skuWeightService.getById(id);
		if(skuWeight==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(skuWeight);
	}

    /**
    * 导出excel
    *
    * @param skuIds
    */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(@RequestParam(value = "selections[]", required = false) List<String> skuIds) {
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		List<SkuWeightPage> skuWeightList;
		if (skuIds == null || skuIds.isEmpty()) {
			skuWeightList = skuWeightService.listLatestWeights();
		} else {
			skuWeightList = skuWeightService.listLatestWeightForSkus(skuIds);
		}
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		mv.addObject(NormalExcelConstants.FILE_NAME, "SKU重量列表");
		mv.addObject(NormalExcelConstants.CLASS, SkuWeightPage.class);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("SKU重量数据", "导出人:" + sysUser.getRealname(), "SKU重量"));
		mv.addObject(NormalExcelConstants.DATA_LIST, skuWeightList);
		return mv;
	}

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
	  @Transactional
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.info("Importing Sku weights from Excel...");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

		ResponsesWithMsg responses = new ResponsesWithMsg();
		List<SkuWeight> skuWeights = new ArrayList<>();
		Map<String, SkuWeight> skuWeightMappedByErpCode = new HashMap<>();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();
			try (InputStream inputStream = file.getInputStream()){
				Workbook workbook = new HSSFWorkbook(inputStream);
				Sheet firstSheet = workbook.getSheetAt(0);
				int firstRow = firstSheet.getFirstRowNum();
				int lastRow = firstSheet.getLastRowNum();
				for (int rowIndex = firstRow + 1; rowIndex <= lastRow; rowIndex++) {
					Row row = firstSheet.getRow(rowIndex);
					SkuWeight skuWeight = new SkuWeight();
					boolean hasError = false;
					String erpCode = null;
					for (int cellIndex = row.getFirstCellNum(); cellIndex < NUMBER_OF_SKU_EXCEL_COLUMNS; cellIndex++) {
						Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
						if(hasError) continue;
						if(cell.getCellType().equals(CellType.BLANK)){
							responses.addFailure("Row " + rowIndex + " has empty cell at index " + cellIndex);
							hasError = true;
							continue;
						}
						switch (cellIndex) {
							case 0:
								String skuCode = cell.getStringCellValue();
								Sku sku = skuService.getByErpCode(skuCode);
								if(sku == null){
									responses.addFailure("Row " + rowIndex + " SKU not found : " + skuCode);
									hasError = true;
									continue;
								}
								erpCode = skuCode;
								skuWeight.setSkuId(sku.getId());
								break;
							case 1:
								int weight;
								if(cell.getCellType().equals(CellType.STRING))
									weight = Integer.parseInt(cell.getStringCellValue());
								else if(cell.getCellType().equals(CellType.NUMERIC))
									weight = (int) cell.getNumericCellValue();
								else {
									responses.addFailure("Row " + rowIndex + " Weight is not a number - Sku : " + erpCode);
									hasError = true;
									continue;
								}
								skuWeight.setWeight(weight);
								break;
							case 2:
								String cellValue = cell.getStringCellValue();
								Date effectiveDate = formatter.parse(cellValue);
								skuWeight.setEffectiveDate(effectiveDate);
								break;
						}
					}
					if(hasError) continue;
					skuWeights.add(skuWeight);
					assert erpCode != null;
					skuWeightMappedByErpCode.put(erpCode, skuWeight);
				}
				log.info("Import weights for skus: {}", skuWeightMappedByErpCode.keySet());
				ResponsesWithMsg mabangResponses = skuListMabangService.mabangSkuWeightUpdate(skuWeights);
				List<SkuWeight> skuWeightSuccesses = new ArrayList<>();
				mabangResponses.getSuccesses().forEach((skuErpCode, messages) -> {
					skuWeightSuccesses.add(skuWeightMappedByErpCode.get(skuErpCode));
				});
				skuWeightSuccesses.forEach(skuWeight -> skuMongoService.upsertSkuWeight(skuWeight));
				skuWeightService.saveBatch(skuWeights);

				mabangResponses.getSuccesses().forEach(responses::addSuccess);
				mabangResponses.getFailures().forEach(responses::addFailure);
			} catch (Exception e) {
				e.printStackTrace();
				String msg = e.getMessage();
				log.error(msg, e);
				return Result.error("文件导入失败:" + e.getMessage());
			} finally {
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return Result.OK(responses);
    }

	 /**
	  * /!\ Not maintained use updateBatch instead.
	  * Updating weight of a SKU, creates new sku_weight entry with new effective_date and weight.
	  * @param param
	  * @return
	  */
	@PostMapping(value = "/update")
	public Result<String> updateWeight(@RequestBody SkuWeightParam param) {
		boolean isEmployee = securityService.checkIsEmployee();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(!isEmployee){
			log.info("User {}, tried to access /skuWeight/update but is not authorized.", sysUser.getUsername());
			return Result.error(403,"Forbidden.");
		}
		String skuId = param.getIds().get(0);
		Sku sku = skuService.getById(skuId);
		if(sku == null){
			return Result.error(404,"SKU not found.");
		}
		SkuWeight skuWeight = new SkuWeight();
		skuWeight.setCreateBy(sysUser.getUsername());
		skuWeight.setEffectiveDate(new Date());
		skuWeight.setSkuId(skuId);
		skuWeightService.save(skuWeight);
		return Result.OK("data.invoice.effectiveDate");
	}
	 /**
	  * Updating weight of multiple SKUs, creates new sku_weight entries with new effective_date and weight.
	  * Updates the weight in Mabang.
	  * Updates the weight in MongoDB.
	  * @param param
	  * @return
	  */
	@Transactional
	@PostMapping(value = "/updateBatch")
	public Result<?> updateBatch(@RequestBody SkuWeightParam param) {
		boolean isEmployee = securityService.checkIsEmployee();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(!isEmployee){
			log.info("User {}, tried to access /skuWeight/updateBatch but is not authorized.", sysUser.getUsername());
			return Result.error(403,"Forbidden.");
		}
		Map<String, SkuWeight> skuWeightsMap = new HashMap<>();
		for(String skuId : param.getIds()){
			Sku sku = skuService.getById(skuId);
			if(sku == null){
				return Result.error(404,"SKU not found : " + skuId);
			}
			SkuWeight skuWeight = new SkuWeight();
			skuWeight.setCreateBy(sysUser.getUsername());
			skuWeight.setEffectiveDate(param.getEffectiveDate());
			skuWeight.setSkuId(skuId);
			skuWeight.setWeight(param.getWeight());
			skuWeightsMap.put(sku.getErpCode(), skuWeight);
		}
		List<SkuWeight> skuWeights = new ArrayList<>(skuWeightsMap.values());
		ResponsesWithMsg responses = skuListMabangService.mabangSkuWeightUpdate(skuWeights);
		List<SkuWeight> skuWeightSuccesses = new ArrayList<>();
		responses.getSuccesses().forEach((skuErpCode, messages) -> {
			skuWeightSuccesses.add(skuWeightsMap.get(skuErpCode));
		});

		skuWeightSuccesses.forEach(skuWeight -> skuMongoService.upsertSkuWeight(skuWeight));
		skuWeightService.saveBatch(skuWeights);
		return Result.OK(responses);
	}
}
