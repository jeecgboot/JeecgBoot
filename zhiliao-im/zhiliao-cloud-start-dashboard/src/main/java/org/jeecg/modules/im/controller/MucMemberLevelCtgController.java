package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucMemberLevelCtg;
import org.jeecg.modules.im.entity.query_helper.QMucMemberLevelCtg;
import org.jeecg.modules.im.service.MucMemberLevelCtgService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 群组成员等级分类
 */
@RestController
@RequestMapping("/im/mucMemberLevel/ctg")
public class MucMemberLevelCtgController extends BaseBackController {
    @Resource
    private MucMemberLevelCtgService mucMemberLevelCtgService;

    @RequestMapping("/pagination")
    public Result<Object> list(QMucMemberLevelCtg q){
        return success(mucMemberLevelCtgService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }

    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody MucMemberLevelCtg ctg){
        return mucMemberLevelCtgService.createOrUpdate(ctg);
    }


    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam Integer id){
        return success(mucMemberLevelCtgService.getById(id));
    }
}
