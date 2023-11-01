package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Collection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.im.entity.Collection;
import org.jeecg.modules.im.entity.query_helper.QCollection;

import java.util.List;

/**
 * <p>
 * 收藏的消息 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2023-03-14
 */
@Mapper
public interface CollectionMapper extends BaseMapper<Collection> {
    MyPage<Collection> pagination(@Param("pg") MyPage<Collection> pg, @Param("q") QCollection q);

    List<Collection> paginationApi(@Param("pg") MyPage<Collection> pg, @Param("q") QCollection q);
}
