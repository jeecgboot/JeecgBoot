package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.GifAlbum;
import org.jeecg.modules.im.entity.query_helper.QGifAlbum;
import org.jeecg.modules.im.mapper.GifAlbumMapper;
import org.jeecg.modules.im.service.GifAlbumService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * gif图集 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-11-27
 */
@Service
public class GifAlbumServiceImpl extends BaseServiceImpl<GifAlbumMapper, GifAlbum> implements GifAlbumService {
    @Autowired
    private GifAlbumMapper gifAlbumMapper;

    @Override
    public IPage<GifAlbum> pagination(MyPage<GifAlbum> page, QGifAlbum q) {
        return gifAlbumMapper.pagination(page, q);
    }


    @Override
    public Result<Object> createOrUpdate(GifAlbum gifAlbum) {
        if(gifAlbum.getId()==null){
            gifAlbum.setTsCreate(getTs());
            if(!save(gifAlbum)){
                return fail("添加失败");
            }
        }else{
            if(!updateById(gifAlbum)){
                return fail("更新失败");
            }
        }
        return success();
    }

    //逻辑删除
    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        gifAlbumMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }

    @Override
    public Result<Object> findUngroup() {
        GifAlbum album = gifAlbumMapper.findUngroup();
        if(album==null){
            album = new GifAlbum();
            album.setId(0);
            album.setName("未分组");
            album.setTsCreate(getTs());
            save(album);
        }
        return success(album);
    }
    @Override
    public List<GifAlbum> findAll() {
        return gifAlbumMapper.findAll();
    }
}
