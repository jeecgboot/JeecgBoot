package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.BlockIp;
import org.jeecg.modules.im.entity.query_helper.QBlockIp;

import java.util.List;

/**
 * <p>
 * 前台ip黑名单 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Mapper
public interface BlockIpMapper extends BaseMapper<BlockIp> {
    MyPage<BlockIp> pagination(@Param("pg") MyPage<BlockIp> pg, @Param("q") QBlockIp q);
    BlockIp findByIp(String ip);
    List<BlockIp> findByIp2(long ip);
    List<BlockIp> findByIp3(String ip);
    BlockIp findByIp4(String country);
}
