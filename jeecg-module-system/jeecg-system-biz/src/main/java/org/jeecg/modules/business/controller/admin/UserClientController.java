package org.jeecg.modules.business.controller.admin;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.business.entity.Client;
import org.jeecg.modules.business.entity.PlatformOrder;
import org.jeecg.modules.business.entity.PlatformOrderContent;
import org.jeecg.modules.business.entity.Shop;
import org.jeecg.modules.business.service.IPlatformOrderService;
import org.jeecg.modules.business.service.IShopService;
import org.jeecg.modules.business.service.IUserClientService;
import org.jeecg.modules.system.entity.SysRole;
import org.jeecg.modules.system.service.ISysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(tags = "UserClient")
@RestController
@RequestMapping("/userClient")
@Slf4j
public class UserClientController {
    @Autowired
    private IUserClientService userClientService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private IShopService shopService;
    @Autowired
    private IPlatformOrderService platformOrderService;

    /**
     * Checks if the user is a client and then lists orders of erp_status 1
     * @return
     */
    @GetMapping(value = "/getClient")
    public Result<?> getClientByUserId() {
        LoginUser loginUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        String userId = loginUser.getId();
        userId = "1698676211346821122";
        Client client = userClientService.getClientByUserId(userId);
        if(client == null) {
            List<SysRole> sysRoles = sysUserRoleService.getUserRole(userId);
            boolean isAllowed = false;
            for(SysRole sysRole: sysRoles) {
                if(sysRole.getRoleCode().equals("admin") || sysRole.getRoleCode().equals("dev")) {
                    isAllowed = true;
                    break;
                }
            }
            if(isAllowed)
                return Result.OK("Permission Granted", "admin");
            else
                return Result.error(403, "Access Denied.");
        }
        return Result.ok(client);
    }
}
