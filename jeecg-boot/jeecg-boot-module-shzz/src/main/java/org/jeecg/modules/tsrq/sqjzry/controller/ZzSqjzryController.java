package org.jeecg.modules.tsrq.sqjzry.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.tsrq.sqjzry.entity.ZzSqjzry;
import org.jeecg.modules.tsrq.sqjzry.service.IZzSqjzryService;
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
 * @Description: 社区矫正人员
 * @Author: jeecg-boot
 * @Date:   2020-02-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags="社区矫正人员")
@RestController
@RequestMapping("/sqjzry/zzSqjzry")
public class ZzSqjzryController extends JeecgController<ZzSqjzry, IZzSqjzryService> {
	@Autowired
	private IZzSqjzryService zzSqjzryService;

	/**
	 * 分页列表查询
	 *
	 * @param zzSqjzry
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "社区矫正人员-分页列表查询")
	@ApiOperation(value="社区矫正人员-分页列表查询", notes="社区矫正人员-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzSqjzry>> queryPageList(ZzSqjzry zzSqjzry,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzSqjzry> queryWrapper = QueryGenerator.initQueryWrapper(zzSqjzry, req.getParameterMap());
		Page<ZzSqjzry> page = new Page<ZzSqjzry>(pageNo, pageSize);
		IPage<ZzSqjzry> pageList = zzSqjzryService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzSqjzry
	 * @return
	 */
	@AutoLog(value = "社区矫正人员-添加")
	@ApiOperation(value="社区矫正人员-添加", notes="社区矫正人员-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzSqjzry zzSqjzry) {
		zzSqjzryService.save(zzSqjzry);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzSqjzry
	 * @return
	 */
	@AutoLog(value = "社区矫正人员-编辑")
	@ApiOperation(value="社区矫正人员-编辑", notes="社区矫正人员-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzSqjzry zzSqjzry) {
		zzSqjzryService.updateById(zzSqjzry);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "社区矫正人员-通过id删除")
	@ApiOperation(value="社区矫正人员-通过id删除", notes="社区矫正人员-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzSqjzryService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "社区矫正人员-批量删除")
	@ApiOperation(value="社区矫正人员-批量删除", notes="社区矫正人员-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzSqjzryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "社区矫正人员-通过id查询")
	@ApiOperation(value="社区矫正人员-通过id查询", notes="社区矫正人员-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzSqjzry> queryById(@RequestParam(name="id",required=true) String id) {
		ZzSqjzry zzSqjzry = zzSqjzryService.getById(id);
		return Result.ok(zzSqjzry);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzSqjzry
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzSqjzry zzSqjzry) {
      return super.exportXls(request, zzSqjzry, ZzSqjzry.class, "社区矫正人员");
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
      return super.importExcel(request, response, ZzSqjzry.class);
  }

}
