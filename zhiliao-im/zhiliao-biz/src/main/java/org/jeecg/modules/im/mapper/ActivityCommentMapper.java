package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.ActivityComment;
import org.jeecg.modules.im.entity.query_helper.QActivityComment;

import java.util.List;

/**
 * <p>
 * 动态评论列表 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-11-18
 */
@Mapper
public interface ActivityCommentMapper extends BaseMapper<ActivityComment> {
    MyPage<ActivityComment> pagination(@Param("pg") MyPage<ActivityComment> pg, @Param("q") QActivityComment q);
    void delLogicBatch(String ids,Long tsDelete);
    List<ActivityComment> findByCommentId(Integer commentId);
}
