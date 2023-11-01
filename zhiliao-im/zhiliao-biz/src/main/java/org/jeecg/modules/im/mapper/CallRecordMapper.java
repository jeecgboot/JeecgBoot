package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.CallRecord;
import org.jeecg.modules.im.entity.CallRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.im.entity.query_helper.QCallRecord;

import java.util.List;

/**
 * <p>
 * 通话记录 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-12-31
 */
@Mapper
public interface CallRecordMapper extends BaseMapper<CallRecord> {
    MyPage<CallRecord> pagination(@Param("pg") MyPage<CallRecord> pg, @Param("q") QCallRecord q);
    List<CallRecord> findAll(int userId);
}
