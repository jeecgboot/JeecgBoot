package org.jeecg.modules.im.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.modules.im.entity.SecretQuestion;
import org.jeecg.modules.im.entity.Tag;
import org.jeecg.modules.im.service.SecretQuestionService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 密保问题
 */
@Slf4j
@RestController
@RequestMapping("/a/secretQuestion")
public class SecretQuestionCtrl extends BaseApiCtrl {
    @Resource
    private SecretQuestionService secretQuestionService;

    @NoNeedUserToken
    @PostMapping("/findAll")
    public Result<Object> findAll(){
        return success(secretQuestionService.findAll());
    }

}
