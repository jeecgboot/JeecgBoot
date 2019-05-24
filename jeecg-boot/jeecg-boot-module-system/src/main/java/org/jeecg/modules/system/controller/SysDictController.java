package org.jeecg.modules.system.controller;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.test.entity.JeecgOrderCustomer;
import org.jeecg.modules.demo.test.entity.JeecgOrderMain;
import org.jeecg.modules.demo.test.entity.JeecgOrderTicket;
import org.jeecg.modules.demo.test.vo.JeecgOrderMainPage;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.model.SysDictTree;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.vo.SysDictPage;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
@RestController
@RequestMapping("/sys/dict")
@Slf4j
public class SysDictController {

	@Autowired
	private ISysDictService sysDictService;

	@Autowired
	private ISysDictItemService sysDictItemService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<IPage<SysDict>> queryPageList(SysDict sysDict,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
		Result<IPage<SysDict>> result = new Result<IPage<SysDict>>();
		QueryWrapper<SysDict> queryWrapper = QueryGenerator.initQueryWrapper(sysDict, req.getParameterMap());
		queryWrapper.eq("del_flag", "1");
		Page<SysDict> page = new Page<SysDict>(pageNo, pageSize);
		IPage<SysDict> pageList = sysDictService.page(page, queryWrapper);
		log.info("查询当前页："+pageList.getCurrent());
		log.info("查询当前页数量："+pageList.getSize());
		log.info("查询结果数量："+pageList.getRecords().size());
		log.info("数据总数："+pageList.getTotal());
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 * @功能：获取树形字典数据
	 * @param sysDict
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/treeList", method = RequestMethod.GET)
	public Result<List<SysDictTree>> treeList(SysDict sysDict,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
		Result<List<SysDictTree>> result = new Result<>();
		LambdaQueryWrapper<SysDict> query = new LambdaQueryWrapper<>();
		// 构造查询条件
		String dictName = sysDict.getDictName();
		if(oConvertUtils.isNotEmpty(dictName)) {
			query.like(true, SysDict::getDictName, dictName);
		}
		query.eq(true, SysDict::getDelFlag, "1");
		query.orderByDesc(true, SysDict::getCreateTime);
		List<SysDict> list = sysDictService.list(query);
		List<SysDictTree> treeList = new ArrayList<>();
		for (SysDict node : list) {
			treeList.add(new SysDictTree(node));
		}
		result.setSuccess(true);
		result.setResult(treeList);
		return result;
	}

	/**
	 * 获取字典数据
	 * @param dictCode 字典code
	 * @param dictCode 表名,文本字段,code字段  | 举例：sys_user,realname,id
	 * @return
	 */
	@RequestMapping(value = "/getDictItems/{dictCode}", method = RequestMethod.GET)
	public Result<List<DictModel>> getDictItems(@PathVariable String dictCode) {
		log.info(" dictCode : "+ dictCode);
		Result<List<DictModel>> result = new Result<List<DictModel>>();
		List<DictModel> ls = null;
		try {
			if(dictCode.indexOf(",")!=-1) {
				//关联表字典（举例：sys_user,realname,id）
				String[] params = dictCode.split(",");
				if(params.length!=3) {
					result.error500("字典Code格式不正确！");
					return result;
				}
				ls = sysDictService.queryTableDictItemsByCode(params[0],params[1],params[2]);
			}else {
				//字典表
				 ls = sysDictService.queryDictItemsByCode(dictCode);
			}

			 result.setSuccess(true);
			 result.setResult(ls);
			 log.info(result.toString());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
			return result;
		}

		return result;
	}

	/**
	 * 获取字典数据
	 * @param dictCode
	 * @return
	 */
	@RequestMapping(value = "/getDictText/{dictCode}/{key}", method = RequestMethod.GET)
	public Result<String> getDictItems(@PathVariable("dictCode") String dictCode, @PathVariable("key") String key) {
		log.info(" dictCode : "+ dictCode);
		Result<String> result = new Result<String>();
		String text = null;
		try {
			text = sysDictService.queryDictTextByKey(dictCode, key);
			 result.setSuccess(true);
			 result.setResult(text);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
			return result;
		}
		return result;
	}

	/**
	 * @功能：新增
	 * @param sysDict
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Result<SysDict> add(@RequestBody SysDict sysDict) {
		Result<SysDict> result = new Result<SysDict>();
		try {
			sysDict.setCreateTime(new Date());
			sysDictService.save(sysDict);
			result.success("保存成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	 * @功能：编辑
	 * @param sysDict
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	public Result<SysDict> edit(@RequestBody SysDict sysDict) {
		Result<SysDict> result = new Result<SysDict>();
		SysDict sysdict = sysDictService.getById(sysDict.getId());
		if(sysdict==null) {
			result.error500("未找到对应实体");
		}else {
			sysDict.setUpdateTime(new Date());
			boolean ok = sysDictService.updateById(sysDict);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("编辑成功!");
			}
		}
		return result;
	}

	/**
	 * @功能：删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@CacheEvict(value="dictCache", allEntries=true)
	public Result<SysDict> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysDict> result = new Result<SysDict>();
		SysDict sysDict = sysDictService.getById(id);
		if(sysDict==null) {
			result.error500("未找到对应实体");
		}else {
			//update-begin--Author:huangzhilin  Date:20140417 for：[bugfree号]数据字典增加级联删除功能--------------------
			boolean ok = sysDictService.deleteByDictId(sysDict);
			//update-begin--Author:huangzhilin  Date:20140417 for：[bugfree号]数据字典增加级联删除功能--------------------
			if(ok) {
				result.success("删除成功!");
			}
		}
		return result;
	}

	/**
	 * @功能：批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	@CacheEvict(value="dictCache", allEntries=true)
	public Result<SysDict> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysDict> result = new Result<SysDict>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			String[] id=ids.split(",");
			for(int i=0;i<id.length;i++) {
				SysDict sysDict = sysDictService.getById(id[i]);
				sysDict.setDelFlag(2);
				sysDictService.updateById(sysDict);
			}
			result.success("删除成功!");
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
	public ModelAndView exportXls(SysDict sysDict,HttpServletRequest request) {
		// Step.1 组装查询条件
		QueryWrapper<SysDict> queryWrapper = QueryGenerator.initQueryWrapper(sysDict, request.getParameterMap());
		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		List<SysDictPage> pageList = new ArrayList<SysDictPage>();

		List<SysDict> sysDictList = sysDictService.list(queryWrapper);
		for (SysDict dictMain : sysDictList) {
			SysDictPage vo = new SysDictPage();
			BeanUtils.copyProperties(dictMain, vo);
			// 查询机票
			List<SysDictItem> sysDictItemList = sysDictItemService.selectItemsByMainId(dictMain.getId());
			vo.setSysDictItemList(sysDictItemList);
			pageList.add(vo);
		}

		// 导出文件名称
		mv.addObject(NormalExcelConstants.FILE_NAME, "数据字典");
		// 注解对象Class
		mv.addObject(NormalExcelConstants.CLASS, SysDictPage.class);
		// 自定义表格参数
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("数据字典列表", "导出人:Jeecg", "数据字典"));
		// 导出数据列表
		mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
		return mv;
	}

	/**
	 * 通过excel导入数据
	 *
	 * @param request
	 * @param
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
			params.setHeadRows(2);
			params.setNeedSave(true);
			try {
				List<SysDictPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SysDictPage.class, params);
				for (SysDictPage page : list) {
					SysDict po = new SysDict();
					BeanUtils.copyProperties(page, po);
					if(page.getDelFlag()==null){
					    po.setDelFlag(1);
                    }
					sysDictService.saveMain(po, page.getSysDictItemList());
				}
				return Result.ok("文件导入成功！");
			} catch (Exception e) {
				log.error(e.getMessage(),e);
				return Result.error("文件导入失败:"+e.getMessage());
			} finally {
				try {
					file.getInputStream().close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return Result.error("文件导入失败！");
	}

}
