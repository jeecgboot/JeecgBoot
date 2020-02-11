package org.jeecg.modules.jjshzz.shzz.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.jjshzz.shzz.entity.ZzShzz;
import org.jeecg.modules.jjshzz.shzz.service.IZzShzzService;
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
 * @Description: 社会组织
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Slf4j
@Api(tags="社会组织")
@RestController
@RequestMapping("/shzz/zzShzz")
public class ZzShzzController extends JeecgController<ZzShzz, IZzShzzService> {
	@Autowired
	private IZzShzzService zzShzzService;

	/**
	 * 分页列表查询
	 *
	 * @param zzShzz
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "社会组织-分页列表查询")
	@ApiOperation(value="社会组织-分页列表查询", notes="社会组织-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzShzz>> queryPageList(ZzShzz zzShzz,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzShzz> queryWrapper = QueryGenerator.initQueryWrapper(zzShzz, req.getParameterMap());
		Page<ZzShzz> page = new Page<ZzShzz>(pageNo, pageSize);
		IPage<ZzShzz> pageList = zzShzzService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzShzz
	 * @return
	 */
	@AutoLog(value = "社会组织-添加")
	@ApiOperation(value="社会组织-添加", notes="社会组织-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzShzz zzShzz) {
		zzShzzService.save(zzShzz);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzShzz
	 * @return
	 */
	@AutoLog(value = "社会组织-编辑")
	@ApiOperation(value="社会组织-编辑", notes="社会组织-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzShzz zzShzz) {
		zzShzzService.updateById(zzShzz);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "社会组织-通过id删除")
	@ApiOperation(value="社会组织-通过id删除", notes="社会组织-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzShzzService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "社会组织-批量删除")
	@ApiOperation(value="社会组织-批量删除", notes="社会组织-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzShzzService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "社会组织-通过id查询")
	@ApiOperation(value="社会组织-通过id查询", notes="社会组织-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzShzz> queryById(@RequestParam(name="id",required=true) String id) {
		ZzShzz zzShzz = zzShzzService.getById(id);
		return Result.ok(zzShzz);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzShzz
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzShzz zzShzz) {
      return super.exportXls(request, zzShzz, ZzShzz.class, "社会组织");
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
      return super.importExcel(request, response, ZzShzz.class);
  }

}
