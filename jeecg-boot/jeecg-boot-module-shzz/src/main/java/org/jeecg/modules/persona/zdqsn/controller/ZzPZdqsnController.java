package org.jeecg.modules.persona.zdqsn.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.persona.zdqsn.entity.ZzPZdqsn;
import org.jeecg.modules.persona.zdqsn.service.IZzPZdqsnService;
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
 * @Date:   2020-02-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags="重点青少年人员")
@RestController
@RequestMapping("/zdqsn/zzPZdqsn")
public class ZzPZdqsnController extends JeecgController<ZzPZdqsn, IZzPZdqsnService> {
	@Autowired
	private IZzPZdqsnService zzPZdqsnService;

	/**
	 * 分页列表查询
	 *
	 * @param zzPZdqsn
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "重点青少年人员-分页列表查询")
	@ApiOperation(value="重点青少年人员-分页列表查询", notes="重点青少年人员-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzPZdqsn>> queryPageList(ZzPZdqsn zzPZdqsn,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzPZdqsn> queryWrapper = QueryGenerator.initQueryWrapper(zzPZdqsn, req.getParameterMap());
		Page<ZzPZdqsn> page = new Page<ZzPZdqsn>(pageNo, pageSize);
		IPage<ZzPZdqsn> pageList = zzPZdqsnService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzPZdqsn
	 * @return
	 */
	@AutoLog(value = "重点青少年人员-添加")
	@ApiOperation(value="重点青少年人员-添加", notes="重点青少年人员-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzPZdqsn zzPZdqsn) {
		zzPZdqsnService.save(zzPZdqsn);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzPZdqsn
	 * @return
	 */
	@AutoLog(value = "重点青少年人员-编辑")
	@ApiOperation(value="重点青少年人员-编辑", notes="重点青少年人员-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzPZdqsn zzPZdqsn) {
		zzPZdqsnService.updateById(zzPZdqsn);
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
		zzPZdqsnService.removeById(id);
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
		this.zzPZdqsnService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<ZzPZdqsn> queryById(@RequestParam(name="id",required=true) String id) {
		ZzPZdqsn zzPZdqsn = zzPZdqsnService.getById(id);
		return Result.ok(zzPZdqsn);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzPZdqsn
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzPZdqsn zzPZdqsn) {
      return super.exportXls(request, zzPZdqsn, ZzPZdqsn.class, "重点青少年人员");
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
      return super.importExcel(request, response, ZzPZdqsn.class);
  }

}
