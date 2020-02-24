package org.jeecg.modules.fms.reimburse.biz.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.fms.reimburse.biz.entity.ReimburseBizBaseDetailInfo;
import org.jeecg.modules.fms.reimburse.biz.entity.ReimburseBizMainInfo;
import org.jeecg.modules.fms.reimburse.biz.entity.ReimburseBizPaymentList;
import org.jeecg.modules.fms.reimburse.biz.entity.ReimburseBizVatDeductionVouchers;
import org.jeecg.modules.fms.reimburse.biz.service.IReimburseBizBaseDetailInfoService;
import org.jeecg.modules.fms.reimburse.biz.service.IReimburseBizMainInfoService;
import org.jeecg.modules.fms.reimburse.biz.service.IReimburseBizPaymentListService;
import org.jeecg.modules.fms.reimburse.biz.service.IReimburseBizVatDeductionVouchersService;
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
 * @Description: 报销单基本信息
 * @Author: jeecg-boot
 * @Date:   2020-01-08
 * @Version: V1.0
 */
@RestController
@RequestMapping("/biz/reimburseBizMainInfo")
@Slf4j
public class ReimburseBizMainInfoController extends JeecgController<ReimburseBizMainInfo, IReimburseBizMainInfoService> {

	@Autowired
	private IReimburseBizMainInfoService reimburseBizMainInfoService;

	@Autowired
	private IReimburseBizBaseDetailInfoService reimburseBizBaseDetailInfoService;

	@Autowired
	private IReimburseBizVatDeductionVouchersService reimburseBizVatDeductionVouchersService;

	@Autowired
	private IReimburseBizPaymentListService reimburseBizPaymentListService;


	/*---------------------------------主表处理-begin-------------------------------------*/

	/**
	 * 分页列表查询
	 * @param reimburseBizMainInfo
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@GetMapping(value = "/list")
	public Result<?> queryPageList(ReimburseBizMainInfo reimburseBizMainInfo,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		reimburseBizMainInfo.setUserId(sysUser.getUsername());
		QueryWrapper<ReimburseBizMainInfo> queryWrapper = QueryGenerator.initQueryWrapper(reimburseBizMainInfo, req.getParameterMap());
		Page<ReimburseBizMainInfo> page = new Page<ReimburseBizMainInfo>(pageNo, pageSize);
		IPage<ReimburseBizMainInfo> pageList = reimburseBizMainInfoService.page(page, queryWrapper);
		return Result.ok(pageList);
	}

	/**
     *   添加
     * @param reimburseBizMainInfo
     * @return
     */
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ReimburseBizMainInfo reimburseBizMainInfo) {
        reimburseBizMainInfoService.save(reimburseBizMainInfo);
        return Result.ok("添加成功！");
    }

    /**
     *  编辑
     * @param reimburseBizMainInfo
     * @return
     */
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ReimburseBizMainInfo reimburseBizMainInfo) {
    	LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
    	reimburseBizMainInfo.setUserId(sysUser.getUsername());
    	reimburseBizMainInfo.setUserName(sysUser.getRealname());
    	reimburseBizMainInfo.setUserCompanyCode(sysUser.getOrgCode());
        reimburseBizMainInfoService.updateById(reimburseBizMainInfo);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name="id",required=true) String id) {
        reimburseBizMainInfoService.delMain(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.reimburseBizMainInfoService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 导出
     * @return
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ReimburseBizMainInfo reimburseBizMainInfo) {
        return super.exportXls(request, reimburseBizMainInfo, ReimburseBizMainInfo.class, "报销单基本信息");
    }

    /**
     * 导入
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ReimburseBizMainInfo.class);
    }
    
    
    /**
	 * 分页列表查询
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/genReimburseNum", method = RequestMethod.GET)
	public Result<?> queryMaxApplyNo(HttpServletRequest req) {
		
		//获取当前用户
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String rbsTemplateCode = "CM01";
		String applyNo = reimburseBizMainInfoService.genReimburseNum(sysUser.getOrgCode(),rbsTemplateCode);
		System.out.println("applyNo == "+applyNo);
		return Result.ok(applyNo);
	}
	
	
	/**
	 * 分页列表查询
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/initRbsBizMainInfo", method = RequestMethod.GET)
	public Result<?> initRbsBizMainInfo(HttpServletRequest req) {
		
		//获取当前用户
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String rbsTemplateCode = "CM01";
		ReimburseBizMainInfo rbsBizMainInfo = reimburseBizMainInfoService.initRbsBizMainInfo(sysUser,sysUser.getOrgCode(),rbsTemplateCode);
		log.debug("rbsBizMainInfo == "+rbsBizMainInfo);
		return Result.ok(rbsBizMainInfo);
	}
	
	@RequestMapping(value = "/getRbmsBizMainInfoForAudit", method = RequestMethod.GET)
	public Result<?> getRbmsBizMainInfoForAudit(@RequestParam(name="applyNo",required=true) String applyNo) {
		
		//获取当前用户
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
		String rbsTemplateCode = "CM01";
		//String applyNo = req.getParameter("applyNo");
		
		ReimburseBizMainInfo rbsBizMainInfo = reimburseBizMainInfoService.getReimburseBizMainInfoByApplyNo(applyNo);
		log.debug("rbsBizMainInfo == "+rbsBizMainInfo);
		return Result.ok(rbsBizMainInfo);
	}
	
       
    
	/*---------------------------------主表处理-end-------------------------------------*/
	

    /*--------------------------------子表处理-报销单基本明细-begin----------------------------------------------*/
	/**
	 * 查询子表信息 会传入主表ID
	 * @return
	 */
	@GetMapping(value = "/listReimburseBizBaseDetailInfoByMainId")
    public Result<?> listReimburseBizBaseDetailInfoByMainId(ReimburseBizBaseDetailInfo reimburseBizBaseDetailInfo,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
		System.out.println("params==="+req.getParameterMap());
        QueryWrapper<ReimburseBizBaseDetailInfo> queryWrapper = QueryGenerator.initQueryWrapper(reimburseBizBaseDetailInfo, req.getParameterMap());
        Page<ReimburseBizBaseDetailInfo> page = new Page<ReimburseBizBaseDetailInfo>(pageNo, pageSize);
        IPage<ReimburseBizBaseDetailInfo> pageList = reimburseBizBaseDetailInfoService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

	/**
	 * 添加
	 * @param reimburseBizBaseDetailInfo
	 * @return
	 */
	@PostMapping(value = "/addReimburseBizBaseDetailInfo")
	public Result<?> addReimburseBizBaseDetailInfo(@RequestBody ReimburseBizBaseDetailInfo reimburseBizBaseDetailInfo) {
		reimburseBizBaseDetailInfoService.save(reimburseBizBaseDetailInfo);
		return Result.ok("添加成功！");
	}

    /**
	 * 编辑
	 * @param reimburseBizBaseDetailInfo
	 * @return
	 */
	@PutMapping(value = "/editReimburseBizBaseDetailInfo")
	public Result<?> editReimburseBizBaseDetailInfo(@RequestBody ReimburseBizBaseDetailInfo reimburseBizBaseDetailInfo) {
		reimburseBizBaseDetailInfoService.updateById(reimburseBizBaseDetailInfo);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/deleteReimburseBizBaseDetailInfo")
	public Result<?> deleteReimburseBizBaseDetailInfo(@RequestParam(name="id",required=true) String id) {
		reimburseBizBaseDetailInfoService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatchReimburseBizBaseDetailInfo")
	public Result<?> deleteBatchReimburseBizBaseDetailInfo(@RequestParam(name="ids",required=true) String ids) {
		this.reimburseBizBaseDetailInfoService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

    /*--------------------------------子表处理-报销单基本明细-end----------------------------------------------*/

    /*--------------------------------子表处理-报销单抵扣凭证-begin----------------------------------------------*/
	/**
	 * 查询子表信息 会传入主表ID
	 * @return
	 */
	@GetMapping(value = "/listReimburseBizVatDeductionVouchersByMainId")
    public Result<?> listReimburseBizVatDeductionVouchersByMainId(ReimburseBizVatDeductionVouchers reimburseBizVatDeductionVouchers,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<ReimburseBizVatDeductionVouchers> queryWrapper = QueryGenerator.initQueryWrapper(reimburseBizVatDeductionVouchers, req.getParameterMap());
        Page<ReimburseBizVatDeductionVouchers> page = new Page<ReimburseBizVatDeductionVouchers>(pageNo, pageSize);
        IPage<ReimburseBizVatDeductionVouchers> pageList = reimburseBizVatDeductionVouchersService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

	/**
	 * 添加
	 * @param reimburseBizVatDeductionVouchers
	 * @return
	 */
	@PostMapping(value = "/addReimburseBizVatDeductionVouchers")
	public Result<?> addReimburseBizVatDeductionVouchers(@RequestBody ReimburseBizVatDeductionVouchers reimburseBizVatDeductionVouchers) {
		reimburseBizVatDeductionVouchersService.save(reimburseBizVatDeductionVouchers);
		return Result.ok("添加成功！");
	}

    /**
	 * 编辑
	 * @param reimburseBizVatDeductionVouchers
	 * @return
	 */
	@PutMapping(value = "/editReimburseBizVatDeductionVouchers")
	public Result<?> editReimburseBizVatDeductionVouchers(@RequestBody ReimburseBizVatDeductionVouchers reimburseBizVatDeductionVouchers) {
		reimburseBizVatDeductionVouchersService.updateById(reimburseBizVatDeductionVouchers);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/deleteReimburseBizVatDeductionVouchers")
	public Result<?> deleteReimburseBizVatDeductionVouchers(@RequestParam(name="id",required=true) String id) {
		reimburseBizVatDeductionVouchersService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatchReimburseBizVatDeductionVouchers")
	public Result<?> deleteBatchReimburseBizVatDeductionVouchers(@RequestParam(name="ids",required=true) String ids) {
		this.reimburseBizVatDeductionVouchersService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

    /*--------------------------------子表处理-报销单抵扣凭证-end----------------------------------------------*/

    /*--------------------------------子表处理-报销单付款清单-begin----------------------------------------------*/
	/**
	 * 查询子表信息 会传入主表ID
	 * @return
	 */
	@GetMapping(value = "/listReimburseBizPaymentListByMainId")
    public Result<?> listReimburseBizPaymentListByMainId(ReimburseBizPaymentList reimburseBizPaymentList,
                                                    @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                    HttpServletRequest req) {
        QueryWrapper<ReimburseBizPaymentList> queryWrapper = QueryGenerator.initQueryWrapper(reimburseBizPaymentList, req.getParameterMap());
        Page<ReimburseBizPaymentList> page = new Page<ReimburseBizPaymentList>(pageNo, pageSize);
        IPage<ReimburseBizPaymentList> pageList = reimburseBizPaymentListService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

	/**
	 * 添加
	 * @param reimburseBizPaymentList
	 * @return
	 */
	@PostMapping(value = "/addReimburseBizPaymentList")
	public Result<?> addReimburseBizPaymentList(@RequestBody ReimburseBizPaymentList reimburseBizPaymentList) {
		reimburseBizPaymentListService.save(reimburseBizPaymentList);
		return Result.ok("添加成功！");
	}

    /**
	 * 编辑
	 * @param reimburseBizPaymentList
	 * @return
	 */
	@PutMapping(value = "/editReimburseBizPaymentList")
	public Result<?> editReimburseBizPaymentList(@RequestBody ReimburseBizPaymentList reimburseBizPaymentList) {
		reimburseBizPaymentListService.updateById(reimburseBizPaymentList);
		return Result.ok("编辑成功!");
	}

	/**
	 * 通过id删除
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/deleteReimburseBizPaymentList")
	public Result<?> deleteReimburseBizPaymentList(@RequestParam(name="id",required=true) String id) {
		reimburseBizPaymentListService.removeById(id);
		return Result.ok("删除成功!");
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@DeleteMapping(value = "/deleteBatchReimburseBizPaymentList")
	public Result<?> deleteBatchReimburseBizPaymentList(@RequestParam(name="ids",required=true) String ids) {
		this.reimburseBizPaymentListService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}

    /*--------------------------------子表处理-报销单付款清单-end----------------------------------------------*/




}
