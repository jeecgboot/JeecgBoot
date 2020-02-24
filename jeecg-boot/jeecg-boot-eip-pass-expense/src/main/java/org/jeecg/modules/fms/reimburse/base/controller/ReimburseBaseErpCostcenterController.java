package org.jeecg.modules.fms.reimburse.base.controller;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.fms.reimburse.base.entity.ReimburseBaseErpCostcenter;
import org.jeecg.modules.fms.reimburse.base.service.IReimburseBaseErpCostcenterService;
import org.jeecg.modules.fms.reimburse.base.vo.CostcenterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import lombok.extern.slf4j.Slf4j;

 /**
 * @Description: reimburse_base_erp_costcenter
 * @Author: jeecg-boot
 * @Date:   2020-02-11
 * @Version: V1.0
 */
@RestController
@RequestMapping("/base/rmbsErpCostcenter")
@Slf4j
public class ReimburseBaseErpCostcenterController extends JeecgController<ReimburseBaseErpCostcenter, IReimburseBaseErpCostcenterService> {
	@Autowired
	private IReimburseBaseErpCostcenterService reimburseBaseErpCostcenterService;
	
	/**
	 * 分页列表查询
	 *
	 * @param reimburseBaseErpCostcenter
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ReimburseBaseErpCostcenter reimburseBaseErpCostcenter,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ReimburseBaseErpCostcenter> queryWrapper = QueryGenerator.initQueryWrapper(reimburseBaseErpCostcenter, req.getParameterMap());
		Page<ReimburseBaseErpCostcenter> page = new Page<ReimburseBaseErpCostcenter>(pageNo, pageSize);
		IPage<ReimburseBaseErpCostcenter> pageList = reimburseBaseErpCostcenterService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param reimburseBaseErpCostcenter
	 * @return
	 */
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody ReimburseBaseErpCostcenter reimburseBaseErpCostcenter) {
		reimburseBaseErpCostcenterService.save(reimburseBaseErpCostcenter);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param reimburseBaseErpCostcenter
	 * @return
	 */
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody ReimburseBaseErpCostcenter reimburseBaseErpCostcenter) {
		reimburseBaseErpCostcenterService.updateById(reimburseBaseErpCostcenter);
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
		reimburseBaseErpCostcenterService.removeById(id);
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
		this.reimburseBaseErpCostcenterService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		ReimburseBaseErpCostcenter reimburseBaseErpCostcenter = reimburseBaseErpCostcenterService.getById(id);
		if(reimburseBaseErpCostcenter==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(reimburseBaseErpCostcenter);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param reimburseBaseErpCostcenter
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ReimburseBaseErpCostcenter reimburseBaseErpCostcenter) {
        return super.exportXls(request, reimburseBaseErpCostcenter, ReimburseBaseErpCostcenter.class, "reimburse_base_erp_costcenter");
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
        return super.importExcel(request, response, ReimburseBaseErpCostcenter.class);
    }
    
    /**
	 * 初始化成本中心
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/initCostCenterInfo", method = RequestMethod.GET)
	public Result<?> initCostCenterInfo(HttpServletRequest req) {
		
		//获取当前用户
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String rbsTemplateCode = "CM01";
		Result<CostcenterVO> result = new Result<CostcenterVO>();
		CostcenterVO costCenter  = this.reimburseBaseErpCostcenterService.initCostCenterInfo(sysUser.getOrgCode());
		log.info("costCenterVO == "+costCenter);
		
		if(costCenter!=null ) {
			result.setSuccess(true);
			result.setResult(costCenter);
		}else {
			result.setSuccess(false);
			result.error500("操作失败");
			return result;
		}
		
		return result;
	}

}
