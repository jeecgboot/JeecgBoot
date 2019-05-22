package org.jeecg.modules.message.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.formula.functions.T;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.message.service.ISysMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description: 消息
 * @author: jeecg-boot
 * @date: 2019-04-09
 * @version: V1.0
 */
@Slf4j
@RestController
@RequestMapping("/message/sysMessage")
public class SysMessageController extends JeecgController<SysMessage, ISysMessageService> {
	@Autowired
	private ISysMessageService sysMessageService;

	/**
	 * 分页列表查询
	 * 
	 * @param sysMessage
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<SysMessage>> queryPageList(SysMessage sysMessage, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
			@RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
		Result<IPage<SysMessage>> result = new Result<IPage<SysMessage>>();
		QueryWrapper<SysMessage> queryWrapper = QueryGenerator.initQueryWrapper(sysMessage, req.getParameterMap());
		Page<SysMessage> page = new Page<SysMessage>(pageNo, pageSize);
		IPage<SysMessage> pageList = sysMessageService.page(page, queryWrapper);
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}

	/**
	 * 添加
	 * 
	 * @param sysMessage
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<T> add(@RequestBody SysMessage sysMessage) {
		Result<T> result = new Result<T>();
		try {
			sysMessageService.save(sysMessage);
			result.success("添加成功！");
		} catch (Exception e) {
			log.info(e.getMessage(), e);
			result.error500("操作失败");
		}
		return result;
	}

	/**
	 * 编辑
	 * 
	 * @param sysMessage
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<T> edit(@RequestBody SysMessage sysMessage) {
		Result<T> result = new Result<T>();
		SysMessage sysMessageEntity = sysMessageService.getById(sysMessage.getId());
		if (sysMessageEntity == null) {
			result.error500("未找到对应实体");
		} else {
			boolean ok = sysMessageService.updateById(sysMessage);
			if (ok) {
				result.success("修改成功!");
			} else {
				result.error500("修改失败!");
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
	public Result<T> delete(@RequestParam(name = "id", required = true) String id) {
		Result<T> result = new Result<T>();
		SysMessage sysMessage = sysMessageService.getById(id);
		if (sysMessage == null) {
			result.error500("未找到对应实体");
		} else {
			boolean ok = sysMessageService.removeById(id);
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
	public Result<T> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
		Result<T> result = new Result<T>();
		if (ids == null || "".equals(ids.trim())) {
			result.error500("ids参数不允许为空！");
		} else {
			this.sysMessageService.removeByIds(Arrays.asList(ids.split(",")));
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
	public Result<SysMessage> queryById(@RequestParam(name = "id", required = true) String id) {
		Result<SysMessage> result = new Result<SysMessage>();
		SysMessage sysMessage = sysMessageService.getById(id);
		if (sysMessage == null) {
			result.error500("未找到对应实体");
		} else {
			result.setResult(sysMessage);
			result.setSuccess(true);
		}
		return result;
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param response
	 */
	@GetMapping(value = "/exportXls")
	public ModelAndView exportXls(HttpServletRequest request, SysMessage sysMessage) {
		return super.exportXls(request,sysMessage,SysMessage.class, "推送消息模板");
	}

	/**
	 * excel导入
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping(value = "/importExcel")
	public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
		return super.importExcel(request, response, SysMessage.class);
	}

}
