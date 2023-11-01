package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SensitiveWord;
import org.jeecg.modules.im.entity.query_helper.QSensitiveWord;

import java.util.List;

/**
 * <p>
 * 敏感词 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-02-20
 */
@Mapper
public interface SensitiveWordMapper extends BaseMapper<SensitiveWord> {
    MyPage<SensitiveWord> pagination(@Param("pg") MyPage<SensitiveWord> myPage, @Param("q") QSensitiveWord q);
    List<SensitiveWord> findAll();
}
