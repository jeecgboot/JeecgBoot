package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.ExceptionLog;
import org.jeecg.modules.im.entity.query_helper.QExceptionLog;

/**
 * <p>
 * 系统异常日志 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Mapper
public interface ExceptionLogMapper extends BaseMapper<ExceptionLog> {
    MyPage<ExceptionLog> pagination(@Param("pg") MyPage<ExceptionLog> pg, @Param("q") QExceptionLog q);
}
