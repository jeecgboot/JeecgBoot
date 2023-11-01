package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.GifAlbum;
import org.jeecg.modules.im.entity.query_helper.QGifAlbum;

import java.util.List;

/**
 * <p>
 * gif图集 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-11-27
 */
@Mapper
public interface GifAlbumMapper extends BaseMapper<GifAlbum> {
    MyPage<GifAlbum> pagination(@Param("pg") MyPage<GifAlbum> pg, @Param("q") QGifAlbum q);
    //查询未分组
    GifAlbum findUngroup();
    List<GifAlbum> findAll();
}
