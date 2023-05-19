package org.jeecg.modules.wo.queue.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.wo.queue.entity.WoQueueRecord;
import org.jeecg.modules.wo.queue.service.IWoQueueRecordService;
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
 * @Description: 排队记录
 * @Author: jeecg-boot
 * @Date:   2023-04-26
 * @Version: V1.0
 */
@Slf4j
@Api(tags="排队记录")
@RestController
@RequestMapping("/queue/woQueueRecord")
public class WoQueueRecordController extends JeecgController<WoQueueRecord, IWoQueueRecordService> {
	@Autowired
	private IWoQueueRecordService woQueueRecordService;
	
	/**
	 * 分页列表查询
	 *
	 * @param woQueueRecord
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "排队记录-分页列表查询")
	@ApiOperation(value="排队记录-分页列表查询", notes="排队记录-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(WoQueueRecord woQueueRecord,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<WoQueueRecord> queryWrapper = QueryGenerator.initQueryWrapper(woQueueRecord, req.getParameterMap());
		Page<WoQueueRecord> page = new Page<WoQueueRecord>(pageNo, pageSize);
		IPage<WoQueueRecord> pageList = woQueueRecordService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 * 添加
	 *
	 * @param woQueueRecord
	 * @return
	 */
	@AutoLog(value = "排队记录-添加")
	@ApiOperation(value="排队记录-添加", notes="排队记录-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody WoQueueRecord woQueueRecord) {
		woQueueRecordService.save(woQueueRecord);
		return Result.OK("添加成功！");
	}
	
	/**
	 * 编辑
	 *
	 * @param woQueueRecord
	 * @return
	 */
	@AutoLog(value = "排队记录-编辑")
	@ApiOperation(value="排队记录-编辑", notes="排队记录-编辑")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<?> edit(@RequestBody WoQueueRecord woQueueRecord) {
		woQueueRecordService.updateById(woQueueRecord);
		return Result.OK("编辑成功!");
	}
	
	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "排队记录-通过id删除")
	@ApiOperation(value="排队记录-通过id删除", notes="排队记录-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		woQueueRecordService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "排队记录-批量删除")
	@ApiOperation(value="排队记录-批量删除", notes="排队记录-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.woQueueRecordService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "排队记录-通过id查询")
	@ApiOperation(value="排队记录-通过id查询", notes="排队记录-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		WoQueueRecord woQueueRecord = woQueueRecordService.getById(id);
		return Result.OK(woQueueRecord);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param woQueueRecord
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, WoQueueRecord woQueueRecord) {
      return super.exportXls(request, woQueueRecord, WoQueueRecord.class, "排队记录");
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
      return super.importExcel(request, response, WoQueueRecord.class);
  }

}
