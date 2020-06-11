package org.jeecg.modules.business.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.CompanyApply;
import org.jeecg.modules.business.entity.CompanyCleanProduct;
import org.jeecg.modules.business.service.ICompanyApplyService;
import org.jeecg.modules.business.service.ICompanyCleanProductService;
import org.jeecg.modules.business.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @Description: 清洁生产
 * @Author: jeecg-boot
 * @Date:   2020-06-03
 * @Version: V1.0
 */
@Api(tags="清洁生产")
@RestController
@RequestMapping("/cleanProduct/companyCleanProduct")
@Slf4j
public class CompanyCleanProductController extends JeecgController<CompanyCleanProduct, ICompanyCleanProductService> {
	@Autowired
	private ICompanyCleanProductService companyCleanProductService;

	@Autowired
	private ICompanyApplyService companyApplyService;
	
	/**
	 * 分页列表查询
	 *
	 * @param companyCleanProduct
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "清洁生产-分页列表查询")
	@ApiOperation(value="清洁生产-分页列表查询", notes="清洁生产-分页列表查询")
	@GetMapping(value = "/list/{listType}")
	public Result<?> queryPageList(CompanyCleanProduct companyCleanProduct,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req, @PathVariable int listType) {
		QueryWrapper<CompanyCleanProduct> queryWrapper = QueryGenerator.initQueryWrapper(companyCleanProduct, req.getParameterMap());
		if (listType == 0) {
			queryWrapper.ne("status", Constant.status.EXPIRED);
		} else {
			queryWrapper.eq("status", Constant.status.PEND).or().eq("status", Constant.status.NORMAL);
		}
		Page<CompanyCleanProduct> page = new Page<CompanyCleanProduct>(pageNo, pageSize);
		IPage<CompanyCleanProduct> pageList = companyCleanProductService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param companyCleanProduct
	 * @return
	 */
	@AutoLog(value = "清洁生产-添加")
	@ApiOperation(value="清洁生产-添加", notes="清洁生产-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanyCleanProduct companyCleanProduct) {
		companyCleanProduct.setStatus(Constant.status.TEMPORARY);
		companyCleanProductService.save(companyCleanProduct);
		//新增申报记录（暂存）
		companyApplyService.saveByBase(companyCleanProduct.getCompanyId(),companyCleanProduct.getId(),companyCleanProduct.getStatus(),"", Constant.tables.CLEANPRODUCT);
		return Result.ok("添加成功！");
	}

	/**
	 * 申报
	 *
	 * @param companyCleanProduct
	 * @return
	 */
	@AutoLog(value = "清洁生产-申报")
	@ApiOperation(value = "清洁生产-申报", notes = "清洁生产-申报")
	@PutMapping(value = "/declare")
	public Result<?> declare(@RequestBody CompanyCleanProduct companyCleanProduct) {
		companyCleanProduct.setStatus(Constant.status.PEND);
		//判断是新增还是编辑
		if (!StrUtil.isEmpty(companyCleanProduct.getId())) {
			//编辑
			//查询修改之前的对象
			CompanyCleanProduct oldCompanyCleanProduct = companyCleanProductService.getById(companyCleanProduct.getId());
			//状态为正常
			if (Constant.status.NORMAL.equals(oldCompanyCleanProduct.getStatus())) {
				//修改老数据状态为过期
				oldCompanyCleanProduct.setStatus(Constant.status.EXPIRED);
				companyCleanProductService.updateById(oldCompanyCleanProduct);
				//新增修改后的为新数据
				companyCleanProduct.setId("");
				companyCleanProductService.save(companyCleanProduct);
				//新增申报记录
				companyApplyService.saveByBase(companyCleanProduct.getCompanyId(),companyCleanProduct.getId(),companyCleanProduct.getStatus(),oldCompanyCleanProduct.getId(), Constant.tables.CLEANPRODUCT);
			} else if (Constant.status.NOPASS.equals(oldCompanyCleanProduct.getStatus()) || Constant.status.TEMPORARY.equals(oldCompanyCleanProduct.getStatus())) {
				//状态为审核未通过、暂存（直接修改）
				companyCleanProductService.updateById(companyCleanProduct);
				//修改申报记录状态为待审核
				CompanyApply companyApply = companyApplyService.findByNewId(companyCleanProduct.getId(), Constant.tables.CLEANPRODUCT);
				companyApply.setStatus(Constant.status.PEND);
				companyApplyService.updateById(companyApply);
			}
		} else {
			//新增
			companyCleanProductService.save(companyCleanProduct);
			//新增申报记录
			companyApplyService.saveByBase(companyCleanProduct.getCompanyId(),companyCleanProduct.getId(),companyCleanProduct.getStatus(),"", Constant.tables.CLEANPRODUCT);
		}
		return Result.ok("申报成功!");
	}
	
	/**
	 *  编辑
	 *
	 * @param companyCleanProduct
	 * @return
	 */
	@AutoLog(value = "清洁生产-编辑")
	@ApiOperation(value="清洁生产-编辑", notes="清洁生产-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanyCleanProduct companyCleanProduct) {
		CompanyCleanProduct oldCompanyCleanProduct = companyCleanProductService.getById(companyCleanProduct.getId());
		//查询数据状态
		if (Constant.status.NORMAL.equals(companyCleanProduct.getStatus())) {
			companyCleanProduct.setStatus(Constant.status.TEMPORARY);
			//正常
			oldCompanyCleanProduct.setStatus(Constant.status.EXPIRED);
			companyCleanProductService.updateById(oldCompanyCleanProduct);
			//新增修改后的为新数据
			companyCleanProduct.setId("");
			companyCleanProductService.save(companyCleanProduct);
			//新增申报记录
			companyApplyService.saveByBase(companyCleanProduct.getCompanyId(),companyCleanProduct.getId(),companyCleanProduct.getStatus(),oldCompanyCleanProduct.getId(), Constant.tables.CLEANPRODUCT);
		} else if (Constant.status.NOPASS.equals(oldCompanyCleanProduct.getStatus()) || Constant.status.TEMPORARY.equals(oldCompanyCleanProduct.getStatus())) {
			companyCleanProduct.setStatus(Constant.status.TEMPORARY);
			//状态为未通过和暂存的
			companyCleanProductService.updateById(companyCleanProduct);
			//修改申报记录状态为暂存
			CompanyApply companyApply = companyApplyService.findByNewId(companyCleanProduct.getId(), Constant.tables.CLEANPRODUCT);
			companyApply.setStatus(Constant.status.TEMPORARY);
			companyApplyService.updateById(companyApply);
		}
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "清洁生产-通过id删除")
	@ApiOperation(value="清洁生产-通过id删除", notes="清洁生产-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		companyCleanProductService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "清洁生产-批量删除")
	@ApiOperation(value="清洁生产-批量删除", notes="清洁生产-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.companyCleanProductService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "清洁生产-通过id查询")
	@ApiOperation(value="清洁生产-通过id查询", notes="清洁生产-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyCleanProduct companyCleanProduct = companyCleanProductService.getById(id);
		if(companyCleanProduct==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(companyCleanProduct);
	}

	/**
	 * @Description: 批量申报
	 * @Param:
	 * @return:
	 * @Author: 周志远
	 * @Date: 2020/6/11
	 */
	@AutoLog(value = "清洁生产-批量申报")
	@ApiOperation(value = "清洁生产-批量申报", notes = "清洁生产-批量申报")
	@GetMapping(value = "/batchDeclare")
	public Result<?> batchDeclare(@RequestParam(name = "ids", required = true) String ids) {
		List<String> idList = Arrays.asList(ids.split(","));
		if (CollectionUtil.isNotEmpty(idList)) {
			for (Iterator<String> iterator = idList.iterator(); iterator.hasNext(); ) {
				String id = iterator.next();
				//查询
				CompanyCleanProduct companyCleanProduct = companyCleanProductService.getById(id);
				//判断申报的是否是暂存
				if (!Constant.status.TEMPORARY.equals(companyCleanProduct.getStatus())) {
					return Result.error("请选择暂存的竣工验收信息申报！");
				}
				//修改状态为1：待审核状态
				companyCleanProduct.setStatus(Constant.status.PEND);
				companyCleanProductService.updateById(companyCleanProduct);
				//查询申报记录
				CompanyApply companyApply = companyApplyService.findByNewId(companyCleanProduct.getId(), Constant.tables.CLEANPRODUCT);
				companyApply.setStatus(Constant.status.PEND);
				companyApplyService.updateById(companyApply);
			}
		}
		return Result.ok("申报成功!");
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companyCleanProduct
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyCleanProduct companyCleanProduct) {
        return super.exportXls(request, companyCleanProduct, CompanyCleanProduct.class, "清洁生产");
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
        return super.importExcel(request, response, CompanyCleanProduct.class);
    }

}
