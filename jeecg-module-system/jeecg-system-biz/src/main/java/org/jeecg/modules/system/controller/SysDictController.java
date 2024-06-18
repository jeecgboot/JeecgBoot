package org.jeecg.modules.system.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.config.TenantContext;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.SymbolConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.DictQuery;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.ImportExcelUtil;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.TokenUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.config.mybatis.MybatisPlusSaasConfig;
import org.jeecg.config.shiro.ShiroRealm;
import org.jeecg.modules.system.entity.SysDict;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.model.SysDictTree;
import org.jeecg.modules.system.model.TreeSelectModel;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.jeecg.modules.system.service.ISysDictService;
import org.jeecg.modules.system.vo.SysDictPage;
import org.jeecg.modules.system.vo.lowapp.SysDictVo;
import org.jeecgframework.poi.excel.ExcelImportCheckUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

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
	@Autowired
	public RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private ShiroRealm shiroRealm;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<IPage<SysDict>> queryPageList(SysDict sysDict,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
		Result<IPage<SysDict>> result = new Result<IPage<SysDict>>();
		//------------------------------------------------------------------------------------------------
		//是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
		if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
			sysDict.setTenantId(oConvertUtils.getInt(TenantContext.getTenant(),0));
		}
		//------------------------------------------------------------------------------------------------
		QueryWrapper<SysDict> queryWrapper = QueryGenerator.initQueryWrapper(sysDict, req.getParameterMap());
		Page<SysDict> page = new Page<SysDict>(pageNo, pageSize);
		IPage<SysDict> pageList = sysDictService.page(page, queryWrapper);
		log.debug("查询当前页："+pageList.getCurrent());
		log.debug("查询当前页数量："+pageList.getSize());
		log.debug("查询结果数量："+pageList.getRecords().size());
		log.debug("数据总数："+pageList.getTotal());
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
	 * 获取全部字典数据
	 *
	 * @return
	 */
	@RequestMapping(value = "/queryAllDictItems", method = RequestMethod.GET)
	public Result<?> queryAllDictItems(HttpServletRequest request) {
		Map<String, List<DictModel>> res = new HashMap(5);
		res = sysDictService.queryAllDictItems();
		return Result.ok(res);
	}

	/**
	 * 获取字典数据
	 * @param dictCode
	 * @return
	 */
	@RequestMapping(value = "/getDictText/{dictCode}/{key}", method = RequestMethod.GET)
	public Result<String> getDictText(@PathVariable("dictCode") String dictCode, @PathVariable("key") String key) {
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
	 * 获取字典数据 【接口签名验证】
	 * @param dictCode 字典code
	 * @param dictCode 表名,文本字段,code字段  | 举例：sys_user,realname,id
	 * @return
	 */
	@RequestMapping(value = "/getDictItems/{dictCode}", method = RequestMethod.GET)
	public Result<List<DictModel>> getDictItems(@PathVariable("dictCode") String dictCode, @RequestParam(value = "sign",required = false) String sign,HttpServletRequest request) {
		log.info(" dictCode : "+ dictCode);
		Result<List<DictModel>> result = new Result<List<DictModel>>();
		try {
			List<DictModel> ls = sysDictService.getDictItems(dictCode);
			if (ls == null) {
				result.error500("字典Code格式不正确！");
				return result;
			}
			result.setSuccess(true);
			result.setResult(ls);
			log.debug(result.toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.error500("操作失败");
			return result;
		}
		return result;
	}

	/**
	 * 【接口签名验证】
	 * 【JSearchSelectTag下拉搜索组件专用接口】
	 * 大数据量的字典表 走异步加载  即前端输入内容过滤数据
	 * @param dictCode 字典code格式：table,text,code
	 * @return
	 */
	@RequestMapping(value = "/loadDict/{dictCode}", method = RequestMethod.GET)
	public Result<List<DictModel>> loadDict(@PathVariable("dictCode") String dictCode,
			@RequestParam(name="keyword",required = false) String keyword,
			@RequestParam(value = "sign",required = false) String sign,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		
		//update-begin-author:taoyan date:2023-5-22 for: /issues/4905 因为中括号(%5)的问题导致的 表单生成器字段配置时，选择关联字段，在进行高级配置时，无法加载数据库列表，提示 Sgin签名校验错误！ #4905 RouteToRequestUrlFilter
		if(keyword!=null && keyword.indexOf("%5")>=0){
			try {
				keyword = URLDecoder.decode(keyword, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				log.error("下拉搜索关键字解码失败", e);
			}
		}
		//update-end-author:taoyan date:2023-5-22 for: /issues/4905 因为中括号(%5)的问题导致的  表单生成器字段配置时，选择关联字段，在进行高级配置时，无法加载数据库列表，提示 Sgin签名校验错误！ #4905
		
		log.info(" 加载字典表数据,加载关键字: "+ keyword);
		Result<List<DictModel>> result = new Result<List<DictModel>>();
		try {
			List<DictModel> ls = sysDictService.loadDict(dictCode, keyword, pageSize);
			if (ls == null) {
				result.error500("字典Code格式不正确！");
				return result;
			}
			result.setSuccess(true);
			result.setResult(ls);
			log.info(result.toString());
			return result;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败：" + e.getMessage());
			return result;
		}
	}

	/**
	 * 【接口签名验证】
	 * 【给表单设计器的表字典使用】下拉搜索模式，有值时动态拼接数据
	 * @param dictCode
	 * @param keyword 当前控件的值，可以逗号分割
	 * @param sign
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/loadDictOrderByValue/{dictCode}", method = RequestMethod.GET)
	public Result<List<DictModel>> loadDictOrderByValue(
			@PathVariable("dictCode") String dictCode,
			@RequestParam(name = "keyword") String keyword,
			@RequestParam(value = "sign", required = false) String sign,
			@RequestParam(value = "pageSize", required = false) Integer pageSize) {
		// 首次查询查出来用户选中的值，并且不分页
		Result<List<DictModel>> firstRes = this.loadDict(dictCode, keyword, sign, null);
		if (!firstRes.isSuccess()) {
			return firstRes;
		}
		// 然后再查询出第一页的数据
		Result<List<DictModel>> result = this.loadDict(dictCode, "", sign, pageSize);
		if (!result.isSuccess()) {
			return result;
		}
		// 合并两次查询的数据
		List<DictModel> firstList = firstRes.getResult();
		List<DictModel> list = result.getResult();
		for (DictModel firstItem : firstList) {
			// anyMatch 表示：判断的条件里，任意一个元素匹配成功，返回true
			// allMatch 表示：判断条件里的元素，所有的都匹配成功，返回true
			// noneMatch 跟 allMatch 相反，表示：判断条件里的元素，所有的都匹配失败，返回true
			boolean none = list.stream().noneMatch(item -> item.getValue().equals(firstItem.getValue()));
			// 当元素不存在时，再添加到集合里
			if (none) {
				list.add(0, firstItem);
			}
		}
		return result;
	}

	/**
	 * 【接口签名验证】
	 * 根据字典code加载字典text 返回
	 * @param dictCode 顺序：tableName,text,code
	 * @param keys 要查询的key
	 * @param sign
	 * @param delNotExist 是否移除不存在的项，默认为true，设为false如果某个key不存在数据库中，则直接返回key本身
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/loadDictItem/{dictCode}", method = RequestMethod.GET)
	public Result<List<String>> loadDictItem(@PathVariable("dictCode") String dictCode,@RequestParam(name="key") String keys, @RequestParam(value = "sign",required = false) String sign,@RequestParam(value = "delNotExist",required = false,defaultValue = "true") boolean delNotExist,HttpServletRequest request) {
		Result<List<String>> result = new Result<>();
		try {
			if(dictCode.indexOf(SymbolConstant.COMMA)!=-1) {
				String[] params = dictCode.split(SymbolConstant.COMMA);
				if(params.length!=3) {
					result.error500("字典Code格式不正确！");
					return result;
				}
				List<String> texts = sysDictService.queryTableDictByKeys(params[0], params[1], params[2], keys, delNotExist);

				result.setSuccess(true);
				result.setResult(texts);
				log.info(result.toString());
			}else {
				result.error500("字典Code格式不正确！");
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
			return result;
		}

		return result;
	}

	/**
	 * 【接口签名验证】
	 * 根据表名——显示字段-存储字段 pid 加载树形数据
	 * @param hasChildField 是否叶子节点字段
	 * @param converIsLeafVal 是否需要系统转换 是否叶子节点的值 (0标识不转换、1标准系统自动转换)
	 * @param tableName 表名
	 * @param text label字段
	 * @param code value 字段
	 * @param condition  查询条件  ？
	 *            
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/loadTreeData", method = RequestMethod.GET)
	public Result<List<TreeSelectModel>> loadTreeData(@RequestParam(name="pid",required = false) String pid,@RequestParam(name="pidField") String pidField,
												  @RequestParam(name="tableName") String tableName,
												  @RequestParam(name="text") String text,
												  @RequestParam(name="code") String code,
												  @RequestParam(name="hasChildField") String hasChildField,
												  @RequestParam(name="converIsLeafVal",defaultValue ="1") int converIsLeafVal,
												  @RequestParam(name="condition") String condition,
												  @RequestParam(value = "sign",required = false) String sign,HttpServletRequest request) {
		Result<List<TreeSelectModel>> result = new Result<List<TreeSelectModel>>();

		// 【QQYUN-9207】防止参数为空导致报错
		if (oConvertUtils.isEmpty(tableName) || oConvertUtils.isEmpty(text) || oConvertUtils.isEmpty(code)) {
			result.error500("字典Code格式不正确！");
			return result;
		}

		// 1.获取查询条件参数
		Map<String, String> query = null;
		if(oConvertUtils.isNotEmpty(condition)) {
			query = JSON.parseObject(condition, Map.class);
		}
		
		// 2.返回查询结果
		List<TreeSelectModel> ls = sysDictService.queryTreeList(query,tableName, text, code, pidField, pid,hasChildField,converIsLeafVal);
		result.setSuccess(true);
		result.setResult(ls);
		return result;
	}

	/**
	 * 【APP接口】根据字典配置查询表字典数据（目前暂未找到调用的地方）
	 * @param query
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@Deprecated
	@GetMapping("/queryTableData")
	public Result<List<DictModel>> queryTableData(DictQuery query,
												  @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
												  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
												  @RequestParam(value = "sign",required = false) String sign,HttpServletRequest request){
		Result<List<DictModel>> res = new Result<List<DictModel>>();
		List<DictModel> ls = this.sysDictService.queryDictTablePageList(query,pageSize,pageNo);
		res.setResult(ls);
		res.setSuccess(true);
		return res;
	}

	/**
	 * @功能：新增
	 * @param sysDict
	 * @return
	 */
    @RequiresPermissions("system:dict:add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public Result<SysDict> add(@RequestBody SysDict sysDict) {
		Result<SysDict> result = new Result<SysDict>();
		try {
			sysDict.setCreateTime(new Date());
			sysDict.setDelFlag(CommonConstant.DEL_FLAG_0);
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
    @RequiresPermissions("system:dict:edit")
	@RequestMapping(value = "/edit", method = { RequestMethod.PUT,RequestMethod.POST })
	public Result<SysDict> edit(@RequestBody SysDict sysDict) {
		Result<SysDict> result = new Result<SysDict>();
		SysDict sysdict = sysDictService.getById(sysDict.getId());
		if(sysdict==null) {
			result.error500("未找到对应实体");
		}else {
			sysDict.setUpdateTime(new Date());
			boolean ok = sysDictService.updateById(sysDict);
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
    @RequiresPermissions("system:dict:delete")
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@CacheEvict(value={CacheConstant.SYS_DICT_CACHE, CacheConstant.SYS_ENABLE_DICT_CACHE}, allEntries=true)
	public Result<SysDict> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysDict> result = new Result<SysDict>();
		boolean ok = sysDictService.removeById(id);
		if(ok) {
			result.success("删除成功!");
		}else{
			result.error500("删除失败!");
		}
		return result;
	}

	/**
	 * @功能：批量删除
	 * @param ids
	 * @return
	 */
    @RequiresPermissions("system:dict:deleteBatch")
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	@CacheEvict(value= {CacheConstant.SYS_DICT_CACHE, CacheConstant.SYS_ENABLE_DICT_CACHE}, allEntries=true)
	public Result<SysDict> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysDict> result = new Result<SysDict>();
		if(oConvertUtils.isEmpty(ids)) {
			result.error500("参数不识别！");
		}else {
			sysDictService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

	/**
	 * @功能：刷新缓存
	 * @return
	 */
	@RequestMapping(value = "/refleshCache")
	public Result<?> refleshCache() {
		Result<?> result = new Result<SysDict>();
		//清空字典缓存
//		Set keys = redisTemplate.keys(CacheConstant.SYS_DICT_CACHE + "*");
//		Set keys7 = redisTemplate.keys(CacheConstant.SYS_ENABLE_DICT_CACHE + "*");
//		Set keys2 = redisTemplate.keys(CacheConstant.SYS_DICT_TABLE_CACHE + "*");
//		Set keys21 = redisTemplate.keys(CacheConstant.SYS_DICT_TABLE_BY_KEYS_CACHE + "*");
//		Set keys3 = redisTemplate.keys(CacheConstant.SYS_DEPARTS_CACHE + "*");
//		Set keys4 = redisTemplate.keys(CacheConstant.SYS_DEPART_IDS_CACHE + "*");
//		Set keys5 = redisTemplate.keys( "jmreport:cache:dict*");
//		Set keys6 = redisTemplate.keys( "jmreport:cache:dictTable*");
//		redisTemplate.delete(keys);
//		redisTemplate.delete(keys2);
//		redisTemplate.delete(keys21);
//		redisTemplate.delete(keys3);
//		redisTemplate.delete(keys4);
//		redisTemplate.delete(keys5);
//		redisTemplate.delete(keys6);
//		redisTemplate.delete(keys7);

		//update-begin-author:liusq date:20230404 for:  [issue/4358]springCache中的清除缓存的操作使用了“keys”
		redisUtil.removeAll(CacheConstant.SYS_DICT_CACHE);
		redisUtil.removeAll(CacheConstant.SYS_ENABLE_DICT_CACHE);
		redisUtil.removeAll(CacheConstant.SYS_DICT_TABLE_CACHE);
		redisUtil.removeAll(CacheConstant.SYS_DICT_TABLE_BY_KEYS_CACHE);
		redisUtil.removeAll(CacheConstant.SYS_DEPARTS_CACHE);
		redisUtil.removeAll(CacheConstant.SYS_DEPART_IDS_CACHE);
		redisUtil.removeAll("jmreport:cache:dict");
		redisUtil.removeAll("jmreport:cache:dictTable");
		//update-end-author:liusq date:20230404 for:  [issue/4358]springCache中的清除缓存的操作使用了“keys”
		
		//update-begin---author:scott ---date:2024-06-18  for：【TV360X-1320】分配权限必须退出重新登录才生效，造成很多用户困扰---
		// 清除当前用户的授权缓存信息
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser.isAuthenticated()) {
			shiroRealm.clearCache(currentUser.getPrincipals());
		}
		//update-end---author:scott ---date::2024-06-18  for：【TV360X-1320】分配权限必须退出重新登录才生效，造成很多用户困扰---
		return result;
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 */
	@RequestMapping(value = "/exportXls")
	public ModelAndView exportXls(SysDict sysDict,HttpServletRequest request) {
		//------------------------------------------------------------------------------------------------
		//是否开启系统管理模块的多租户数据隔离【SAAS多租户模式】
		if(MybatisPlusSaasConfig.OPEN_SYSTEM_TENANT_CONTROL){
			sysDict.setTenantId(oConvertUtils.getInt(TenantContext.getTenant(), 0));
		}
		//------------------------------------------------------------------------------------------------
		
		// Step.1 组装查询条件
		QueryWrapper<SysDict> queryWrapper = QueryGenerator.initQueryWrapper(sysDict, request.getParameterMap());
		//Step.2 AutoPoi 导出Excel
		ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
		String selections = request.getParameter("selections");
		if(!oConvertUtils.isEmpty(selections)){
			queryWrapper.in("id",selections.split(","));
		}
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
		LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("数据字典列表", "导出人:"+user.getRealname(), "数据字典"));
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
    @RequiresPermissions("system:dict:importExcel")
	@RequestMapping(value = "/importExcel", method = RequestMethod.POST)
	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
 		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            // 获取上传文件对象
			MultipartFile file = entity.getValue();
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(2);
			params.setNeedSave(true);
			try {
				//导入Excel格式校验，看匹配的字段文本概率
				Boolean t = ExcelImportCheckUtil.check(file.getInputStream(), SysDictPage.class, params);
				if(t!=null && !t){
					throw new RuntimeException("导入Excel校验失败 ！");
				}
				List<SysDictPage> list = ExcelImportUtil.importExcel(file.getInputStream(), SysDictPage.class, params);
				// 错误信息
				List<String> errorMessage = new ArrayList<>();
				int successLines = 0, errorLines = 0;
				for (int i=0;i< list.size();i++) {
					SysDict po = new SysDict();
					BeanUtils.copyProperties(list.get(i), po);
					po.setDelFlag(CommonConstant.DEL_FLAG_0);
					try {
						Integer integer = sysDictService.saveMain(po, list.get(i).getSysDictItemList());
						if(integer>0){
							successLines++;
                        //update-begin---author:wangshuai ---date:20220211  for：[JTC-1168]如果字典项值为空，则字典项忽略导入------------
						}else if(integer == -1){
                            errorLines++;
                            errorMessage.add("字典名称：" + po.getDictName() + "，对应字典列表的字典项值不能为空，忽略导入。");
                        }else{
                        //update-end---author:wangshuai ---date:20220211  for：[JTC-1168]如果字典项值为空，则字典项忽略导入------------
							errorLines++;
							int lineNumber = i + 1;
                            //update-begin---author:wangshuai ---date:20220209  for：[JTC-1168]字典编号不能为空------------
                            if(oConvertUtils.isEmpty(po.getDictCode())){
                                errorMessage.add("第 " + lineNumber + " 行：字典编码不能为空，忽略导入。");
                            }else{
                                errorMessage.add("第 " + lineNumber + " 行：字典编码已经存在，忽略导入。");
                            }
                            //update-end---author:wangshuai ---date:20220209  for：[JTC-1168]字典编号不能为空------------
                        }
					}  catch (Exception e) {
						errorLines++;
						int lineNumber = i + 1;
						errorMessage.add("第 " + lineNumber + " 行：字典编码已经存在，忽略导入。");
					}
				}
				return ImportExcelUtil.imporReturnRes(errorLines,successLines,errorMessage);
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


	/**
	 * 查询被删除的列表
	 * @return
	 */
	@RequestMapping(value = "/deleteList", method = RequestMethod.GET)
	public Result<List<SysDict>> deleteList(HttpServletRequest request) {
		Result<List<SysDict>> result = new Result<List<SysDict>>();
		String tenantId = TokenUtils.getTenantIdByRequest(request);
		List<SysDict> list = this.sysDictService.queryDeleteList(tenantId);
		result.setSuccess(true);
		result.setResult(list);
		return result;
	}

	/**
	 * 物理删除
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deletePhysic/{id}", method = RequestMethod.DELETE)
	public Result<?> deletePhysic(@PathVariable("id") String id) {
		try {
			sysDictService.deleteOneDictPhysically(id);
			return Result.ok("删除成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("删除失败!");
		}
	}

	/**
	 * 逻辑删除的字段，进行取回
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/back/{id}", method = RequestMethod.PUT)
	public Result<?> back(@PathVariable("id") String id) {
		try {
			sysDictService.updateDictDelFlag(0,id);
			return Result.ok("操作成功!");
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("操作失败!");
		}
	}

	/**
	 * VUEN-2584【issue】平台sql注入漏洞几个问题
	 * 部分特殊函数 可以将查询结果混夹在错误信息中，导致数据库的信息暴露
	 * @param e
	 * @return
	 */
	@ExceptionHandler(java.sql.SQLException.class)
	public Result<?> handleSQLException(Exception e){
		String msg = e.getMessage();
		String extractvalue = "extractvalue";
		String updatexml = "updatexml";
		if(msg!=null && (msg.toLowerCase().indexOf(extractvalue)>=0 || msg.toLowerCase().indexOf(updatexml)>=0)){
			return Result.error("校验失败，sql解析异常！");
		}
		return Result.error("校验失败，sql解析异常！" + msg);
	}

	/**
	 * 根据应用id获取字典列表和详情
	 * @param request
	 */
	@GetMapping("/getDictListByLowAppId")
	public Result<List<SysDictVo>> getDictListByLowAppId(HttpServletRequest request){
		String lowAppId = oConvertUtils.getString(TokenUtils.getLowAppIdByRequest(request));
		List<SysDictVo> list = sysDictService.getDictListByLowAppId(lowAppId);
		return Result.ok(list);
	}

	/**
	 * 添加字典
	 * @param sysDictVo
	 * @param request
	 * @return
	 */
	@PostMapping("/addDictByLowAppId")
	public Result<String> addDictByLowAppId(@RequestBody SysDictVo sysDictVo,HttpServletRequest request){
		String lowAppId = oConvertUtils.getString(TokenUtils.getLowAppIdByRequest(request));
		String tenantId = oConvertUtils.getString(TokenUtils.getTenantIdByRequest(request));
		sysDictVo.setLowAppId(lowAppId);
		sysDictVo.setTenantId(oConvertUtils.getInteger(tenantId, null));
		sysDictService.addDictByLowAppId(sysDictVo);
		return Result.ok("添加成功");
	}

	@PutMapping("/editDictByLowAppId")
	public Result<String> editDictByLowAppId(@RequestBody SysDictVo sysDictVo,HttpServletRequest request){
		String lowAppId = oConvertUtils.getString(TokenUtils.getLowAppIdByRequest(request));
		sysDictVo.setLowAppId(lowAppId);
		sysDictService.editDictByLowAppId(sysDictVo);
		return Result.ok("编辑成功");
	}
}
