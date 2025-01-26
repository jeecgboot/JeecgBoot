package org.jeecg.modules.openapi.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.openapi.entity.OpenApiPermission;
import org.jeecg.modules.openapi.service.OpenApiPermissionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/openapi/permission")
public class OpenApiPermissionController extends JeecgController<OpenApiPermission, OpenApiPermissionService> {

    @PostMapping("add")
    public Result add(@RequestBody OpenApiPermission openApiPermission) {
        return Result.ok(service.save(openApiPermission));
    }
}
