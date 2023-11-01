package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.entity.query_helper.QUser;

import java.util.List;

/**
 * <p>
 * 用户 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-18
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    MyPage<User> pagination(@Param("pg") MyPage<User> pg,@Param("q") QUser q);

    User findByMobile(String mobile);
    User findByEmail(String email);

    User findByAccount(String account);

    User findByUsername(String username);
    User findByQrCode(String qrCode);

    User findByIdWithInfo(Integer id);

    List<Integer> getIdsByMucId(Integer mucId);

    List<User> search(Integer userId,String keyword,Integer type,Boolean accountSearch,Boolean mobileSearch,Boolean nicknameSearch,Boolean usernameSearch,Boolean nicknameSearchExact);


    Integer getOnlineCount();

    Integer getTotalUser();
    String getPassword(Integer id);

    int getCountOfNickname(@Param("q")QUser user);

    List<User> findSysUser(int type);

    List<User> getByIds(String ids);
}
