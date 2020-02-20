package org.jeecg.modules.clxx.clxx.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.clxx.clxx.entity.ZzClxx;
import org.jeecg.modules.clxx.clxx.service.IZzClxxService;
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

 /**
 * @Description: 车辆信息
 * @Author: jeecg-boot
 * @Date:   2020-02-20
 * @Version: V1.0
 */
@Slf4j
@Api(tags="车辆信息")
@RestController
@RequestMapping("/clxx/zzClxx")
public class ZzClxxController extends JeecgController<ZzClxx, IZzClxxService> {
	@Autowired
	private IZzClxxService zzClxxService;

	/**
	 * 分页列表查询
	 *
	 * @param zzClxx
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "车辆信息-分页列表查询")
	@ApiOperation(value="车辆信息-分页列表查询", notes="车辆信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzClxx>> queryPageList(ZzClxx zzClxx,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzClxx> queryWrapper = QueryGenerator.initQueryWrapper(zzClxx, req.getParameterMap());
		Page<ZzClxx> page = new Page<ZzClxx>(pageNo, pageSize);
		IPage<ZzClxx> pageList = zzClxxService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzClxx
	 * @return
	 */
	@AutoLog(value = "车辆信息-添加")
	@ApiOperation(value="车辆信息-添加", notes="车辆信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzClxx zzClxx) {
		zzClxxService.save(zzClxx);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzClxx
	 * @return
	 */
	@AutoLog(value = "车辆信息-编辑")
	@ApiOperation(value="车辆信息-编辑", notes="车辆信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzClxx zzClxx) {
		zzClxxService.updateById(zzClxx);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "车辆信息-通过id删除")
	@ApiOperation(value="车辆信息-通过id删除", notes="车辆信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzClxxService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "车辆信息-批量删除")
	@ApiOperation(value="车辆信息-批量删除", notes="车辆信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzClxxService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "车辆信息-通过id查询")
	@ApiOperation(value="车辆信息-通过id查询", notes="车辆信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzClxx> queryById(@RequestParam(name="id",required=true) String id) {
		ZzClxx zzClxx = zzClxxService.getById(id);
		return Result.ok(zzClxx);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzClxx
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzClxx zzClxx) {
      return super.exportXls(request, zzClxx, ZzClxx.class, "车辆信息");
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
      return super.importExcel(request, response, ZzClxx.class);
  }

}
