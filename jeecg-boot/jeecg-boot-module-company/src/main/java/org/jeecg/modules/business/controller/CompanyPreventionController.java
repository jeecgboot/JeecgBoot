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
import org.jeecg.modules.business.entity.CompanyPrevention;
import org.jeecg.modules.business.service.ICompanyApplyService;
import org.jeecg.modules.business.service.ICompanyPreventionService;
import org.jeecg.modules.business.utils.Constant.status;
import org.jeecg.modules.business.utils.Constant.tables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @Description: 污染防治信息
 * @Author: jeecg-boot
 * @Date: 2020-06-01
 * @Version: V1.0
 */
@Api(tags = "污染防治信息")
@RestController
@RequestMapping("/prevention/companyPrevention")
@Slf4j
public class CompanyPreventionController extends JeecgController<CompanyPrevention, ICompanyPreventionService> {
    @Autowired
    private ICompanyPreventionService companyPreventionService;

    @Autowired
    private ICompanyApplyService companyApplyService;

    /**
     * 分页列表查询
     *
     * @param companyPrevention
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "污染防治信息-分页列表查询")
    @ApiOperation(value = "污染防治信息-分页列表查询", notes = "污染防治信息-分页列表查询")
    @GetMapping(value = "/list/{listType}")
    public Result<?> queryPageList(CompanyPrevention companyPrevention,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, @PathVariable int listType) {
        QueryWrapper<CompanyPrevention> queryWrapper = QueryGenerator.initQueryWrapper(companyPrevention, req.getParameterMap());
        if (listType == 0) {
            queryWrapper.ne("status", status.EXPIRED);
        } else {
            queryWrapper.eq("status", status.PEND).or().eq("status", status.NORMAL);
        }
        Page<CompanyPrevention> page = new Page<CompanyPrevention>(pageNo, pageSize);
        IPage<CompanyPrevention> pageList = companyPreventionService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param companyPrevention
     * @return
     */
    @AutoLog(value = "污染防治信息-添加")
    @ApiOperation(value = "污染防治信息-添加", notes = "污染防治信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody CompanyPrevention companyPrevention) {
        companyPrevention.setStatus(status.TEMPORARY);
        companyPreventionService.save(companyPrevention);
        //新增申报记录（暂存）
        companyApplyService.saveByBase(companyPrevention.getCompanyId(), companyPrevention.getId(), companyPrevention.getStatus(), "", tables.PREVENTION);
        return Result.ok("添加成功！");
    }

    /**
     * 编辑
     *
     * @param companyPrevention
     * @return
     */
    @AutoLog(value = "污染防治信息-编辑")
    @ApiOperation(value = "污染防治信息-编辑", notes = "污染防治信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody CompanyPrevention companyPrevention) {
        CompanyPrevention oldcompanyPrevention = companyPreventionService.getById(companyPrevention.getId());
        //查询数据状态
        if (status.NORMAL.equals(companyPrevention.getStatus())) {
            companyPrevention.setStatus(status.TEMPORARY);
            //正常
            oldcompanyPrevention.setStatus(status.EXPIRED);
            companyPreventionService.updateById(oldcompanyPrevention);
            //新增修改后的为新数据
            companyPrevention.setId("");
            companyPreventionService.save(companyPrevention);
            //新增申报记录
            companyApplyService.saveByBase(companyPrevention.getCompanyId(), companyPrevention.getId(), companyPrevention.getStatus(), oldcompanyPrevention.getId(), tables.PREVENTION);
        } else if (status.NOPASS.equals(oldcompanyPrevention.getStatus()) || status.TEMPORARY.equals(oldcompanyPrevention.getStatus())) {
            companyPrevention.setStatus(status.TEMPORARY);
            //状态为未通过和暂存的
            companyPreventionService.updateById(companyPrevention);
            //修改申报记录状态为暂存
            CompanyApply companyApply = companyApplyService.findByNewId(companyPrevention.getId(), tables.PREVENTION);
            companyApply.setStatus(status.TEMPORARY);
            companyApplyService.updateById(companyApply);
        }
        return Result.ok("编辑成功!");
    }

    /**
     * 申报
     *
     * @param companyPrevention
     * @return
     */
    @AutoLog(value = "污染防治信息-申报")
    @ApiOperation(value = "污染防治信息-申报", notes = "污染防治信息-申报")
    @PutMapping(value = "/declare")
    public Result<?> declare(@RequestBody CompanyPrevention companyPrevention) {
        companyPrevention.setStatus(status.PEND);
        //判断是新增还是编辑
        if (!StrUtil.isEmpty(companyPrevention.getId())) {
            //编辑
            //查询修改之前的对象
            CompanyPrevention oldcompanyPrevention = companyPreventionService.getById(companyPrevention.getId());
            //状态为正常
            if (status.NORMAL.equals(oldcompanyPrevention.getStatus())) {
                //修改老数据状态为过期
                oldcompanyPrevention.setStatus(status.EXPIRED);
                companyPreventionService.updateById(oldcompanyPrevention);
                //新增修改后的为新数据
                companyPrevention.setId("");
                companyPreventionService.save(companyPrevention);
                //新增申报记录
                companyApplyService.saveByBase(companyPrevention.getCompanyId(), companyPrevention.getId(), companyPrevention.getStatus(), oldcompanyPrevention.getId(), tables.PREVENTION);
            } else if (status.NOPASS.equals(oldcompanyPrevention.getStatus()) || status.TEMPORARY.equals(oldcompanyPrevention.getStatus())) {
                //状态为审核未通过、暂存（直接修改）
                companyPreventionService.updateById(companyPrevention);
                //修改申报记录状态为待审核
                CompanyApply companyApply = companyApplyService.findByNewId(companyPrevention.getId(), tables.PREVENTION);
                companyApply.setStatus(status.PEND);
                companyApplyService.updateById(companyApply);
            }
        } else {
            //新增
            companyPreventionService.save(companyPrevention);
            //新增申报记录
            companyApplyService.saveByBase(companyPrevention.getCompanyId(), companyPrevention.getId(), companyPrevention.getStatus(), "", tables.PREVENTION);
        }
        return Result.ok("申报成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "污染防治信息-通过id删除")
    @ApiOperation(value = "污染防治信息-通过id删除", notes = "污染防治信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        companyPreventionService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "污染防治信息-批量删除")
    @ApiOperation(value = "污染防治信息-批量删除", notes = "污染防治信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.companyPreventionService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "污染防治信息-通过id查询")
    @ApiOperation(value = "污染防治信息-通过id查询", notes = "污染防治信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        CompanyPrevention companyPrevention = companyPreventionService.getById(id);
        if (companyPrevention == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(companyPrevention);
    }

    /**
     * @Description: 申报
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/6/4
     */
    @AutoLog(value = "污染防治信息-批量申报")
    @ApiOperation(value = "污染防治信息-批量申报", notes = "污染防治信息-批量申报")
    @GetMapping(value = "/batchDeclare")
    public Result<?> declare(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        if (CollectionUtil.isNotEmpty(idList)) {
            for (Iterator<String> iterator = idList.iterator(); iterator.hasNext(); ) {
                String id = iterator.next();
                //查询
                CompanyPrevention companyPrevention = companyPreventionService.getById(id);
                //判断申报的是否是暂存
                if (!status.TEMPORARY.equals(companyPrevention.getStatus())) {
                    return Result.error("请选择暂存的竣工验收信息申报！");
                }
                //修改状态为1：待审核状态
                companyPrevention.setStatus(status.PEND);
                companyPreventionService.updateById(companyPrevention);
                //查询申报记录
                CompanyApply companyApply = companyApplyService.findByNewId(companyPrevention.getId(), tables.PREVENTION);
                companyApply.setStatus(status.PEND);
                companyApplyService.updateById(companyApply);
            }
        }
        return Result.ok("申报成功!");
    }

    /**
     * 导出excel
     *
     * @param request
     * @param companyPrevention
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyPrevention companyPrevention) {
        return super.exportXls(request, companyPrevention, CompanyPrevention.class, "污染防治信息");
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
        return super.importExcel(request, response, CompanyPrevention.class);
    }

}
