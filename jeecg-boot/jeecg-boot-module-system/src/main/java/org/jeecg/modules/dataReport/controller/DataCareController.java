package org.jeecg.modules.dataReport.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.dataReport.entity.BillOrderData;
import org.jeecg.modules.dataReport.entity.BlDataCare;
import org.jeecg.modules.dataReport.entity.ContainerTatistics;
import org.jeecg.modules.dataReport.service.IDataCareService;
import org.jeecg.modules.demo.test.entity.JeecgDemo;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.system.entity.SysLog;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * @author Administrator
 * @date 2019-08-08
 * 数据比对
 */
@Slf4j
@RestController
@RequestMapping("/dataReport/dataCare")
@Api(tags = "数据比对")
public class DataCareController {

    @Autowired
    private IDataCareService iDataCareService;

    /**
     * 分页列表查询
     *
     * @param blDataCare
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<IPage<BlDataCare>> queryPageList(BlDataCare blDataCare,  HttpServletRequest req) {
        Result<IPage<BlDataCare>> result = new Result<IPage<BlDataCare>>();

        QueryWrapper<BlDataCare> queryWrapper = QueryGenerator.initQueryWrapper(blDataCare, req.getParameterMap());
        String bolcode = req.getParameter("bolcodeQuery");
        if(oConvertUtils.isNotEmpty(bolcode)) {
            queryWrapper.like("OO.bolcode",bolcode);
        }
        //客商
        String payer = req.getParameter("payerQuery");
        if(oConvertUtils.isNotEmpty(payer)) {
            queryWrapper.like("OO.langname1",payer);
        }
        System.out.println(queryWrapper.getSqlSegment());//打印前端自带的查询where后的条件
        //数据状态
        String datastatus = req.getParameter("datastatusQuery");
        List<BlDataCare> pageList = iDataCareService.getAllBlnoByErp(queryWrapper);
        if (CollectionUtil.isEmpty(pageList)){
            result.setSuccess(true);
            result.setResult(new Page<BlDataCare>());
            return result;
        }
        pageList = this.removeDuplicateOutputField(pageList);
        List<Map<String, Object>> tmsBlNoDatas = iDataCareService.getTmsBlNoDatas(queryWrapper);

        List<BlDataCare> diffDatesList = findDiffDates(pageList, tmsBlNoDatas);
        List<BlDataCare> recordsNew = new ArrayList<>();
        if (oConvertUtils.isNotEmpty(datastatus)){
            String status = "2";
            for (BlDataCare record : pageList) {
                if (oConvertUtils.isEmpty(record.getDatastatus())){

                    if (status.equals(datastatus)){
                        recordsNew.add(record);
                    }
                }else{
                    if (record.getDatastatus().toString().equals(datastatus)){
                        recordsNew.add(record);
                    }
                }

            }
        }else {
            recordsNew.addAll(diffDatesList);

        }

        Page<BlDataCare> page = new Page<BlDataCare>(1, recordsNew.size());
        page.setRecords(recordsNew);
        result.setSuccess(true);
        result.setResult(page);
        return result;
    }

    /**
     * 比对数据判断ERP和TMS数据中哪些数据未实现对接
     * @param pageList
     * @param tmsBlNoDatas
     * @return
     */
    private List<BlDataCare> findDiffDates(List<BlDataCare> pageList, List<Map<String, Object>> tmsBlNoDatas){
        for (BlDataCare record : pageList) {
            for (Map<String, Object> tmsBlNoData : tmsBlNoDatas) {
                if (record.getBolcode().equals(tmsBlNoData.get("bl_no"))){
                    record.setDatastatus(CommonConstant.DATA_STATUS_TRUE);
                    record.setContainertype(Convert.toStr(tmsBlNoData.get("cnum")) );
                }
            }
        }
        return pageList;


    }

    /**
     * 根据list中对象某些字段去重
     * @param list 需要去重的list
     * @return 返回去重后的list
     */
    private List<BlDataCare> removeDuplicateOutputField(List<BlDataCare> list) {
        Set<BlDataCare> set = new TreeSet<>(new Comparator<BlDataCare>() {
            @Override
            public int compare(BlDataCare o1, BlDataCare o2) {
                int compareToResult = 1;//==0表示重复
                //根据需求添加StringUtils.equals(o1.getUserName(), o2.getUserName()) ；
                if(StringUtils.equals(o1.getBolcode(), o2.getBolcode())) {
                    compareToResult = 0;
                }
                return compareToResult;
            }
        });
        set.addAll(list);
        return new ArrayList<>(set);
    }


    /**
     * 导出excel
     *
     * @param req
     * @param response
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(BlDataCare blDataCare,HttpServletRequest req, HttpServletResponse response) {
        QueryWrapper<BlDataCare> queryWrapper = QueryGenerator.initQueryWrapper(blDataCare, req.getParameterMap());
        String bolcode = req.getParameter("bolcodeQuery");
        if(oConvertUtils.isNotEmpty(bolcode)) {
            queryWrapper.like("OO.bolcode",bolcode);
        }
        //客商
        String payer = req.getParameter("payerQuery");
        if(oConvertUtils.isNotEmpty(payer)) {
            queryWrapper.like("OO.langname1",payer);
        }
        System.out.println(queryWrapper.getSqlSegment());//打印前端自带的查询where后的条件
        //数据状态
        String datastatus = req.getParameter("datastatusQuery");
        List<BlDataCare> pageList = iDataCareService.getAllBlnoByErp(queryWrapper);


        pageList = this.removeDuplicateOutputField(pageList);
        List<Map<String, Object>> tmsBlNoDatas = iDataCareService.getTmsBlNoDatas(queryWrapper);

        List<BlDataCare> diffDatesList = findDiffDates(pageList, tmsBlNoDatas);
        List<BlDataCare> recordsNew = new ArrayList<>();
        if (oConvertUtils.isNotEmpty(datastatus)){
            String status = "2";
            for (BlDataCare record : pageList) {
                if (oConvertUtils.isEmpty(record.getDatastatus())){

                    if (status.equals(datastatus)){
                        recordsNew.add(record);
                    }
                }else{
                    if (record.getDatastatus().toString().equals(datastatus)){
                        recordsNew.add(record);
                    }
                }

            }
        }else {
            recordsNew.addAll(diffDatesList);

        }

        //Step.2 AutoPoi 导出Excel
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
        //导出文件名称
        mv.addObject(NormalExcelConstants.FILE_NAME, "数据比对列表");
        mv.addObject(NormalExcelConstants.CLASS, BlDataCare.class);
        mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("数据比对列表数据", "导出人:" + user.getRealname() ,"导出信息"));
        mv.addObject(NormalExcelConstants.DATA_LIST, recordsNew);
        return mv;
    }


}
