package org.jeecg.modules.business.controller.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.CaseFormat;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.business.entity.ExtraFee;
import org.jeecg.modules.business.entity.ExtraFeeOption;
import org.jeecg.modules.business.service.IExtraFeeOptionService;
import org.jeecg.modules.business.service.IExtraFeeService;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.service.IShopService;
import org.jeecg.modules.business.vo.ExtraFeeParam;
import org.jeecg.modules.business.vo.ExtraFeeResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import static org.jeecg.common.util.SqlInjectionUtil.specialFilterContentForDictSql;

/**
 * @Description: extra fee content
 * @Author: jeecg-boot
 * @Date:   2024-11-15
 * @Version: V1.0
 */
@Api(tags="extra fee content")
@RestController
@RequestMapping("/extraFee")
@Slf4j
public class ExtraFeeController extends JeecgController<ExtraFee, IExtraFeeService> {
	@Autowired
	private IExtraFeeService extraFeeService;
	@Autowired
	private IExtraFeeOptionService extraFeeOptionService;
	@Autowired
	private IShopService shopService;
	
	/**
	 * 分页列表查询
	 *
	 * @param pageNo page number
	 * @param pageSize page size
	 * @param column column to order by
	 * @param order result order
	 * @param shop shop code
	 * @param statuses fee status
	 * @return result
	 */
	//@AutoLog(value = "extra fee content-分页列表查询")
	@ApiOperation(value="extra fee content-分页列表查询", notes="extra fee content-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ExtraFeeResult>> listWithFilters(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
												 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
												 @RequestParam(name = "column", defaultValue = "create_time") String column,
												 @RequestParam(name = "order", defaultValue = "DESC") String order,
												 @RequestParam(name = "shop", required = false) String shop,
												 @RequestParam(name = "status", required = false) String statuses
	) {
		String parsedColumn = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, column.replace("_dictText", ""));
		String parsedOrder = order.toUpperCase();
		if(!parsedOrder.equals("ASC") && !parsedOrder.equals("DESC")) {
			return Result.error("Error 400 Bad Request");
		}
		try {
			specialFilterContentForDictSql(parsedColumn);
		} catch (RuntimeException e) {
			return Result.error("Error 400 Bad Request");
		}
		List<String> statusList = new ArrayList<>();
		if(statuses != null)
			statusList = Arrays.asList(statuses.split(","));
		String status = null;
		if(statusList.size() == 1) {
			status = statusList.get(0);
		}
		int total = extraFeeService.countAllFees(shop, status);
		List<ExtraFeeResult> extraFeeResults = extraFeeService.listWithFilters(shop, status, pageNo, pageSize, parsedColumn, parsedOrder);

		IPage<ExtraFeeResult> page = new Page<>();
		page.setRecords(extraFeeResults);
		page.setCurrent(pageNo);
		page.setSize(pageSize);
		page.setTotal(total);

		return Result.OK(page);
	}
	
	/**
	 *   添加
	 *
	 * @param extraFee
	 * @return
	 */
	@AutoLog(value = "extra fee content-添加")
	@ApiOperation(value="extra fee content-添加", notes="extra fee content-添加")
	@RequiresPermissions("business:extra_fee:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ExtraFee extraFee) {
		extraFeeService.save(extraFee);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param extraFee
	 * @return
	 */
	@AutoLog(value = "extra fee content-编辑")
	@ApiOperation(value="extra fee content-编辑", notes="extra fee content-编辑")
	@RequiresPermissions("business:extra_fee:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ExtraFee extraFee) {
		extraFeeService.updateById(extraFee);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "extra fee content-通过id删除")
	@ApiOperation(value="extra fee content-通过id删除", notes="extra fee content-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id") String id) {
		ExtraFee extraFee = extraFeeService.getById(id);
		if(extraFee == null) {
			return Result.error(404,"Fee not found");
		}
		if(extraFee.getInvoiceNumber() != null) {
			return Result.error(403, "Cannot delete invoiced fee");
		}
		extraFeeService.removeById(id);
		return Result.OK();
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "extra fee content-批量删除")
	@ApiOperation(value="extra fee content-批量删除", notes="extra fee content-批量删除")
	@RequiresPermissions("business:extra_fee:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.extraFeeService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "extra fee content-通过id查询")
	@ApiOperation(value="extra fee content-通过id查询", notes="extra fee content-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ExtraFee> queryById(@RequestParam(name="id",required=true) String id) {
		ExtraFee extraFee = extraFeeService.getById(id);
		if(extraFee==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(extraFee);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param extraFee
    */
    @RequiresPermissions("business:extra_fee:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ExtraFee extraFee) {
        return super.exportXls(request, extraFee, ExtraFee.class, "extra fee content");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("business:extra_fee:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ExtraFee.class);
    }

	@PostMapping(value = "/create")
	public Result<?> create(@RequestBody ExtraFeeParam feeParam) {
		String autresOptionName = "Autres";
		ExtraFeeOption option = extraFeeOptionService.getById(feeParam.getOptionId());
		String shopId = shopService.getIdByCode(feeParam.getShop());
		if(shopId == null) {
			return Result.error(404, "Shop not found");
		}
		if(option == null) {
			return Result.error(404, "Option not found");
		}
		ExtraFeeOption otherOption = extraFeeOptionService.getByName(autresOptionName);
		if(option.getId().equals(otherOption.getId())) {
			if(feeParam.getDescription().isEmpty())
				return Result.error(403, "Description is empty");
		} else {
			feeParam.setDescription(null);
		}
		ExtraFee fee = new ExtraFee();
		fee.setShop_id(shopId);
		fee.setOptionId(feeParam.getOptionId());
		fee.setDescription(feeParam.getDescription());
		fee.setQuantity(feeParam.getQuantity());
		fee.setUnitPrice(feeParam.getUnitPrice());
		extraFeeService.save(fee);
		return Result.ok();
	}

	@PostMapping(value = "/update")
	public Result<?> update(@RequestBody ExtraFeeParam feeParam) throws Exception {
		extraFeeService.updateFee(feeParam);
		return Result.ok();
	}
}
