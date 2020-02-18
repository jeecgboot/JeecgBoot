package org.jeecg.modules.schoolAround.school.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.schoolAround.school.entity.ZzSchool;
import org.jeecg.modules.schoolAround.school.service.IZzSchoolService;
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
 * @Description: 学校
 * @Author: jeecg-boot
 * @Date:   2020-02-18
 * @Version: V1.0
 */
@Slf4j
@Api(tags="学校")
@RestController
@RequestMapping("/school/zzSchool")
public class ZzSchoolController extends JeecgController<ZzSchool, IZzSchoolService> {
	@Autowired
	private IZzSchoolService zzSchoolService;

	/**
	 * 分页列表查询
	 *
	 * @param zzSchool
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "学校-分页列表查询")
	@ApiOperation(value="学校-分页列表查询", notes="学校-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ZzSchool>> queryPageList(ZzSchool zzSchool,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzSchool> queryWrapper = QueryGenerator.initQueryWrapper(zzSchool, req.getParameterMap());
		Page<ZzSchool> page = new Page<ZzSchool>(pageNo, pageSize);
		IPage<ZzSchool> pageList = zzSchoolService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
	 * 添加
	 *
	 * @param zzSchool
	 * @return
	 */
	@AutoLog(value = "学校-添加")
	@ApiOperation(value="学校-添加", notes="学校-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzSchool zzSchool) {
		zzSchoolService.save(zzSchool);
		return Result.ok("添加成功！");
	}

	/**
	 * 编辑
	 *
	 * @param zzSchool
	 * @return
	 */
	@AutoLog(value = "学校-编辑")
	@ApiOperation(value="学校-编辑", notes="学校-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzSchool zzSchool) {
		zzSchoolService.updateById(zzSchool);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "学校-通过id删除")
	@ApiOperation(value="学校-通过id删除", notes="学校-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzSchoolService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "学校-批量删除")
	@ApiOperation(value="学校-批量删除", notes="学校-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzSchoolService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "学校-通过id查询")
	@ApiOperation(value="学校-通过id查询", notes="学校-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ZzSchool> queryById(@RequestParam(name="id",required=true) String id) {
		ZzSchool zzSchool = zzSchoolService.getById(id);
		return Result.ok(zzSchool);
	}

  /**
   * 导出excel
   *
   * @param request
   * @param zzSchool
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(HttpServletRequest request, ZzSchool zzSchool) {
      return super.exportXls(request, zzSchool, ZzSchool.class, "学校");
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
      return super.importExcel(request, response, ZzSchool.class);
  }

}
