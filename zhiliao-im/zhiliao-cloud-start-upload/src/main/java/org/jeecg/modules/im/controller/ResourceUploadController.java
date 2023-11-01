package org.jeecg.modules.im.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.modules.im.service.base.BaseUploadCtrl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 资源文件上传
 */
@RestController
@RequestMapping("/u/resource")
@Slf4j
public class ResourceUploadController extends BaseUploadCtrl {

    @PostMapping({"","/"})
    public Result<Object> index() {
        return success();
    }
}
