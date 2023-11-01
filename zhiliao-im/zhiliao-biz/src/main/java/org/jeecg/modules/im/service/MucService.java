package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Muc;
import org.jeecg.modules.im.entity.MucMember;
import org.jeecg.modules.im.entity.query_helper.QMuc;

import java.util.List;

/**
 * <p>
 * 群组 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
public interface MucService extends IService<Muc> {
    IPage<Muc> pagination(MyPage<Muc> page, QMuc q);

    /**
     * 解散
     */
    Result<Object> consoleDestroy(Integer mucId);
    /**
     * 锁定
     */
    boolean lock(Integer mucId);
    /**
     * 解锁
     */
    boolean unlock(Integer mucId);

    /**
     * 根据名称查找
     */
    Muc findByName(String name);
    List<Muc> getByIds(String ids);

    Muc findByIdOfUser(Integer id,Integer userId);

    Muc findByAccount(String account);

    //用户创建群组
    Result<Object> create(Muc muc, String inviteAccounts);
    //查询用户创建的、管理的、加入的，角色为非僵尸号
    Result<Object> findMyAll(Integer userId);

    /**
     * 后台创建或更新群组
     */
    Result<Object> createOrUpdate(Muc muc);

    /**
     * 查询用户指定类型的群聊数
     * @return
     */
    Integer getCountOfRole(Integer userId, MucMember.Role role);

    Result<Object> updateQrcode(Muc muc);
    Result<Object> updateName(Muc muc);
    Result<Object> updateInfo(Muc muc);
    Result<Object> updateWelcomes(QMuc muc);
    Result<Object> updateAvatar(Muc muc);

    /**
     * 批量设置/取消管理员
     * @param mucId
     * @param memberIds
     * @param flag
     * @return
     */
    Result<Object> setManagers(Integer mucId, String memberIds,Integer flag);
}
