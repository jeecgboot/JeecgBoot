package org.jeecg.modules.business.controller.admin;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.config.JeecgBaseConfig;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.entity.SkuWeight;
import org.jeecg.modules.business.model.SkuDocument;
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
import org.jeecg.modules.message.websocket.WebSocketSender;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
	 @Autowired
	 private MongoTemplate mongoTemplate;

	private static final Integer DEFAULT_NUMBER_OF_THREADS = 1;
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
	@DeleteMapping(value = "/deleteBatch")
	 public Result<String> deleteBatch(@RequestBody List<String> ids) {
		 LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		 boolean isEmployee = securityService.checkIsEmployee();
		 if (!isEmployee) {
			 log.info("User {}, tried to access /skuWeight/updateBatch but is not authorized.", sysUser.getUsername());
			 return Result.error(403,"Forbidden.");
		 }
		 List<SkuWeight> weightsToDelete = skuWeightService.listByIds(ids);
		 // delete from mysql
		 skuWeightService.removeByIds(ids);
		//update the latest SKU weight in MongoDB
		 for (SkuWeight deleted : weightsToDelete) {
			 String skuId = deleted.getSkuId();
			 Date deletedDate = deleted.getEffectiveDate();

			 Query query = new Query(Criteria.where("skuId").is(skuId));
			 SkuDocument doc = mongoTemplate.findOne(query, SkuDocument.class);

			 boolean isLatestDeleted = doc != null &&
					 doc.getLatestSkuWeight() != null &&
					 deletedDate.equals(doc.getLatestSkuWeight().getEffectiveDate());
			 if (isLatestDeleted) {
				 skuMongoService.deleteSkuWeightBySkuId(skuId);
				 List<SkuWeight> remaining = skuWeightService.list(
						 new QueryWrapper<SkuWeight>().eq("sku_id", skuId).orderByDesc("effective_date"));
				 if (!remaining.isEmpty()) {
					 skuMongoService.upsertSkuWeight(remaining.get(0));
				 }
			 }
		 }
		 log.info("Deleted SKU weights: {}", weightsToDelete);
		 return Result.OK("SKU重量批量删除成功！");
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
		  boolean isEmployee = securityService.checkIsEmployee();
		  LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		  if(!isEmployee){
			  log.info("User {}, tried to access /skuWeight/updateBatch but is not authorized.", sysUser.getUsername());
			  return Result.error(403,"Forbidden.");
		  }
		  String userId = ((LoginUser) SecurityUtils.getSubject().getPrincipal()).getId();
		  MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		  Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		  ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_NUMBER_OF_THREADS);

		  for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			  MultipartFile file = entry.getValue();

			  executor.submit(() -> {
				  try (InputStream inputStream = file.getInputStream()) {
					  Workbook workbook = file.getOriginalFilename().endsWith(".xlsx")
							  ? new XSSFWorkbook(inputStream)
							  : new HSSFWorkbook(inputStream);
					  Sheet sheet = workbook.getSheetAt(0);
					  int firstRow = sheet.getFirstRowNum();
					  int lastRow = sheet.getLastRowNum();

					  List<SkuWeight> skuWeights = new ArrayList<>();
					  Map<String, SkuWeight> skuWeightMappedByErpCode = new HashMap<>();
					  ResponsesWithMsg<String> responses = new ResponsesWithMsg<>();

					  for (int rowIndex = firstRow + 1; rowIndex <= lastRow; rowIndex++) {
						  Row row = sheet.getRow(rowIndex);
						  if (row == null) continue;

						  SkuWeight skuWeight = new SkuWeight();
						  boolean hasError = false;
						  String erpCode = null;

						  for (int cellIndex = 0; cellIndex < NUMBER_OF_SKU_EXCEL_COLUMNS; cellIndex++) {
							  Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
							  if (hasError) break;

							  if (cell.getCellType() == CellType.BLANK) {
								  responses.addFailure("Row " + (rowIndex + 1), " Cell " + cellIndex + " is blank");
								  log.error("Row {} Cell {} is blank", rowIndex + 1, cellIndex);
								  hasError = true;
								  break;
							  }

							  try {
								  switch (cellIndex) {
									  case 0: // ERP Code
										  String skuCode = cell.getStringCellValue().trim();
										  Sku sku = skuService.getByErpCode(skuCode);
										  if (sku == null || sku.getId() == null) {
											  responses.addFailure("Row " + (rowIndex + 1), " 无效ERP编码: " + skuCode);
											  log.error("Row {} is not a valid SKU", rowIndex + 1);
											  hasError = true;
											  break;
										  }
										  erpCode = skuCode;
										  skuWeight.setSkuId(sku.getId());
										  break;

									  case 1: // Weight
										  int weight;
										  if (cell.getCellType() == CellType.STRING) {
											  weight = Integer.parseInt(cell.getStringCellValue().trim());
										  } else if (cell.getCellType() == CellType.NUMERIC) {
											  weight = (int) cell.getNumericCellValue();
										  } else {
											  responses.addFailure("Row " + (rowIndex + 1),
													  "无法识别的重量类型");
											  hasError = true;
											  throw new IllegalArgumentException("无法识别的重量类型");
										  }
										  skuWeight.setWeight(weight);
										  break;

									  case 2: // Date
										  String dateStr;
										  if (cell.getCellType() == CellType.STRING) {
											  dateStr = cell.getStringCellValue().trim();
										  } else if (cell.getCellType() == CellType.NUMERIC) {
											  dateStr = formatter.format(cell.getDateCellValue());
										  } else {
											  responses.addFailure("Row " + (rowIndex + 1),
													  "日期格式错误");
											  hasError = true;
											  throw new IllegalArgumentException("日期格式错误");
										  }
										  Date effectiveDate = formatter.parse(dateStr);
										  skuWeight.setEffectiveDate(effectiveDate);
										  break;
								  }
							  } catch (Exception ex) {
								  responses.addFailure("Row " + (rowIndex + 1), " Column " + cellIndex + " error: " + ex.getMessage());
								  hasError = true;
							  }
						  }

						  if (hasError) continue;

						  log.info("Querying sku_weight with sku_id = {}", skuWeight.getSkuId());
						  QueryWrapper<SkuWeight> wrapper = new QueryWrapper<>();
						  wrapper.eq("sku_id", skuWeight.getSkuId());
						  List<SkuWeight> existingWeight = skuWeightService.list(wrapper);
						  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						  String dateStr = sdf.format(skuWeight.getEffectiveDate());

						  boolean sameDateExists = existingWeight.stream()
								  .anyMatch(e -> sdf.format(e.getEffectiveDate()).equals(dateStr));
						  if (sameDateExists) {
							  responses.addSuccess("Row " + (rowIndex + 1), " 已存在相同SKU和日期，跳过");
							  log.info("Row {} 已存在相同SKU和日期，跳过", rowIndex + 1);
							  log.info("Row {} has existing weight with same date for SKU {}, skipping",
									  rowIndex + 1, skuWeight.getSkuId());
							  continue;
						  }

						  boolean sameContentExists = existingWeight.stream()
								  .anyMatch(e -> e.getWeight() == skuWeight.getWeight());
						  if (sameContentExists) {
							  responses.addSuccess("Row " + (rowIndex + 1), " 内容一致，仅日期不同，跳过");
							  log.info("Row {} has existing weight with same content but different date for SKU {}, skipping",
									  rowIndex + 1, skuWeight.getSkuId());
							  continue;
						  }

						  skuWeights.add(skuWeight);
						  skuWeightMappedByErpCode.put(erpCode, skuWeight);
						  responses.addSuccess("Row " + (rowIndex + 1),
								  erpCode + " 成功解析并导入");
					  }
					  if (!skuWeights.isEmpty()) {
						  //step 1: Save to MySQL and MongoDB
						  skuWeightService.saveBatch(skuWeights);
						  skuWeights.forEach(skuWeight -> skuMongoService.upsertSkuWeight(skuWeight));
						  log.info("Database import completed, {} records processed", skuWeights.size());

						  //step 2: Notify frontend about the import result
						  JSONObject detailMsg = new JSONObject();
						  detailMsg.put("cmd", "import-result");
						  detailMsg.put("msgTxt", "SKU重量导入数据库完成，正在同步马帮...");

						  detailMsg.put("data", new JSONObject() {{
							  put("successes", responses.getSuccesses());
							  put("failures", responses.getFailures());
						  }});

						  log.info("import-result 消息体: {}", detailMsg.toJSONString());
						  log.info("import-result message:{} ", detailMsg.toJSONString());
						  WebSocketSender.sendToUser(userId, detailMsg.toJSONString());

						  //step 3: Asynchronously call Mabang API
						  executor.submit(() -> {
							  try {
								  ResponsesWithMsg<String> mabangResponses = skuListMabangService.mabangSkuWeightUpdate(skuWeights);

								  List<SkuWeight> matchedSuccesses = new ArrayList<>();
								  mabangResponses.getSuccesses().forEach((skuErpCode, messages) -> {
									  SkuWeight matched = skuWeightMappedByErpCode.get(skuErpCode);
									  if (matched != null) {
										  matchedSuccesses.add(matched);
									  }
								  });
								  log.info("Mabang synchronization completed - successes: {}, failures: {}",
										  mabangResponses.getSuccesses().size(), mabangResponses.getFailures().size());
							  } catch (Exception ex) {
								  log.error("Mabang synchronization failed", ex);
							  }
						  });

					  } else {
						  log.warn("No valid SKU weights found to import, skipping");
						  JSONObject detailMsg = new JSONObject();
						  detailMsg.put("cmd", "import-result");
						  detailMsg.put("msgTxt", "未发现可导入的 SKU 权重记录");
						  detailMsg.put("data", new JSONObject() {{
							  put("successes", responses.getSuccesses());
							  put("failures", responses.getFailures());
						  }});

						  WebSocketSender.sendToUser(userId, detailMsg.toJSONString());
					  }
				  } catch (Exception e) {
					  log.error("asynchronous import failed", e);
				  }
			  });
		  }
		  return Result.OK("导入任务已提交，请稍后查看结果");
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
		String userId = ((LoginUser) SecurityUtils.getSubject().getPrincipal()).getId();
		Map<String, SkuWeight> skuWeightsMap = new HashMap<>();

		ResponsesWithMsg<String> responses = new ResponsesWithMsg<>();
		for (String weightId : param.getIds()) {
			//get SkuWeight by weightId
			SkuWeight weightRecord = skuWeightService.getById(weightId);
			if (weightRecord == null) {
				log.warn("weightId {} invalid, no corresponding SKU weight record found", weightId);
				responses.addFailure("weightId 无效: " + weightId);
				continue;
			}
			// get SKU by skuId
			String skuId = weightRecord.getSkuId();
			Sku sku = skuService.getById(skuId);
			if (sku == null) {
				log.warn("SKU not found for weightId: {}", weightId);
				responses.addFailure("SKU not found for weightId: " + weightId);
				continue;
			}
			//check if there is an existing record with the same sku_id and effective_date
			QueryWrapper<SkuWeight> wrapper = new QueryWrapper<>();
			wrapper.eq("sku_id", skuId);
			wrapper.apply("DATE(effective_date) = {0}", new SimpleDateFormat("yyyy-MM-dd").format(param.getEffectiveDate()));
			if (skuWeightService.count(wrapper) > 0) {
				log.warn("SKU {} already has a weight record for the same date, skipping", sku.getErpCode());
				responses.addSuccess(sku.getErpCode() , ": 相同日期的重量记录已存在，跳过");
				continue;
			}
			//check if there is an existing record with the same sku_id and weight but different effective_date
			wrapper = new QueryWrapper<>();
			wrapper.eq("sku_id", skuId).eq("weight", param.getWeight());
			List<SkuWeight> existing = skuWeightService.list(wrapper);
			boolean sameContentDiffDate = existing.stream()
					.anyMatch(e -> !e.getEffectiveDate().equals(param.getEffectiveDate()));
			if (sameContentDiffDate) {
				log.warn("SKU {} has the same weight but different date, skipping", sku.getErpCode());
				responses.addSuccess(sku.getErpCode() , ": 日期不同但重量相同，跳过");
				continue;
			}
			SkuWeight skuWeight = new SkuWeight();
			skuWeight.setCreateBy(sysUser.getUsername());
			skuWeight.setEffectiveDate(param.getEffectiveDate());
			skuWeight.setSkuId(skuId);
			skuWeight.setWeight(param.getWeight());
			skuWeightsMap.put(sku.getErpCode(), skuWeight);
		}
		// Submit task asynchronously
		List<SkuWeight> skuWeights = new ArrayList<>(skuWeightsMap.values());
		if (skuWeights.isEmpty()) {
			log.warn("No valid SKU weights to update, task not submitted");
			return Result.error("日期或者重量重复，没有有效的数据可更新，任务未提交");
		}
		// Update Mongo + save to database
		skuWeightService.saveBatch(skuWeights);
		skuWeights.forEach(skuMongoService::upsertSkuWeight);
		skuWeights.forEach(skuWeight -> {
			skuMongoService.upsertSkuWeight(skuWeight);
			String erpCode = skuService.getById(skuWeight.getSkuId()).getErpCode();
			responses.addSuccess(erpCode, "数据库重量已成功更新");
		});

		log.info("SKU weights updated successfully, {} records processed", skuWeights.size());
		// send WebSocket message
		JSONObject detailMsg = new JSONObject();
		detailMsg.put("cmd", "update-result");
		detailMsg.put("msgTxt", "SKU重量已更新到数据库，正在同步马帮...");
		detailMsg.put("data", new JSONObject() {{
			put("successes", responses.getSuccesses());
			put("failures", responses.getFailures());
		}});
		log.info("update-result message: {}", detailMsg.toJSONString());
		WebSocketSender.sendToUser(userId, detailMsg.toJSONString());
		// Asynchronously call Mabang API to update SKU weights
		ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_NUMBER_OF_THREADS);
		executor.submit(() -> {
			try {
				skuListMabangService.mabangSkuWeightUpdate(skuWeights);
				log.info("updateBatch call to Mabang submitted successfully");
			} catch (Exception e) {
				log.error("Asynchronous updateBatch call to Mabang failed", e);
			}
		});
		return Result.OK("Batch Edit task has been submitted, please check progress later");
	}
}
