package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Muc;
import org.jeecg.modules.im.entity.MucMember;
import org.jeecg.modules.im.entity.query_helper.QMucMember;

import java.util.List;

/**
 * <p>
 * 群组成员 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
public interface MucMemberService extends IService<MucMember> {
    IPage<MucMember> pagination(MyPage<MucMember> page, QMucMember q);

    /**
     * 分页查找
     */
    MyPage<MucMember> pageApi(MyPage<MucMember> page, QMucMember q);

    /**
     * 后台邀请用户进群
     */
    Result<Object> consoleInvite(Integer mucId,  String userIds);

    /**
     * 查询用户是否在某个群里
     */
    MucMember findByMucIdOfUser(Integer mucId, Integer userId);

    MucMember findById(Integer id);

    List<MucMember> findAll(Integer mucId);
    //获取群主
    MucMember getMaster(Integer mucId);
    //获取群管理
    List<MucMember> getManagers(Integer mucId);
    //获取群成员
    List<MucMember> getMembers(Integer mucId);

    List<MucMember> findMine(Integer userId);
    //移除群聊
    Result<Object> kick(Integer userId, Integer mucId, String memberIds);
    //退群
    Result<Object> quit(Integer userId, Integer mucId);
    //踢除全部成员
    Result<Object> kickAll(Muc muc, MucMember.Status status);
    //获取群人数
    Integer getCount(Integer mucId,Integer status);

    Result<Object> updateMember(MucMember member);

}
