package org.jeecg.modules.system.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysUserAgent;
import org.jeecg.modules.system.service.ISysUserAgentService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

 /**
 * @Title: Controller
 * @Description: 用户代理人设置
 * @Author: jeecg-boot
 * @Date:  2019-04-17
 * @Version: V1.0
 */
@RestController
@RequestMapping("/sys/sysUserAgent")
@Slf4j
public class SysUserAgentController {
	@Autowired
	private ISysUserAgentService sysUserAgentService;

	 @Value("${jeecg.path.upload}")
	 private String upLoadPath;
	
	/**
	  * 分页列表查询
	 * @param sysUserAgent
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<SysUserAgent>> queryPageList(SysUserAgent sysUserAgent,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<SysUserAgent>> result = new Result<IPage<SysUserAgent>>();
		QueryWrapper<SysUserAgent> queryWrapper = QueryGenerator.initQueryWrapper(sysUserAgent, req.getParameterMap());
		Page<SysUserAgent> page = new Page<SysUserAgent>(pageNo, pageSize);
		IPage<SysUserAgent> pageList = sysUserAgentService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param sysUserAgent
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<SysUserAgent> add(@RequestBody SysUserAgent sysUserAgent) {
		Result<SysUserAgent> result = new Result<SysUserAgent>();
		try {
			sysUserAgentService.save(sysUserAgent);
			result.success("代理人设置成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param sysUserAgent
	 * @return
	 */
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<SysUserAgent> edit(@RequestBody SysUserAgent sysUserAgent) {
		Result<SysUserAgent> result = new Result<SysUserAgent>();
		SysUserAgent sysUserAgentEntity = sysUserAgentService.getById(sysUserAgent.getId());
		if(sysUserAgentEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysUserAgentService.updateById(sysUserAgent);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("代理人设置成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<SysUserAgent> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysUserAgent> result = new Result<SysUserAgent>();
		SysUserAgent sysUserAgent = sysUserAgentService.getById(id);
		if(sysUserAgent==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysUserAgentService.removeById(id);
			if(ok) {
				result.success("删除成功!");
			}
		}
		
		return result;
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<SysUserAgent> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysUserAgent> result = new Result<SysUserAgent>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sysUserAgentService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<SysUserAgent> queryById(@RequestParam(name="id",required=true) String id) {
		Result<SysUserAgent> result = new Result<SysUserAgent>();
		SysUserAgent sysUserAgent = sysUserAgentService.getById(id);
		if(sysUserAgent==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(sysUserAgent);
			result.setSuccess(true);
		}
		return result;
	}
	
	/**
	  * 通过userName查询
	 * @param userName
	 * @return
	 */
	@GetMapping(value = "/queryByUserName")
	public Result<SysUserAgent> queryByUserName(@RequestParam(name="userName",required=true) String userName) {
		Result<SysUserAgent> result = new Result<SysUserAgent>();
		LambdaQueryWrapper<SysUserAgent> queryWrapper = new LambdaQueryWrapper<SysUserAgent>();
		queryWrapper.eq(SysUserAgent::getUserName, userName);
		SysUserAgent sysUserAgent = sysUserAgentService.getOne(queryWrapper);
		if(sysUserAgent==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(sysUserAgent);
			result.setSuccess(true);
		}
		return result;
	}

  /**
      * 导出excel
   *
   * @param sysUserAgent
   * @param request
   */
  @RequestMapping(value = "/exportXls")
  public ModelAndView exportXls(SysUserAgent sysUserAgent,HttpServletRequest request) {
      // Step.1 组装查询条件
      QueryWrapper<SysUserAgent> queryWrapper = QueryGenerator.initQueryWrapper(sysUserAgent, request.getParameterMap());
      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<SysUserAgent> pageList = sysUserAgentService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "用户代理人设置列表");
      mv.addObject(NormalExcelConstants.CLASS, SysUserAgent.class);
      LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
	  ExportParams exportParams = new ExportParams("用户代理人设置列表数据", "导出人:"+user.getRealname(), "导出信息");
	  exportParams.setImageBasePath(upLoadPath);
      mv.addObject(NormalExcelConstants.PARAMS, exportParams);
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
  @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
  public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          // 获取上传文件对象
          MultipartFile file = entity.getValue();
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<SysUserAgent> listSysUserAgents = ExcelImportUtil.importExcel(file.getInputStream(), SysUserAgent.class, params);
              for (SysUserAgent sysUserAgentExcel : listSysUserAgents) {
                  sysUserAgentService.save(sysUserAgentExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listSysUserAgents.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败！");
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
