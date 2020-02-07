package org.jeecg.modules.tsrq.xmsfry.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.tsrq.xmsfry.entity.ZzXmsfry;
import org.jeecg.modules.tsrq.xmsfry.service.IZzXmsfryService;
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
 * @Description: 刑满释放人员
 * @Author: jeecg-boot
 * @Date:   2020-02-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags="刑满释放人员")
@RestController
@RequestMapping("/xmsfry/zzXmsfry")
public class ZzXmsfryController extends JeecgController<ZzXmsfry, IZzXmsfryService> {
	@Autowired
	private IZzXmsfryService zzXmsfryService;

	/**
	 * 分页列表查询
	 *
	 * @param zzXmsfry
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "刑满释放人员-分页列表查询")
	@ApiOperation(value="刑满释放人员-分页列表查询", notes="刑满释放人员-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzXmsfry>> queryPageList(ZzXmsfry zzXmsfry,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzXmsfry> queryWrapper = QueryGenerator.initQueryWrapper(zzXmsfry, req.getParameterMap());
		Page<ZzXmsfry> page = new Page<ZzXmsfry>(pageNo, pageSize);
		IPage<ZzXmsfry> pageList = zzXmsfryService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzXmsfry
	 * @return
	 */
	@AutoLog(value = "刑满释放人员-添加")
	@ApiOperation(value="刑满释放人员-添加", notes="刑满释放人员-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzXmsfry zzXmsfry) {
		zzXmsfryService.save(zzXmsfry);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzXmsfry
	 * @return
	 */
	@AutoLog(value = "刑满释放人员-编辑")
	@ApiOperation(value="刑满释放人员-编辑", notes="刑满释放人员-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzXmsfry zzXmsfry) {
		zzXmsfryService.updateById(zzXmsfry);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "刑满释放人员-通过id删除")
	@ApiOperation(value="刑满释放人员-通过id删除", notes="刑满释放人员-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzXmsfryService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "刑满释放人员-批量删除")
	@ApiOperation(value="刑满释放人员-批量删除", notes="刑满释放人员-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzXmsfryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "刑满释放人员-通过id查询")
	@ApiOperation(value="刑满释放人员-通过id查询", notes="刑满释放人员-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzXmsfry> queryById(@RequestParam(name="id",required=true) String id) {
		ZzXmsfry zzXmsfry = zzXmsfryService.getById(id);
		return Result.ok(zzXmsfry);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzXmsfry
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzXmsfry zzXmsfry) {
      return super.exportXls(request, zzXmsfry, ZzXmsfry.class, "刑满释放人员");
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
      return super.importExcel(request, response, ZzXmsfry.class);
  }

}
