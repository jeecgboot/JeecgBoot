package org.jeecg.modules.business.controller.admin;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.SkuCategoryCriteria;
import org.jeecg.modules.business.service.ISkuCategoryCriteriaService;

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
 * @Description: sku erpCode category criteria joint table
 * @Author: jeecg-boot
 * @Date:   2024-08-28
 * @Version: V1.0
 */
@Api(tags="sku erpCode category criteria joint table")
@RestController
@RequestMapping("/skuCategoryCriteria")
@Slf4j
public class SkuCategoryCriteriaController extends JeecgController<SkuCategoryCriteria, ISkuCategoryCriteriaService> {
	@Autowired
	private ISkuCategoryCriteriaService skuCategoryCriteriaService;
	
	/**
	 * 分页列表查询
	 *
	 * @param skuCategoryCriteria
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "sku erpCode category criteria joint table-分页列表查询")
	@ApiOperation(value="sku erpCode category criteria joint table-分页列表查询", notes="sku erpCode category criteria joint table-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SkuCategoryCriteria>> queryPageList(SkuCategoryCriteria skuCategoryCriteria,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SkuCategoryCriteria> queryWrapper = QueryGenerator.initQueryWrapper(skuCategoryCriteria, req.getParameterMap());
		Page<SkuCategoryCriteria> page = new Page<SkuCategoryCriteria>(pageNo, pageSize);
		IPage<SkuCategoryCriteria> pageList = skuCategoryCriteriaService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param skuCategoryCriteria
	 * @return
	 */
	@AutoLog(value = "sku erpCode category criteria joint table-添加")
	@ApiOperation(value="sku erpCode category criteria joint table-添加", notes="sku erpCode category criteria joint table-添加")
	@RequiresPermissions("business:sku_category_criteria:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SkuCategoryCriteria skuCategoryCriteria) {
		skuCategoryCriteriaService.save(skuCategoryCriteria);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param skuCategoryCriteria
	 * @return
	 */
	@AutoLog(value = "sku erpCode category criteria joint table-编辑")
	@ApiOperation(value="sku erpCode category criteria joint table-编辑", notes="sku erpCode category criteria joint table-编辑")
	@RequiresPermissions("business:sku_category_criteria:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SkuCategoryCriteria skuCategoryCriteria) {
		skuCategoryCriteriaService.updateById(skuCategoryCriteria);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sku erpCode category criteria joint table-通过id删除")
	@ApiOperation(value="sku erpCode category criteria joint table-通过id删除", notes="sku erpCode category criteria joint table-通过id删除")
	@RequiresPermissions("business:sku_category_criteria:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		skuCategoryCriteriaService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sku erpCode category criteria joint table-批量删除")
	@ApiOperation(value="sku erpCode category criteria joint table-批量删除", notes="sku erpCode category criteria joint table-批量删除")
	@RequiresPermissions("business:sku_category_criteria:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.skuCategoryCriteriaService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "sku erpCode category criteria joint table-通过id查询")
	@ApiOperation(value="sku erpCode category criteria joint table-通过id查询", notes="sku erpCode category criteria joint table-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SkuCategoryCriteria> queryById(@RequestParam(name="id",required=true) String id) {
		SkuCategoryCriteria skuCategoryCriteria = skuCategoryCriteriaService.getById(id);
		if(skuCategoryCriteria==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(skuCategoryCriteria);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param skuCategoryCriteria
    */
    @RequiresPermissions("business:sku_category_criteria:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SkuCategoryCriteria skuCategoryCriteria) {
        return super.exportXls(request, skuCategoryCriteria, SkuCategoryCriteria.class, "sku erpCode category criteria joint table");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("business:sku_category_criteria:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SkuCategoryCriteria.class);
    }
}
