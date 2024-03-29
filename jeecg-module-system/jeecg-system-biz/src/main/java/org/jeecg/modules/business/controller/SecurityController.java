package org.jeecg.modules.business.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Security-安全管理")
@RestController
@RequestMapping("/security")
@Slf4j
public class SecurityController {

    @GetMapping(value = "/isEmployee")
    public Result<?> checkIsEmployee () {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        return Result.ok(!sysUser.getOrgCode().contains("A04"));
    }
}
