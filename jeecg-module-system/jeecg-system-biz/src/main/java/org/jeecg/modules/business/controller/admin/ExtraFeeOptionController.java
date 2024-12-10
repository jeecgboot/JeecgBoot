package org.jeecg.modules.business.controller.admin;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.ExtraFeeOption;
import org.jeecg.modules.business.service.IExtraFeeOptionService;

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
 * @Description: extra fee option
 * @Author: jeecg-boot
 * @Date:   2024-11-15
 * @Version: V1.0
 */
@Api(tags="extra fee option")
@RestController
@RequestMapping("/extraFeeOption")
@Slf4j
public class ExtraFeeOptionController extends JeecgController<ExtraFeeOption, IExtraFeeOptionService> {
	@Autowired
	private IExtraFeeOptionService ExtraFeeOptionService;
	
	/**
	 * 分页列表查询
	 *
	 * @param ExtraFeeOption
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "extra fee option-分页列表查询")
	@ApiOperation(value="extra fee option-分页列表查询", notes="extra fee option-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ExtraFeeOption>> queryPageList(ExtraFeeOption ExtraFeeOption,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ExtraFeeOption> queryWrapper = QueryGenerator.initQueryWrapper(ExtraFeeOption, req.getParameterMap());
		Page<ExtraFeeOption> page = new Page<ExtraFeeOption>(pageNo, pageSize);
		IPage<ExtraFeeOption> pageList = ExtraFeeOptionService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ExtraFeeOption
	 * @return
	 */
	@AutoLog(value = "extra fee option-添加")
	@ApiOperation(value="extra fee option-添加", notes="extra fee option-添加")
	@RequiresPermissions("business:extra_fee_option:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ExtraFeeOption ExtraFeeOption) {
		ExtraFeeOptionService.save(ExtraFeeOption);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ExtraFeeOption
	 * @return
	 */
	@AutoLog(value = "extra fee option-编辑")
	@ApiOperation(value="extra fee option-编辑", notes="extra fee option-编辑")
	@RequiresPermissions("business:extra_fee_option:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ExtraFeeOption ExtraFeeOption) {
		ExtraFeeOptionService.updateById(ExtraFeeOption);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "extra fee option-通过id删除")
	@ApiOperation(value="extra fee option-通过id删除", notes="extra fee option-通过id删除")
	@RequiresPermissions("business:extra_fee_option:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		ExtraFeeOptionService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "extra fee option-批量删除")
	@ApiOperation(value="extra fee option-批量删除", notes="extra fee option-批量删除")
	@RequiresPermissions("business:extra_fee_option:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.ExtraFeeOptionService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "extra fee option-通过id查询")
	@ApiOperation(value="extra fee option-通过id查询", notes="extra fee option-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ExtraFeeOption> queryById(@RequestParam(name="id",required=true) String id) {
		ExtraFeeOption ExtraFeeOption = ExtraFeeOptionService.getById(id);
		if(ExtraFeeOption==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(ExtraFeeOption);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param ExtraFeeOption
    */
    @RequiresPermissions("business:extra_fee_option:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ExtraFeeOption ExtraFeeOption) {
        return super.exportXls(request, ExtraFeeOption, ExtraFeeOption.class, "extra fee option");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("business:extra_fee_option:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ExtraFeeOption.class);
    }

}
