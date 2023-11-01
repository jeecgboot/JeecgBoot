package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SecretAnswer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.im.entity.SecretAnswer;
import org.jeecg.modules.im.entity.query_helper.QSecretAnswer;

/**
 * <p>
 * 密保答案 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-11-02
 */
@Mapper
public interface SecretAnswerMapper extends BaseMapper<SecretAnswer> {
    MyPage<SecretAnswer> pagination(@Param("pg") MyPage<SecretAnswer> pg, @Param("q") QSecretAnswer q);

}
