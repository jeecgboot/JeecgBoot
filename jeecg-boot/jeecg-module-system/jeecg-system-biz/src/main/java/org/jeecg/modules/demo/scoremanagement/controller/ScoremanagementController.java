package org.jeecg.modules.demo.scoremanagement.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.query.QueryRuleEnum;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.scoremanagement.entity.Scoremanagement;
import org.jeecg.modules.demo.scoremanagement.service.IScoremanagementService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.demo.student.entity.Student;
import org.jeecg.modules.demo.student.service.IStudentService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 成绩管理模块
 * @Author: jeecg-boot
 * @Date:   2025-06-11
 * @Version: V1.0
 */
@Tag(name="成绩管理模块")
@RestController
@RequestMapping("/scoremanagement/scoremanagement")
@Slf4j
public class ScoremanagementController extends JeecgController<Scoremanagement, IScoremanagementService> {
	@Autowired
	private IScoremanagementService scoremanagementService;
	
	/**
	 * 分页列表查询
	 *
	 * @param scoremanagement
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "成绩管理模块-分页列表查询")
	@Operation(summary="成绩管理模块-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<Scoremanagement>> queryPageList(Scoremanagement scoremanagement,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
        QueryWrapper<Scoremanagement> queryWrapper = QueryGenerator.initQueryWrapper(scoremanagement, req.getParameterMap());
		Page<Scoremanagement> page = new Page<Scoremanagement>(pageNo, pageSize);
		IPage<Scoremanagement> pageList = scoremanagementService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param scoremanagement
	 * @return
	 */
	@AutoLog(value = "成绩管理模块-添加")
	@Operation(summary = "成绩管理模块-添加")
	@RequiresPermissions("scoremanagement:scoremanagement:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody Scoremanagement scoremanagement) {
		scoremanagementService.save(scoremanagement);
		String ID = scoremanagement.getStudentid();
		QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("id", ID);
		Student student = studentService.getOne(queryWrapper);
		if (student != null) {
			String NAME = student.getName();
			return Result.OK("添加成功！");
		} else {
			return Result.error("未找到对应学生！");
		}
	}

	 @Autowired
	 private IStudentService studentService;
	
	/**
	 *  编辑
	 *
	 * @param scoremanagement
	 * @return
	 */
	@AutoLog(value = "成绩管理模块-编辑")
	@Operation(summary="成绩管理模块-编辑")
	@RequiresPermissions("scoremanagement:scoremanagement:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody Scoremanagement scoremanagement) {
		scoremanagementService.updateById(scoremanagement);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "成绩管理模块-通过id删除")
	@Operation(summary="成绩管理模块-通过id删除")
	@RequiresPermissions("scoremanagement:scoremanagement:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		scoremanagementService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "成绩管理模块-批量删除")
	@Operation(summary="成绩管理模块-批量删除")
	@RequiresPermissions("scoremanagement:scoremanagement:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.scoremanagementService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "成绩管理模块-通过id查询")
	@Operation(summary="成绩管理模块-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<Scoremanagement> queryById(@RequestParam(name="id",required=true) String id) {
		Scoremanagement scoremanagement = scoremanagementService.getById(id);
		if(scoremanagement==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(scoremanagement);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param scoremanagement
    */
    @RequiresPermissions("scoremanagement:scoremanagement:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Scoremanagement scoremanagement) {
        return super.exportXls(request, scoremanagement, Scoremanagement.class, "成绩管理模块");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("scoremanagement:scoremanagement:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Scoremanagement.class);
    }

}
