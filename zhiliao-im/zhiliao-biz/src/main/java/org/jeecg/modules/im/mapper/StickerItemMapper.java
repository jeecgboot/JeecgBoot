package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.StickerItem;
import org.jeecg.modules.im.entity.query_helper.QStickerItem;

/**
 * <p>
 * 贴纸项 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-27
 */
@Mapper
public interface StickerItemMapper extends BaseMapper<StickerItem> {
    MyPage<StickerItem> pagination(@Param("pg") MyPage<StickerItem> pg, @Param("q") QStickerItem q);
}
