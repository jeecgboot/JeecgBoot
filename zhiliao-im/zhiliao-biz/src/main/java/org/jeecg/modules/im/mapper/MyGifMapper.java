package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MyGif;
import org.jeecg.modules.im.entity.query_helper.QMyGif;

import java.util.List;

/**
 * <p>
 * 我的gif Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-11-27
 */
@Mapper
public interface MyGifMapper extends BaseMapper<MyGif> {

    List<MyGif> findAll(Integer userId);

    MyGif findByGifId(Integer userId, Integer gifId);
}
