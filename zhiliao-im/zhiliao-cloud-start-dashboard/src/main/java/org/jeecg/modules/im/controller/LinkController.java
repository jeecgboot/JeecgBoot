package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Link;
import org.jeecg.modules.im.entity.query_helper.QLink;
import org.jeecg.modules.im.service.LinkService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 发现页网页链接
 */
@RestController
@RequestMapping("/im/link")
public class LinkController extends BaseBackController {
    @Resource
    private LinkService linkService;

    @RequestMapping("/pagination")
    public Result<Object> list(QLink q){
        return success(linkService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }

    /**
     * 创建或更新
     */
    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody @Validated Link link, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return linkService.createOrUpdate(link);
    }

    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam String id){
        return success(linkService.findById(id));
    }

    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return linkService.del(ids);
    }

}
