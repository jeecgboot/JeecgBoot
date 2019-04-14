package org.jeecg.modules.message.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.message.entity.MsgParams;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.entity.SysMessageTemplate;
import org.jeecg.modules.message.service.ISysMessageService;
import org.jeecg.modules.message.service.ISysMessageTemplateService;
import org.jeecg.modules.message.util.PushMsgUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;

 /**
 * @Title: Controller
 * @Description: 消息模板
 * @author： jeecg-boot
 * @date：   2019-04-09
 * @version： V1.0
 */
@RestController
@RequestMapping("/message/sysMessageTemplate")
@Slf4j
public class SysMessageTemplateController {
	@Autowired
	private ISysMessageTemplateService sysMessageTemplateService;
	@Autowired
	private ISysMessageService sysMessageService;
	@Autowired
	private PushMsgUtil pushMsgUtil;
	
	/**
	  * 分页列表查询
	 * @param sysMessageTemplate
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<SysMessageTemplate>> queryPageList(SysMessageTemplate sysMessageTemplate,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<SysMessageTemplate>> result = new Result<IPage<SysMessageTemplate>>();
		QueryWrapper<SysMessageTemplate> queryWrapper = QueryGenerator.initQueryWrapper(sysMessageTemplate, req.getParameterMap());
		Page<SysMessageTemplate> page = new Page<SysMessageTemplate>(pageNo, pageSize);
		IPage<SysMessageTemplate> pageList = sysMessageTemplateService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param sysMessageTemplate
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<SysMessageTemplate> add(@RequestBody SysMessageTemplate sysMessageTemplate) {
		Result<SysMessageTemplate> result = new Result<SysMessageTemplate>();
		try {
			sysMessageTemplateService.save(sysMessageTemplate);
			result.success("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param sysMessageTemplate
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<SysMessageTemplate> edit(@RequestBody SysMessageTemplate sysMessageTemplate) {
		Result<SysMessageTemplate> result = new Result<SysMessageTemplate>();
		SysMessageTemplate sysMessageTemplateEntity = sysMessageTemplateService.getById(sysMessageTemplate.getId());
		if(sysMessageTemplateEntity==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysMessageTemplateService.updateById(sysMessageTemplate);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("修改成功!");
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
	public Result<SysMessageTemplate> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysMessageTemplate> result = new Result<SysMessageTemplate>();
		SysMessageTemplate sysMessageTemplate = sysMessageTemplateService.getById(id);
		if(sysMessageTemplate==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysMessageTemplateService.removeById(id);
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
	public Result<SysMessageTemplate> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysMessageTemplate> result = new Result<SysMessageTemplate>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sysMessageTemplateService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<SysMessageTemplate> queryById(@RequestParam(name="id",required=true) String id) {
		Result<SysMessageTemplate> result = new Result<SysMessageTemplate>();
		SysMessageTemplate sysMessageTemplate = sysMessageTemplateService.getById(id);
		if(sysMessageTemplate==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(sysMessageTemplate);
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
  public ModelAndView exportXls(HttpServletRequest request, HttpServletResponse response) {
      // Step.1 组装查询条件
      QueryWrapper<SysMessageTemplate> queryWrapper = null;
      try {
          String paramsStr = request.getParameter("paramsStr");
          if (oConvertUtils.isNotEmpty(paramsStr)) {
              String deString = URLDecoder.decode(paramsStr, "UTF-8");
              SysMessageTemplate sysMessageTemplate = JSON.parseObject(deString, SysMessageTemplate.class);
              queryWrapper = QueryGenerator.initQueryWrapper(sysMessageTemplate, request.getParameterMap());
          }
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      //Step.2 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      List<SysMessageTemplate> pageList = sysMessageTemplateService.list(queryWrapper);
      //导出文件名称
      mv.addObject(NormalExcelConstants.FILE_NAME, "消息模板列表");
      mv.addObject(NormalExcelConstants.CLASS, SysMessageTemplate.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("消息模板列表数据", "导出人:Jeecg", "导出信息"));
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
          MultipartFile file = entity.getValue();// 获取上传文件对象
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<SysMessageTemplate> listSysMessageTemplates = ExcelImportUtil.importExcel(file.getInputStream(), SysMessageTemplate.class, params);
              for (SysMessageTemplate sysMessageTemplateExcel : listSysMessageTemplates) {
                  sysMessageTemplateService.save(sysMessageTemplateExcel);
              }
              return Result.ok("文件导入成功！数据行数：" + listSysMessageTemplates.size());
          } catch (Exception e) {
              log.error(e.getMessage());
              return Result.error("文件导入失败！");
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.ok("文件导入失败！");
  }
  
	/**
	 * 发送消息
	 */
	@PostMapping(value = "/sendMsg")
	public Result<SysMessageTemplate> sendMessage(@RequestBody MsgParams msgParams) {
		Result<SysMessageTemplate> result = new Result<SysMessageTemplate>();
		Map<String, String> map = null;
		try {
			map = (Map<String, String>) JSON.parse(msgParams.getTestData());
		} catch (Exception e) {
			result.error500("解析Json出错！");
			return result;
		}
		boolean is_sendSuccess = pushMsgUtil.sendMessage(msgParams.getMsgType(), msgParams.getTemplateCode(), map,
				msgParams.getReceiver());
		if (is_sendSuccess)
			result.success("发送消息任务添加成功！");
		else
			result.error500("发送消息任务添加失败！");
		return result;
	}
}
