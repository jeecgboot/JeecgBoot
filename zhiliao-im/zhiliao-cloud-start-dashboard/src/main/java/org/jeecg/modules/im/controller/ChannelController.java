package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Channel;
import org.jeecg.modules.im.entity.query_helper.QChannel;
import org.jeecg.modules.im.service.ChannelService;
import org.jeecg.modules.im.service.base.BaseBackController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/channel")
public class ChannelController extends BaseBackController {
    @Resource
    private ChannelService channelService;

    @RequestMapping("/pagination")
    public Result<Object> list(QChannel q) {
        return success(channelService.pagination(new MyPage<>(getPage(), getPageSize()), q));
    }
    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody Channel channel){
        return channelService.createOrUpdate(channel);
    }

}
