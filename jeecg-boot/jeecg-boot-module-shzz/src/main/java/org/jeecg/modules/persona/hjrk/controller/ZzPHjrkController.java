package org.jeecg.modules.persona.hjrk.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.persona.hjrk.entity.ZzPHjrk;
import org.jeecg.modules.persona.hjrk.service.IZzPHjrkService;
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
 * @Description: 户籍人口
 * @Author: jeecg-boot
 * @Date:   2020-02-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags="户籍人口")
@RestController
@RequestMapping("/hjrk/zzPHjrk")
public class ZzPHjrkController extends JeecgController<ZzPHjrk, IZzPHjrkService> {
	@Autowired
	private IZzPHjrkService zzPHjrkService;

	/**
	 * 分页列表查询
	 *
	 * @param zzPHjrk
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "户籍人口-分页列表查询")
	@ApiOperation(value="户籍人口-分页列表查询", notes="户籍人口-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzPHjrk>> queryPageList(ZzPHjrk zzPHjrk,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzPHjrk> queryWrapper = QueryGenerator.initQueryWrapper(zzPHjrk, req.getParameterMap());
		Page<ZzPHjrk> page = new Page<ZzPHjrk>(pageNo, pageSize);
		IPage<ZzPHjrk> pageList = zzPHjrkService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzPHjrk
	 * @return
	 */
	@AutoLog(value = "户籍人口-添加")
	@ApiOperation(value="户籍人口-添加", notes="户籍人口-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzPHjrk zzPHjrk) {
		zzPHjrkService.save(zzPHjrk);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzPHjrk
	 * @return
	 */
	@AutoLog(value = "户籍人口-编辑")
	@ApiOperation(value="户籍人口-编辑", notes="户籍人口-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzPHjrk zzPHjrk) {
		zzPHjrkService.updateById(zzPHjrk);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "户籍人口-通过id删除")
	@ApiOperation(value="户籍人口-通过id删除", notes="户籍人口-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzPHjrkService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "户籍人口-批量删除")
	@ApiOperation(value="户籍人口-批量删除", notes="户籍人口-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzPHjrkService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "户籍人口-通过id查询")
	@ApiOperation(value="户籍人口-通过id查询", notes="户籍人口-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzPHjrk> queryById(@RequestParam(name="id",required=true) String id) {
		ZzPHjrk zzPHjrk = zzPHjrkService.getById(id);
		return Result.ok(zzPHjrk);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzPHjrk
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzPHjrk zzPHjrk) {
      return super.exportXls(request, zzPHjrk, ZzPHjrk.class, "户籍人口");
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
      return super.importExcel(request, response, ZzPHjrk.class);
  }

}
