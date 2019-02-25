package org.jeecg.modules.demo.test.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.test.entity.JeecgOrderCustomer;
import org.jeecg.modules.demo.test.entity.JeecgOrderMain;
import org.jeecg.modules.demo.test.entity.JeecgOrderTicket;
import org.jeecg.modules.demo.test.service.IJeecgOrderCustomerService;
import org.jeecg.modules.demo.test.service.IJeecgOrderMainService;
import org.jeecg.modules.demo.test.service.IJeecgOrderTicketService;
import org.jeecg.modules.demo.test.vo.JeecgOrderMainPage;
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
 * @Description: 订单
 * @author： jeecg-boot
 * @date：   2019-02-15
 * @version： V1.0
 */
@RestController
@RequestMapping("/test/jeecgOrderMain")
@Slf4j
public class JeecgOrderMainController {
	@Autowired
	private IJeecgOrderMainService jeecgOrderMainService;
	@Autowired
	private IJeecgOrderCustomerService jeecgOrderCustomerService;
	@Autowired
	private IJeecgOrderTicketService jeecgOrderTicketService;
	
	/**
	  * 分页列表查询
	 * @param jeecgOrderMain
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<IPage<JeecgOrderMain>> queryPageList(JeecgOrderMain jeecgOrderMain,
									  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
									  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
									  HttpServletRequest req) {
		Result<IPage<JeecgOrderMain>> result = new Result<IPage<JeecgOrderMain>>();
		QueryWrapper<JeecgOrderMain> queryWrapper = new QueryWrapper<JeecgOrderMain>(jeecgOrderMain);
		Page<JeecgOrderMain> page = new Page<JeecgOrderMain>(pageNo,pageSize);
		//排序逻辑 处理
		String column = req.getParameter("column");
		String order = req.getParameter("order");
		if(oConvertUtils.isNotEmpty(column) && oConvertUtils.isNotEmpty(order)) {
			if("asc".equals(order)) {
				queryWrapper.orderByAsc(oConvertUtils.camelToUnderline(column));
			}else {
				queryWrapper.orderByDesc(oConvertUtils.camelToUnderline(column));
			}
		}
		IPage<JeecgOrderMain> pageList = jeecgOrderMainService.page(page, queryWrapper);
		//log.debug("查询当前页："+pageList.getCurrent());
		//log.debug("查询当前页数量："+pageList.getSize());
		//log.debug("查询结果数量："+pageList.getRecords().size());
		//log.debug("数据总数："+pageList.getTotal());
		result.setSuccess(true);
		result.setResult(pageList);
		return result;
	}
	
	/**
	  *   添加
	 * @param jeecgOrderMain
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<JeecgOrderMain> add(@RequestBody JeecgOrderMainPage jeecgOrderMainPage) {
		Result<JeecgOrderMain> result = new Result<JeecgOrderMain>();
		try {
			jeecgOrderMainService.saveMain(jeecgOrderMainPage.getJeecgOrderMain(), jeecgOrderMainPage.getJeecgOrderCustomerList(), jeecgOrderMainPage.getJeecgOrderTicketList());;
			result.success("添加成功！");
		} catch (Exception e) {
			e.printStackTrace();
			log.info(e.getMessage());
			result.error500("操作失败");
		}
		return result;
	}
	
	/**
	  *  编辑
	 * @param jeecgOrderMain
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<JeecgOrderMain> eidt(@RequestBody JeecgOrderMainPage jeecgOrderMainPage) {
		Result<JeecgOrderMain> result = new Result<JeecgOrderMain>();
		JeecgOrderMain jeecgOrderMain = jeecgOrderMainPage.getJeecgOrderMain();
		JeecgOrderMain jeecgOrderMainEntity = jeecgOrderMainService.getById(jeecgOrderMain.getId());
		if(jeecgOrderMainEntity==null) {
			result.error500("未找到对应实体");
		}else {
			jeecgOrderMainService.updateMain(jeecgOrderMain, jeecgOrderMainPage.getJeecgOrderCustomerList(), jeecgOrderMainPage.getJeecgOrderTicketList());
			result.success("修改成功!");
		}
		
		return result;
	}
	
	/**
	  *   通过id删除
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/delete")
	public Result<JeecgOrderMain> delete(@RequestParam(name="id",required=true) String id) {
		Result<JeecgOrderMain> result = new Result<JeecgOrderMain>();
		JeecgOrderMain jeecgOrderMain = jeecgOrderMainService.getById(id);
		if(jeecgOrderMain==null) {
			result.error500("未找到对应实体");
		}else {
			jeecgOrderMainService.delMain(id);
			result.success("删除成功!");
		}
		
		return result;
	}
	
	/**
	  *  批量删除
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatch")
	public Result<JeecgOrderMain> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		Result<JeecgOrderMain> result = new Result<JeecgOrderMain>();
		if(ids==null || "".equals(ids.trim())) {
			result.error500("参数不识别！");
		}else {
			this.jeecgOrderMainService.removeByIds(Arrays.asList(ids.split(",")));
			result.success("删除成功!");
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<JeecgOrderMain> queryById(@RequestParam(name="id",required=true) String id) {
		Result<JeecgOrderMain> result = new Result<JeecgOrderMain>();
		JeecgOrderMain jeecgOrderMain = jeecgOrderMainService.getById(id);
		if(jeecgOrderMain==null) {
			result.error500("未找到对应实体");
		}else {
			result.setResult(jeecgOrderMain);
			result.setSuccess(true);
		}
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryOrderCustomerListByMainId")
	public Result<List<JeecgOrderCustomer>> queryOrderCustomerListByMainId(@RequestParam(name="id",required=true) String id) {
		Result<List<JeecgOrderCustomer>> result = new Result<List<JeecgOrderCustomer>>();
		List<JeecgOrderCustomer> jeecgOrderCustomerList = jeecgOrderCustomerService.selectCustomersByMainId(id);
		result.setResult(jeecgOrderCustomerList);
		result.setSuccess(true);
		return result;
	}
	
	/**
	  * 通过id查询
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryOrderTicketListByMainId")
	public Result<List<JeecgOrderTicket>> queryOrderTicketListByMainId(@RequestParam(name="id",required=true) String id) {
		Result<List<JeecgOrderTicket>> result = new Result<List<JeecgOrderTicket>>();
		List<JeecgOrderTicket> jeecgOrderTicketList = jeecgOrderTicketService.selectTicketsByMainId(id);
		result.setResult(jeecgOrderTicketList);
		result.setSuccess(true);
		return result;
	}

}
