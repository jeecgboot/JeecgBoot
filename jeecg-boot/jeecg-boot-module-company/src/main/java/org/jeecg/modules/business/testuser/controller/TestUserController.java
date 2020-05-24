package org.jeecg.modules.business.testuser.controller;

import java.util.Arrays;
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
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.testuser.entity.TestUser;
import org.jeecg.modules.business.testuser.service.ITestUserService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 测试用户
 * @Author: jeecg-boot
 * @Date:   2020-05-24
 * @Version: V1.0
 */
@Api(tags="测试用户")
@RestController
@RequestMapping("/testuser/testUser")
@Slf4j
public class TestUserController extends JeecgController<TestUser, ITestUserService> {
	@Autowired
	private ITestUserService testUserService;
	
	/**
	 * 分页列表查询
	 *
	 * @param testUser
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "测试用户-分页列表查询")
	@ApiOperation(value="测试用户-分页列表查询", notes="测试用户-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(TestUser testUser,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TestUser> queryWrapper = QueryGenerator.initQueryWrapper(testUser, req.getParameterMap());
		Page<TestUser> page = new Page<TestUser>(pageNo, pageSize);
		IPage<TestUser> pageList = testUserService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param testUser
	 * @return
	 */
	@AutoLog(value = "测试用户-添加")
	@ApiOperation(value="测试用户-添加", notes="测试用户-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody TestUser testUser) {
		testUserService.save(testUser);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param testUser
	 * @return
	 */
	@AutoLog(value = "测试用户-编辑")
	@ApiOperation(value="测试用户-编辑", notes="测试用户-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody TestUser testUser) {
		testUserService.updateById(testUser);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试用户-通过id删除")
	@ApiOperation(value="测试用户-通过id删除", notes="测试用户-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		testUserService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "测试用户-批量删除")
	@ApiOperation(value="测试用户-批量删除", notes="测试用户-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.testUserService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "测试用户-通过id查询")
	@ApiOperation(value="测试用户-通过id查询", notes="测试用户-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		TestUser testUser = testUserService.getById(id);
		if(testUser==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(testUser);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param testUser
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TestUser testUser) {
        return super.exportXls(request, testUser, TestUser.class, "测试用户");
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
        return super.importExcel(request, response, TestUser.class);
    }

}
