package org.jeecg.modules.online.cgreport.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgreport.def.CgReportConstant;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportHead;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportItem;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportHeadService;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportItemService;
import org.jeecg.modules.online.cgreport.util.BrowserUtils;
import org.jeecg.modules.online.cgreport.util.CgReportQueryParamUtil;
import org.jeecg.modules.online.cgreport.util.SqlUtil;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.params.ExcelExportEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @Title: Controller
 * @Description: 在线报表配置
 * @author: jeecg-boot
 * @date: 2019-03-08
 * @version: V1.0
 */
@Slf4j
@RestController
@RequestMapping("/online/cgreport/api")
public class OnlCgreportAPI {

	@Autowired
	private IOnlCgreportHeadService onlCgreportHeadService;
	@Autowired
	private IOnlCgreportItemService onlCgreportItemService;
	@Autowired
	private ISysDictService sysDictService;

	/**
	 * 通过 head code 获取 所有的 item，并生成 columns 所需的 json
	 * 
	 * @param code
	 *            head code
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/getColumns/{code}")
	public Result<?> getColumns(@PathVariable("code") String code) {

		QueryWrapper<OnlCgreportItem> queryWrapper = new QueryWrapper<OnlCgreportItem>();
		queryWrapper.eq("cgrhead_id", code);
		queryWrapper.eq("is_show", 1);
		queryWrapper.orderByAsc("order_num");

		List<OnlCgreportItem> list = onlCgreportItemService.list(queryWrapper);

		List<Map<String, String>> array = new ArrayList<Map<String, String>>();
		for (OnlCgreportItem item : list) {
			Map<String, String> column = new HashMap<String, String>(3);
			column.put("title", item.getFieldTxt());
			column.put("dataIndex", item.getFieldName());
			column.put("align", "center");
			column.put("sorter", "true");

			array.add(column);
		}

		Map<String, Object> result = new HashMap<String, Object>(1);
		result.put("columns", array);

		return Result.ok(result);
	}

	/**
	 * 通过 head code 获取 sql语句，并执行该语句返回查询数据
	 * 
	 * @param code
	 * @return
	 */
	@GetMapping(value = "/getData/{code}")
	public Result<?> getData(@PathVariable("code") String code,HttpServletRequest request) {
		OnlCgreportHead head = onlCgreportHeadService.getById(code);
		if (head == null) {
			return Result.error("实体不存在");
		}
		String sql = head.getCgrSql();
		try {
			Map<String,Object> params = SqlUtil.getParameterMap(request);
			Map<String, Object> reslutMap = onlCgreportHeadService.executeSelectSql(sql,params);
			return Result.ok(reslutMap);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("SQL执行失败：" + e.getMessage());
		}

	}
	
	
	/**
	 * 获取查询条件
	 */
	@GetMapping(value = "/getQueryInfo/{code}")
	public Result<?> getQueryInfo(@PathVariable("code") String cgrheadId) {
	    try {
	    	List<Map<String,String>> list = onlCgreportItemService.getAutoListQueryInfo(cgrheadId);
			return Result.ok(list);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("OnlCgformApiController.getQueryInfo()发生异常：" + e.getMessage());
			return Result.error("查询失败");
		}
	}
	
	
	/**
	 * 将报表导出为excel
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/exportXls/{reportId}")
	public void exportXls(@PathVariable("reportId") String reportId,HttpServletRequest request,HttpServletResponse response) {
		//step.1 设置，获取配置信息
		String codedFileName = "报表";
		String sheetName="导出信息";
		if (oConvertUtils.isNotEmpty(reportId)) {
			Map<String, Object> cgReportMap = null;
			try{
				cgReportMap = onlCgreportHeadService.queryCgReportConfig(reportId);
			}catch (Exception e) {
				throw new JeecgBootException("动态报表配置不存在!");
			}
			List<Map<String,Object>> fieldList = (List<Map<String, Object>>) cgReportMap.get(CgReportConstant.ITEMS);
			Map<String, Object> configM = (Map<String, Object>) cgReportMap.get(CgReportConstant.MAIN);
			codedFileName = configM.get("name")+codedFileName;
			String querySql = (String) configM.get(CgReportConstant.CONFIG_SQL);
			List<Map<String,Object>> items = (List<Map<String, Object>>) cgReportMap.get(CgReportConstant.ITEMS);
			List<String> paramList = (List<String>) cgReportMap.get(CgReportConstant.PARAMS);
			//页面参数查询字段（占位符的条件语句）
			Map<String, Object> pageSearchFields =  new LinkedHashMap<String,Object>();
			//step.2 装载查询条件
			Map<String,Object> paramData = new HashMap<String, Object>();
			if(paramList!=null&&paramList.size()>0){
				for(String param :paramList){
					String value = request.getParameter(param);
					value = value==null?"":value;
					querySql = querySql.replace("'${"+param+"}'", ":"+param);
					querySql = querySql.replace("${"+param+"}", ":"+param);
					paramData.put(param, value);
				}
			}
			for(Map<String,Object> item:items){
				String isQuery = item.get(CgReportConstant.ITEM_ISQUERY).toString();
				if(CgReportConstant.BOOL_TRUE.equalsIgnoreCase(isQuery)){
					CgReportQueryParamUtil.loadQueryParams(request, item, pageSearchFields,paramData);
				}
			}
			//step.3 进行查询返回结果
            String dbKey=(String)configM.get("db_source");
            List<Map<?, ?>> result=null;
            if(oConvertUtils.isNotEmpty(dbKey)){
            	if(paramData!=null&&paramData.size()>0){
            		//TODO 动态数据源功能未实现
            		//result= DynamicDBUtil.findListByHash(dbKey,SqlUtil.getFullSql(querySql,pageSearchFields),(HashMap<String, Object>)paramData);
            	}else{
            		//result= DynamicDBUtil.findList(dbKey, SqlUtil.getFullSql(querySql,pageSearchFields));
            	}
            }else{
                result= onlCgreportHeadService.queryByCgReportSql(querySql, pageSearchFields,paramData, -1, -1);
            }
            
			// Step.4 组装AutoPoi所需参数
			List<ExcelExportEntity> entityList = new ArrayList<ExcelExportEntity>();
			for (int i = 0; i < fieldList.size(); i++) {
				// 过滤不展示的字段
				if ("1".equals(oConvertUtils.getString(fieldList.get(i).get("is_show")))) {
					ExcelExportEntity expEntity = new ExcelExportEntity(fieldList.get(i).get("field_txt").toString(), fieldList.get(i).get("field_name"), 15);
					
					//1. 字典值设置
					Object dictCode = fieldList.get(i).get("dict_code");
					if (oConvertUtils.isNotEmpty(dictCode)) {
						List<String> dictReplaces = new ArrayList<String>();
						List<Map<String, Object>> dictList = sysDictService.queryDictItemsByCode(dictCode.toString());
						for (Map<String, Object> d : dictList) {
							dictReplaces.add(d.get("text") + "_" + d.get("value"));
						}
						//男_1, 女_2
						expEntity.setReplace(dictReplaces.toArray(new String[dictReplaces.size()]));
					}
					//2. 取值表达式(男_1, 女_2)
					//TODO oracle下大小写兼容问题
					Object replace_val = fieldList.get(i).get("replace_val");
					if(oConvertUtils.isNotEmpty(replace_val)) {
						expEntity.setReplace(replace_val.toString().split(","));
					}
					entityList.add(expEntity);
				}
			}
			
			//---------------------------------------------------------------------------------------------------------------------
			response.setContentType("application/vnd.ms-excel");
			OutputStream fOut = null;
			try {

				// step.4 根据浏览器进行转码，使其支持中文文件名
				String browse = BrowserUtils.checkBrowse(request);
				if ("MSIE".equalsIgnoreCase(browse.substring(0, 4))) {
					response.setHeader("content-disposition", "attachment;filename=" + java.net.URLEncoder.encode(codedFileName, "UTF-8") + ".xls");
				} else {
					String newtitle = new String(codedFileName.getBytes("UTF-8"), "ISO8859-1");
					response.setHeader("content-disposition", "attachment;filename=" + newtitle + ".xls");
				}
				// step.5 产生工作簿对象
				//HSSFWorkbook workbook = cgReportExcelService.exportExcel(codedFileName, fieldList, result);
				Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(null,sheetName), entityList, result);
				fOut = response.getOutputStream();
				workbook.write(fOut);
			} catch (Exception e) {

			} finally {
				try {
					fOut.flush();
					fOut.close();
				} catch (Exception e) {

				}
			}
			//---------------------------------------------------------------------------------------------------------------------
			
		} else {
			throw new JeecgBootException("参数错误");
		}
	}

}
