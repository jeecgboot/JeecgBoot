package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QRedPack;
import org.jeecg.modules.im.service.RedPackService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/redPack")
public class RedPackController extends BaseBackController {

    @Resource
    private RedPackService redPackService;

    @RequestMapping("/pagination")
    public Result<Object> pagination(QRedPack q){
        return success(redPackService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }


}
