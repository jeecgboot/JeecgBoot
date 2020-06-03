package org.jeecg.modules.business.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.CompanyCleanProduct;
import org.jeecg.modules.business.service.ICompanyCleanProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

 /**
 * @Description: 清洁生产
 * @Author: jeecg-boot
 * @Date:   2020-06-03
 * @Version: V1.0
 */
@Api(tags="清洁生产")
@RestController
@RequestMapping("/cleanProduct/companyCleanProduct")
@Slf4j
public class CompanyCleanProductController extends JeecgController<CompanyCleanProduct, ICompanyCleanProductService> {
	@Autowired
	private ICompanyCleanProductService companyCleanProductService;
	
	/**
	 * 分页列表查询
	 *
	 * @param companyCleanProduct
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "清洁生产-分页列表查询")
	@ApiOperation(value="清洁生产-分页列表查询", notes="清洁生产-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CompanyCleanProduct companyCleanProduct,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CompanyCleanProduct> queryWrapper = QueryGenerator.initQueryWrapper(companyCleanProduct, req.getParameterMap());
		Page<CompanyCleanProduct> page = new Page<CompanyCleanProduct>(pageNo, pageSize);
		IPage<CompanyCleanProduct> pageList = companyCleanProductService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param companyCleanProduct
	 * @return
	 */
	@AutoLog(value = "清洁生产-添加")
	@ApiOperation(value="清洁生产-添加", notes="清洁生产-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanyCleanProduct companyCleanProduct) {
		companyCleanProductService.save(companyCleanProduct);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param companyCleanProduct
	 * @return
	 */
	@AutoLog(value = "清洁生产-编辑")
	@ApiOperation(value="清洁生产-编辑", notes="清洁生产-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanyCleanProduct companyCleanProduct) {
		companyCleanProductService.updateById(companyCleanProduct);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "清洁生产-通过id删除")
	@ApiOperation(value="清洁生产-通过id删除", notes="清洁生产-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		companyCleanProductService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "清洁生产-批量删除")
	@ApiOperation(value="清洁生产-批量删除", notes="清洁生产-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.companyCleanProductService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "清洁生产-通过id查询")
	@ApiOperation(value="清洁生产-通过id查询", notes="清洁生产-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyCleanProduct companyCleanProduct = companyCleanProductService.getById(id);
		if(companyCleanProduct==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(companyCleanProduct);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companyCleanProduct
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyCleanProduct companyCleanProduct) {
        return super.exportXls(request, companyCleanProduct, CompanyCleanProduct.class, "清洁生产");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, CompanyCleanProduct.class);
    }

}
