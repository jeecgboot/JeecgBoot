package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Friend;
import org.jeecg.modules.im.entity.query_helper.QFriend;

import java.util.List;

/**
 * <p>
 * 好友 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
@Mapper
public interface FriendMapper extends BaseMapper<Friend> {
    MyPage<Friend> pagination(@Param("pg") MyPage<Friend> pg, @Param("q") QFriend q);

    List<Friend> findAll(QFriend q);

    Friend findByIdOfUser(Friend friend);
    Friend findOne(Integer userId,Integer toUserId);
}
