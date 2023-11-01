package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.StickerItem;
import org.jeecg.modules.im.entity.query_helper.QStickerItem;

/**
 * <p>
 * 贴纸项 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-27
 */
public interface StickerItemService extends IService<StickerItem> {
    IPage<StickerItem> pagination(MyPage<StickerItem> page, QStickerItem q);
    Result<Object> createOrUpdate(StickerItem item);
    Result<Object> del(String ids);

}
