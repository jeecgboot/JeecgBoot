package org.jeecg.modules.business.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.system.service.ISysDepartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Security-安全管理")
@RestController
@RequestMapping("/security")
@Slf4j
public class SecurityController {
    @Autowired
    private ISysDepartService sysDepartService;
    @Autowired
    private Environment env;
    @GetMapping(value = "/isEmployee")
    public Result<?> checkIsEmployee () {
        String companyOrgCode = sysDepartService.queryCodeByDepartName(env.getProperty("company.orgName"));
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        return Result.ok(sysUser.getOrgCode().equals(companyOrgCode));
    }
}
