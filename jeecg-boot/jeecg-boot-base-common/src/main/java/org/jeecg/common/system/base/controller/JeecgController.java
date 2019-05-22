package org.jeecg.common.system.base.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecg.common.system.base.service.JeecgService;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: Controller基类
 * @Author: dangzhenghui@163.com
 * @Date: 2019-4-21 8:13
 * @Version: 1.0
 */
@Slf4j
public class JeecgController<T extends JeecgEntity, S extends JeecgService<T>> {
	@Autowired
	S service;

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param response
	 */
	protected ModelAndView exportXls(HttpServletRequest request,T object,Class<T> clazz,String title) {
		//--------------------------------------------------------------------------------
		//获取当前用户
		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		//--------------------------------------------------------------------------------
		// Step.1 组装查询条件
		QueryWrapper<T> queryWrapper = QueryGenerator.initQueryWrapper(object, request.getParameterMap());

		// Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<T> pageList = service.list(queryWrapper);
		// 导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, title); //此处设置的filename无效 ,前端会重更新设置一下
		mv.addObject(NormalExcelConstants.CLASS, clazz);
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams(title + "报表", "导出人:"+sysUser.getRealname(), title + "表"));
		mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
		return mv;
	}

	/**
	 * 通过excel导入数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	protected Result<?> importExcel(HttpServletRequest request, HttpServletResponse response, Class<T> clazz) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// 获取上传文件对象
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				List<T> list = ExcelImportUtil.importExcel(file.getInputStream(), clazz, params);
				for (T t : list) {
					service.save(t);
				}
				return Result.ok("文件导入成功！数据行数：" + list.size());
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				return Result.error("文件导入失败:" + e.getMessage());
			} finally {
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return Result.error("文件导入失败！");
	}
}
