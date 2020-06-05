package org.jeecg.modules.business.controller;

import cn.hutool.core.collection.CollectionUtil;
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
import org.jeecg.modules.business.entity.CompanyAcceptance;
import org.jeecg.modules.business.service.ICompanyAcceptanceService;
import org.jeecg.modules.business.utils.Constant.status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @Description: company_acceptance
 * @Author: jeecg-boot
 * @Date: 2020-05-27
 * @Version: V1.0
 */
@Api(tags = "company_acceptance")
@RestController
@RequestMapping("/business/companyAcceptance")
@Slf4j
public class CompanyAcceptanceController extends JeecgController<CompanyAcceptance, ICompanyAcceptanceService> {
    @Autowired
    private ICompanyAcceptanceService companyAcceptanceService;

    /**
     * 分页列表查询
     *
     * @param companyAcceptance
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "company_acceptance-分页列表查询")
    @ApiOperation(value = "company_acceptance-分页列表查询", notes = "company_acceptance-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(CompanyAcceptance companyAcceptance,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<CompanyAcceptance> queryWrapper = QueryGenerator.initQueryWrapper(companyAcceptance, req.getParameterMap());
        Page<CompanyAcceptance> page = new Page<CompanyAcceptance>(pageNo, pageSize);
        IPage<CompanyAcceptance> pageList = companyAcceptanceService.page(page, queryWrapper);
        return Result.ok(pageList);
    }


    /**
     * 添加
     *
     * @param companyAcceptance
     * @return
     */
    @AutoLog(value = "company_acceptance-添加")
    @ApiOperation(value = "company_acceptance-添加", notes = "company_acceptance-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody CompanyAcceptance companyAcceptance) {
        companyAcceptanceService.save(companyAcceptance);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param companyAcceptance
     * @return
     */
    @AutoLog(value = "company_acceptance-编辑")
    @ApiOperation(value = "company_acceptance-编辑", notes = "company_acceptance-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody CompanyAcceptance companyAcceptance) {
        //查询修改之前的
        CompanyAcceptance oldcompanyAcceptance = companyAcceptanceService.getById(companyAcceptance.getId());
        //修改老的实体状态为过期
        oldcompanyAcceptance.setStatus(status.EXPIRED);
        companyAcceptanceService.updateById(oldcompanyAcceptance);
        //把编辑过得新增
        companyAcceptance.setId("");
        companyAcceptance.setOldid(oldcompanyAcceptance.getId());
        companyAcceptanceService.save(companyAcceptance);
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "company_acceptance-通过id删除")
    @ApiOperation(value = "company_acceptance-通过id删除", notes = "company_acceptance-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        companyAcceptanceService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "company_acceptance-批量删除")
    @ApiOperation(value = "company_acceptance-批量删除", notes = "company_acceptance-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.companyAcceptanceService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "company_acceptance-通过id查询")
    @ApiOperation(value = "company_acceptance-通过id查询", notes = "company_acceptance-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        CompanyAcceptance companyAcceptance = companyAcceptanceService.getById(id);
        if (companyAcceptance == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(companyAcceptance);
    }

    /**
     * @Description: 申报
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/6/4
     */
    @AutoLog(value = "company_acceptance-申报")
    @ApiOperation(value = "company_acceptance-申报", notes = "company_acceptance-申报")
    @GetMapping(value = "/declare")
    public Result<?> declare(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        if (CollectionUtil.isNotEmpty(idList)) {
            for (Iterator<String> iterator = idList.iterator(); iterator.hasNext(); ) {
                String id = iterator.next();
                //查询
                CompanyAcceptance companyAcceptance = companyAcceptanceService.getById(id);
                //判断申报的是否是暂存
                if (!status.TEMPORARY.equals(companyAcceptance.getStatus())) {
                    return Result.error("请选择暂存的竣工验收信息申报！");
                }
                //修改状态为1：待审核状态
                companyAcceptance.setStatus(status.PEND);
                boolean issuccess = companyAcceptanceService.updateById(companyAcceptance);
                if (!issuccess) {
                    return Result.error("申报失败！");
                }
            }
        }
        return Result.ok("申报成功!");
    }


    /**
     * 导出excel
     *
     * @param request
     * @param companyAcceptance
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyAcceptance companyAcceptance) {
        return super.exportXls(request, companyAcceptance, CompanyAcceptance.class, "company_acceptance");
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
        return super.importExcel(request, response, CompanyAcceptance.class);
    }


}
