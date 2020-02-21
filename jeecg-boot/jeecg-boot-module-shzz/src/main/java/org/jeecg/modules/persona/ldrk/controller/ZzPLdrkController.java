package org.jeecg.modules.persona.ldrk.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.persona.ldrk.entity.ZzPLdrk;
import org.jeecg.modules.persona.ldrk.service.IZzPLdrkService;
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
 * @Description: 流动人口
 * @Author: jeecg-boot
 * @Date:   2020-02-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags="流动人口")
@RestController
@RequestMapping("/ldrk/zzPLdrk")
public class ZzPLdrkController extends JeecgController<ZzPLdrk, IZzPLdrkService> {
	@Autowired
	private IZzPLdrkService zzPLdrkService;

	/**
	 * 分页列表查询
	 *
	 * @param zzPLdrk
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "流动人口-分页列表查询")
	@ApiOperation(value="流动人口-分页列表查询", notes="流动人口-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzPLdrk>> queryPageList(ZzPLdrk zzPLdrk,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzPLdrk> queryWrapper = QueryGenerator.initQueryWrapper(zzPLdrk, req.getParameterMap());
		Page<ZzPLdrk> page = new Page<ZzPLdrk>(pageNo, pageSize);
		IPage<ZzPLdrk> pageList = zzPLdrkService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzPLdrk
	 * @return
	 */
	@AutoLog(value = "流动人口-添加")
	@ApiOperation(value="流动人口-添加", notes="流动人口-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzPLdrk zzPLdrk) {
		zzPLdrkService.save(zzPLdrk);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzPLdrk
	 * @return
	 */
	@AutoLog(value = "流动人口-编辑")
	@ApiOperation(value="流动人口-编辑", notes="流动人口-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzPLdrk zzPLdrk) {
		zzPLdrkService.updateById(zzPLdrk);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "流动人口-通过id删除")
	@ApiOperation(value="流动人口-通过id删除", notes="流动人口-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzPLdrkService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "流动人口-批量删除")
	@ApiOperation(value="流动人口-批量删除", notes="流动人口-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzPLdrkService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "流动人口-通过id查询")
	@ApiOperation(value="流动人口-通过id查询", notes="流动人口-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzPLdrk> queryById(@RequestParam(name="id",required=true) String id) {
		ZzPLdrk zzPLdrk = zzPLdrkService.getById(id);
		return Result.ok(zzPLdrk);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzPLdrk
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzPLdrk zzPLdrk) {
      return super.exportXls(request, zzPLdrk, ZzPLdrk.class, "流动人口");
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
      return super.importExcel(request, response, ZzPLdrk.class);
  }

}
