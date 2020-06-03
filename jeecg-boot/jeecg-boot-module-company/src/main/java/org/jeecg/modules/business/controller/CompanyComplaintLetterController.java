package org.jeecg.modules.business.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.business.entity.CompanyComplaintLetter;
import org.jeecg.modules.business.service.ICompanyComplaintLetterService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: 信访投诉信息
 * @Author: jeecg-boot
 * @Date:   2020-06-02
 * @Version: V1.0
 */
@Api(tags="信访投诉信息")
@RestController
@RequestMapping("/ccl/companyComplaintLetter")
@Slf4j
public class CompanyComplaintLetterController extends JeecgController<CompanyComplaintLetter, ICompanyComplaintLetterService> {
	@Autowired
	private ICompanyComplaintLetterService companyComplaintLetterService;
	
	/**
	 * 分页列表查询
	 *
	 * @param companyComplaintLetter
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	@AutoLog(value = "信访投诉信息-分页列表查询")
	@ApiOperation(value="信访投诉信息-分页列表查询", notes="信访投诉信息-分页列表查询")
	@GetMapping(value = "/list")
	public Result<?> queryPageList(CompanyComplaintLetter companyComplaintLetter,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<CompanyComplaintLetter> queryWrapper = QueryGenerator.initQueryWrapper(companyComplaintLetter, req.getParameterMap());
		Page<CompanyComplaintLetter> page = new Page<CompanyComplaintLetter>(pageNo, pageSize);
		IPage<CompanyComplaintLetter> pageList = companyComplaintLetterService.page(page, queryWrapper);
		return Result.ok(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param companyComplaintLetter
	 * @return
	 */
	@AutoLog(value = "信访投诉信息-添加")
	@ApiOperation(value="信访投诉信息-添加", notes="信访投诉信息-添加")
	@PostMapping(value = "/add")
	public Result<?> add(@RequestBody CompanyComplaintLetter companyComplaintLetter) {
		companyComplaintLetterService.save(companyComplaintLetter);
		return Result.ok("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param companyComplaintLetter
	 * @return
	 */
	@AutoLog(value = "信访投诉信息-编辑")
	@ApiOperation(value="信访投诉信息-编辑", notes="信访投诉信息-编辑")
	@PutMapping(value = "/edit")
	public Result<?> edit(@RequestBody CompanyComplaintLetter companyComplaintLetter) {
		companyComplaintLetterService.updateById(companyComplaintLetter);
		return Result.ok("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "信访投诉信息-通过id删除")
	@ApiOperation(value="信访投诉信息-通过id删除", notes="信访投诉信息-通过id删除")
	@DeleteMapping(value = "/delete")
	public Result<?> delete(@RequestParam(name="id",required=true) String id) {
		companyComplaintLetterService.removeById(id);
		return Result.ok("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "信访投诉信息-批量删除")
	@ApiOperation(value="信访投诉信息-批量删除", notes="信访投诉信息-批量删除")
	@DeleteMapping(value = "/deleteBatch")
	public Result<?> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.companyComplaintLetterService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.ok("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "信访投诉信息-通过id查询")
	@ApiOperation(value="信访投诉信息-通过id查询", notes="信访投诉信息-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<?> queryById(@RequestParam(name="id",required=true) String id) {
		CompanyComplaintLetter companyComplaintLetter = companyComplaintLetterService.getById(id);
		if(companyComplaintLetter==null) {
			return Result.error("未找到对应数据");
		}
		return Result.ok(companyComplaintLetter);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param companyComplaintLetter
    */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyComplaintLetter companyComplaintLetter) {
        return super.exportXls(request, companyComplaintLetter, CompanyComplaintLetter.class, "信访投诉信息");
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
        return super.importExcel(request, response, CompanyComplaintLetter.class);
    }

}
