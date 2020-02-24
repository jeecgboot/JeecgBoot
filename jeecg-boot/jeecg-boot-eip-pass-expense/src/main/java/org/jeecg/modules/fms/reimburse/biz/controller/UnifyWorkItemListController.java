package org.jeecg.modules.fms.reimburse.biz.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.SqlInjectionUtil;
import org.jeecg.modules.fms.reimburse.biz.service.IUnfiyWorkItemListService;
import org.jeecg.modules.fms.reimburse.biz.utils.VO.UnifyWorkItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;


/**
* @Description: 报销单基本信息
* @Author: jeecg-boot
* @Date:   2020-01-08
* @Version: V1.0
*/
@RestController
@RequestMapping("/biz/unifyworkitemList")
@Slf4j
public class UnifyWorkItemListController extends JeecgController<UnifyWorkItemVO, IUnfiyWorkItemListService> {

	@Autowired
	private IUnfiyWorkItemListService unifyworkItemListService;
	
	/**
	 * 分页待办列表查询
	 * @param reimburseBizMainInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/pagedListWorkItem")
	public Result<?> queryPageList(UnifyWorkItemVO unifyWorkItemVO,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		//QueryWrapper<UnifyWorkItemVO> queryWrapper = QueryGenerator.initQueryWrapper(unifyWorkItemVO, req.getParameterMap());
		Page<UnifyWorkItemVO> page = new Page<UnifyWorkItemVO>(pageNo, pageSize);
		//IPage<UnifyWorkItemVO> pageList = unifyworkItemListService.page(page, queryWrapper);
		Map<String,String[]> paramMap =  req.getParameterMap();
		 String applyNo = "";
		 if(paramMap!=null) {
			 String[] args = paramMap.get("applyNo");
			 applyNo = args!=null?args[0].trim():"";
			
		 }
		 String reimbursementItem = "";
		 if(paramMap!=null) {
			 String[] args = paramMap.get("reimbursementItem");
			 reimbursementItem = args!=null?args[0].trim():"";
			
		 }
		 
		 /**参数过滤*/
		 final String[] sqlInjCheck = {applyNo,reimbursementItem};
		 SqlInjectionUtil.filterContent(sqlInjCheck);
		 
		 
		 Map<String,String> paramsMap = new HashMap();
		 paramsMap.put("applyNo", applyNo);
		 paramsMap.put("reimbursementItem", reimbursementItem);
		 paramsMap.put("userCode", sysUser.getUsername());
		 
		 IPage<UnifyWorkItemVO> pageList = unifyworkItemListService.pageUnfiyWorkItemList(page, paramsMap);
		 
		return Result.ok(pageList);
	}
	
	
	
}
