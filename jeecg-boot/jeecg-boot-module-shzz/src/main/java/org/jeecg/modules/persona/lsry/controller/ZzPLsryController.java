package org.jeecg.modules.persona.lsry.controller;

import java.io.IOException;
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
import org.jeecg.modules.persona.lsry.entity.ZzPLsryPersona;
import org.jeecg.modules.persona.lsry.entity.ZzPLsry;
import org.jeecg.modules.persona.lsry.vo.ZzPLsryPage;
import org.jeecg.modules.persona.lsry.service.IZzPLsryService;
import org.jeecg.modules.persona.lsry.service.IZzPLsryPersonaService;
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

/**
 * @Description: 留守人员
 * @Author: jeecg-boot
 * @Date:   2020-02-21
 * @Version: V1.0
 */
@RestController
@RequestMapping("/lsry/zzPLsry")
@Slf4j
public class ZzPLsryController {
	@Autowired
	private IZzPLsryService zzPLsryService;
	@Autowired
	private IZzPLsryPersonaService zzPLsryPersonaService;
	
	/**
	 * 分页列表查询
	 *
	 * @param zzPLsry
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ZzPLsry zzPLsry,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzPLsry> queryWrapper = QueryGenerator.initQueryWrapper(zzPLsry, req.getParameterMap());
		Page<ZzPLsry> page = new Page<ZzPLsry>(pageNo, pageSize);
		IPage<ZzPLsry> pageList = zzPLsryService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param zzPLsryPage
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzPLsryPage zzPLsryPage) {
		ZzPLsry zzPLsry = new ZzPLsry();
		BeanUtils.copyProperties(zzPLsryPage, zzPLsry);
		zzPLsryService.saveMain(zzPLsry, zzPLsryPage.getZzPLsryPersonaList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param zzPLsryPage
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzPLsryPage zzPLsryPage) {
		ZzPLsry zzPLsry = new ZzPLsry();
		BeanUtils.copyProperties(zzPLsryPage, zzPLsry);
		ZzPLsry zzPLsryEntity = zzPLsryService.getById(zzPLsry.getId());
		if(zzPLsryEntity==null) {
			return Result.error("未找到对应数据");
		}
		zzPLsryService.updateMain(zzPLsry, zzPLsryPage.getZzPLsryPersonaList());
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		zzPLsryService.delMain(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.zzPLsryService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ZzPLsry zzPLsry = zzPLsryService.getById(id);
		if(zzPLsry==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(zzPLsry);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryZzPLsryPersonaByMainId")
	public Result<?> queryZzPLsryPersonaListByMainId(@RequestParam(name="id",required=true) String id) {
		List<ZzPLsryPersona> zzPLsryPersonaList = zzPLsryPersonaService.selectByMainId(id);
		return Result.ok(zzPLsryPersonaList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param zzPLsry
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZzPLsry zzPLsry) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<ZzPLsry> queryWrapper = QueryGenerator.initQueryWrapper(zzPLsry, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<ZzPLsry> queryList = zzPLsryService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<ZzPLsry> zzPLsryList = new ArrayList<ZzPLsry>();
      if(oConvertUtils.isEmpty(selections)) {
          zzPLsryList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          zzPLsryList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<ZzPLsryPage> pageList = new ArrayList<ZzPLsryPage>();
      for (ZzPLsry main : zzPLsryList) {
          ZzPLsryPage vo = new ZzPLsryPage();
          BeanUtils.copyProperties(main, vo);
          List<ZzPLsryPersona> zzPLsryPersonaList = zzPLsryPersonaService.selectByMainId(main.getId());
          vo.setZzPLsryPersonaList(zzPLsryPersonaList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "留守人员列表");
      mv.addObject(NormalExcelConstants.CLASS, ZzPLsryPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("留守人员数据", "导出人:"+sysUser.getRealname(), "留守人员"));
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
              List<ZzPLsryPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ZzPLsryPage.class, params);
              for (ZzPLsryPage page : list) {
                  ZzPLsry po = new ZzPLsry();
                  BeanUtils.copyProperties(page, po);
                  zzPLsryService.saveMain(po, page.getZzPLsryPersonaList());
              }
              return Result.ok("文件导入成功！数据行数:" + list.size());
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
      return Result.ok("文件导入失败！");
    }

}
