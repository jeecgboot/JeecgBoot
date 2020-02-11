package org.jeecg.modules.shza.wlaq.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.shza.wlaq.entity.ZzWlaq;
import org.jeecg.modules.shza.wlaq.service.IZzWlaqService;
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
 * @Description: 寄递物流安全管理
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@Slf4j
@Api(tags="寄递物流安全管理")
@RestController
@RequestMapping("/wlaq/zzWlaq")
public class ZzWlaqController extends JeecgController<ZzWlaq, IZzWlaqService> {
	@Autowired
	private IZzWlaqService zzWlaqService;

	/**
	 * 分页列表查询
	 *
	 * @param zzWlaq
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "寄递物流安全管理-分页列表查询")
	@ApiOperation(value="寄递物流安全管理-分页列表查询", notes="寄递物流安全管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzWlaq>> queryPageList(ZzWlaq zzWlaq,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzWlaq> queryWrapper = QueryGenerator.initQueryWrapper(zzWlaq, req.getParameterMap());
		Page<ZzWlaq> page = new Page<ZzWlaq>(pageNo, pageSize);
		IPage<ZzWlaq> pageList = zzWlaqService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzWlaq
	 * @return
	 */
	@AutoLog(value = "寄递物流安全管理-添加")
	@ApiOperation(value="寄递物流安全管理-添加", notes="寄递物流安全管理-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzWlaq zzWlaq) {
		zzWlaqService.save(zzWlaq);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzWlaq
	 * @return
	 */
	@AutoLog(value = "寄递物流安全管理-编辑")
	@ApiOperation(value="寄递物流安全管理-编辑", notes="寄递物流安全管理-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzWlaq zzWlaq) {
		zzWlaqService.updateById(zzWlaq);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "寄递物流安全管理-通过id删除")
	@ApiOperation(value="寄递物流安全管理-通过id删除", notes="寄递物流安全管理-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzWlaqService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "寄递物流安全管理-批量删除")
	@ApiOperation(value="寄递物流安全管理-批量删除", notes="寄递物流安全管理-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzWlaqService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "寄递物流安全管理-通过id查询")
	@ApiOperation(value="寄递物流安全管理-通过id查询", notes="寄递物流安全管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzWlaq> queryById(@RequestParam(name="id",required=true) String id) {
		ZzWlaq zzWlaq = zzWlaqService.getById(id);
		return Result.ok(zzWlaq);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzWlaq
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzWlaq zzWlaq) {
      return super.exportXls(request, zzWlaq, ZzWlaq.class, "寄递物流安全管理");
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
      return super.importExcel(request, response, ZzWlaq.class);
  }

}
