package org.jeecg.modules.business.controller.admin;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.Product;
import org.jeecg.modules.business.service.IProductService;

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
 * @Description: sku product
 * @Author: jeecg-boot
 * @Date:   2024-10-03
 * @Version: V1.0
 */
@Api(tags="sku product")
@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController extends JeecgController<Product, IProductService> {
	@Autowired
	private IProductService productService;
	
	/**
	 * 分页列表查询
	 *
	 * @param product
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "sku product-分页列表查询")
	@ApiOperation(value="sku product-分页列表查询", notes="sku product-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Product>> queryPageList(Product product,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Product> queryWrapper = QueryGenerator.initQueryWrapper(product, req.getParameterMap());
		Page<Product> page = new Page<Product>(pageNo, pageSize);
		IPage<Product> pageList = productService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param product
	 * @return
	 */
	@AutoLog(value = "sku product-添加")
	@ApiOperation(value="sku product-添加", notes="sku product-添加")
	@RequiresPermissions("business:product:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Product product) {
		productService.save(product);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param product
	 * @return
	 */
	@AutoLog(value = "sku product-编辑")
	@ApiOperation(value="sku product-编辑", notes="sku product-编辑")
	@RequiresPermissions("business:product:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Product product) {
		productService.updateById(product);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sku product-通过id删除")
	@ApiOperation(value="sku product-通过id删除", notes="sku product-通过id删除")
	@RequiresPermissions("business:product:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		productService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sku product-批量删除")
	@ApiOperation(value="sku product-批量删除", notes="sku product-批量删除")
	@RequiresPermissions("business:product:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.productService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "sku product-通过id查询")
	@ApiOperation(value="sku product-通过id查询", notes="sku product-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Product> queryById(@RequestParam(name="id",required=true) String id) {
		Product product = productService.getById(id);
		if(product==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(product);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param product
    */
    @RequiresPermissions("business:product:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Product product) {
        return super.exportXls(request, product, Product.class, "sku product");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("business:product:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Product.class);
    }

	@GetMapping(value="/listByCategory")
	public Result<?> listByCategory(@RequestParam(name="categoryCode") String categoryCode) {
		return Result.OK(productService.listByCategory(categoryCode));
	}
	@GetMapping(value="/translateProductNameByValue")
	public Result<?> translateProductNameByValue( @RequestParam(name="field") String field,
												  @RequestParam(name="categoryCode") String categoryCode,
												  @RequestParam(name="value") String value) {
		String translatedValue = productService.translateProductNameByValue(field, categoryCode, value);
		if(translatedValue == null) {
			return Result.ok(Collections.singletonList(value));
		}
		return Result.ok(Collections.singletonList(translatedValue));
	}
}
