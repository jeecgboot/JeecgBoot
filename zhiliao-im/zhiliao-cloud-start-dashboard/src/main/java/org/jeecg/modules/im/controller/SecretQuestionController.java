package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SecretQuestion;
import org.jeecg.modules.im.entity.query_helper.QSecretQuestion;
import org.jeecg.modules.im.service.SecretQuestionService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 密保问题
 */
@RestController
@RequestMapping("/im/secretQuestion")
public class SecretQuestionController extends BaseBackController {
    @Resource
    private SecretQuestionService secretQuestionService;

    @RequestMapping("/pagination")
    public Result<Object> list(QSecretQuestion q){
        return success(secretQuestionService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }

    /**
     * 添加或更新
     */
    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody @Validated SecretQuestion item, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return secretQuestionService.createOrUpdate(item);
    }

    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam String id){
        return success(secretQuestionService.getById(id));
    }

    /**
     * 批量删除
     */
    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return secretQuestionService.del(ids);
    }
}
