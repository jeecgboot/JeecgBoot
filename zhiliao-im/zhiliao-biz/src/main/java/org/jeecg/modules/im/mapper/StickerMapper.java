package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Sticker;
import org.jeecg.modules.im.entity.query_helper.QSticker;

import java.util.List;

/**
 * <p>
 * 贴纸 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-27
 */
@Mapper
public interface StickerMapper extends BaseMapper<Sticker> {
    MyPage<Sticker> pagination(@Param("pg") MyPage<Sticker> pg, @Param("q") QSticker q);
    MyPage<Sticker> paginationApi(@Param("pg") MyPage<Sticker> pg, @Param("q") QSticker q);

    Sticker findById(Integer id);
    Sticker findByName(String name);
    List<Sticker> findAll(Integer senderId);

    Sticker getBigEmoji();

    List<Sticker> getHot();
    List<Sticker> getEmojis();
}
