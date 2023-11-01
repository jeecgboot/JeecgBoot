package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SecretQuestion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.im.entity.SecretQuestion;
import org.jeecg.modules.im.entity.query_helper.QSecretQuestion;

import java.util.List;

/**
 * <p>
 * 密保问题 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-11-02
 */
@Mapper
public interface SecretQuestionMapper extends BaseMapper<SecretQuestion> {

    List<SecretQuestion> findAll();
    MyPage<SecretQuestion> pagination(@Param("pg") MyPage<SecretQuestion> pg, @Param("q") QSecretQuestion q);

}
