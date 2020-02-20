package org.jeecg.modules.schoolAround.xxzbzdry.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.schoolAround.xxzbzdry.entity.ZzXxzbzdry;
import org.jeecg.modules.schoolAround.xxzbzdry.service.IZzXxzbzdryService;
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
 * @Description: 学校周边重点人员
 * @Author: jeecg-boot
 * @Date:   2020-02-19
 * @Version: V1.0
 */
@Slf4j
@Api(tags="学校周边重点人员")
@RestController
@RequestMapping("/xxzbzdry/zzXxzbzdry")
public class ZzXxzbzdryController extends JeecgController<ZzXxzbzdry, IZzXxzbzdryService> {
	@Autowired
	private IZzXxzbzdryService zzXxzbzdryService;

	/**
	 * 分页列表查询
	 *
	 * @param zzXxzbzdry
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "学校周边重点人员-分页列表查询")
	@ApiOperation(value="学校周边重点人员-分页列表查询", notes="学校周边重点人员-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzXxzbzdry>> queryPageList(ZzXxzbzdry zzXxzbzdry,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzXxzbzdry> queryWrapper = QueryGenerator.initQueryWrapper(zzXxzbzdry, req.getParameterMap());
		Page<ZzXxzbzdry> page = new Page<ZzXxzbzdry>(pageNo, pageSize);
		IPage<ZzXxzbzdry> pageList = zzXxzbzdryService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzXxzbzdry
	 * @return
	 */
	@AutoLog(value = "学校周边重点人员-添加")
	@ApiOperation(value="学校周边重点人员-添加", notes="学校周边重点人员-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzXxzbzdry zzXxzbzdry) {
		zzXxzbzdryService.save(zzXxzbzdry);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzXxzbzdry
	 * @return
	 */
	@AutoLog(value = "学校周边重点人员-编辑")
	@ApiOperation(value="学校周边重点人员-编辑", notes="学校周边重点人员-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzXxzbzdry zzXxzbzdry) {
		zzXxzbzdryService.updateById(zzXxzbzdry);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "学校周边重点人员-通过id删除")
	@ApiOperation(value="学校周边重点人员-通过id删除", notes="学校周边重点人员-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzXxzbzdryService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "学校周边重点人员-批量删除")
	@ApiOperation(value="学校周边重点人员-批量删除", notes="学校周边重点人员-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzXxzbzdryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "学校周边重点人员-通过id查询")
	@ApiOperation(value="学校周边重点人员-通过id查询", notes="学校周边重点人员-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzXxzbzdry> queryById(@RequestParam(name="id",required=true) String id) {
		ZzXxzbzdry zzXxzbzdry = zzXxzbzdryService.getById(id);
		return Result.ok(zzXxzbzdry);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzXxzbzdry
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzXxzbzdry zzXxzbzdry) {
      return super.exportXls(request, zzXxzbzdry, ZzXxzbzdry.class, "学校周边重点人员");
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
      return super.importExcel(request, response, ZzXxzbzdry.class);
  }

}
