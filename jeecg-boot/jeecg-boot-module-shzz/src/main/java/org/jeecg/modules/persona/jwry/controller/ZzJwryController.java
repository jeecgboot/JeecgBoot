package org.jeecg.modules.persona.jwry.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.persona.jwry.entity.ZzJwry;
import org.jeecg.modules.persona.jwry.service.IZzJwryService;
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
 * @Description: 境外人员
 * @Author: jeecg-boot
 * @Date:   2020-02-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags="境外人员")
@RestController
@RequestMapping("/jwry/zzJwry")
public class ZzJwryController extends JeecgController<ZzJwry, IZzJwryService> {
	@Autowired
	private IZzJwryService zzJwryService;

	/**
	 * 分页列表查询
	 *
	 * @param zzJwry
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "境外人员-分页列表查询")
	@ApiOperation(value="境外人员-分页列表查询", notes="境外人员-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzJwry>> queryPageList(ZzJwry zzJwry,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzJwry> queryWrapper = QueryGenerator.initQueryWrapper(zzJwry, req.getParameterMap());
		Page<ZzJwry> page = new Page<ZzJwry>(pageNo, pageSize);
		IPage<ZzJwry> pageList = zzJwryService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzJwry
	 * @return
	 */
	@AutoLog(value = "境外人员-添加")
	@ApiOperation(value="境外人员-添加", notes="境外人员-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzJwry zzJwry) {
		zzJwryService.save(zzJwry);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzJwry
	 * @return
	 */
	@AutoLog(value = "境外人员-编辑")
	@ApiOperation(value="境外人员-编辑", notes="境外人员-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzJwry zzJwry) {
		zzJwryService.updateById(zzJwry);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "境外人员-通过id删除")
	@ApiOperation(value="境外人员-通过id删除", notes="境外人员-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzJwryService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "境外人员-批量删除")
	@ApiOperation(value="境外人员-批量删除", notes="境外人员-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzJwryService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "境外人员-通过id查询")
	@ApiOperation(value="境外人员-通过id查询", notes="境外人员-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzJwry> queryById(@RequestParam(name="id",required=true) String id) {
		ZzJwry zzJwry = zzJwryService.getById(id);
		return Result.ok(zzJwry);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzJwry
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzJwry zzJwry) {
      return super.exportXls(request, zzJwry, ZzJwry.class, "境外人员");
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
      return super.importExcel(request, response, ZzJwry.class);
  }

}
