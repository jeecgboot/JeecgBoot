package org.jeecg.modules.openapi.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.openapi.entity.OpenApiPermission;
import org.jeecg.modules.openapi.service.OpenApiPermissionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/openapi/permission")
public class OpenApiPermissionController extends JeecgController<OpenApiPermission, OpenApiPermissionService> {

    @PostMapping("add")
    public Result add(@RequestBody OpenApiPermission openApiPermission) {
        service.add(openApiPermission);
        return Result.ok("保存成功");
    }
    @GetMapping("/getOpenApi")
    public Result<?> getOpenApi( String apiAuthId) {
        return service.getOpenApi(apiAuthId);
    }
}
