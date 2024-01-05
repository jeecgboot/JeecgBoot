package org.jeecg.modules.business.controller.admin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.entity.Debit;
import org.jeecg.modules.business.service.IDebitService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: debit
 * @Author: jeecg-boot
 * @Date:   2023-08-23
 * @Version: V1.0
 */
@Api(tags="debit")
@RestController
@RequestMapping("/business/debit")
@Slf4j
public class DebitController extends JeecgController<Debit, IDebitService> {
	@Autowired
	private IDebitService debitService;
	
	/**
	 * 分页列表查询
	 *
	 * @param debit
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "debit-分页列表查询")
	@ApiOperation(value="debit-分页列表查询", notes="debit-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Debit>> queryPageList(Debit debit,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<Debit> queryWrapper = QueryGenerator.initQueryWrapper(debit, req.getParameterMap());
		Page<Debit> page = new Page<Debit>(pageNo, pageSize);
		IPage<Debit> pageList = debitService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param debit
	 * @return
	 */
	@AutoLog(value = "debit-添加")
	@ApiOperation(value="debit-添加", notes="debit-添加")
	@RequiresPermissions("business:debit:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Debit debit) {
		debitService.save(debit);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param debit
	 * @return
	 */
	@AutoLog(value = "debit-编辑")
	@ApiOperation(value="debit-编辑", notes="debit-编辑")
	@RequiresPermissions("business:debit:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Debit debit) {
		debitService.updateById(debit);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "debit-通过id删除")
	@ApiOperation(value="debit-通过id删除", notes="debit-通过id删除")
	@RequiresPermissions("business:debit:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		debitService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "debit-批量删除")
	@ApiOperation(value="debit-批量删除", notes="debit-批量删除")
	@RequiresPermissions("business:debit:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.debitService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "debit-通过id查询")
	@ApiOperation(value="debit-通过id查询", notes="debit-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Debit> queryById(@RequestParam(name="id",required=true) String id) {
		Debit debit = debitService.getById(id);
		if(debit==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(debit);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param debit
    */
    @RequiresPermissions("business:debit:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Debit debit) {
        return super.exportXls(request, debit, Debit.class, "debit");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("business:debit:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Debit.class);
    }

}
