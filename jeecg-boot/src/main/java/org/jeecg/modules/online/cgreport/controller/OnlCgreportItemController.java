package org.jeecg.modules.online.cgreport.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportItem;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * @Title: Controller
 * @Description: 在线报表配置
 * @author: jeecg-boot
 * @date: 2019-03-08
 * @version: V1.0
 */
@RestController
@RequestMapping("/online/cgreport/item")
@Slf4j
public class OnlCgreportItemController {

	@Autowired
	private IOnlCgreportItemService onlCgreportItemService;

	/**
	 * 根据 headId 查询出 所有的 item
	 */
	@GetMapping(value = "/listByHeadId")
	public Result<?> queryPageListByHeadId(String headId) {

		QueryWrapper<OnlCgreportItem> queryWrapper = new QueryWrapper<OnlCgreportItem>();
		queryWrapper.eq("cgrhead_id", headId);
		queryWrapper.orderByAsc("order_num");
		List<OnlCgreportItem> list = onlCgreportItemService.list(queryWrapper);

		Result<List<OnlCgreportItem>> result = new Result<List<OnlCgreportItem>>();
		result.setSuccess(true);
		result.setResult(list);
		return result;
	}

	/**
	 * 分页列表查询
	 * 
	 * @param onlCgreportItem
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<OnlCgreportItem>> queryPageList(OnlCgreportItem onlCgreportItem, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		Result<IPage<OnlCgreportItem>> result = new Result<IPage<OnlCgreportItem>>();
		QueryWrapper<OnlCgreportItem> queryWrapper = QueryGenerator.initQueryWrapper(onlCgreportItem, req.getParameterMap());
		Page<OnlCgreportItem> page = new Page<OnlCgreportItem>(pageNo, pageSize);
		IPage<OnlCgreportItem> pageList = onlCgreportItemService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 * 添加
	 * 
	 * @param onlCgreportItem
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<OnlCgreportItem> add(@RequestBody OnlCgreportItem onlCgreportItem) {
		Result<OnlCgreportItem> result = new Result<OnlCgreportItem>();
		try {
			onlCgreportItemService.save(onlCgreportItem);
			result.success("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("操作失败");
		}
		return result;
	}

	/**
	 * 编辑
	 * 
	 * @param onlCgreportItem
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<OnlCgreportItem> edit(@RequestBody OnlCgreportItem onlCgreportItem) {
		Result<OnlCgreportItem> result = new Result<OnlCgreportItem>();
		OnlCgreportItem onlCgreportItemEntity = onlCgreportItemService.getById(onlCgreportItem.getId());
		if (onlCgreportItemEntity == null) {
			result.error500("未找到对应实体");
		} else {
			boolean ok = onlCgreportItemService.updateById(onlCgreportItem);
			// TODO 返回false说明什么？
			if (ok) {
				result.success("修改成功!");
			}
		}

		return result;
	}

	/**
	 * 通过id删除
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<OnlCgreportItem> delete(@RequestParam(name = "id", required = true) String id) {
		Result<OnlCgreportItem> result = new Result<OnlCgreportItem>();
		OnlCgreportItem onlCgreportItem = onlCgreportItemService.getById(id);
		if (onlCgreportItem == null) {
			result.error500("未找到对应实体");
		} else {
			boolean ok = onlCgreportItemService.removeById(id);
			if (ok) {
				result.success("删除成功!");
			}
		}

		return result;
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<OnlCgreportItem> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		Result<OnlCgreportItem> result = new Result<OnlCgreportItem>();
		if (ids == null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		} else {
			this.onlCgreportItemService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}

	/**
	 * 通过id查询
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<OnlCgreportItem> queryById(@RequestParam(name = "id", required = true) String id) {
		Result<OnlCgreportItem> result = new Result<OnlCgreportItem>();
		OnlCgreportItem onlCgreportItem = onlCgreportItemService.getById(id);
		if (onlCgreportItem == null) {
			result.error500("未找到对应实体");
		} else {
			result.setResult(onlCgreportItem);
			result.setSuccess(true);
		}
		return result;
	}

}
