package org.jeecg.modules.business.controller.admin;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.BillingOptions;
import org.jeecg.modules.business.service.IBillingOptionsService;

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
 * @Description: billing options
 * @Author: jeecg-boot
 * @Date:   2024-10-23
 * @Version: V1.0
 */
@Api(tags="billing options")
@RestController
@RequestMapping("/billingOptions")
@Slf4j
public class BillingOptionsController extends JeecgController<BillingOptions, IBillingOptionsService> {
	@Autowired
	private IBillingOptionsService billingOptionsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param billingOptions
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "billing options-分页列表查询")
	@ApiOperation(value="billing options-分页列表查询", notes="billing options-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<BillingOptions>> queryPageList(BillingOptions billingOptions,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<BillingOptions> queryWrapper = QueryGenerator.initQueryWrapper(billingOptions, req.getParameterMap());
		Page<BillingOptions> page = new Page<BillingOptions>(pageNo, pageSize);
		IPage<BillingOptions> pageList = billingOptionsService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param billingOptions
	 * @return
	 */
	@AutoLog(value = "billing options-添加")
	@ApiOperation(value="billing options-添加", notes="billing options-添加")
	@RequiresPermissions("business:billing_options:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody BillingOptions billingOptions) {
		billingOptionsService.save(billingOptions);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param billingOptions
	 * @return
	 */
	@AutoLog(value = "billing options-编辑")
	@ApiOperation(value="billing options-编辑", notes="billing options-编辑")
	@RequiresPermissions("business:billing_options:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody BillingOptions billingOptions) {
		billingOptionsService.updateById(billingOptions);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "billing options-通过id删除")
	@ApiOperation(value="billing options-通过id删除", notes="billing options-通过id删除")
	@RequiresPermissions("business:billing_options:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		billingOptionsService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "billing options-批量删除")
	@ApiOperation(value="billing options-批量删除", notes="billing options-批量删除")
	@RequiresPermissions("business:billing_options:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.billingOptionsService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "billing options-通过id查询")
	@ApiOperation(value="billing options-通过id查询", notes="billing options-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<BillingOptions> queryById(@RequestParam(name="id",required=true) String id) {
		BillingOptions billingOptions = billingOptionsService.getById(id);
		if(billingOptions==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(billingOptions);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param billingOptions
    */
    @RequiresPermissions("business:billing_options:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BillingOptions billingOptions) {
        return super.exportXls(request, billingOptions, BillingOptions.class, "billing options");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("business:billing_options:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, BillingOptions.class);
    }

}
