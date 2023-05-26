package org.jeecg.modules.business.controller.admin.logisticChannel;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.business.domain.logistic.CostTrialCalculation;
import org.jeecg.modules.business.entity.LogisticChannel;
import org.jeecg.modules.business.entity.LogisticChannelPrice;
import org.jeecg.modules.business.entity.SkuMeasure;
import org.jeecg.modules.business.service.CountryService;
import org.jeecg.modules.business.service.ILogisticChannelPriceService;
import org.jeecg.modules.business.service.ILogisticChannelService;
import org.jeecg.modules.business.service.ISkuService;
import org.jeecg.modules.business.vo.CountryName;
import org.jeecg.modules.business.vo.LogisticChannelPage;
import org.jeecg.modules.business.vo.PopularCountry;
import org.jeecg.modules.business.vo.SkuQuantity;
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
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description: 物流渠道
 * @Author: jeecg-boot
 * @Date: 2021-04-03
 * @Version: V1.0
 */
@Api(tags = "物流渠道")
@RestController
@RequestMapping("/business/logisticChannel")
@Slf4j
public class LogisticChannelController {
    private final ILogisticChannelService logisticChannelService;
    private final ILogisticChannelPriceService logisticChannelPriceService;
    private final CountryService countryService;
    private final ISkuService skuService;

    @Autowired
    public LogisticChannelController(ILogisticChannelService logisticChannelService, ILogisticChannelPriceService logisticChannelPriceService, CountryService countryService, ISkuService skuService) {
        this.logisticChannelService = logisticChannelService;
        this.logisticChannelPriceService = logisticChannelPriceService;
        this.countryService = countryService;
        this.skuService = skuService;
    }

    /**
     * 分页列表查询
     *
     * @param logisticChannel
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "物流渠道-分页列表查询")
    @ApiOperation(value = "物流渠道-分页列表查询", notes = "物流渠道-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(LogisticChannel logisticChannel,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<LogisticChannel> queryWrapper = QueryGenerator.initQueryWrapper(logisticChannel, req.getParameterMap());
        Page<LogisticChannel> page = new Page<>(pageNo, pageSize);
        IPage<LogisticChannel> pageList = logisticChannelService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param logisticChannelPage
     * @return
     */
    @AutoLog(value = "物流渠道-添加")
    @ApiOperation(value = "物流渠道-添加", notes = "物流渠道-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody LogisticChannelPage logisticChannelPage) {
        LogisticChannel logisticChannel = new LogisticChannel();
        BeanUtils.copyProperties(logisticChannelPage, logisticChannel);
        logisticChannelService.saveMain(logisticChannel, logisticChannelPage.getLogisticChannelPriceList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param logisticChannelPage
     * @return
     */
    @AutoLog(value = "物流渠道-编辑")
    @ApiOperation(value = "物流渠道-编辑", notes = "物流渠道-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody LogisticChannelPage logisticChannelPage) {
        LogisticChannel logisticChannel = new LogisticChannel();
        BeanUtils.copyProperties(logisticChannelPage, logisticChannel);
        LogisticChannel logisticChannelEntity = logisticChannelService.getById(logisticChannel.getId());
        if (logisticChannelEntity == null) {
            return Result.error("未找到对应数据");
        }
        logisticChannelService.updateMain(logisticChannel, logisticChannelPage.getLogisticChannelPriceList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "物流渠道-通过id删除")
    @ApiOperation(value = "物流渠道-通过id删除", notes = "物流渠道-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        logisticChannelService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "物流渠道-批量删除")
    @ApiOperation(value = "物流渠道-批量删除", notes = "物流渠道-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.logisticChannelService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "物流渠道-通过id查询")
    @ApiOperation(value = "物流渠道-通过id查询", notes = "物流渠道-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        LogisticChannel logisticChannel = logisticChannelService.getById(id);
        if (logisticChannel == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(logisticChannel);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "物流渠道价格通过主表ID查询")
    @ApiOperation(value = "物流渠道价格主表ID查询", notes = "物流渠道价格-通主表ID查询")
    @GetMapping(value = "/queryLogisticChannelPriceByMainId")
    public Result<?> queryLogisticChannelPriceListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<LogisticChannelPrice> logisticChannelPriceList = logisticChannelPriceService.selectByMainId(id);
        return Result.OK(logisticChannelPriceList);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param logisticChannel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, LogisticChannel logisticChannel) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<LogisticChannel> queryWrapper = QueryGenerator.initQueryWrapper(logisticChannel, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<LogisticChannel> queryList = logisticChannelService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<LogisticChannel> logisticChannelList = new ArrayList<LogisticChannel>();
        if (oConvertUtils.isEmpty(selections)) {
            logisticChannelList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            logisticChannelList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<LogisticChannelPage> pageList = new ArrayList<LogisticChannelPage>();
        for (LogisticChannel main : logisticChannelList) {
            LogisticChannelPage vo = new LogisticChannelPage();
            BeanUtils.copyProperties(main, vo);
            List<LogisticChannelPrice> logisticChannelPriceList = logisticChannelPriceService.selectByMainId(main.getId());
            vo.setLogisticChannelPriceList(logisticChannelPriceList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "物流渠道列表");
        mv.addObject(NormalExcelConstants.CLASS, LogisticChannelPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("物流渠道数据", "导出人:" + sysUser.getRealname(), "物流渠道"));
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
                List<LogisticChannelPage> list = ExcelImportUtil.importExcel(file.getInputStream(), LogisticChannelPage.class, params);
                for (LogisticChannelPage page : list) {
                    LogisticChannel po = new LogisticChannel();
                    BeanUtils.copyProperties(page, po);
                    logisticChannelService.saveMain(po, page.getLogisticChannelPriceList());
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


    @GetMapping(value = "/find")
    public Result<List<CostTrialCalculation>> findPrices(@RequestParam(name = "country") String country,
                                                         @RequestParam(name = "weight") int weight,
                                                         @RequestParam(name = "volume", defaultValue = "1") int volume) {

        List<CostTrialCalculation> calculations = logisticChannelService.logisticChannelTrial(weight, volume, country);
        return Result.OK(calculations);
    }


    /**
     * API for trial calculation of shipping cost.
     * Destination country and the skus to be shipped are needed.
     *
     * @param param trial param
     * @return list of propre price
     */
    @PostMapping(value = "/findBySku")
    public Result<List<CostTrialCalculation>> findPricesBySku(@RequestBody TrialCalcReqParam param) {
        log.info(
                String.format(
                        "Request for trial calculation for country: %s and sku: %s",
                        param.getCountryCode(),
                        param.getSkuQuantities()
                )
        );

        // get ids of all sku to run trial
        Set<String> skuIds = param.getSkuQuantities().stream()
                .map(SkuQuantity::getID)
                .collect(Collectors.toSet());

        // get map between id and measure
        Map<String, SkuMeasure> idToMeasure = skuService.measureSku(skuIds)
                .stream()
                .collect(
                        Collectors.toMap(
                                SkuMeasure::getId,
                                Function.identity()
                        )
                );

        if (idToMeasure.size() < skuIds.size()) {
            // TODO 2021-7-7 error handle in case of lack of measure data of certain
        }
        // calculate total weight and volume
        // iterate on sku quantity list, thus enable duplication of sku
        int totalWeight = 0, totalVolume = 0;
        for (SkuQuantity skuQuantity : param.getSkuQuantities()) {
            String skuId = skuQuantity.getID();
            int quantity = skuQuantity.getQuantity();
            SkuMeasure measure = idToMeasure.get(skuId);
            totalWeight += quantity * measure.getWeight();
            totalVolume += quantity * measure.getVolume();
        }

        // search propre channel price according to weight, volume and destination country
        List<CostTrialCalculation> calculations =
                logisticChannelService.logisticChannelTrial(
                        totalWeight,
                        totalVolume,
                        param.getCountryCode()
                );

        return Result.OK(calculations);
    }

    @PostMapping(value = "/findByCountriesAndSku")
    public Result<List<CostTrialCalculation>> findPricesBySkuAndCountries(
            @RequestBody TrialCalcCountriesAndSku param) {
        List<CostTrialCalculation> res = param.getCountryCodes().stream()
                .map(code -> new TrialCalcReqParam(code, param.getSkuQuantities()))
                .map(this::findPricesBySku)
                .map(Result::getResult)
                .reduce(
                        new ArrayList<>(),
                        (identity, list) -> {
                            identity.addAll(list);
                            return identity;
                        }
                );

        return Result.OK(res);

    }


    @GetMapping(value = "/countries")
    public Result<List<CountryName>> countryList() {
        List<CountryName> countries = logisticChannelPriceService.getAllCountry();
        return Result.OK(countries);
    }

    @GetMapping(value = "/popularCountries")
    public Result<List<PopularCountry>> popularCountry() {
        List<PopularCountry> countries = logisticChannelPriceService.getPopularCountryList();
        return Result.OK(countries);
    }
}
