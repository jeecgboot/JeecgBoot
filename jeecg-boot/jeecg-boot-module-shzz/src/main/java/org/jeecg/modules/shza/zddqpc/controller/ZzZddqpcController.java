package org.jeecg.modules.shza.zddqpc.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.shza.zddqpc.entity.ZzZddqpc;
import org.jeecg.modules.shza.zddqpc.service.IZzZddqpcService;
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
 * @Description: 重点地区排查整治
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Slf4j
@Api(tags="重点地区排查整治")
@RestController
@RequestMapping("/zddqpc/zzZddqpc")
public class ZzZddqpcController extends JeecgController<ZzZddqpc, IZzZddqpcService> {
	@Autowired
	private IZzZddqpcService zzZddqpcService;

	/**
	 * 分页列表查询
	 *
	 * @param zzZddqpc
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "重点地区排查整治-分页列表查询")
	@ApiOperation(value="重点地区排查整治-分页列表查询", notes="重点地区排查整治-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzZddqpc>> queryPageList(ZzZddqpc zzZddqpc,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzZddqpc> queryWrapper = QueryGenerator.initQueryWrapper(zzZddqpc, req.getParameterMap());
		Page<ZzZddqpc> page = new Page<ZzZddqpc>(pageNo, pageSize);
		IPage<ZzZddqpc> pageList = zzZddqpcService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzZddqpc
	 * @return
	 */
	@AutoLog(value = "重点地区排查整治-添加")
	@ApiOperation(value="重点地区排查整治-添加", notes="重点地区排查整治-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzZddqpc zzZddqpc) {
		zzZddqpcService.save(zzZddqpc);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzZddqpc
	 * @return
	 */
	@AutoLog(value = "重点地区排查整治-编辑")
	@ApiOperation(value="重点地区排查整治-编辑", notes="重点地区排查整治-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzZddqpc zzZddqpc) {
		zzZddqpcService.updateById(zzZddqpc);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "重点地区排查整治-通过id删除")
	@ApiOperation(value="重点地区排查整治-通过id删除", notes="重点地区排查整治-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzZddqpcService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "重点地区排查整治-批量删除")
	@ApiOperation(value="重点地区排查整治-批量删除", notes="重点地区排查整治-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzZddqpcService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "重点地区排查整治-通过id查询")
	@ApiOperation(value="重点地区排查整治-通过id查询", notes="重点地区排查整治-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzZddqpc> queryById(@RequestParam(name="id",required=true) String id) {
		ZzZddqpc zzZddqpc = zzZddqpcService.getById(id);
		return Result.ok(zzZddqpc);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzZddqpc
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzZddqpc zzZddqpc) {
      return super.exportXls(request, zzZddqpc, ZzZddqpc.class, "重点地区排查整治");
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
      return super.importExcel(request, response, ZzZddqpc.class);
  }

}
