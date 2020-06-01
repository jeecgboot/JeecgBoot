package org.jeecg.modules.business.service.impl;

import org.jeecg.modules.business.service.ICompanyAcceptanceService;
import org.jeecg.modules.business.service.ICompanyBasicService;
import org.jeecg.modules.business.service.ICompanyDirtyAllowService;
import org.jeecg.modules.business.service.ICompanyPreventionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 企业人员信息
 * @Author: jeecg-boot
 * @Date:   2020-05-27
 * @Version: V1.0
 */



@Service
public class CompanyBasicServiceImpl implements ICompanyBasicService {
    @Autowired
    private ICompanyAcceptanceService companyAcceptanceService;

    @Autowired
    private ICompanyPreventionService companyPreventionService;

    @Autowired
    private ICompanyDirtyAllowService companyDirtyAllowService;

    /**
     *  根据conpanyId组装 一企一档基础信息的菜单信息
     * @param companyId
     * @return 菜单信息
     */
    public List<Map<String,String>> getbasicInfoMenus(String companyId){
        List<Map<String,String>> basicInfoMenus = new ArrayList<>();
        addElements("1"," 基本信息",0,basicInfoMenus);
        addElements("2"," 企业资质",2,basicInfoMenus);
        addElements("3"," 员工信息",3,basicInfoMenus);
        addElements("4"," 产品物料信息",2,basicInfoMenus);
        addElements("5"," 环评审批信息",3,basicInfoMenus);
        addElements("6"," 竣工验收信息",companyAcceptanceService.findCountByCompanyId(companyId),basicInfoMenus);
        addElements("7"," 污染防治信息",companyPreventionService.findCountByCompanyId(companyId),basicInfoMenus);
        addElements("8"," 排污许可证信息",companyDirtyAllowService.findCountByCompanyId(companyId),basicInfoMenus);
        addElements("9"," 危废经营许可信息",3,basicInfoMenus);
        addElements("10"," 固废许可证信息",0,basicInfoMenus);

        addElements("11"," 辐射许可证信息",0,basicInfoMenus);

        addElements("12"," 环保税信息",0,basicInfoMenus);

        addElements("13"," 清洁生产信息",0,basicInfoMenus);

        addElements("14"," 在线监控验收信息",0,basicInfoMenus);

        return basicInfoMenus;
    }
    /**
     *  根据conpanyId组装 一企一档监督检查的菜单信息
     * @param companyId
     * @return 菜单信息
     */
    public List<Map<String,String>> getSuperviseMenus(String companyId){
        List<Map<String,String>> basicInfoMenus = new ArrayList<>();
        addElements("1"," 年度动态监管",13,basicInfoMenus);
        addElements("2"," 行政处罚信息",2,basicInfoMenus);
        addElements("3"," 监督性监测信息",3,basicInfoMenus);
        addElements("4"," 信访投诉信息",2,basicInfoMenus);

        return basicInfoMenus;
    }

    private void addElements(String key,String value,Integer counts,List<Map<String,String>> basicInfoMenus){
        Map<String,String> param = new HashMap<>(2);
        param.put("key",key);
        param.put("text",value);
        param.put("point",Integer.toString(counts));
        basicInfoMenus.add(param);
    }

}
