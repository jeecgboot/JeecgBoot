package org.jeecg.modules.business.controller.admin;

import cn.hutool.core.date.DateTime;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.business.service.DashboardService;
import org.jeecg.modules.system.service.impl.SysBaseApiImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@Api(tags = "Admin-管理员管理")
@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    @Autowired private DashboardService dashboardService;
    @Autowired private SysBaseApiImpl sysBaseApi;
    @GetMapping(value = "/kpis")
    public Result<?> kpis(@RequestParam(value = "period", required = false) String period) {
        LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        List<String> roles = sysBaseApi.getRolesByUsername(sysUser.getUsername());
        LocalDateTime start = LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("CTT"))).minusMonths(11).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("CTT")));

        if("currentYear".equals(period)){
            start = LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("CTT"))).withMonth(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        } else if("currentMonth".equals(period)){
            start = LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("CTT"))).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        }
        Map<String, ?> kpiMap = dashboardService.getKpis(start, end, roles, sysUser.getUsername());

        return Result.ok(kpiMap);
    }
}
