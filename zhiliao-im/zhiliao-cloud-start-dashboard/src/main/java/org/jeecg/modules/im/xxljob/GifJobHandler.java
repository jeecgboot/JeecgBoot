package org.jeecg.modules.im.xxljob;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.im.base.constant.ConstantCache;
import org.jeecg.modules.im.entity.Gif;
import org.jeecg.modules.im.service.GifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * gif任务
 */
@Component
@Slf4j
public class GifJobHandler {

    @Resource
    private GifService gifService;
    @Lazy
    @Autowired
    private RedisUtil redisUtil;

    @XxlJob(value = "gif_sendTimes_update")
    public ReturnT<String> gifSendTimesUpdate(String params) {
        String prefix = ConstantCache.GIF_SEND_TIMES.replace("%s","");
        Set<String> keys = redisUtil.keys(prefix);
        List<Gif> gifs = new ArrayList<>();
        Gif gif;
        for (String key : keys) {
            int gifId = Integer.parseInt(key.replace(prefix,""));
            gif = gifService.getById(gifId);
            if(gif==null){
                continue;
            }
            gif.setSendTimes(gif.getSendTimes()+(int)redisUtil.get(key));
            gifs.add(gif);
        }
        gifService.updateBatchById(gifs);
        redisUtil.removeAll(prefix);
        return ReturnT.SUCCESS;
    }
    @XxlJob(value = "gif_addTimes_update")
    public ReturnT<String> gifAddTimesUpdate(String params) {
        String prefix = ConstantCache.GIF_ADD_TIMES.replace("%s","");
        Set<String> keys = redisUtil.keys(prefix);
        List<Gif> gifs = new ArrayList<>();
        Gif gif;
        for (String key : keys) {
            int gifId = Integer.parseInt(key.replace(prefix,""));
            gif = gifService.getById(gifId);
            if(gif==null){
                continue;
            }
            gif.setAddTimes(gif.getAddTimes()+(int)redisUtil.get(key));
            gifs.add(gif);
        }
        gifService.updateBatchById(gifs);
        redisUtil.removeAll(prefix);
        return ReturnT.SUCCESS;
    }
}

