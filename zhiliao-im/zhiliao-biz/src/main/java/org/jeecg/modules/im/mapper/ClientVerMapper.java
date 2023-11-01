package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.ClientVer;
import org.jeecg.modules.im.entity.query_helper.QClientVer;

import java.util.List;

/**
 * <p>
 * app版本 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-30
 */
@Mapper
public interface ClientVerMapper extends BaseMapper<ClientVer> {

    MyPage<ClientVer> pagination(@Param("pg") MyPage<ClientVer> pg, @Param("q") QClientVer q);

    ClientVer findLatestByPlatform(String platform);

    List<ClientVer> findLatestOfAll();
}
