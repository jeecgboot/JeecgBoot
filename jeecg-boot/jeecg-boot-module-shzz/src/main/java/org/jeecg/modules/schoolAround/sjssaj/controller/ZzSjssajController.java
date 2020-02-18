package org.jeecg.modules.schoolAround.sjssaj.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.schoolAround.sjssaj.entity.ZzSjssaj;
import org.jeecg.modules.schoolAround.sjssaj.service.IZzSjssajService;
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
 * @Description: 涉及师生案件
 * @Author: jeecg-boot
 * @Date:   2020-02-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags="涉及师生案件")
@RestController
@RequestMapping("/sjssaj/zzSjssaj")
public class ZzSjssajController extends JeecgController<ZzSjssaj, IZzSjssajService> {
	@Autowired
	private IZzSjssajService zzSjssajService;

	/**
	 * 分页列表查询
	 *
	 * @param zzSjssaj
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "涉及师生案件-分页列表查询")
	@ApiOperation(value="涉及师生案件-分页列表查询", notes="涉及师生案件-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzSjssaj>> queryPageList(ZzSjssaj zzSjssaj,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzSjssaj> queryWrapper = QueryGenerator.initQueryWrapper(zzSjssaj, req.getParameterMap());
		Page<ZzSjssaj> page = new Page<ZzSjssaj>(pageNo, pageSize);
		IPage<ZzSjssaj> pageList = zzSjssajService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzSjssaj
	 * @return
	 */
	@AutoLog(value = "涉及师生案件-添加")
	@ApiOperation(value="涉及师生案件-添加", notes="涉及师生案件-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzSjssaj zzSjssaj) {
		zzSjssajService.save(zzSjssaj);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzSjssaj
	 * @return
	 */
	@AutoLog(value = "涉及师生案件-编辑")
	@ApiOperation(value="涉及师生案件-编辑", notes="涉及师生案件-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzSjssaj zzSjssaj) {
		zzSjssajService.updateById(zzSjssaj);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "涉及师生案件-通过id删除")
	@ApiOperation(value="涉及师生案件-通过id删除", notes="涉及师生案件-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzSjssajService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "涉及师生案件-批量删除")
	@ApiOperation(value="涉及师生案件-批量删除", notes="涉及师生案件-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzSjssajService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "涉及师生案件-通过id查询")
	@ApiOperation(value="涉及师生案件-通过id查询", notes="涉及师生案件-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzSjssaj> queryById(@RequestParam(name="id",required=true) String id) {
		ZzSjssaj zzSjssaj = zzSjssajService.getById(id);
		return Result.ok(zzSjssaj);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzSjssaj
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzSjssaj zzSjssaj) {
      return super.exportXls(request, zzSjssaj, ZzSjssaj.class, "涉及师生案件");
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
      return super.importExcel(request, response, ZzSjssaj.class);
  }

}
