package org.jeecg.modules.dataReport.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.dataReport.entity.BlDataCare;
import org.jeecg.modules.dataReport.service.IDataCareService;
import org.jeecg.modules.demo.test.entity.JeecgDemo;
import org.jeecg.modules.message.entity.SysMessage;
import org.jeecg.modules.system.entity.SysLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<IPage<BlDataCare>> queryPageList(BlDataCare blDataCare, @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                  @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<BlDataCare>> result = new Result<IPage<BlDataCare>>();
        Page<BlDataCare> page = new Page<BlDataCare>(pageNo, pageSize);
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
        IPage<BlDataCare> pageList = iDataCareService.getAllBlnoByErp(page,queryWrapper);
        List<String> tmsBlNoDatas = iDataCareService.getTmsBlNoDatas(queryWrapper);
        IPage<BlDataCare> diffDatesList = findDiffDates(pageList, tmsBlNoDatas);
        if (oConvertUtils.isNotEmpty(datastatus)){
            List<BlDataCare> records = pageList.getRecords();
            List<BlDataCare> recordsNew = new ArrayList<>();
            String status = "2";
            for (BlDataCare record : records) {
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
            diffDatesList.setRecords(recordsNew);
            diffDatesList.setTotal(recordsNew.size());
        }
        result.setSuccess(true);
        result.setResult(diffDatesList);
        return result;
    }

    /**
     * 比对数据判断ERP和TMS数据中哪些数据未实现对接
     * @param pageList
     * @param tmsBlNoDatas
     * @return
     */
    private IPage<BlDataCare> findDiffDates(IPage<BlDataCare> pageList, List<String> tmsBlNoDatas){
        List<BlDataCare> records = pageList.getRecords();
        for (BlDataCare record : records) {
            for (String tmsBlNoData : tmsBlNoDatas) {
                if (record.getBolcode().equals(tmsBlNoData)){
                    record.setDatastatus(CommonConstant.DATA_STATUS_TRUE);
                }
            }
        }
        return pageList;


    }
}
