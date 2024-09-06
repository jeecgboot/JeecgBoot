package org.jeecg.modules.business.controller.admin;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.SkuCriteriaValueCategory;
import org.jeecg.modules.business.service.ISkuCriteriaValueCategoryService;

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
 * @Description: SKU erpCode criteria value category
 * @Author: jeecg-boot
 * @Date:   2024-08-28
 * @Version: V1.0
 */
@Api(tags="SKU erpCode criteria value category")
@RestController
@RequestMapping("/skuCriteriaValueCategory")
@Slf4j
public class SkuCriteriaValueCategoryController extends JeecgController<SkuCriteriaValueCategory, ISkuCriteriaValueCategoryService> {
	@Autowired
	private ISkuCriteriaValueCategoryService skuCriteriaValueCategoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param skuCriteriaValueCategory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "SKU erpCode criteria value category-分页列表查询")
	@ApiOperation(value="SKU erpCode criteria value category-分页列表查询", notes="SKU erpCode criteria value category-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SkuCriteriaValueCategory>> queryPageList(SkuCriteriaValueCategory skuCriteriaValueCategory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SkuCriteriaValueCategory> queryWrapper = QueryGenerator.initQueryWrapper(skuCriteriaValueCategory, req.getParameterMap());
		Page<SkuCriteriaValueCategory> page = new Page<SkuCriteriaValueCategory>(pageNo, pageSize);
		IPage<SkuCriteriaValueCategory> pageList = skuCriteriaValueCategoryService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param skuCriteriaValueCategory
	 * @return
	 */
	@AutoLog(value = "SKU erpCode criteria value category-添加")
	@ApiOperation(value="SKU erpCode criteria value category-添加", notes="SKU erpCode criteria value category-添加")
	@RequiresPermissions("business:sku_criteria_value_category:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SkuCriteriaValueCategory skuCriteriaValueCategory) {
		skuCriteriaValueCategoryService.save(skuCriteriaValueCategory);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param skuCriteriaValueCategory
	 * @return
	 */
	@AutoLog(value = "SKU erpCode criteria value category-编辑")
	@ApiOperation(value="SKU erpCode criteria value category-编辑", notes="SKU erpCode criteria value category-编辑")
	@RequiresPermissions("business:sku_criteria_value_category:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SkuCriteriaValueCategory skuCriteriaValueCategory) {
		skuCriteriaValueCategoryService.updateById(skuCriteriaValueCategory);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "SKU erpCode criteria value category-通过id删除")
	@ApiOperation(value="SKU erpCode criteria value category-通过id删除", notes="SKU erpCode criteria value category-通过id删除")
	@RequiresPermissions("business:sku_criteria_value_category:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		skuCriteriaValueCategoryService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "SKU erpCode criteria value category-批量删除")
	@ApiOperation(value="SKU erpCode criteria value category-批量删除", notes="SKU erpCode criteria value category-批量删除")
	@RequiresPermissions("business:sku_criteria_value_category:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.skuCriteriaValueCategoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "SKU erpCode criteria value category-通过id查询")
	@ApiOperation(value="SKU erpCode criteria value category-通过id查询", notes="SKU erpCode criteria value category-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SkuCriteriaValueCategory> queryById(@RequestParam(name="id",required=true) String id) {
		SkuCriteriaValueCategory skuCriteriaValueCategory = skuCriteriaValueCategoryService.getById(id);
		if(skuCriteriaValueCategory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(skuCriteriaValueCategory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param skuCriteriaValueCategory
    */
    @RequiresPermissions("business:sku_criteria_value_category:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SkuCriteriaValueCategory skuCriteriaValueCategory) {
        return super.exportXls(request, skuCriteriaValueCategory, SkuCriteriaValueCategory.class, "SKU erpCode criteria value category");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("business:sku_criteria_value_category:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SkuCriteriaValueCategory.class);
    }

}
