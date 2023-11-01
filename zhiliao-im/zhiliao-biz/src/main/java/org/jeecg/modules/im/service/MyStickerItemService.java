package org.jeecg.modules.im.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.MyGif;
import org.jeecg.modules.im.entity.MyStickerItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 我收藏的贴纸项 服务类
 * </p>
 *
 * @author junko
 * @since 2021-12-12
 */
public interface MyStickerItemService extends IService<MyStickerItem> {
    //查询用户全部的收藏贴纸项
    List<MyStickerItem> findAll(Integer userId);
    Result<Object> createOrUpdate(MyStickerItem myStickerItem);
    Result<Object> del(String ids);
    Result<Object> addStickerItem(Integer stickerItemId);
}
