package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SignIn;
import org.jeecg.modules.im.entity.query_helper.QSignIn;

import java.util.Date;

/**
 * <p>
 * 签到 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-02-10
 */
@Mapper
public interface SignInMapper extends BaseMapper<SignIn> {
    MyPage<SignIn> pagination(@Param("pg") MyPage<SignIn> myPage, @Param("q") QSignIn q);
    SignIn findByDateOfUser(Date dateBegin,Date dateEnd,Integer userId);
}
