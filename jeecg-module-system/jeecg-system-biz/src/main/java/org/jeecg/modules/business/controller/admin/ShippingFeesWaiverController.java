package org.jeecg.modules.business.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.business.entity.ShippingDiscount;
import org.jeecg.modules.business.entity.ShippingFeesWaiver;
import org.jeecg.modules.business.entity.ShippingFeesWaiverProduct;
import org.jeecg.modules.business.service.IShippingFeesWaiverProductService;
import org.jeecg.modules.business.service.IShippingFeesWaiverService;
import org.jeecg.modules.business.vo.ShippingFeesWaiverPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 采购运费免除
 * @Author: jeecg-boot
 * @Date: 2021-06-02
 * @Version: V1.0
 */
@Api(tags = "采购运费免除")
@RestController
@RequestMapping("/waiver/shippingFeesWaiver")
@Slf4j
public class ShippingFeesWaiverController {
    @Autowired
    private IShippingFeesWaiverService shippingFeesWaiverService;
    @Autowired
    private IShippingFeesWaiverProductService shippingFeesWaiverProductService;

    /**
     * 分页列表查询
     *
     * @param shippingFeesWaiver
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "采购运费免除-分页列表查询")
    @ApiOperation(value = "采购运费免除-分页列表查询", notes = "采购运费免除-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(ShippingFeesWaiver shippingFeesWaiver,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<ShippingFeesWaiver> queryWrapper = QueryGenerator.initQueryWrapper(shippingFeesWaiver, req.getParameterMap());
        Page<ShippingFeesWaiver> page = new Page<ShippingFeesWaiver>(pageNo, pageSize);
        IPage<ShippingFeesWaiver> pageList = shippingFeesWaiverService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param shippingFeesWaiverPage
     * @return
     */
    @AutoLog(value = "采购运费免除-添加")
    @ApiOperation(value = "采购运费免除-添加", notes = "采购运费免除-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ShippingFeesWaiverPage shippingFeesWaiverPage) {
        ShippingFeesWaiver shippingFeesWaiver = new ShippingFeesWaiver();
        BeanUtils.copyProperties(shippingFeesWaiverPage, shippingFeesWaiver);
        shippingFeesWaiverService.saveMain(shippingFeesWaiver, shippingFeesWaiverPage.getShippingFeesWaiverProductList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param shippingFeesWaiverPage
     * @return
     */
    @AutoLog(value = "采购运费免除-编辑")
    @ApiOperation(value = "采购运费免除-编辑", notes = "采购运费免除-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ShippingFeesWaiverPage shippingFeesWaiverPage) {
        ShippingFeesWaiver shippingFeesWaiver = new ShippingFeesWaiver();
        BeanUtils.copyProperties(shippingFeesWaiverPage, shippingFeesWaiver);
        ShippingFeesWaiver shippingFeesWaiverEntity = shippingFeesWaiverService.getById(shippingFeesWaiver.getId());
        if (shippingFeesWaiverEntity == null) {
            return Result.error("未找到对应数据");
        }
        shippingFeesWaiverService.updateMain(shippingFeesWaiver, shippingFeesWaiverPage.getShippingFeesWaiverProductList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "采购运费免除-通过id删除")
    @ApiOperation(value = "采购运费免除-通过id删除", notes = "采购运费免除-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        shippingFeesWaiverService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "采购运费免除-批量删除")
    @ApiOperation(value = "采购运费免除-批量删除", notes = "采购运费免除-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.shippingFeesWaiverService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "采购运费免除-通过id查询")
    @ApiOperation(value = "采购运费免除-通过id查询", notes = "采购运费免除-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        ShippingFeesWaiver shippingFeesWaiver = shippingFeesWaiverService.getById(id);
        if (shippingFeesWaiver == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(shippingFeesWaiver);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "采购运费免除产品通过主表ID查询")
    @ApiOperation(value = "采购运费免除产品主表ID查询", notes = "采购运费免除产品-通主表ID查询")
    @GetMapping(value = "/queryShippingFeesWaiverProductByMainId")
    public Result<?> queryShippingFeesWaiverProductListByMainId(@RequestParam(name = "id") String id) {
        List<ShippingFeesWaiverProduct> shippingFeesWaiverProductList = shippingFeesWaiverProductService.selectByMainId(id);
        IPage<ShippingFeesWaiverProduct> page = new Page<>();
        page.setRecords(shippingFeesWaiverProductList);
        page.setTotal(shippingFeesWaiverProductList.size());
        return Result.OK(page);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param shippingFeesWaiver
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ShippingFeesWaiver shippingFeesWaiver) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<ShippingFeesWaiver> queryWrapper = QueryGenerator.initQueryWrapper(shippingFeesWaiver, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<ShippingFeesWaiver> queryList = shippingFeesWaiverService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<ShippingFeesWaiver> shippingFeesWaiverList = new ArrayList<ShippingFeesWaiver>();
        if (oConvertUtils.isEmpty(selections)) {
            shippingFeesWaiverList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            shippingFeesWaiverList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<ShippingFeesWaiverPage> pageList = new ArrayList<ShippingFeesWaiverPage>();
        for (ShippingFeesWaiver main : shippingFeesWaiverList) {
            ShippingFeesWaiverPage vo = new ShippingFeesWaiverPage();
            BeanUtils.copyProperties(main, vo);
            List<ShippingFeesWaiverProduct> shippingFeesWaiverProductList = shippingFeesWaiverProductService.selectByMainId(main.getId());
            vo.setShippingFeesWaiverProductList(shippingFeesWaiverProductList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "采购运费免除列表");
        mv.addObject(NormalExcelConstants.CLASS, ShippingFeesWaiverPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("采购运费免除数据", "导出人:" + sysUser.getRealname(), "采购运费免除"));
        mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
        return mv;
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
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
        for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
            MultipartFile file = entity.getValue();// 获取上传文件对象
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(1);
            params.setNeedSave(true);
            try {
                List<ShippingFeesWaiverPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ShippingFeesWaiverPage.class, params);
                for (ShippingFeesWaiverPage page : list) {
                    ShippingFeesWaiver po = new ShippingFeesWaiver();
                    BeanUtils.copyProperties(page, po);
                    shippingFeesWaiverService.saveMain(po, page.getShippingFeesWaiverProductList());
                }
                return Result.OK("文件导入成功！数据行数:" + list.size());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return Result.error("文件导入失败:" + e.getMessage());
            } finally {
                try {
                    file.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.OK("文件导入失败！");
    }

}
