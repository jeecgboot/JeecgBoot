package org.jeecg.modules.business.controller.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.business.entity.Sku;
import org.jeecg.modules.business.entity.SkuWeight;
import org.jeecg.modules.business.service.ISecurityService;
import org.jeecg.modules.business.service.ISkuService;
import org.jeecg.modules.business.service.ISkuWeightService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.vo.SkuWeightParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: sku_weight
 * @Author: jeecg-boot
 * @Date:   2024-08-19
 * @Version: V1.0
 */
@Api(tags="sku_weight")
@RestController
@RequestMapping("/skuWeight")
@Slf4j
public class SkuWeightController extends JeecgController<SkuWeight, ISkuWeightService> {
	@Autowired
	private ISkuService skuService;
	@Autowired
	private ISkuWeightService skuWeightService;
	@Autowired
	private ISecurityService securityService;
	
	/**
	 * 分页列表查询
	 *
	 * @param skuWeight
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "sku_weight-分页列表查询")
	@ApiOperation(value="sku_weight-分页列表查询", notes="sku_weight-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SkuWeight>> queryPageList(SkuWeight skuWeight,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SkuWeight> queryWrapper = QueryGenerator.initQueryWrapper(skuWeight, req.getParameterMap());
		Page<SkuWeight> page = new Page<SkuWeight>(pageNo, pageSize);
		IPage<SkuWeight> pageList = skuWeightService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param skuWeight
	 * @return
	 */
	@AutoLog(value = "sku_weight-添加")
	@ApiOperation(value="sku_weight-添加", notes="sku_weight-添加")
	@RequiresPermissions("business:sku_weight:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SkuWeight skuWeight) {
		skuWeightService.save(skuWeight);
		return Result.OK("添加成功！");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "sku_weight-通过id删除")
	@ApiOperation(value="sku_weight-通过id删除", notes="sku_weight-通过id删除")
	@RequiresPermissions("business:sku_weight:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		skuWeightService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "sku_weight-批量删除")
	@ApiOperation(value="sku_weight-批量删除", notes="sku_weight-批量删除")
	@RequiresPermissions("business:sku_weight:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.skuWeightService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "sku_weight-通过id查询")
	@ApiOperation(value="sku_weight-通过id查询", notes="sku_weight-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SkuWeight> queryById(@RequestParam(name="id",required=true) String id) {
		SkuWeight skuWeight = skuWeightService.getById(id);
		if(skuWeight==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(skuWeight);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param skuWeight
    */
    @RequiresPermissions("business:sku_weight:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SkuWeight skuWeight) {
        return super.exportXls(request, skuWeight, SkuWeight.class, "sku_weight");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("business:sku_weight:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SkuWeight.class);
    }

	 /**
	  * Updating weight of multiple SKUs, creates new sku_weight entries with new effective_date and weight.
	  * @param param
	  * @return
	  */
	@PostMapping(value = "/update")
	public Result<String> updateWeight(@RequestBody SkuWeightParam param) {
		boolean isEmployee = securityService.checkIsEmployee();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(!isEmployee){
			log.info("User {}, tried to access /skuWeight/update but is not authorized.", sysUser.getUsername());
			return Result.error(403,"Forbidden.");
		}
		String skuId = param.getIds().get(0);
		Sku sku = skuService.getById(skuId);
		if(sku == null){
			return Result.error(404,"SKU not found.");
		}
		SkuWeight skuWeight = new SkuWeight();
		skuWeight.setCreateBy(sysUser.getUsername());
		skuWeight.setEffective_date(new Date());
		skuWeight.setSkuId(skuId);
		skuWeightService.save(skuWeight);
		return Result.OK("data.invoice.effectiveDate");
	}
	@PostMapping(value = "/updateBatch")
	public Result<String> updateBatch(@RequestBody SkuWeightParam param) {
		boolean isEmployee = securityService.checkIsEmployee();
		LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		if(!isEmployee){
			log.info("User {}, tried to access /skuWeight/updateBatch but is not authorized.", sysUser.getUsername());
			return Result.error(403,"Forbidden.");
		}
		List<SkuWeight> skuWeights = new ArrayList<>();
		for(String skuId : param.getIds()){
			Sku sku = skuService.getById(skuId);
			if(sku == null){
				return Result.error(404,"SKU not found : " + skuId);
			}
			SkuWeight skuWeight = new SkuWeight();
			skuWeight.setCreateBy(sysUser.getUsername());
			skuWeight.setEffective_date(param.getEffectiveDate());
			skuWeight.setSkuId(skuId);
			skuWeight.setWeight(param.getWeight());
			skuWeights.add(skuWeight);
		}
		skuWeightService.saveBatch(skuWeights);
		return Result.OK("data.invoice.effectiveDate");
	}
}
