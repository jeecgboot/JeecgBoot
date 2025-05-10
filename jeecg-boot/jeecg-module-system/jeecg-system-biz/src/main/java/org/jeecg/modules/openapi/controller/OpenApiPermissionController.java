package org.jeecg.modules.openapi.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.openapi.entity.OpenApiPermission;
import org.jeecg.modules.openapi.service.OpenApiPermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/openapi/permission")
public class OpenApiPermissionController extends JeecgController<OpenApiPermission, OpenApiPermissionService> {

    @PostMapping("add")
    public Result add(@RequestBody OpenApiPermission openApiPermission) {
        List<String> list = Arrays.asList(openApiPermission.getApiId().split(","));
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(l->{
                OpenApiPermission saveApiPermission = new OpenApiPermission();
                saveApiPermission.setApiId(l);
                saveApiPermission.setApiAuthId(openApiPermission.getApiAuthId());
                service.save(saveApiPermission);
            });
        }
        return Result.ok("保存成功");
    }
    @GetMapping("/list")
    public Result list( String apiAuthId) {
        return Result.ok(service.list(Wrappers.<OpenApiPermission>lambdaQuery().eq(OpenApiPermission::getApiAuthId,apiAuthId)));
    }
}
