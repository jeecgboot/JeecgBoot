package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.im.entity.Notice;
import org.jeecg.modules.im.entity.query_helper.QNotice;

/**
 * <p>
 * 公告 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-10-29
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
    MyPage<Notice> pagination(@Param("pg") MyPage<Notice> pg, @Param("q") QNotice q);
}
