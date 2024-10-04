package org.jeecg.modules.business.controller.admin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.SkuCriteriaValue;
import org.jeecg.modules.business.service.ISkuCriteriaValueService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: SKU criteria value
 * @Author: jeecg-boot
 * @Date:   2024-08-28
 * @Version: V1.0
 */
@Api(tags="SKU criteria value")
@RestController
@RequestMapping("/skuCriteriaValue")
@Slf4j
public class SkuCriteriaValueController extends JeecgController<SkuCriteriaValue, ISkuCriteriaValueService> {
	@Autowired
	private ISkuCriteriaValueService skuCriteriaValueService;
	
	/**
	 * 分页列表查询
	 *
	 * @param skuCriteriaValue
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "SKU criteria value-分页列表查询")
	@ApiOperation(value="SKU criteria value-分页列表查询", notes="SKU criteria value-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SkuCriteriaValue>> queryPageList(SkuCriteriaValue skuCriteriaValue,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SkuCriteriaValue> queryWrapper = QueryGenerator.initQueryWrapper(skuCriteriaValue, req.getParameterMap());
		Page<SkuCriteriaValue> page = new Page<SkuCriteriaValue>(pageNo, pageSize);
		IPage<SkuCriteriaValue> pageList = skuCriteriaValueService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param skuCriteriaValue
	 * @return
	 */
	@AutoLog(value = "SKU criteria value-添加")
	@ApiOperation(value="SKU criteria value-添加", notes="SKU criteria value-添加")
	@RequiresPermissions("business:sku_criteria_value:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SkuCriteriaValue skuCriteriaValue) {
		skuCriteriaValueService.save(skuCriteriaValue);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param skuCriteriaValue
	 * @return
	 */
	@AutoLog(value = "SKU criteria value-编辑")
	@ApiOperation(value="SKU criteria value-编辑", notes="SKU criteria value-编辑")
	@RequiresPermissions("business:sku_criteria_value:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SkuCriteriaValue skuCriteriaValue) {
		skuCriteriaValueService.updateById(skuCriteriaValue);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "SKU criteria value-通过id删除")
	@ApiOperation(value="SKU criteria value-通过id删除", notes="SKU criteria value-通过id删除")
	@RequiresPermissions("business:sku_criteria_value:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		skuCriteriaValueService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "SKU criteria value-批量删除")
	@ApiOperation(value="SKU criteria value-批量删除", notes="SKU criteria value-批量删除")
	@RequiresPermissions("business:sku_criteria_value:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.skuCriteriaValueService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "SKU criteria value-通过id查询")
	@ApiOperation(value="SKU criteria value-通过id查询", notes="SKU criteria value-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SkuCriteriaValue> queryById(@RequestParam(name="id",required=true) String id) {
		SkuCriteriaValue skuCriteriaValue = skuCriteriaValueService.getById(id);
		if(skuCriteriaValue==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(skuCriteriaValue);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param skuCriteriaValue
    */
    @RequiresPermissions("business:sku_criteria_value:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SkuCriteriaValue skuCriteriaValue) {
        return super.exportXls(request, skuCriteriaValue, SkuCriteriaValue.class, "SKU criteria value");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("business:sku_criteria_value:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SkuCriteriaValue.class);
    }
	@GetMapping(value = "/listByCriteria")
	public Result<?> listByCriteria(@RequestParam(name="criteriaId") String criteriaId) {
		List<SkuCriteriaValue> list = skuCriteriaValueService.listByCriteria(criteriaId);
		return Result.OK(list);
	}
	@GetMapping(value = "/translateValueByCriteria")
	public Result<?> translateValueByCriteria(@RequestParam(name="criteria") String criteria,
											  @RequestParam(name="field") String field,
											  @RequestParam(name="value") String value)
	{
		String translatedValue = skuCriteriaValueService.translateValueByCriteria(criteria, field, value);
		if(translatedValue == null) {
			return Result.ok(Collections.singletonList(value));
		}
		return Result.ok(Collections.singletonList(translatedValue));
	}
}
