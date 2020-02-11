package org.jeecg.modules.shza.mafk.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.shza.mafk.entity.ZzMafk;
import org.jeecg.modules.shza.mafk.entity.ZzMafkPerson;
import org.jeecg.modules.shza.mafk.service.IZzMafkPersonService;
import org.jeecg.modules.shza.mafk.service.IZzMafkService;
import org.jeecg.modules.shza.mafk.vo.ZzMafkPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 命案主表
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@RestController
@RequestMapping("/mafk/zzMafk")
@Slf4j
public class ZzMafkController {
	@Autowired
	private IZzMafkService zzMafkService;
	@Autowired
	private IZzMafkPersonService zzMafkPersonService;

	/**
	 * 分页列表查询
	 *
	 * @param zzMafk
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ZzMafk zzMafk,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ZzMafk> queryWrapper = QueryGenerator.initQueryWrapper(zzMafk, req.getParameterMap());
		Page<ZzMafk> page = new Page<ZzMafk>(pageNo, pageSize);
		IPage<ZzMafk> pageList = zzMafkService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param zzMafkPage
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ZzMafkPage zzMafkPage) {
		ZzMafk zzMafk = new ZzMafk();
		BeanUtils.copyProperties(zzMafkPage, zzMafk);
		zzMafkService.saveMain(zzMafk, zzMafkPage.getZzMafkPersonShrList(),zzMafkPage.getZzMafkPersonXyrList());
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param zzMafkPage
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ZzMafkPage zzMafkPage) {
		ZzMafk zzMafk = new ZzMafk();
		BeanUtils.copyProperties(zzMafkPage, zzMafk);
		ZzMafk zzMafkEntity = zzMafkService.getById(zzMafk.getId());
		if(zzMafkEntity==null) {
			return Result.error("未找到对应数据");
		}
		zzMafkService.updateMain(zzMafk, zzMafkPage.getZzMafkPersonShrList(),zzMafkPage.getZzMafkPersonXyrList());
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
		zzMafkService.delMain(id);
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
		this.zzMafkService.delBatchMain(Arrays.asList(ids.split(",")));
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
		ZzMafk zzMafk = zzMafkService.getById(id);
		if(zzMafk==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(zzMafk);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryZzMafkPersonShrByMainId")
	public Result<?> queryZzMafkPersonShrListByMainId(@RequestParam(name="id",required=true) String id) {
		List<ZzMafkPerson> zzMafkPersonShrList = zzMafkPersonService.selectByMainId(id, 1);
		return Result.ok(zzMafkPersonShrList);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryZzMafkPersonXyrByMainId")
	public Result<?> queryZzMafkPersonXyrListByMainId(@RequestParam(name="id",required=true) String id) {
		List<ZzMafkPerson> zzMafkPersonXyrList = zzMafkPersonService.selectByMainId(id, 2);
		return Result.ok(zzMafkPersonXyrList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param zzMafk
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ZzMafk zzMafk) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<ZzMafk> queryWrapper = QueryGenerator.initQueryWrapper(zzMafk, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //Step.2 获取导出数据
      List<ZzMafk> queryList = zzMafkService.list(queryWrapper);
      // 过滤选中数据
      String selections = request.getParameter("selections");
      List<ZzMafk> zzMafkList = new ArrayList<ZzMafk>();
      if(oConvertUtils.isEmpty(selections)) {
          zzMafkList = queryList;
      }else {
          List<String> selectionList = Arrays.asList(selections.split(","));
          zzMafkList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
      }

      // Step.3 组装pageList
      List<ZzMafkPage> pageList = new ArrayList<ZzMafkPage>();
      for (ZzMafk main : zzMafkList) {
          ZzMafkPage vo = new ZzMafkPage();
          BeanUtils.copyProperties(main, vo);
          List<ZzMafkPerson> zzMafkPersonShrList = zzMafkPersonService.selectByMainId(main.getId(), 1);
          vo.setZzMafkPersonShrList(zzMafkPersonShrList);
          List<ZzMafkPerson> zzMafkPersonXyrList = zzMafkPersonService.selectByMainId(main.getId(), 2);
          vo.setZzMafkPersonXyrList(zzMafkPersonXyrList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "命案主表列表");
      mv.addObject(NormalExcelConstants.CLASS, ZzMafkPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("命案主表数据", "导出人:"+sysUser.getRealname(), "命案主表"));
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
              List<ZzMafkPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ZzMafkPage.class, params);
              for (ZzMafkPage page : list) {
                  ZzMafk po = new ZzMafk();
                  BeanUtils.copyProperties(page, po);
                  zzMafkService.saveMain(po, page.getZzMafkPersonShrList(),page.getZzMafkPersonXyrList());
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
