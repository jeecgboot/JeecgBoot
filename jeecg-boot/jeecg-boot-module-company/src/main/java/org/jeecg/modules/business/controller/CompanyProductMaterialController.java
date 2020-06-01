package org.jeecg.modules.business.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.CompanyProductMaterial;
import org.jeecg.modules.business.service.ICompanyProductMaterialService;

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

 /**
 * @Description: company_product_material
 * @Author: jeecg-boot
 * @Date:   2020-06-01
 * @Version: V1.0
 */
@Api(tags="company_product_material")
@RestController
@RequestMapping("/companyProductMaterial")
@Slf4j
public class CompanyProductMaterialController extends JeecgController<CompanyProductMaterial, ICompanyProductMaterialService> {
	@Autowired
	private ICompanyProductMaterialService companyProductMaterialService;
	
	/**
	 * 分页列表查询
	 *
	 * @param companyProductMaterial
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "company_product_material-分页列表查询")
	@ApiOperation(value="company_product_material-分页列表查询", notes="company_product_material-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CompanyProductMaterial companyProductMaterial,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CompanyProductMaterial> queryWrapper = QueryGenerator.initQueryWrapper(companyProductMaterial, req.getParameterMap());
		Page<CompanyProductMaterial> page = new Page<CompanyProductMaterial>(pageNo, pageSize);
		IPage<CompanyProductMaterial> pageList = companyProductMaterialService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param companyProductMaterial
	 * @return
	 */
	@AutoLog(value = "company_product_material-添加")
	@ApiOperation(value="company_product_material-添加", notes="company_product_material-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanyProductMaterial companyProductMaterial) {
		companyProductMaterialService.save(companyProductMaterial);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param companyProductMaterial
	 * @return
	 */
	@AutoLog(value = "company_product_material-编辑")
	@ApiOperation(value="company_product_material-编辑", notes="company_product_material-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanyProductMaterial companyProductMaterial) {
		companyProductMaterialService.updateById(companyProductMaterial);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "company_product_material-通过id删除")
	@ApiOperation(value="company_product_material-通过id删除", notes="company_product_material-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		companyProductMaterialService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "company_product_material-批量删除")
	@ApiOperation(value="company_product_material-批量删除", notes="company_product_material-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.companyProductMaterialService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "company_product_material-通过id查询")
	@ApiOperation(value="company_product_material-通过id查询", notes="company_product_material-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyProductMaterial companyProductMaterial = companyProductMaterialService.getById(id);
		if(companyProductMaterial==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(companyProductMaterial);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companyProductMaterial
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyProductMaterial companyProductMaterial) {
        return super.exportXls(request, companyProductMaterial, CompanyProductMaterial.class, "company_product_material");
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
        return super.importExcel(request, response, CompanyProductMaterial.class);
    }

}
