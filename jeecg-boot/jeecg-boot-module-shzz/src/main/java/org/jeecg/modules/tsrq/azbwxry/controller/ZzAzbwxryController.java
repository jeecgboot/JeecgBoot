package org.jeecg.modules.tsrq.azbwxry.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.tsrq.azbwxry.entity.ZzAzbwxry;
import org.jeecg.modules.tsrq.azbwxry.service.IZzAzbwxryService;
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
 * @Description: 艾滋病危险人员
 * @Author: jeecg-boot
 * @Date:   2020-02-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags="艾滋病危险人员")
@RestController
@RequestMapping("/azbwxry/zzAzbwxry")
public class ZzAzbwxryController extends JeecgController<ZzAzbwxry, IZzAzbwxryService> {
	@Autowired
	private IZzAzbwxryService zzAzbwxryService;

	/**
	 * 分页列表查询
	 *
	 * @param zzAzbwxry
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "艾滋病危险人员-分页列表查询")
	@ApiOperation(value="艾滋病危险人员-分页列表查询", notes="艾滋病危险人员-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzAzbwxry>> queryPageList(ZzAzbwxry zzAzbwxry,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzAzbwxry> queryWrapper = QueryGenerator.initQueryWrapper(zzAzbwxry, req.getParameterMap());
		Page<ZzAzbwxry> page = new Page<ZzAzbwxry>(pageNo, pageSize);
		IPage<ZzAzbwxry> pageList = zzAzbwxryService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzAzbwxry
	 * @return
	 */
	@AutoLog(value = "艾滋病危险人员-添加")
	@ApiOperation(value="艾滋病危险人员-添加", notes="艾滋病危险人员-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzAzbwxry zzAzbwxry) {
		zzAzbwxryService.save(zzAzbwxry);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzAzbwxry
	 * @return
	 */
	@AutoLog(value = "艾滋病危险人员-编辑")
	@ApiOperation(value="艾滋病危险人员-编辑", notes="艾滋病危险人员-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzAzbwxry zzAzbwxry) {
		zzAzbwxryService.updateById(zzAzbwxry);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "艾滋病危险人员-通过id删除")
	@ApiOperation(value="艾滋病危险人员-通过id删除", notes="艾滋病危险人员-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzAzbwxryService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "艾滋病危险人员-批量删除")
	@ApiOperation(value="艾滋病危险人员-批量删除", notes="艾滋病危险人员-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzAzbwxryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "艾滋病危险人员-通过id查询")
	@ApiOperation(value="艾滋病危险人员-通过id查询", notes="艾滋病危险人员-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzAzbwxry> queryById(@RequestParam(name="id",required=true) String id) {
		ZzAzbwxry zzAzbwxry = zzAzbwxryService.getById(id);
		return Result.ok(zzAzbwxry);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzAzbwxry
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzAzbwxry zzAzbwxry) {
      return super.exportXls(request, zzAzbwxry, ZzAzbwxry.class, "艾滋病危险人员");
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
      return super.importExcel(request, response, ZzAzbwxry.class);
  }

}
