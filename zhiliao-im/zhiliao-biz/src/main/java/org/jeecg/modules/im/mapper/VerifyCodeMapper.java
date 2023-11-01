package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.VerifyCode;
import org.jeecg.modules.im.entity.query_helper.QVerifyCode;

/**
 * <p>
 * 短信发送 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-23
 */
@Mapper
public interface VerifyCodeMapper extends BaseMapper<VerifyCode> {
    MyPage<VerifyCode> pagination(@Param("pg") MyPage<VerifyCode> pg, @Param("q") QVerifyCode q);
    /**
     * 根据手机号和类型查询最新一条
     */
    VerifyCode findLatestByMobileAndType(String mobile, String type);
    /**
     * 根据邮箱和类型查询最新一条
     */
    VerifyCode findLatestByEmailAndType(String email, String type);
}
