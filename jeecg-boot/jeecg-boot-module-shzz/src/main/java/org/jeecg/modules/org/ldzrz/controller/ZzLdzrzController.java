package org.jeecg.modules.org.ldzrz.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.org.ldzrz.entity.ZzLdzrz;
import org.jeecg.modules.org.ldzrz.service.IZzLdzrzService;
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
 * @Description: 综治领导责任制
 * @Author: jeecg-boot
 * @Date:   2020-02-06
 * @Version: V1.0
 */
@Slf4j
@Api(tags="综治领导责任制")
@RestController
@RequestMapping("/ldzrz/zzLdzrz")
public class ZzLdzrzController extends JeecgController<ZzLdzrz, IZzLdzrzService> {
	@Autowired
	private IZzLdzrzService zzLdzrzService;

	/**
	 * 分页列表查询
	 *
	 * @param zzLdzrz
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "综治领导责任制-分页列表查询")
	@ApiOperation(value="综治领导责任制-分页列表查询", notes="综治领导责任制-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzLdzrz>> queryPageList(ZzLdzrz zzLdzrz,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzLdzrz> queryWrapper = QueryGenerator.initQueryWrapper(zzLdzrz, req.getParameterMap());
		Page<ZzLdzrz> page = new Page<ZzLdzrz>(pageNo, pageSize);
		IPage<ZzLdzrz> pageList = zzLdzrzService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzLdzrz
	 * @return
	 */
	@AutoLog(value = "综治领导责任制-添加")
	@ApiOperation(value="综治领导责任制-添加", notes="综治领导责任制-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzLdzrz zzLdzrz) {
		zzLdzrzService.save(zzLdzrz);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzLdzrz
	 * @return
	 */
	@AutoLog(value = "综治领导责任制-编辑")
	@ApiOperation(value="综治领导责任制-编辑", notes="综治领导责任制-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzLdzrz zzLdzrz) {
		zzLdzrzService.updateById(zzLdzrz);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "综治领导责任制-通过id删除")
	@ApiOperation(value="综治领导责任制-通过id删除", notes="综治领导责任制-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzLdzrzService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "综治领导责任制-批量删除")
	@ApiOperation(value="综治领导责任制-批量删除", notes="综治领导责任制-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzLdzrzService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "综治领导责任制-通过id查询")
	@ApiOperation(value="综治领导责任制-通过id查询", notes="综治领导责任制-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzLdzrz> queryById(@RequestParam(name="id",required=true) String id) {
		ZzLdzrz zzLdzrz = zzLdzrzService.getById(id);
		return Result.ok(zzLdzrz);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzLdzrz
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzLdzrz zzLdzrz) {
      return super.exportXls(request, zzLdzrz, ZzLdzrz.class, "综治领导责任制");
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
      return super.importExcel(request, response, ZzLdzrz.class);
  }

}
