package org.jeecg.modules.business.controller.admin;

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
import org.jeecg.modules.business.entity.Parcel;
import org.jeecg.modules.business.entity.ParcelTrace;
import org.jeecg.modules.business.service.IParcelService;
import org.jeecg.modules.business.service.IParcelTraceService;
import org.jeecg.modules.business.vo.ParcelPage;
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
 * @Description: 包裹
 * @Author: jeecg-boot
 * @Date: 2022-02-18
 * @Version: V1.0
 */
@Api(tags = "包裹")
@RestController
@RequestMapping("/parcel/parcel")
@Slf4j
public class ParcelController {
    @Autowired
    private IParcelService parcelService;
    @Autowired
    private IParcelTraceService parcelTraceService;

    /**
     * 分页列表查询
     *
     * @param parcel
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "包裹-分页列表查询")
    @ApiOperation(value = "包裹-分页列表查询", notes = "包裹-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(Parcel parcel,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<Parcel> queryWrapper = QueryGenerator.initQueryWrapper(parcel, req.getParameterMap());
        Page<Parcel> page = new Page<>(pageNo, pageSize);
        IPage<Parcel> pageList = parcelService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param parcelPage
     * @return
     */
    @AutoLog(value = "包裹-添加")
    @ApiOperation(value = "包裹-添加", notes = "包裹-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody ParcelPage parcelPage) {
        Parcel parcel = new Parcel();
        BeanUtils.copyProperties(parcelPage, parcel);
        parcelService.saveMain(parcel, parcelPage.getParcelTraceList());
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param parcelPage
     * @return
     */
    @AutoLog(value = "包裹-编辑")
    @ApiOperation(value = "包裹-编辑", notes = "包裹-编辑")
    @PutMapping(value = "/edit")
    public Result<?> edit(@RequestBody ParcelPage parcelPage) {
        Parcel parcel = new Parcel();
        BeanUtils.copyProperties(parcelPage, parcel);
        Parcel parcelEntity = parcelService.getById(parcel.getId());
        if (parcelEntity == null) {
            return Result.error("未找到对应数据");
        }
        parcelService.updateMain(parcel, parcelPage.getParcelTraceList());
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "包裹-通过id删除")
    @ApiOperation(value = "包裹-通过id删除", notes = "包裹-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        parcelService.delMain(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "包裹-批量删除")
    @ApiOperation(value = "包裹-批量删除", notes = "包裹-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.parcelService.delBatchMain(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "包裹-通过id查询")
    @ApiOperation(value = "包裹-通过id查询", notes = "包裹-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        Parcel parcel = parcelService.getById(id);
        if (parcel == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(parcel);

    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    @AutoLog(value = "包裹轨迹通过主表ID查询")
    @ApiOperation(value = "包裹轨迹主表ID查询", notes = "包裹轨迹-通主表ID查询")
    @GetMapping(value = "/queryParcelTraceByMainId")
    public Result<?> queryParcelTraceListByMainId(@RequestParam(name = "id", required = true) String id) {
        List<ParcelTrace> parcelTraceList = parcelTraceService.selectByMainId(id);
        IPage<ParcelTrace> page = new Page<>();
        page.setRecords(parcelTraceList);
        page.setTotal(parcelTraceList.size());
        return Result.OK(page);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param parcel
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Parcel parcel) {
        // Step.1 组装查询条件查询数据
        QueryWrapper<Parcel> queryWrapper = QueryGenerator.initQueryWrapper(parcel, request.getParameterMap());
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

        //Step.2 获取导出数据
        List<Parcel> queryList = parcelService.list(queryWrapper);
        // 过滤选中数据
        String selections = request.getParameter("selections");
        List<Parcel> parcelList = new ArrayList<Parcel>();
        if (oConvertUtils.isEmpty(selections)) {
            parcelList = queryList;
        } else {
            List<String> selectionList = Arrays.asList(selections.split(","));
            parcelList = queryList.stream().filter(item -> selectionList.contains(item.getId())).collect(Collectors.toList());
        }

        // Step.3 组装pageList
        List<ParcelPage> pageList = new ArrayList<ParcelPage>();
        for (Parcel main : parcelList) {
            ParcelPage vo = new ParcelPage();
            BeanUtils.copyProperties(main, vo);
            List<ParcelTrace> parcelTraceList = parcelTraceService.selectByMainId(main.getId());
            vo.setParcelTraceList(parcelTraceList);
            pageList.add(vo);
        }

        // Step.4 AutoPoi 导出Excel
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        mv.addObject(NormalExcelConstants.FILE_NAME, "包裹列表");
        mv.addObject(NormalExcelConstants.CLASS, ParcelPage.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("包裹数据", "导出人:" + sysUser.getRealname(), "包裹"));
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
                List<ParcelPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ParcelPage.class, params);
                for (ParcelPage page : list) {
                    Parcel po = new Parcel();
                    BeanUtils.copyProperties(page, po);
                    parcelService.saveMain(po, page.getParcelTraceList());
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
