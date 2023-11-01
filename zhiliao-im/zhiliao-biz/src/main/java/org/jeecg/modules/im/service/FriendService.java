package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Friend;
import org.jeecg.modules.im.entity.query_helper.QFriend;

import java.util.List;

/**
 * <p>
 * 好友 服务类
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
public interface FriendService extends IService<Friend> {
    IPage<Friend> pagination(MyPage<Friend> page, QFriend q);

    List<Friend> findAll(QFriend q);
    //查询用户的指定id的好友
    Friend findByIdOfUser(Integer id, Integer userId);
    //查询用户与对方的好友记录
    Friend findOne(Integer userId, Integer toUserId) ;
    //查询用户的好友数
    int getCountOfUser(Integer userId);
    //删除某个用户的好友
    Result<Object> deleteOne(Integer userId, Integer toUserId);
    //添加好友
    Result<Object> addFriend(Integer userId, Integer toUserId,boolean saveSayHello);
    //关注用户
    Result<Object> followUser(Integer userId, Integer toUserId,boolean saveSayHello);
    //控制台直接加好友
    Result<Object> consoleAddFriend(Integer userId, Integer toUserId,boolean saveSayHello);
    //拉黑
    Result<Object> setBlack(Integer userId, Integer toUserId, boolean isBlack);
    //阅后即焚
    Result<Object> setReadDel(Integer userId, Integer toUserId, boolean isReadDel);
}
