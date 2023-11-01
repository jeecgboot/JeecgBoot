package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.MySticker;

import java.util.List;

/**
 * <p>
 * 我添加的贴纸 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-03-24
 */
@Mapper
public interface MyStickerMapper extends BaseMapper<MySticker> {

    List<MySticker> findAll(Integer userId);

    MySticker findByIdOfUser(Integer userId, Integer stickerId);
}
