package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucMember;
import org.jeecg.modules.im.entity.query_helper.QMucMember;

import java.util.List;

/**
 * <p>
 * 群组成员 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Mapper
public interface MucMemberMapper extends BaseMapper<MucMember> {
    MyPage<MucMember> pagination(@Param("pg") MyPage<MucMember> myPage, @Param("q") QMucMember q);
    MyPage<MucMember> pageApi(@Param("pg") MyPage<MucMember> myPage, @Param("q") QMucMember q);
    MucMember findByMucIdOfUser(Integer mucId, Integer userId);
    MucMember findById(Integer id);
    Integer getCount(Integer mucId,Integer status);

    List<MucMember> findAll(Integer mucId, Integer status);
    List<MucMember> findMine(Integer userId, Integer status);
}
