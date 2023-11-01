package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QCallRecord;
import org.jeecg.modules.im.service.CallRecordService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/callRecord")
public class CallRecordController extends BaseBackController {
    @Resource
    private CallRecordService callRecordService;

    @RequestMapping("/pagination")
    public Result<Object> list(QCallRecord q) {
        return success(callRecordService.pagination(new MyPage<>(getPage(), getPageSize()), q));
    }

    /**
     * 批量删除
     */
    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return callRecordService.del(ids);
    }

}
