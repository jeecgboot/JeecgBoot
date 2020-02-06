package org.jeecg.modules.ldz.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.ldz.entity.ZzLdz;
import org.jeecg.modules.ldz.service.IZzLdzService;
import java.util.Date;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

 /**
 * @Description: 楼栋长
 * @Author: jeecg-boot
 * @Date:   2020-02-06
 * @Version: V1.0
 */
@Slf4j
@Api(tags="楼栋长")
@RestController
@RequestMapping("/ldz/zzLdz")
public class ZzLdzController extends JeecgController<ZzLdz, IZzLdzService> {
	@Autowired
	private IZzLdzService zzLdzService;

	/**
	 * 分页列表查询
	 *
	 * @param zzLdz
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "楼栋长-分页列表查询")
	@ApiOperation(value="楼栋长-分页列表查询", notes="楼栋长-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzLdz>> queryPageList(ZzLdz zzLdz,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzLdz> queryWrapper = QueryGenerator.initQueryWrapper(zzLdz, req.getParameterMap());
		Page<ZzLdz> page = new Page<ZzLdz>(pageNo, pageSize);
		IPage<ZzLdz> pageList = zzLdzService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzLdz
	 * @return
	 */
	@AutoLog(value = "楼栋长-添加")
	@ApiOperation(value="楼栋长-添加", notes="楼栋长-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzLdz zzLdz) {
		zzLdzService.save(zzLdz);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzLdz
	 * @return
	 */
	@AutoLog(value = "楼栋长-编辑")
	@ApiOperation(value="楼栋长-编辑", notes="楼栋长-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzLdz zzLdz) {
		zzLdzService.updateById(zzLdz);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "楼栋长-通过id删除")
	@ApiOperation(value="楼栋长-通过id删除", notes="楼栋长-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzLdzService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "楼栋长-批量删除")
	@ApiOperation(value="楼栋长-批量删除", notes="楼栋长-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzLdzService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "楼栋长-通过id查询")
	@ApiOperation(value="楼栋长-通过id查询", notes="楼栋长-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzLdz> queryById(@RequestParam(name="id",required=true) String id) {
		ZzLdz zzLdz = zzLdzService.getById(id);
		return Result.ok(zzLdz);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzLdz
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzLdz zzLdz) {
      return super.exportXls(request, zzLdz, ZzLdz.class, "楼栋长");
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
      return super.importExcel(request, response, ZzLdz.class);
  }

}
