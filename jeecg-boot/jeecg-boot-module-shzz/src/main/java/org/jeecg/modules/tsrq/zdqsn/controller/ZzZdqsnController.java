package org.jeecg.modules.tsrq.zdqsn.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.tsrq.zdqsn.entity.ZzZdqsn;
import org.jeecg.modules.tsrq.zdqsn.service.IZzZdqsnService;
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
 * @Description: 重点青少年人员
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Slf4j
@Api(tags="重点青少年人员")
@RestController
@RequestMapping("/zdqsn/zzZdqsn")
public class ZzZdqsnController extends JeecgController<ZzZdqsn, IZzZdqsnService> {
	@Autowired
	private IZzZdqsnService zzZdqsnService;

	/**
	 * 分页列表查询
	 *
	 * @param zzZdqsn
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "重点青少年人员-分页列表查询")
	@ApiOperation(value="重点青少年人员-分页列表查询", notes="重点青少年人员-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzZdqsn>> queryPageList(ZzZdqsn zzZdqsn,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzZdqsn> queryWrapper = QueryGenerator.initQueryWrapper(zzZdqsn, req.getParameterMap());
		Page<ZzZdqsn> page = new Page<ZzZdqsn>(pageNo, pageSize);
		IPage<ZzZdqsn> pageList = zzZdqsnService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzZdqsn
	 * @return
	 */
	@AutoLog(value = "重点青少年人员-添加")
	@ApiOperation(value="重点青少年人员-添加", notes="重点青少年人员-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzZdqsn zzZdqsn) {
		zzZdqsnService.save(zzZdqsn);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzZdqsn
	 * @return
	 */
	@AutoLog(value = "重点青少年人员-编辑")
	@ApiOperation(value="重点青少年人员-编辑", notes="重点青少年人员-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzZdqsn zzZdqsn) {
		zzZdqsnService.updateById(zzZdqsn);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "重点青少年人员-通过id删除")
	@ApiOperation(value="重点青少年人员-通过id删除", notes="重点青少年人员-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzZdqsnService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "重点青少年人员-批量删除")
	@ApiOperation(value="重点青少年人员-批量删除", notes="重点青少年人员-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzZdqsnService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "重点青少年人员-通过id查询")
	@ApiOperation(value="重点青少年人员-通过id查询", notes="重点青少年人员-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzZdqsn> queryById(@RequestParam(name="id",required=true) String id) {
		ZzZdqsn zzZdqsn = zzZdqsnService.getById(id);
		return Result.ok(zzZdqsn);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzZdqsn
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzZdqsn zzZdqsn) {
      return super.exportXls(request, zzZdqsn, ZzZdqsn.class, "重点青少年人员");
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
      return super.importExcel(request, response, ZzZdqsn.class);
  }

}
