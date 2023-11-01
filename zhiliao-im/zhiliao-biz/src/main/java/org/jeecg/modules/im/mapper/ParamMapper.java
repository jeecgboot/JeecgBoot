package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Param;
import org.jeecg.modules.im.entity.query_helper.QParam;

import java.util.List;

/**
 * <p>
 * 参数 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Mapper
public interface ParamMapper extends BaseMapper<Param> {

    MyPage<Param> pagination(@org.apache.ibatis.annotations.Param("pg") MyPage<Param> pg, @org.apache.ibatis.annotations.Param("q") QParam q);

    List<Param> findAll();

    Param findByName(String name);
}
