package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.ClientVer;
import org.jeecg.modules.im.entity.query_helper.QClientVer;
import org.jeecg.modules.im.service.ClientVerService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/im/clientVer")
public class ClientVerController extends BaseBackController {
    @Resource
    ClientVerService paramService;

    @RequestMapping("/pagination")
    public Result<Object> list(QClientVer q){
        return success(paramService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
    /**
     * 更新
     */
    @RequestMapping("/update")
    public Result<Object> destroy(@RequestBody @Validated ClientVer param, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return paramService.updateById(param)?success():fail();
    }
}
