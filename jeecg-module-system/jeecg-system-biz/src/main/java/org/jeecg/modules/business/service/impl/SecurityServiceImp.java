package org.jeecg.modules.business.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.business.service.ISecurityService;
import org.jeecg.modules.system.service.ISysDepartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SecurityServiceImp implements ISecurityService {
    @Autowired private ISysDepartService sysDepartService;
    @Autowired private Environment env;

    @Override
    public boolean checkIsEmployee() {
        String companyOrgCode = sysDepartService.queryCodeByDepartName(env.getProperty("company.orgName"));
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        return sysUser.getOrgCode().equals(companyOrgCode);
    }
}
