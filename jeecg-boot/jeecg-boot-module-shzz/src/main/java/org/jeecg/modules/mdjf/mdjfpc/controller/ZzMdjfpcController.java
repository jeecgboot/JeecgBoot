package org.jeecg.modules.mdjf.mdjfpc.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.mdjf.mdjfpc.entity.ZzMdjfpc;
import org.jeecg.modules.mdjf.mdjfpc.service.IZzMdjfpcService;
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
 * @Description: 矛盾纠纷排查化解
 * @Author: jeecg-boot
 * @Date:   2020-02-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags="矛盾纠纷排查化解")
@RestController
@RequestMapping("/mdjfpc/zzMdjfpc")
public class ZzMdjfpcController extends JeecgController<ZzMdjfpc, IZzMdjfpcService> {
	@Autowired
	private IZzMdjfpcService zzMdjfpcService;

	/**
	 * 分页列表查询
	 *
	 * @param zzMdjfpc
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "矛盾纠纷排查化解-分页列表查询")
	@ApiOperation(value="矛盾纠纷排查化解-分页列表查询", notes="矛盾纠纷排查化解-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzMdjfpc>> queryPageList(ZzMdjfpc zzMdjfpc,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzMdjfpc> queryWrapper = QueryGenerator.initQueryWrapper(zzMdjfpc, req.getParameterMap());
		Page<ZzMdjfpc> page = new Page<ZzMdjfpc>(pageNo, pageSize);
		IPage<ZzMdjfpc> pageList = zzMdjfpcService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzMdjfpc
	 * @return
	 */
	@AutoLog(value = "矛盾纠纷排查化解-添加")
	@ApiOperation(value="矛盾纠纷排查化解-添加", notes="矛盾纠纷排查化解-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzMdjfpc zzMdjfpc) {
		zzMdjfpcService.save(zzMdjfpc);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzMdjfpc
	 * @return
	 */
	@AutoLog(value = "矛盾纠纷排查化解-编辑")
	@ApiOperation(value="矛盾纠纷排查化解-编辑", notes="矛盾纠纷排查化解-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzMdjfpc zzMdjfpc) {
		zzMdjfpcService.updateById(zzMdjfpc);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "矛盾纠纷排查化解-通过id删除")
	@ApiOperation(value="矛盾纠纷排查化解-通过id删除", notes="矛盾纠纷排查化解-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzMdjfpcService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "矛盾纠纷排查化解-批量删除")
	@ApiOperation(value="矛盾纠纷排查化解-批量删除", notes="矛盾纠纷排查化解-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzMdjfpcService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "矛盾纠纷排查化解-通过id查询")
	@ApiOperation(value="矛盾纠纷排查化解-通过id查询", notes="矛盾纠纷排查化解-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzMdjfpc> queryById(@RequestParam(name="id",required=true) String id) {
		ZzMdjfpc zzMdjfpc = zzMdjfpcService.getById(id);
		return Result.ok(zzMdjfpc);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzMdjfpc
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzMdjfpc zzMdjfpc) {
      return super.exportXls(request, zzMdjfpc, ZzMdjfpc.class, "矛盾纠纷排查化解");
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
      return super.importExcel(request, response, ZzMdjfpc.class);
  }

}
