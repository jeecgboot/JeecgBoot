package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QRedPackOpen;
import org.jeecg.modules.im.service.RedPackOpenService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/redPackOpen")
public class RedPackOpenController extends BaseBackController {
    @Resource
    private RedPackOpenService redPackOpenService;

    @RequestMapping("/pagination")
    public Result<Object> pagination(QRedPackOpen q){
        return success(redPackOpenService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }


}
