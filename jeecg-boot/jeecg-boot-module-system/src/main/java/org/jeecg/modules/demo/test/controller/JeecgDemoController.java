package org.jeecg.modules.demo.test.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.aspect.annotation.PermissionData;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.DateUtils;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.demo.test.entity.JeecgDemo;
import org.jeecg.modules.demo.test.service.IJeecgDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 测试demo 
 * @Author: jeecg-boot 
 * @Date:2018-12-29 
 * @Version:V1.0
 */
@Slf4j
@Api(tags="单表DEMO")
@RestController
@RequestMapping("/test/jeecgDemo")
public class JeecgDemoController extends JeecgController<JeecgDemo,IJeecgDemoService> {
	@Autowired
	private IJeecgDemoService jeecgDemoService;

	@Autowired
	private RedisUtil redisUtil;

	/**
	 * 分页列表查询
	 * 
	 * @param jeecgDemo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@ApiOperation(value = "获取Demo数据列表", notes = "获取所有Demo数据列表")
	@GetMapping(value = "/list")
	@PermissionData(pageComponent="jeecg/JeecgDemoList")
	public Result<IPage<JeecgDemo>> list(JeecgDemo jeecgDemo, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
			HttpServletRequest req) {
		Result<IPage<JeecgDemo>> result = new Result<IPage<JeecgDemo>>();
		/*
		 * QueryWrapper<JeecgDemo> queryWrapper = null;
		 * //===========================================================================
		 * ===== //高级组合查询 try { String superQueryParams =
		 * req.getParameter("superQueryParams");
		 * if(oConvertUtils.isNotEmpty(superQueryParams)) { // 解码 superQueryParams =
		 * URLDecoder.decode(superQueryParams, "UTF-8"); List<QueryRuleVo> userList =
		 * JSON.parseArray(superQueryParams, QueryRuleVo.class);
		 * log.info(superQueryParams); queryWrapper = new QueryWrapper<JeecgDemo>(); for
		 * (QueryRuleVo rule : userList) { if(oConvertUtils.isNotEmpty(rule.getField())
		 * && oConvertUtils.isNotEmpty(rule.getRule()) &&
		 * oConvertUtils.isNotEmpty(rule.getVal())){
		 * ObjectParseUtil.addCriteria(queryWrapper, rule.getField(),
		 * QueryRuleEnum.getByValue(rule.getRule()), rule.getVal()); } } } } catch
		 * (UnsupportedEncodingException e) { e.printStackTrace(); }
		 * //===========================================================================
		 * =====
		 * 
		 * // 手工转换实体驼峰字段为下划线分隔表字段 queryWrapper = queryWrapper==null?new
		 * QueryWrapper<JeecgDemo>(jeecgDemo):queryWrapper; Page<JeecgDemo> page = new
		 * Page<JeecgDemo>(pageNo, pageSize);
		 * 
		 * // 排序逻辑 处理 String column = req.getParameter("column"); String order =
		 * req.getParameter("order"); if (oConvertUtils.isNotEmpty(column) &&
		 * oConvertUtils.isNotEmpty(order)) { if ("asc".equals(order)) {
		 * queryWrapper.orderByAsc(oConvertUtils.camelToUnderline(column)); } else {
		 * queryWrapper.orderByDesc(oConvertUtils.camelToUnderline(column)); } }
		 */

		QueryWrapper<JeecgDemo> queryWrapper = QueryGenerator.initQueryWrapper(jeecgDemo, req.getParameterMap());
		Page<JeecgDemo> page = new Page<JeecgDemo>(pageNo, pageSize);

		IPage<JeecgDemo> pageList = jeecgDemoService.page(page, queryWrapper);
//		log.info("查询当前页：" + pageList.getCurrent());
//		log.info("查询当前页数量：" + pageList.getSize());
//		log.info("查询结果数量：" + pageList.getRecords().size());
//		log.info("数据总数：" + pageList.getTotal());
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 * 添加
	 * 
	 * @param jeecgDemo
	 * @return
	 */
	@PostMapping(value = "/add")
	@AutoLog(value = "添加测试DEMO")
	@ApiOperation(value = "添加DEMO", notes = "添加DEMO")
	public Result<JeecgDemo> add(@RequestBody JeecgDemo jeecgDemo) {
		Result<JeecgDemo> result = new Result<JeecgDemo>();
		try {
			jeecgDemoService.save(jeecgDemo);
			result.success("添加成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	 * 编辑
	 * 
	 * @param jeecgDemo
	 * @return
	 */
	@PutMapping(value = "/edit")
	@ApiOperation(value = "编辑DEMO", notes = "编辑DEMO")
	public Result<JeecgDemo> eidt(@RequestBody JeecgDemo jeecgDemo) {
		Result<JeecgDemo> result = new Result<JeecgDemo>();
		JeecgDemo jeecgDemoEntity = jeecgDemoService.getById(jeecgDemo.getId());
		if (jeecgDemoEntity == null) {
			result.error500("未找到对应实体");
		} else {
			boolean ok = jeecgDemoService.updateById(jeecgDemo);
			// TODO 返回false说明什么？
			if (ok) {
				result.success("修改成功!");
			}
		}

		return result;
	}

	/**
	 * 通过id删除
	 * 
	 * @param id
	 * @return
	 */
	@AutoLog(value = "删除测试DEMO")
	@DeleteMapping(value = "/delete")
	@ApiOperation(value = "通过ID删除DEMO", notes = "通过ID删除DEMO")
	public Result<JeecgDemo> delete(@RequestParam(name = "id", required = true) String id) {
		Result<JeecgDemo> result = new Result<JeecgDemo>();
		JeecgDemo jeecgDemo = jeecgDemoService.getById(id);
		if (jeecgDemo == null) {
			result.error500("未找到对应实体");
		} else {
			boolean ok = jeecgDemoService.removeById(id);
			if (ok) {
				result.success("删除成功!");
			}
		}

		return result;
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	@ApiOperation(value = "批量删除DEMO", notes = "批量删除DEMO")
	public Result<JeecgDemo> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		Result<JeecgDemo> result = new Result<JeecgDemo>();
		if (ids == null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		} else {
			this.jeecgDemoService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

	/**
	 * 通过id查询
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	@ApiOperation(value = "通过ID查询DEMO", notes = "通过ID查询DEMO")
	public Result<JeecgDemo> queryById(@ApiParam(name = "id", value = "示例id", required = true) @RequestParam(name = "id", required = true) String id) {
		Result<JeecgDemo> result = new Result<JeecgDemo>();
		JeecgDemo jeecgDemo = jeecgDemoService.getById(id);
		if (jeecgDemo == null) {
			result.error500("未找到对应实体");
		} else {
			result.setResult(jeecgDemo);
			result.setSuccess(true);
		}
		return result;
	}

	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/exportXls")
	@PermissionData(pageComponent="jeecg/JeecgDemoList")
	public ModelAndView exportXls(HttpServletRequest request, JeecgDemo jeecgDemo) {
		return super.exportXls(request, jeecgDemo, JeecgDemo.class, "单表模型");
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
		return super.importExcel(request, response, JeecgDemo.class);
	}

	// ================================================================================================================
	/**
	 * redis操作 -- set
	 */
	@GetMapping(value = "/redisSet")
	public void redisSet() {
		redisUtil.set("name", "张三" + DateUtils.now());
	}

	/**
	 * redis操作 -- get
	 */
	@GetMapping(value = "/redisGet")
	public String redisGet() {
		return (String) redisUtil.get("name");
	}

	/**
	 * redis操作 -- setObj
	 */
	@GetMapping(value = "/redisSetObj")
	public void redisSetObj() {
		JeecgDemo p = new JeecgDemo();
		p.setAge(10);
		p.setBirthday(new Date());
		p.setContent("hello");
		p.setName("张三");
		p.setSex("男");
		redisUtil.set("user-zdh", p);
	}

	/**
	 * redis操作 -- setObj
	 */
	@GetMapping(value = "/redisGetObj")
	public Object redisGetObj() {
		return redisUtil.get("user-zdh");
	}

	/**
	 * redis操作 -- get
	 */
	@GetMapping(value = "/redisDemo/{id}")
	public JeecgDemo redisGetJeecgDemo(@PathVariable("id") String id) {
		JeecgDemo t = jeecgDemoService.getByIdCacheable(id);
		System.out.println(t);
		return t;
	}

	/**
	 * freemaker方式 【页面路径： src/main/resources/templates】
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("/demo3")
	public ModelAndView demo3(ModelAndView modelAndView) {
		modelAndView.setViewName("demo3");
		List<String> userList = new ArrayList<String>();
		userList.add("admin");
		userList.add("user1");
		userList.add("user2");
		log.info("--------------test--------------");
		modelAndView.addObject("userList", userList);
		return modelAndView;
	}

	// ================================================================================================================

	// ==========================================动态表单
	// JSON接收测试===========================================//
	@PostMapping(value = "/testOnlineAdd")
	public Result<JeecgDemo> testOnlineAdd(@RequestBody JSONObject json) {
		Result<JeecgDemo> result = new Result<JeecgDemo>();
		log.info(json.toJSONString());
		result.success("添加成功！");
		return result;
	}

}
