package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Activity;
import org.jeecg.modules.im.entity.query_helper.QActivity;

import java.util.List;

/**
 * <p>
 * 动态 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-11-13
 */
@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {
    List<Activity> findAll();
    MyPage<Activity> pagination(@Param("pg") MyPage<Activity> pg, @Param("q") QActivity q);


    void delLogicBatch(String ids,Long tsDelete);
}
