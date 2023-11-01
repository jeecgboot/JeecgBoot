package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QMsg;
import org.jeecg.modules.im.service.MsgService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/msg")
public class MsgController extends BaseBackController {
    @Resource
    private MsgService msgService;

    @RequestMapping("/pagination")
    public Result<Object> list(QMsg q, String sendStart, String sendEnd) {
        if(!isEmpty(sendStart))
            q.setSendStartTime(ToolDateTime.getDate(sendStart, ToolDateTime.pattern_ymd).getTime());
        if(!isEmpty(sendEnd)) {
            q.setSendEndTime(ToolDateTime.getDate(sendEnd, ToolDateTime.pattern_ymd).getTime());
            if(sendStart.equals(sendEnd)){
                q.setSendEndTime(ToolDateTime.getDateByDatePlusDays(ToolDateTime.getDate(q.getSendStartTime()),1).getTime());
            }
        }
        return success(msgService.pagination(new MyPage<>(getPage(), getPageSize()), q));
    }

    /**
     * 批量删除
     */
    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids,@RequestParam Integer type){
        return msgService.del(ids,type);
    }

}
