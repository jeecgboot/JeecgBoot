package org.jeecg.modules.online.cgreport.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgreport.entity.OnlCgreportParam;
import org.jeecg.modules.online.cgreport.service.IOnlCgreportParamService;
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
@RequestMapping("/online/cgreport/param")
@Slf4j
public class OnlCgreportParamController {

	@Autowired
	private IOnlCgreportParamService onlCgreportParamService;

	/**
	 * 根据 headId 查询出 所有的 param
	 */
	@GetMapping(value = "/listByHeadId")
	public Result<?> queryPageListByHeadId(String headId) {

		QueryWrapper<OnlCgreportParam> queryWrapper = new QueryWrapper<OnlCgreportParam>();
		queryWrapper.eq("cgrhead_id", headId);
		queryWrapper.orderByAsc("order_num");
		List<OnlCgreportParam> list = onlCgreportParamService.list(queryWrapper);

		Result<List<OnlCgreportParam>> result = new Result<List<OnlCgreportParam>>();
		result.setSuccess(true);
		result.setResult(list);
		return result;
	}

	/**
	 * 分页列表查询
	 * 
	 * @param onlCgreportParam
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<OnlCgreportParam>> queryPageList(OnlCgreportParam onlCgreportParam, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		Result<IPage<OnlCgreportParam>> result = new Result<IPage<OnlCgreportParam>>();
		QueryWrapper<OnlCgreportParam> queryWrapper = QueryGenerator.initQueryWrapper(onlCgreportParam, req.getParameterMap());
		Page<OnlCgreportParam> page = new Page<OnlCgreportParam>(pageNo, pageSize);
		IPage<OnlCgreportParam> pageList = onlCgreportParamService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 * 添加
	 * 
	 * @param onlCgreportParam
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<OnlCgreportParam> add(@RequestBody OnlCgreportParam onlCgreportParam) {
		Result<OnlCgreportParam> result = new Result<OnlCgreportParam>();
		try {
			onlCgreportParamService.save(onlCgreportParam);
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
	 * @param onlCgreportParam
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<OnlCgreportParam> edit(@RequestBody OnlCgreportParam onlCgreportParam) {
		Result<OnlCgreportParam> result = new Result<OnlCgreportParam>();
		OnlCgreportParam onlCgreportParamEntity = onlCgreportParamService.getById(onlCgreportParam.getId());
		if (onlCgreportParamEntity == null) {
			result.error500("未找到对应实体");
		} else {
			boolean ok = onlCgreportParamService.updateById(onlCgreportParam);
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
	public Result<OnlCgreportParam> delete(@RequestParam(name = "id", required = true) String id) {
		Result<OnlCgreportParam> result = new Result<OnlCgreportParam>();
		OnlCgreportParam onlCgreportParam = onlCgreportParamService.getById(id);
		if (onlCgreportParam == null) {
			result.error500("未找到对应实体");
		} else {
			boolean ok = onlCgreportParamService.removeById(id);
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
	public Result<OnlCgreportParam> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		Result<OnlCgreportParam> result = new Result<OnlCgreportParam>();
		if (ids == null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		} else {
			this.onlCgreportParamService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<OnlCgreportParam> queryById(@RequestParam(name = "id", required = true) String id) {
		Result<OnlCgreportParam> result = new Result<OnlCgreportParam>();
		OnlCgreportParam onlCgreportParam = onlCgreportParamService.getById(id);
		if (onlCgreportParam == null) {
			result.error500("未找到对应实体");
		} else {
			result.setResult(onlCgreportParam);
			result.setSuccess(true);
		}
		return result;
	}

}
