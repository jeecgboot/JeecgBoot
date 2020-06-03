package org.jeecg.modules.business.controller;

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
import org.jeecg.modules.business.entity.CompanyOnlineInfo;
import org.jeecg.modules.business.service.ICompanyOnlineInfoService;

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

 /**
 * @Description: 在线监控验收信息
 * @Author: jeecg-boot
 * @Date:   2020-06-03
 * @Version: V1.0
 */
@Api(tags="在线监控验收信息")
@RestController
@RequestMapping("/onlineInfo/companyOnlineInfo")
@Slf4j
public class CompanyOnlineInfoController extends JeecgController<CompanyOnlineInfo, ICompanyOnlineInfoService> {
	@Autowired
	private ICompanyOnlineInfoService companyOnlineInfoService;
	
	/**
	 * 分页列表查询
	 *
	 * @param companyOnlineInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "在线监控验收信息-分页列表查询")
	@ApiOperation(value="在线监控验收信息-分页列表查询", notes="在线监控验收信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CompanyOnlineInfo companyOnlineInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CompanyOnlineInfo> queryWrapper = QueryGenerator.initQueryWrapper(companyOnlineInfo, req.getParameterMap());
		Page<CompanyOnlineInfo> page = new Page<CompanyOnlineInfo>(pageNo, pageSize);
		IPage<CompanyOnlineInfo> pageList = companyOnlineInfoService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param companyOnlineInfo
	 * @return
	 */
	@AutoLog(value = "在线监控验收信息-添加")
	@ApiOperation(value="在线监控验收信息-添加", notes="在线监控验收信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanyOnlineInfo companyOnlineInfo) {
		companyOnlineInfoService.save(companyOnlineInfo);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param companyOnlineInfo
	 * @return
	 */
	@AutoLog(value = "在线监控验收信息-编辑")
	@ApiOperation(value="在线监控验收信息-编辑", notes="在线监控验收信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanyOnlineInfo companyOnlineInfo) {
		companyOnlineInfoService.updateById(companyOnlineInfo);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "在线监控验收信息-通过id删除")
	@ApiOperation(value="在线监控验收信息-通过id删除", notes="在线监控验收信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		companyOnlineInfoService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "在线监控验收信息-批量删除")
	@ApiOperation(value="在线监控验收信息-批量删除", notes="在线监控验收信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.companyOnlineInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "在线监控验收信息-通过id查询")
	@ApiOperation(value="在线监控验收信息-通过id查询", notes="在线监控验收信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyOnlineInfo companyOnlineInfo = companyOnlineInfoService.getById(id);
		if(companyOnlineInfo==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(companyOnlineInfo);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companyOnlineInfo
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyOnlineInfo companyOnlineInfo) {
        return super.exportXls(request, companyOnlineInfo, CompanyOnlineInfo.class, "在线监控验收信息");
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
        return super.importExcel(request, response, CompanyOnlineInfo.class);
    }

}
