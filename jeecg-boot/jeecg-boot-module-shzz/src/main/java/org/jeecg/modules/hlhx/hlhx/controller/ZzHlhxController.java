package org.jeecg.modules.hlhx.hlhx.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.hlhx.hlhx.entity.ZzHlhx;
import org.jeecg.modules.hlhx.hlhx.service.IZzHlhxService;
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
 * @Description: 护路护线
 * @Author: jeecg-boot
 * @Date:   2020-02-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags="护路护线")
@RestController
@RequestMapping("/hlhx/zzHlhx")
public class ZzHlhxController extends JeecgController<ZzHlhx, IZzHlhxService> {
	@Autowired
	private IZzHlhxService zzHlhxService;

	/**
	 * 分页列表查询
	 *
	 * @param zzHlhx
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "护路护线-分页列表查询")
	@ApiOperation(value="护路护线-分页列表查询", notes="护路护线-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzHlhx>> queryPageList(ZzHlhx zzHlhx,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzHlhx> queryWrapper = QueryGenerator.initQueryWrapper(zzHlhx, req.getParameterMap());
		Page<ZzHlhx> page = new Page<ZzHlhx>(pageNo, pageSize);
		IPage<ZzHlhx> pageList = zzHlhxService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzHlhx
	 * @return
	 */
	@AutoLog(value = "护路护线-添加")
	@ApiOperation(value="护路护线-添加", notes="护路护线-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzHlhx zzHlhx) {
		zzHlhxService.save(zzHlhx);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzHlhx
	 * @return
	 */
	@AutoLog(value = "护路护线-编辑")
	@ApiOperation(value="护路护线-编辑", notes="护路护线-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzHlhx zzHlhx) {
		zzHlhxService.updateById(zzHlhx);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "护路护线-通过id删除")
	@ApiOperation(value="护路护线-通过id删除", notes="护路护线-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzHlhxService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "护路护线-批量删除")
	@ApiOperation(value="护路护线-批量删除", notes="护路护线-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzHlhxService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "护路护线-通过id查询")
	@ApiOperation(value="护路护线-通过id查询", notes="护路护线-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzHlhx> queryById(@RequestParam(name="id",required=true) String id) {
		ZzHlhx zzHlhx = zzHlhxService.getById(id);
		return Result.ok(zzHlhx);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzHlhx
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzHlhx zzHlhx) {
      return super.exportXls(request, zzHlhx, ZzHlhx.class, "护路护线");
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
      return super.importExcel(request, response, ZzHlhx.class);
  }

}
