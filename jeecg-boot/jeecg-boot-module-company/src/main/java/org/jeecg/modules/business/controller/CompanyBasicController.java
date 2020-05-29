package org.jeecg.modules.business.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.business.entity.CompanyBaseinfo;
import org.jeecg.modules.business.service.ICompanyBaseinfoService;
import org.jeecg.modules.business.service.ICompanyBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @Description: company_baseinfo
* @Author: jeecg-boot
* @Date:   2020-05-27
* @Version: V1.0
*/
@Api(tags="company_basic")
@RestController
@RequestMapping("/companyBasic")
@Slf4j
public class CompanyBasicController extends JeecgController<CompanyBaseinfo, ICompanyBaseinfoService> {
    @Autowired
    private ICompanyBasicService companyBasicService;




   /**
    *   查询菜单及其状态信息
    *
    * @param companyId 企业编号
    * @return 菜单及其状态信息
    */
   @AutoLog(value = "company_basic-查询菜单及其状态信息")
   @ApiOperation(value="查询菜单及其状态信息", notes="根据企业编号查询")
   @GetMapping(value = "/Menus")
   public Result<?> queryMenus(@RequestParam(name="companyId",required=true) String companyId) {


       Map<String,List<Map<String,String>>> result = new HashMap<>();
       result.put("basicInfoMenus" , companyBasicService.getbasicInfoMenus(companyId));

       result.put("superviseMenus" , companyBasicService.getSuperviseMenus(companyId));
       return Result.ok(result);
   }



}
