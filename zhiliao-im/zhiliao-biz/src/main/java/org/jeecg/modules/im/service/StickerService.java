package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Sticker;
import org.jeecg.modules.im.entity.query_helper.QSticker;

import java.util.List;

/**
 * <p>
 * 贴纸 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-27
 */
public interface StickerService extends IService<Sticker> {
    IPage<Sticker> pagination(MyPage<Sticker> page, QSticker q);
    IPage<Sticker> paginationApi(MyPage<Sticker> page, QSticker q);
    Result<Object> createOrUpdate(Sticker sticker);

    Sticker findById(Integer id);
    Sticker getBigEmoji();
    List<Sticker> getHot();
    List<Sticker> getEmojis();
    Sticker findByName(String name);
}
