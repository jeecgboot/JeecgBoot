package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SensitiveWord;
import org.jeecg.modules.im.entity.query_helper.QSensitiveWord;
import org.jeecg.modules.im.service.SensitiveWordService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/sensitiveWord")
public class SensitiveWordController extends BaseBackController {
    @Resource
    SensitiveWordService sensitiveWordService;

    @RequestMapping("/pagination")
    public Result<Object> list(QSensitiveWord q){
        return success(sensitiveWordService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
    /**
     * 更新
     */
    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody @Validated SensitiveWord sensitiveWord, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return sensitiveWordService.createOrUpdate(sensitiveWord);
    }

    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam String id){
        return success(sensitiveWordService.getById(id));
    }

    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return sensitiveWordService.del(ids);
    }
}
