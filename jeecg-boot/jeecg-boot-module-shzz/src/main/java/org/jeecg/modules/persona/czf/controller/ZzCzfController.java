package org.jeecg.modules.persona.czf.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.persona.czf.entity.ZzCzf;
import org.jeecg.modules.persona.czf.service.IZzCzfService;
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
 * @Description: 出租房
 * @Author: jeecg-boot
 * @Date:   2020-02-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags="出租房")
@RestController
@RequestMapping("/czf/zzCzf")
public class ZzCzfController extends JeecgController<ZzCzf, IZzCzfService> {
	@Autowired
	private IZzCzfService zzCzfService;

	/**
	 * 分页列表查询
	 *
	 * @param zzCzf
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "出租房-分页列表查询")
	@ApiOperation(value="出租房-分页列表查询", notes="出租房-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzCzf>> queryPageList(ZzCzf zzCzf,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzCzf> queryWrapper = QueryGenerator.initQueryWrapper(zzCzf, req.getParameterMap());
		Page<ZzCzf> page = new Page<ZzCzf>(pageNo, pageSize);
		IPage<ZzCzf> pageList = zzCzfService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzCzf
	 * @return
	 */
	@AutoLog(value = "出租房-添加")
	@ApiOperation(value="出租房-添加", notes="出租房-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzCzf zzCzf) {
		zzCzfService.save(zzCzf);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzCzf
	 * @return
	 */
	@AutoLog(value = "出租房-编辑")
	@ApiOperation(value="出租房-编辑", notes="出租房-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzCzf zzCzf) {
		zzCzfService.updateById(zzCzf);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "出租房-通过id删除")
	@ApiOperation(value="出租房-通过id删除", notes="出租房-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzCzfService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "出租房-批量删除")
	@ApiOperation(value="出租房-批量删除", notes="出租房-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzCzfService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "出租房-通过id查询")
	@ApiOperation(value="出租房-通过id查询", notes="出租房-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzCzf> queryById(@RequestParam(name="id",required=true) String id) {
		ZzCzf zzCzf = zzCzfService.getById(id);
		return Result.ok(zzCzf);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzCzf
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzCzf zzCzf) {
      return super.exportXls(request, zzCzf, ZzCzf.class, "出租房");
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
      return super.importExcel(request, response, ZzCzf.class);
  }

}
