package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Muc;
import org.jeecg.modules.im.entity.query_helper.QMuc;

import java.util.List;

/**
 * <p>
 * 群组 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Mapper
public interface MucMapper extends BaseMapper<Muc> {
    MyPage<Muc> pagination(@Param("pg") MyPage<Muc> pg, @Param("q") QMuc q);

    Muc findByName(String name);
    Muc findByAccount(String account);

    List<Muc> findMyAll(Integer userId, Integer role,Integer status);
    List<Muc> getByIds(String ids);
    Integer getCountOfRole(Integer userId,Integer role);

    Muc findByIdOfUser(Integer id, Integer userId);
}
