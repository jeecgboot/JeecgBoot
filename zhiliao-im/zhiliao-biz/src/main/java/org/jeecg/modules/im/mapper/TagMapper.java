package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.Tag;
import org.jeecg.modules.im.entity.query_helper.QTag;

import java.util.List;

/**
 * <p>
 * 用户的标签 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

    List<Tag> findAllOfUser(Integer userId);

    Tag findByNameOfUser(Tag q);

    List<Tag> findByFriendIdOfUser(QTag q);
}
