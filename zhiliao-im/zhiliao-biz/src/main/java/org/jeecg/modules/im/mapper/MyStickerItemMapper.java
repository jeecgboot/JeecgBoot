package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.MyStickerItem;

import java.util.List;

/**
 * <p>
 * 我收藏的贴纸项 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-12-12
 */
@Mapper
public interface MyStickerItemMapper extends BaseMapper<MyStickerItem> {
    List<MyStickerItem> findAll(Integer userId);

}
