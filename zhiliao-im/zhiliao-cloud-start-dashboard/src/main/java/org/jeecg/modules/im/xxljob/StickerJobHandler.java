package org.jeecg.modules.im.xxljob;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.im.base.constant.ConstantCache;
import org.jeecg.modules.im.entity.Sticker;
import org.jeecg.modules.im.entity.StickerItem;
import org.jeecg.modules.im.service.StickerItemService;
import org.jeecg.modules.im.service.StickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 贴纸任务
 */
@Component
@Slf4j
public class StickerJobHandler {

    @Resource
    private StickerItemService stickerItemService;
    @Lazy
    @Autowired
    private RedisUtil redisUtil;

    @XxlJob(value = "stickerItem_sendTimes_update")
    public ReturnT<String> stickerItemSendTimesUpdate(String params) {
        String prefix = ConstantCache.STICKER_ITEM_SEND_TIMES.replace("%s","");
        Set<String> keys = redisUtil.keys(prefix);
        List<StickerItem> stickerItems = new ArrayList<>();
        StickerItem sticker;
        for (String key : keys) {
            int stickerId = Integer.parseInt(key.replace(prefix,""));
            sticker = stickerItemService.getById(stickerId);
            sticker.setSendTimes(sticker.getSendTimes()+(int)redisUtil.get(key));
            stickerItems.add(sticker);
        }
        stickerItemService.updateBatchById(stickerItems);
        redisUtil.removeAll(prefix);
        return ReturnT.SUCCESS;
    }
}

