package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QMucMemberLevel;
import org.jeecg.modules.im.service.MucMemberLevelService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 群组成员等级
 */
@RestController
@RequestMapping("/im/mucMemberMevel")
public class MucMemberLevelController extends BaseBackController {
    @Resource
    private MucMemberLevelService mucMemberLevelService;

    @RequestMapping("/pagination")
    public Result<Object> list(QMucMemberLevel q){
        return success(mucMemberLevelService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
}
