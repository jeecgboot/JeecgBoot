package org.jeecg.modules.business.controller.admin;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.SkuCategory;
import org.jeecg.modules.business.entity.SkuSubcategory;
import org.jeecg.modules.business.service.ISkuCategoryService;
import org.jeecg.modules.business.service.ISkuSubcategoryService;

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
 * @Description: sku erpCode sub category
 * @Author: jeecg-boot
 * @Date:   2024-08-28
 * @Version: V1.0
 */
@Api(tags="sku erpCode sub category")
@RestController
@RequestMapping("/skuSubcategory")
@Slf4j
public class SkuSubcategoryController extends JeecgController<SkuSubcategory, ISkuSubcategoryService> {
	@Autowired
	private ISkuSubcategoryService skuSubcategoryService;
	@Autowired
	private ISkuCategoryService skuCategoryService;
	/**
	 * 分页列表查询
	 *
	 * @param skuSubcategory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "sku erpCode sub category-分页列表查询")
	@ApiOperation(value="sku erpCode sub category-分页列表查询", notes="sku erpCode sub category-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SkuSubcategory>> queryPageList(SkuSubcategory skuSubcategory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SkuSubcategory> queryWrapper = QueryGenerator.initQueryWrapper(skuSubcategory, req.getParameterMap());
		Page<SkuSubcategory> page = new Page<SkuSubcategory>(pageNo, pageSize);
		IPage<SkuSubcategory> pageList = skuSubcategoryService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param skuSubcategory
	 * @return
	 */
	@AutoLog(value = "sku erpCode sub category-添加")
	@ApiOperation(value="sku erpCode sub category-添加", notes="sku erpCode sub category-添加")
	@RequiresPermissions("business:sku_subcategory:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SkuSubcategory skuSubcategory) {
		skuSubcategoryService.save(skuSubcategory);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param skuSubcategory
	 * @return
	 */
	@AutoLog(value = "sku erpCode sub category-编辑")
	@ApiOperation(value="sku erpCode sub category-编辑", notes="sku erpCode sub category-编辑")
	@RequiresPermissions("business:sku_subcategory:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SkuSubcategory skuSubcategory) {
		skuSubcategoryService.updateById(skuSubcategory);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sku erpCode sub category-通过id删除")
	@ApiOperation(value="sku erpCode sub category-通过id删除", notes="sku erpCode sub category-通过id删除")
	@RequiresPermissions("business:sku_subcategory:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		skuSubcategoryService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sku erpCode sub category-批量删除")
	@ApiOperation(value="sku erpCode sub category-批量删除", notes="sku erpCode sub category-批量删除")
	@RequiresPermissions("business:sku_subcategory:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.skuSubcategoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "sku erpCode sub category-通过id查询")
	@ApiOperation(value="sku erpCode sub category-通过id查询", notes="sku erpCode sub category-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SkuSubcategory> queryById(@RequestParam(name="id",required=true) String id) {
		SkuSubcategory skuSubcategory = skuSubcategoryService.getById(id);
		if(skuSubcategory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(skuSubcategory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param skuSubcategory
    */
    @RequiresPermissions("business:sku_subcategory:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SkuSubcategory skuSubcategory) {
        return super.exportXls(request, skuSubcategory, SkuSubcategory.class, "sku erpCode sub category");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("business:sku_subcategory:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SkuSubcategory.class);
    }

	@GetMapping(value="/listByCategory")
	public Result<?> listByCategory(@RequestParam(name="categoryCode") String categoryCode) {
		QueryWrapper<SkuCategory> categoryQuery = new QueryWrapper<>();
		categoryQuery.eq("code", categoryCode);
		QueryWrapper<SkuSubcategory> queryWrapper = new QueryWrapper<>();
		queryWrapper.in("sku_category_id", skuCategoryService.list(categoryQuery).stream().map(SkuCategory::getId).toArray());
		List<SkuSubcategory> list = skuSubcategoryService.list(queryWrapper);
		return Result.OK(list);
	}
}
