package ${bussiPackage}.${entityPackage}.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
<#list subTables as sub>
import ${bussiPackage}.${entityPackage}.entity.${sub.entityName};
</#list>
import ${bussiPackage}.${entityPackage}.entity.${entityName};
import ${bussiPackage}.${entityPackage}.vo.${entityName}Page;
import ${bussiPackage}.${entityPackage}.service.I${entityName}Service;
<#list subTables as sub>
import ${bussiPackage}.${entityPackage}.service.I${sub.entityName}Service;
</#list>
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
<#assign bpm_flag=false>
<#list originalColumns as po>
<#if po.fieldDbName=='bpm_status'>
  <#assign bpm_flag=true>
</#if>
</#list>

 /**
 * @Description: ${tableVo.ftlDescription}
 * @Author: jeecg-boot
 * @Date:   ${.now?string["yyyy-MM-dd"]}
 * @Version: V1.0
 */
@Api(tags="${tableVo.ftlDescription}")
@RestController
@RequestMapping("/${entityPackage}/${entityName?uncap_first}")
@Slf4j
public class ${entityName}Controller {
	@Autowired
	private I${entityName}Service ${entityName?uncap_first}Service;
	<#list subTables as sub>
	@Autowired
	private I${sub.entityName}Service ${sub.entityName?uncap_first}Service;
	</#list>
	
	/**
	 * 分页列表查询
	 *
	 * @param ${entityName?uncap_first}
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "${tableVo.ftlDescription}-分页列表查询")
	@ApiOperation(value="${tableVo.ftlDescription}-分页列表查询", notes="${tableVo.ftlDescription}-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(${entityName} ${entityName?uncap_first},
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<${entityName}> queryWrapper = QueryGenerator.initQueryWrapper(${entityName?uncap_first}, req.getParameterMap());
		Page<${entityName}> page = new Page<${entityName}>(pageNo, pageSize);
		IPage<${entityName}> pageList = ${entityName?uncap_first}Service.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param ${entityName?uncap_first}Page
	 * @return
	 */
	@AutoLog(value = "${tableVo.ftlDescription}-添加")
	@ApiOperation(value="${tableVo.ftlDescription}-添加", notes="${tableVo.ftlDescription}-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ${entityName}Page ${entityName?uncap_first}Page) {
		${entityName} ${entityName?uncap_first} = new ${entityName}();
		BeanUtils.copyProperties(${entityName?uncap_first}Page, ${entityName?uncap_first});
		<#if bpm_flag>
        ${entityName?uncap_first}.setBpmStatus("1");
        </#if>
		${entityName?uncap_first}Service.saveMain(${entityName?uncap_first}, <#list subTables as sub>${entityName?uncap_first}Page.get${sub.entityName}List()<#if sub_has_next>,</#if></#list>);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param ${entityName?uncap_first}Page
	 * @return
	 */
	@AutoLog(value = "${tableVo.ftlDescription}-编辑")
	@ApiOperation(value="${tableVo.ftlDescription}-编辑", notes="${tableVo.ftlDescription}-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ${entityName}Page ${entityName?uncap_first}Page) {
		${entityName} ${entityName?uncap_first} = new ${entityName}();
		BeanUtils.copyProperties(${entityName?uncap_first}Page, ${entityName?uncap_first});
		${entityName} ${entityName?uncap_first}Entity = ${entityName?uncap_first}Service.getById(${entityName?uncap_first}.getId());
		if(${entityName?uncap_first}Entity==null) {
			return Result.error("未找到对应数据");
		}
		${entityName?uncap_first}Service.updateMain(${entityName?uncap_first}, <#list subTables as sub>${entityName?uncap_first}Page.get${sub.entityName}List()<#if sub_has_next>,</#if></#list>);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "${tableVo.ftlDescription}-通过id删除")
	@ApiOperation(value="${tableVo.ftlDescription}-通过id删除", notes="${tableVo.ftlDescription}-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		${entityName?uncap_first}Service.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "${tableVo.ftlDescription}-批量删除")
	@ApiOperation(value="${tableVo.ftlDescription}-批量删除", notes="${tableVo.ftlDescription}-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.${entityName?uncap_first}Service.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "${tableVo.ftlDescription}-通过id查询")
	@ApiOperation(value="${tableVo.ftlDescription}-通过id查询", notes="${tableVo.ftlDescription}-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		${entityName} ${entityName?uncap_first} = ${entityName?uncap_first}Service.getById(id);
		if(${entityName?uncap_first}==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(${entityName?uncap_first});

	}
	
	<#list subTables as sub>
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "${sub.ftlDescription}通过主表ID查询")
	@ApiOperation(value="${sub.ftlDescription}主表ID查询", notes="${sub.ftlDescription}-通主表ID查询")
	@GetMapping(value = "/query${sub.entityName}ByMainId")
	public Result<?> query${sub.entityName}ListByMainId(@RequestParam(name="id",required=true) String id) {
		List<${sub.entityName}> ${sub.entityName?uncap_first}List = ${sub.entityName?uncap_first}Service.selectByMainId(id);
		return Result.OK(${sub.entityName?uncap_first}List);
	}
	</#list>

    /**
    * 导出excel
    *
    * @param request
    * @param ${entityName?uncap_first}
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ${entityName} ${entityName?uncap_first}) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<${entityName}> queryWrapper = QueryGenerator.initQueryWrapper(${entityName?uncap_first}, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<${entityName}> queryList = ${entityName?uncap_first}Service.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<${entityName}> ${entityName?uncap_first}List = new ArrayList<${entityName}>();
      if(oConvertUtils.isEmpty(selections)) {
          ${entityName?uncap_first}List = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          ${entityName?uncap_first}List = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<${entityName}Page> pageList = new ArrayList<${entityName}Page>();
      for (${entityName} main : ${entityName?uncap_first}List) {
          ${entityName}Page vo = new ${entityName}Page();
          BeanUtils.copyProperties(main, vo);
          <#list subTables as sub>
          List<${sub.entityName}> ${sub.entityName?uncap_first}List = ${sub.entityName?uncap_first}Service.selectByMainId(main.getId());
          vo.set${sub.entityName}List(${sub.entityName?uncap_first}List);
          </#list>
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "${tableVo.ftlDescription}列表");
      mv.addObject(NormalExcelConstants.CLASS, ${entityName}Page.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("${tableVo.ftlDescription}数据", "导出人:"+sysUser.getRealname(), "${tableVo.ftlDescription}"));
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
              List<${entityName}Page> list = ExcelImportUtil.importExcel(file.getInputStream(), ${entityName}Page.class, params);
              for (${entityName}Page page : list) {
                  ${entityName} po = new ${entityName}();
                  BeanUtils.copyProperties(page, po);
                  ${entityName?uncap_first}Service.saveMain(po, <#list subTables as sub>page.get${sub.entityName}List()<#if sub_has_next>,</#if></#list>);
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("文件导入失败！");
    }

}
