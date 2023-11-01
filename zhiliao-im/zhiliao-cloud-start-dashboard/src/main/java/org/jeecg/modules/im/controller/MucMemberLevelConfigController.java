package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QMucMemberLevelConfig;
import org.jeecg.modules.im.service.MucMemberLevelConfigService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 群组成员等级配置
 */
@RestController
@RequestMapping("/im/mucMemberLevel/config")
public class MucMemberLevelConfigController extends BaseBackController {
    @Resource
    private MucMemberLevelConfigService mucMemberLevelConfigService;

    @RequestMapping("/pagination")
    public Result<Object> list(QMucMemberLevelConfig q){
        return success(mucMemberLevelConfigService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
}
