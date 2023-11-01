package org.jeecg.modules.im.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.modules.im.service.base.BaseUploadCtrl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 图片文件上传
 */
@Controller
@RequestMapping("/u")
@Slf4j
public class IndexController extends BaseUploadCtrl {

    @NoNeedUserToken
    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @NoNeedUserToken
    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
