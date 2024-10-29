package org.jeecg.modules.business.controller.admin;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.SkuCategory;
import org.jeecg.modules.business.service.ISkuCategoryService;

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
 * @Description: sku category
 * @Author: jeecg-boot
 * @Date:   2024-08-26
 * @Version: V1.0
 */
@Api(tags="sku category")
@RestController
@RequestMapping("/skuCategory")
@Slf4j
public class SkuCategoryController extends JeecgController<SkuCategory, ISkuCategoryService> {
	@Autowired
	private ISkuCategoryService skuCategoryService;
	
	/**
	 * 分页列表查询
	 *
	 * @param skuCategory
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "sku category-分页列表查询")
	@ApiOperation(value="sku category-分页列表查询", notes="sku category-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SkuCategory>> queryPageList(SkuCategory skuCategory,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SkuCategory> queryWrapper = QueryGenerator.initQueryWrapper(skuCategory, req.getParameterMap());
		Page<SkuCategory> page = new Page<SkuCategory>(pageNo, pageSize);
		IPage<SkuCategory> pageList = skuCategoryService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param skuCategory
	 * @return
	 */
	@AutoLog(value = "sku category-添加")
	@ApiOperation(value="sku category-添加", notes="sku category-添加")
	@RequiresPermissions("business:sku_category:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SkuCategory skuCategory) {
		skuCategoryService.save(skuCategory);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param skuCategory
	 * @return
	 */
	@AutoLog(value = "sku category-编辑")
	@ApiOperation(value="sku category-编辑", notes="sku category-编辑")
	@RequiresPermissions("business:sku_category:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SkuCategory skuCategory) {
		skuCategoryService.updateById(skuCategory);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sku category-通过id删除")
	@ApiOperation(value="sku category-通过id删除", notes="sku category-通过id删除")
	@RequiresPermissions("business:sku_category:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		skuCategoryService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sku category-批量删除")
	@ApiOperation(value="sku category-批量删除", notes="sku category-批量删除")
	@RequiresPermissions("business:sku_category:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.skuCategoryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "sku category-通过id查询")
	@ApiOperation(value="sku category-通过id查询", notes="sku category-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SkuCategory> queryById(@RequestParam(name="id",required=true) String id) {
		SkuCategory skuCategory = skuCategoryService.getById(id);
		if(skuCategory==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(skuCategory);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param skuCategory
    */
    @RequiresPermissions("business:sku_category:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SkuCategory skuCategory) {
        return super.exportXls(request, skuCategory, SkuCategory.class, "sku category");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("business:sku_category:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SkuCategory.class);
    }

}
