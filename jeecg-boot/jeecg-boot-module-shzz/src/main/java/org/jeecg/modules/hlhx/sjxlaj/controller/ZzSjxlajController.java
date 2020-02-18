package org.jeecg.modules.hlhx.sjxlaj.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.hlhx.sjxlaj.entity.ZzSjxlaj;
import org.jeecg.modules.hlhx.sjxlaj.service.IZzSjxlajService;
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
 * @Description: 涉及线路案（事）件
 * @Author: jeecg-boot
 * @Date:   2020-02-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags="涉及线路案（事）件")
@RestController
@RequestMapping("/sjxlaj/zzSjxlaj")
public class ZzSjxlajController extends JeecgController<ZzSjxlaj, IZzSjxlajService> {
	@Autowired
	private IZzSjxlajService zzSjxlajService;

	/**
	 * 分页列表查询
	 *
	 * @param zzSjxlaj
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "涉及线路案（事）件-分页列表查询")
	@ApiOperation(value="涉及线路案（事）件-分页列表查询", notes="涉及线路案（事）件-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzSjxlaj>> queryPageList(ZzSjxlaj zzSjxlaj,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzSjxlaj> queryWrapper = QueryGenerator.initQueryWrapper(zzSjxlaj, req.getParameterMap());
		Page<ZzSjxlaj> page = new Page<ZzSjxlaj>(pageNo, pageSize);
		IPage<ZzSjxlaj> pageList = zzSjxlajService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzSjxlaj
	 * @return
	 */
	@AutoLog(value = "涉及线路案（事）件-添加")
	@ApiOperation(value="涉及线路案（事）件-添加", notes="涉及线路案（事）件-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzSjxlaj zzSjxlaj) {
		zzSjxlajService.save(zzSjxlaj);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzSjxlaj
	 * @return
	 */
	@AutoLog(value = "涉及线路案（事）件-编辑")
	@ApiOperation(value="涉及线路案（事）件-编辑", notes="涉及线路案（事）件-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzSjxlaj zzSjxlaj) {
		zzSjxlajService.updateById(zzSjxlaj);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "涉及线路案（事）件-通过id删除")
	@ApiOperation(value="涉及线路案（事）件-通过id删除", notes="涉及线路案（事）件-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzSjxlajService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "涉及线路案（事）件-批量删除")
	@ApiOperation(value="涉及线路案（事）件-批量删除", notes="涉及线路案（事）件-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzSjxlajService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "涉及线路案（事）件-通过id查询")
	@ApiOperation(value="涉及线路案（事）件-通过id查询", notes="涉及线路案（事）件-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzSjxlaj> queryById(@RequestParam(name="id",required=true) String id) {
		ZzSjxlaj zzSjxlaj = zzSjxlajService.getById(id);
		return Result.ok(zzSjxlaj);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzSjxlaj
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzSjxlaj zzSjxlaj) {
      return super.exportXls(request, zzSjxlaj, ZzSjxlaj.class, "涉及线路案（事）件");
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
      return super.importExcel(request, response, ZzSjxlaj.class);
  }

}
