package org.jeecg.modules.business.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
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
import org.jeecg.modules.business.entity.CompanyEnvTax;
import org.jeecg.modules.business.service.ICompanyEnvTaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;

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
    private RestTemplate restTemplate;

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
    @GetMapping(value = "/list")
    public Result<?> queryPageList(CompanyEnvTax companyEnvTax,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<CompanyEnvTax> queryWrapper = QueryGenerator.initQueryWrapper(companyEnvTax, req.getParameterMap());
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
        companyEnvTaxService.save(companyEnvTax);
        return Result.ok("添加成功！");
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
        companyEnvTaxService.updateById(companyEnvTax);
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
