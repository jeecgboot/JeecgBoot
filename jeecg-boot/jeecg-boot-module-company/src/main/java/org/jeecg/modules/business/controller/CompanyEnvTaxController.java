package org.jeecg.modules.business.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
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
import org.jeecg.modules.business.entity.CompanyEnvTax;
import org.jeecg.modules.business.service.ICompanyApplyService;
import org.jeecg.modules.business.service.ICompanyEnvTaxService;
import org.jeecg.modules.business.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @Description: 环保税信息
 * @Author: jeecg-boot
 * @Date: 2020-06-02
 * @Version: V1.0
 */
@Api(tags = "环保税信息")
@RestController
@RequestMapping("/envtax/companyEnvTax")
@Slf4j
public class CompanyEnvTaxController extends JeecgController<CompanyEnvTax, ICompanyEnvTaxService> {
    @Autowired
    private ICompanyEnvTaxService companyEnvTaxService;

    @Autowired
    private ICompanyApplyService companyApplyService;

    /**
     * 分页列表查询
     *
     * @param companyEnvTax
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "环保税信息-分页列表查询")
    @ApiOperation(value = "环保税信息-分页列表查询", notes = "环保税信息-分页列表查询")
    @GetMapping(value = "/list/{listType}")
    public Result<?> queryPageList(CompanyEnvTax companyEnvTax,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req, @PathVariable int listType) {
        QueryWrapper<CompanyEnvTax> queryWrapper = QueryGenerator.initQueryWrapper(companyEnvTax, req.getParameterMap());
        if (listType == 0) {
            queryWrapper.ne("status", Constant.status.EXPIRED);
        } else {
            queryWrapper.eq("status", Constant.status.PEND).or().eq("status", Constant.status.NORMAL);
        }
        Page<CompanyEnvTax> page = new Page<CompanyEnvTax>(pageNo, pageSize);
        IPage<CompanyEnvTax> pageList = companyEnvTaxService.page(page, queryWrapper);
        return Result.ok(pageList);
    }

    /**
     * 添加
     *
     * @param companyEnvTax
     * @return
     */
    @AutoLog(value = "环保税信息-添加")
    @ApiOperation(value = "环保税信息-添加", notes = "环保税信息-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody CompanyEnvTax companyEnvTax) {
        companyEnvTax.setStatus(Constant.status.TEMPORARY);
        companyEnvTaxService.save(companyEnvTax);
        //新增申报记录（暂存）
        companyApplyService.saveByBase(companyEnvTax.getCompanyId(),companyEnvTax.getId(),companyEnvTax.getStatus(),"", Constant.tables.ENVTAX);
        return Result.ok("添加成功！");
    }

    /**
     * 申报
     *
     * @param companyEnvTax
     * @return
     */
    @AutoLog(value = "环保税信息-申报")
    @ApiOperation(value = "环保税信息-申报", notes = "环保税信息-申报")
    @PutMapping(value = "/declare")
    public Result<?> declare(@RequestBody CompanyEnvTax companyEnvTax) {
        companyEnvTax.setStatus(Constant.status.PEND);
        //判断是新增还是编辑
        if (!StrUtil.isEmpty(companyEnvTax.getId())) {
            //编辑
            //查询修改之前的对象
            CompanyEnvTax oldCompanyEnvTax = companyEnvTaxService.getById(companyEnvTax.getId());
            //状态为正常
            if (Constant.status.NORMAL.equals(oldCompanyEnvTax.getStatus())) {
                //修改老数据状态为过期
                oldCompanyEnvTax.setStatus(Constant.status.EXPIRED);
                companyEnvTaxService.updateById(oldCompanyEnvTax);
                //新增修改后的为新数据
                companyEnvTax.setId("");
                companyEnvTaxService.save(companyEnvTax);
                //新增申报记录
                companyApplyService.saveByBase(companyEnvTax.getCompanyId(),companyEnvTax.getId(),companyEnvTax.getStatus(),oldCompanyEnvTax.getId(), Constant.tables.ENVTAX);
            } else if (Constant.status.NOPASS.equals(oldCompanyEnvTax.getStatus()) || Constant.status.TEMPORARY.equals(oldCompanyEnvTax.getStatus())) {
                //状态为审核未通过、暂存（直接修改）
                companyEnvTaxService.updateById(companyEnvTax);
                //修改申报记录状态为待审核
                CompanyApply companyApply = companyApplyService.findByNewId(companyEnvTax.getId(), Constant.tables.ENVTAX);
                companyApply.setStatus(Constant.status.PEND);
                companyApplyService.updateById(companyApply);
            }
        } else {
            //新增
            companyEnvTaxService.save(companyEnvTax);
            //新增申报记录
            companyApplyService.saveByBase(companyEnvTax.getCompanyId(),companyEnvTax.getId(),companyEnvTax.getStatus(),"", Constant.tables.ENVTAX);
        }
        return Result.ok("申报成功!");
    }

    /**
     * 编辑
     *
     * @param companyEnvTax
     * @return
     */
    @AutoLog(value = "环保税信息-编辑")
    @ApiOperation(value = "环保税信息-编辑", notes = "环保税信息-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody CompanyEnvTax companyEnvTax) {
        CompanyEnvTax oldCompanyEnvTax = companyEnvTaxService.getById(companyEnvTax.getId());
        //查询数据状态
        if (Constant.status.NORMAL.equals(companyEnvTax.getStatus())) {
            companyEnvTax.setStatus(Constant.status.TEMPORARY);
            //正常
            oldCompanyEnvTax.setStatus(Constant.status.EXPIRED);
            companyEnvTaxService.updateById(oldCompanyEnvTax);
            //新增修改后的为新数据
            companyEnvTax.setId("");
            companyEnvTaxService.save(companyEnvTax);
            //新增申报记录
            companyApplyService.saveByBase(companyEnvTax.getCompanyId(),companyEnvTax.getId(),companyEnvTax.getStatus(),oldCompanyEnvTax.getId(), Constant.tables.ENVTAX);
        } else if (Constant.status.NOPASS.equals(oldCompanyEnvTax.getStatus()) || Constant.status.TEMPORARY.equals(oldCompanyEnvTax.getStatus())) {
            companyEnvTax.setStatus(Constant.status.TEMPORARY);
            //状态为未通过和暂存的
            companyEnvTaxService.updateById(companyEnvTax);
            //修改申报记录状态为暂存
            CompanyApply companyApply = companyApplyService.findByNewId(companyEnvTax.getId(), Constant.tables.ENVTAX);
            companyApply.setStatus(Constant.status.TEMPORARY);
            companyApplyService.updateById(companyApply);
        }
        return Result.ok("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "环保税信息-通过id删除")
    @ApiOperation(value = "环保税信息-通过id删除", notes = "环保税信息-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        companyEnvTaxService.removeById(id);
        return Result.ok("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "环保税信息-批量删除")
    @ApiOperation(value = "环保税信息-批量删除", notes = "环保税信息-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.companyEnvTaxService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.ok("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "环保税信息-通过id查询")
    @ApiOperation(value = "环保税信息-通过id查询", notes = "环保税信息-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        CompanyEnvTax companyEnvTax = companyEnvTaxService.getById(id);
        if (companyEnvTax == null) {
            return Result.error("未找到对应数据");
        }
        return Result.ok(companyEnvTax);
    }

    @AutoLog(value = "环保税信息-查询坐标")
    @ApiOperation(value = "环保税信息-查询坐标", notes = "环保税信息-查询坐标")
    @GetMapping(value = "/loadBaiduMap")
    public Result<?> loadBaiduMap(@RequestParam(name = "address", required = true) String address) {
        String output = "json";
        String ak = "IFtTlZIkHXmWAeA4Dupd2AtYdbOmIhxL";
        String pathUrl = "http://api.map.baidu.com/geocoding/v3/";
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("ak", ak);
        paramMap.put("address", address);
        paramMap.put("output", "json");
        String result= HttpUtil.get(pathUrl, paramMap);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String status = jsonObject.getString("status");
        jsonObject.getJSONObject("");
        if(!"0".equals(status)){
            return Result.error(jsonObject.getString("message"));
        }
        return Result.ok(jsonObject.getJSONObject("result").getJSONObject("location"));
	}

    /**
     * @Description: 批量申报
     * @Param:
     * @return:
     * @Author: 周志远
     * @Date: 2020/6/4
     */
    @AutoLog(value = "环保税信息-批量申报")
    @ApiOperation(value = "环保税信息-批量申报", notes = "环保税信息-批量申报")
    @GetMapping(value = "/batchDeclare")
    public Result<?> batchDeclare(@RequestParam(name = "ids", required = true) String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        if (CollectionUtil.isNotEmpty(idList)) {
            for (Iterator<String> iterator = idList.iterator(); iterator.hasNext(); ) {
                String id = iterator.next();
                //查询
                CompanyEnvTax companyEnvTax = companyEnvTaxService.getById(id);
                //判断申报的是否是暂存
                if (!Constant.status.TEMPORARY.equals(companyEnvTax.getStatus())) {
                    return Result.error("请选择暂存的信息申报！");
                }
                //修改状态为1：待审核状态
                companyEnvTax.setStatus(Constant.status.PEND);
                companyEnvTaxService.updateById(companyEnvTax);
                //查询申报记录
                CompanyApply companyApply = companyApplyService.findByNewId(companyEnvTax.getId(), Constant.tables.ENVTAX);
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
     * @param companyEnvTax
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, CompanyEnvTax companyEnvTax) {
        return super.exportXls(request, companyEnvTax, CompanyEnvTax.class, "环保税信息");
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
        return super.importExcel(request, response, CompanyEnvTax.class);
    }

}
