package org.jeecg.modules.persona.hjrk.controller;

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
import org.jeecg.modules.persona.hjrk.entity.ZzPersona;
import org.jeecg.modules.persona.hjrk.entity.ZzPHjrk;
import org.jeecg.modules.persona.hjrk.vo.ZzPHjrkPage;
import org.jeecg.modules.persona.hjrk.service.IZzPHjrkService;
import org.jeecg.modules.persona.hjrk.service.IZzPersonaService;
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
 * @Description: 户籍人口
 * @Author: jeecg-boot
 * @Date:   2020-02-20
 * @Version: V1.0
 */
@RestController
@RequestMapping("/hjrk/zzPHjrk")
@Slf4j
public class ZzPHjrkController {
	@Autowired
	private IZzPHjrkService zzPHjrkService;
	@Autowired
	private IZzPersonaService zzPersonaService;
	
	/**
	 * 分页列表查询
	 *
	 * @param zzPHjrk
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ZzPHjrk zzPHjrk,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzPHjrk> queryWrapper = QueryGenerator.initQueryWrapper(zzPHjrk, req.getParameterMap());
		Page<ZzPHjrk> page = new Page<ZzPHjrk>(pageNo, pageSize);
		IPage<ZzPHjrk> pageList = zzPHjrkService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param zzPHjrkPage
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzPHjrkPage zzPHjrkPage) {
		ZzPHjrk zzPHjrk = new ZzPHjrk();
		BeanUtils.copyProperties(zzPHjrkPage, zzPHjrk);
		zzPHjrkService.saveMain(zzPHjrk, zzPHjrkPage.getZzPersonaList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param zzPHjrkPage
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzPHjrkPage zzPHjrkPage) {
		ZzPHjrk zzPHjrk = new ZzPHjrk();
		BeanUtils.copyProperties(zzPHjrkPage, zzPHjrk);
		ZzPHjrk zzPHjrkEntity = zzPHjrkService.getById(zzPHjrk.getId());
		if(zzPHjrkEntity==null) {
			return Result.error("未找到对应数据");
		}
		zzPHjrkService.updateMain(zzPHjrk, zzPHjrkPage.getZzPersonaList());
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
		zzPHjrkService.delMain(id);
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
		this.zzPHjrkService.delBatchMain(Arrays.asList(ids.split(",")));
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
		ZzPHjrk zzPHjrk = zzPHjrkService.getById(id);
		if(zzPHjrk==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(zzPHjrk);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryZzPersonaByMainId")
	public Result<?> queryZzPersonaListByMainId(@RequestParam(name="id",required=true) String id) {
		List<ZzPersona> zzPersonaList = zzPersonaService.selectByMainId(id);
		return Result.ok(zzPersonaList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param zzPHjrk
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZzPHjrk zzPHjrk) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<ZzPHjrk> queryWrapper = QueryGenerator.initQueryWrapper(zzPHjrk, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<ZzPHjrk> queryList = zzPHjrkService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<ZzPHjrk> zzPHjrkList = new ArrayList<ZzPHjrk>();
      if(oConvertUtils.isEmpty(selections)) {
          zzPHjrkList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          zzPHjrkList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<ZzPHjrkPage> pageList = new ArrayList<ZzPHjrkPage>();
      for (ZzPHjrk main : zzPHjrkList) {
          ZzPHjrkPage vo = new ZzPHjrkPage();
          BeanUtils.copyProperties(main, vo);
          List<ZzPersona> zzPersonaList = zzPersonaService.selectByMainId(main.getId());
          vo.setZzPersonaList(zzPersonaList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "户籍人口列表");
      mv.addObject(NormalExcelConstants.CLASS, ZzPHjrkPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("户籍人口数据", "导出人:"+sysUser.getRealname(), "户籍人口"));
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
              List<ZzPHjrkPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ZzPHjrkPage.class, params);
              for (ZzPHjrkPage page : list) {
                  ZzPHjrk po = new ZzPHjrk();
                  BeanUtils.copyProperties(page, po);
                  zzPHjrkService.saveMain(po, page.getZzPersonaList());
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
