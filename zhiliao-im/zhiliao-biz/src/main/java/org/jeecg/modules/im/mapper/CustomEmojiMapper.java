package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.CustomEmoji;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.im.entity.CustomEmoji;
import org.jeecg.modules.im.entity.CustomEmoji;
import org.jeecg.modules.im.entity.query_helper.QCustomEmoji;

import java.util.List;

/**
 * <p>
 * 自定义表情 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2023-02-21
 */
@Mapper
public interface CustomEmojiMapper extends BaseMapper<CustomEmoji> {
    MyPage<CustomEmoji> paginationApi(@Param("pg") MyPage<CustomEmoji> pg, @Param("q") QCustomEmoji q);
    List<CustomEmoji> findAll(Integer userId);

}
