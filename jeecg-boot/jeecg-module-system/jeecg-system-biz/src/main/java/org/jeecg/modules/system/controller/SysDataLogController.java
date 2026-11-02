package org.jeecg.modules.system.controller;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDataLog;
import org.jeecg.modules.system.service.ISysDataLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 系统数据日志
 * @author: jeecg-boot
 */
@RestController
@RequestMapping("/sys/dataLog")
@Slf4j
public class SysDataLogController {
	@Autowired
	private ISysDataLogService service;
	
	@RequiresPermissions("system:dataLog:list")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<IPage<SysDataLog>> queryPageList(SysDataLog dataLog,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
		Result<IPage<SysDataLog>> result = new Result<IPage<SysDataLog>>();
		dataLog.setType(CommonConstant.DATA_LOG_TYPE_JSON);
		QueryWrapper<SysDataLog> queryWrapper = QueryGenerator.initQueryWrapper(dataLog, req.getParameterMap());
		Page<SysDataLog> page = new Page<SysDataLog>(pageNo, pageSize);
		IPage<SysDataLog> pageList = service.page(page, queryWrapper);
		log.info("查询当前页："+pageList.getCurrent());
		log.info("查询当前页数量："+pageList.getSize());
		log.info("查询结果数量："+pageList.getRecords().size());
		log.info("数据总数："+pageList.getTotal());
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	 * 查询对比数据
	 * @param req
	 * @return
	 */
	@RequiresPermissions("system:dataLog:queryCompareList")
	@RequestMapping(value = "/queryCompareList", method = RequestMethod.GET)
	public Result<List<SysDataLog>> queryCompareList(HttpServletRequest req) {
		Result<List<SysDataLog>> result = new Result<>();
		String dataId1 = req.getParameter("dataId1");
		String dataId2 = req.getParameter("dataId2");
		List<String> idList = new ArrayList<String>();
		idList.add(dataId1);
		idList.add(dataId2);
		try {
			List<SysDataLog> list =  (List<SysDataLog>) service.listByIds(idList);
			result.setResult(list);
			result.setSuccess(true);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return result;
	}
	
	/**
	 * 查询版本信息
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/queryDataVerList", method = RequestMethod.GET)
	public Result<List<SysDataLog>> queryDataVerList(HttpServletRequest req) {
		//update-begin---author:wangshuai ---date:2026-10-25  for：【GHSA-p4f8-h832-99pw】数据日志含业务数据快照，除评论组件查询的评论日志(type=comment)外需 system:dataLog:queryDataVerList 按钮权限-----------
		if (!CommonConstant.DATA_LOG_TYPE_COMMENT.equals(req.getParameter("type")) && !SecurityUtils.getSubject().isPermitted("system:dataLog:queryDataVerList")) {
			return Result.noauth("没有权限，请联系管理员分配权限！");
		}
		//update-end---author:wangshuai ---date:2026-10-25  for：【GHSA-p4f8-h832-99pw】数据日志含业务数据快照，除评论组件查询的评论日志(type=comment)外需 system:dataLog:queryDataVerList 按钮权限-----------
		Result<List<SysDataLog>> result = new Result<>();
		String dataTable = req.getParameter("dataTable");
		String dataId = req.getParameter("dataId");
		QueryWrapper<SysDataLog> queryWrapper = new QueryWrapper<SysDataLog>();
		queryWrapper.eq("data_table", dataTable);
		queryWrapper.eq("data_id", dataId);
		// 代码逻辑说明: 新增查询条件-type
		String type = req.getParameter("type");
		if (oConvertUtils.isNotEmpty(type)) {
			queryWrapper.eq("type", type);
		}
		// 按时间倒序排
		queryWrapper.orderByDesc("create_time");

		List<SysDataLog> list = service.list(queryWrapper);
		if(list==null||list.size()<=0) {
			result.error500("未找到版本信息");
		}else {
			result.setResult(list);
			result.setSuccess(true);
		}
		return result;
	}
	
}
