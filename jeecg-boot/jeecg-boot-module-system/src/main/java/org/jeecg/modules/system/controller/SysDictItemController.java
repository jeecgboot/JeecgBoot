package org.jeecg.modules.system.controller;


import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.system.entity.SysDictItem;
import org.jeecg.modules.system.service.ISysDictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @Author zhangweijian
 * @since 2018-12-28
 */
@RestController
@RequestMapping("/sys/dictItem")
@Slf4j
public class SysDictItemController {

	@Autowired
	private ISysDictItemService sysDictItemService;
	
	/**
	 * @功能：查询字典数据
	 * @param sysDictItem
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public Result<IPage<SysDictItem>> queryPageList(SysDictItem sysDictItem,@RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,HttpServletRequest req) {
		Result<IPage<SysDictItem>> result = new Result<IPage<SysDictItem>>();
		QueryWrapper<SysDictItem> queryWrapper = QueryGenerator.initQueryWrapper(sysDictItem, req.getParameterMap());
		queryWrapper.orderByAsc("sort_order");
		Page<SysDictItem> page = new Page<SysDictItem>(pageNo, pageSize);
		IPage<SysDictItem> pageList = sysDictItemService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	 * @功能：新增
	 * @param sysDict
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@CacheEvict(value= CacheConstant.SYS_DICT_CACHE, allEntries=true)
	public Result<SysDictItem> add(@RequestBody SysDictItem sysDictItem) {
		Result<SysDictItem> result = new Result<SysDictItem>();
		try {
			sysDictItem.setCreateTime(new Date());
			sysDictItemService.save(sysDictItem);
			result.success("保存成功！");
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	 * @功能：编辑
	 * @param sysDictItem
	 * @return
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.PUT)
	@CacheEvict(value=CacheConstant.SYS_DICT_CACHE, allEntries=true)
	public Result<SysDictItem> edit(@RequestBody SysDictItem sysDictItem) {
		Result<SysDictItem> result = new Result<SysDictItem>();
		SysDictItem sysdict = sysDictItemService.getById(sysDictItem.getId());
		if(sysdict==null) {
			result.error500("未找到对应实体");
		}else {
			sysDictItem.setUpdateTime(new Date());
			boolean ok = sysDictItemService.updateById(sysDictItem);
			//TODO 返回false说明什么？
			if(ok) {
				result.success("编辑成功!");
			}
		}
		return result;
	}
	
	/**
	 * @功能：删除字典数据
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.DELETE)
	@CacheEvict(value=CacheConstant.SYS_DICT_CACHE, allEntries=true)
	public Result<SysDictItem> delete(@RequestParam(name="id",required=true) String id) {
		Result<SysDictItem> result = new Result<SysDictItem>();
		SysDictItem joinSystem = sysDictItemService.getById(id);
		if(joinSystem==null) {
			result.error500("未找到对应实体");
		}else {
			boolean ok = sysDictItemService.removeById(id);
			if(ok) {
				result.success("删除成功!");
			}
		}
		return result;
	}
	
	/**
	 * @功能：批量删除字典数据
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE)
	@CacheEvict(value=CacheConstant.SYS_DICT_CACHE, allEntries=true)
	public Result<SysDictItem> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<SysDictItem> result = new Result<SysDictItem>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.sysDictItemService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
}
