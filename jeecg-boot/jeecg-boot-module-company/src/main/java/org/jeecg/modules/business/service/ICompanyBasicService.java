package org.jeecg.modules.business.service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 企业人员信息
 * @Author: jeecg-boot
 * @Date:   2020-05-27
 * @Version: V1.0
 */
public interface ICompanyBasicService  {
    List<Map<String,String>> getbasicInfoMenus(String companyId);
    List<Map<String,String>> getSuperviseMenus(String companyId);

}
