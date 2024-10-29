package org.jeecg.modules.business.controller.admin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.SkuCategory;
import org.jeecg.modules.business.entity.SkuCategoryCriteria;
import org.jeecg.modules.business.entity.SkuCriteria;
import org.jeecg.modules.business.service.ISkuCategoryCriteriaService;
import org.jeecg.modules.business.service.ISkuCategoryService;
import org.jeecg.modules.business.service.ISkuCriteriaService;

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
 * @Description: SKU erpCode criteria
 * @Author: jeecg-boot
 * @Date:   2024-08-28
 * @Version: V1.0
 */
@Api(tags="SKU erpCode criteria")
@RestController
@RequestMapping("/skuCriteria")
@Slf4j
public class SkuCriteriaController extends JeecgController<SkuCriteria, ISkuCriteriaService> {
	@Autowired
	private ISkuCriteriaService skuCriteriaService;
	 @Autowired
	 private ISkuCategoryService skuCategoryService;
	 @Autowired
	 private ISkuCategoryCriteriaService skuCategoryCriteriaService;
	
	/**
	 * 分页列表查询
	 *
	 * @param skuCriteria
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "SKU erpCode criteria-分页列表查询")
	@ApiOperation(value="SKU erpCode criteria-分页列表查询", notes="SKU erpCode criteria-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SkuCriteria>> queryPageList(SkuCriteria skuCriteria,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SkuCriteria> queryWrapper = QueryGenerator.initQueryWrapper(skuCriteria, req.getParameterMap());
		Page<SkuCriteria> page = new Page<SkuCriteria>(pageNo, pageSize);
		IPage<SkuCriteria> pageList = skuCriteriaService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param skuCriteria
	 * @return
	 */
	@AutoLog(value = "SKU erpCode criteria-添加")
	@ApiOperation(value="SKU erpCode criteria-添加", notes="SKU erpCode criteria-添加")
	@RequiresPermissions("business:sku_criteria:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SkuCriteria skuCriteria) {
		skuCriteriaService.save(skuCriteria);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param skuCriteria
	 * @return
	 */
	@AutoLog(value = "SKU erpCode criteria-编辑")
	@ApiOperation(value="SKU erpCode criteria-编辑", notes="SKU erpCode criteria-编辑")
	@RequiresPermissions("business:sku_criteria:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SkuCriteria skuCriteria) {
		skuCriteriaService.updateById(skuCriteria);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "SKU erpCode criteria-通过id删除")
	@ApiOperation(value="SKU erpCode criteria-通过id删除", notes="SKU erpCode criteria-通过id删除")
	@RequiresPermissions("business:sku_criteria:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		skuCriteriaService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "SKU erpCode criteria-批量删除")
	@ApiOperation(value="SKU erpCode criteria-批量删除", notes="SKU erpCode criteria-批量删除")
	@RequiresPermissions("business:sku_criteria:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.skuCriteriaService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "SKU erpCode criteria-通过id查询")
	@ApiOperation(value="SKU erpCode criteria-通过id查询", notes="SKU erpCode criteria-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SkuCriteria> queryById(@RequestParam(name="id",required=true) String id) {
		SkuCriteria skuCriteria = skuCriteriaService.getById(id);
		if(skuCriteria==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(skuCriteria);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param skuCriteria
    */
    @RequiresPermissions("business:sku_criteria:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SkuCriteria skuCriteria) {
        return super.exportXls(request, skuCriteria, SkuCriteria.class, "SKU erpCode criteria");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("business:sku_criteria:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SkuCriteria.class);
    }
	 @GetMapping(value = "/listByCategory")
	 public Result<?> listByCategory(@RequestParam(name = "categoryCode") String categoryCode) {
		 return Result.OK(skuCriteriaService.listByCategoryCode(categoryCode));
	 }
}
