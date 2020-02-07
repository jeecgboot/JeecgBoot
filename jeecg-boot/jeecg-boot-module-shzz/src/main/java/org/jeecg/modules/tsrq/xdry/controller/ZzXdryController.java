package org.jeecg.modules.tsrq.xdry.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.tsrq.xdry.entity.ZzXdry;
import org.jeecg.modules.tsrq.xdry.service.IZzXdryService;
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
 * @Description: 吸毒人员
 * @Author: jeecg-boot
 * @Date:   2020-02-07
 * @Version: V1.0
 */
@Slf4j
@Api(tags="吸毒人员")
@RestController
@RequestMapping("/xdry/zzXdry")
public class ZzXdryController extends JeecgController<ZzXdry, IZzXdryService> {
	@Autowired
	private IZzXdryService zzXdryService;

	/**
	 * 分页列表查询
	 *
	 * @param zzXdry
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "吸毒人员-分页列表查询")
	@ApiOperation(value="吸毒人员-分页列表查询", notes="吸毒人员-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzXdry>> queryPageList(ZzXdry zzXdry,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzXdry> queryWrapper = QueryGenerator.initQueryWrapper(zzXdry, req.getParameterMap());
		Page<ZzXdry> page = new Page<ZzXdry>(pageNo, pageSize);
		IPage<ZzXdry> pageList = zzXdryService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzXdry
	 * @return
	 */
	@AutoLog(value = "吸毒人员-添加")
	@ApiOperation(value="吸毒人员-添加", notes="吸毒人员-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzXdry zzXdry) {
		zzXdryService.save(zzXdry);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzXdry
	 * @return
	 */
	@AutoLog(value = "吸毒人员-编辑")
	@ApiOperation(value="吸毒人员-编辑", notes="吸毒人员-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzXdry zzXdry) {
		zzXdryService.updateById(zzXdry);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "吸毒人员-通过id删除")
	@ApiOperation(value="吸毒人员-通过id删除", notes="吸毒人员-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzXdryService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "吸毒人员-批量删除")
	@ApiOperation(value="吸毒人员-批量删除", notes="吸毒人员-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzXdryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "吸毒人员-通过id查询")
	@ApiOperation(value="吸毒人员-通过id查询", notes="吸毒人员-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzXdry> queryById(@RequestParam(name="id",required=true) String id) {
		ZzXdry zzXdry = zzXdryService.getById(id);
		return Result.ok(zzXdry);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzXdry
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzXdry zzXdry) {
      return super.exportXls(request, zzXdry, ZzXdry.class, "吸毒人员");
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
      return super.importExcel(request, response, ZzXdry.class);
  }

}
