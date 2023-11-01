package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Param;
import org.jeecg.modules.im.entity.query_helper.QParam;
import org.jeecg.modules.im.service.ParamService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/im/param")
public class ParamController extends BaseBackController {
    @Resource
    ParamService paramService;

    @RequestMapping("/pagination")
    public Result<Object> list(QParam q){
        return success(paramService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
    /**
     * 更新
     */
    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody @Validated Param param, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return paramService.createOrUpdate(param);
    }

    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam String id){
        return success(paramService.getById(id));
    }

    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return paramService.del(ids);
    }
}
