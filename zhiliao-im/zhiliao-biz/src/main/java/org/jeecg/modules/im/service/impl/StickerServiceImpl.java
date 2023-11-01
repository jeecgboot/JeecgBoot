package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Sticker;
import org.jeecg.modules.im.entity.query_helper.QSticker;
import org.jeecg.modules.im.mapper.StickerMapper;
import org.jeecg.modules.im.service.StickerService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 贴纸 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-27
 */
@Service
public class StickerServiceImpl extends BaseServiceImpl<StickerMapper, Sticker> implements StickerService {

    @Autowired
    private StickerMapper stickerMapper;
    @Override
    public IPage<Sticker> pagination(MyPage<Sticker> page, QSticker q) {
        return stickerMapper.pagination(page,q);
    }

    @Override
    public Result<Object> createOrUpdate(Sticker sticker) {
        if(sticker.getId()==null){
            if(findByName(sticker.getName())!=null){
                return fail("名称已存在");
            }
            sticker.setTsCreate(getTs());
            sticker.setUserId(getCurrentUserId());
            sticker.setAdminId(getCurrentAdminId());
            if(!save(sticker)){
                return fail("添加失败");
            }
        }else{
            Sticker old = findByName(sticker.getName());
            if(old!=null&&!old.getId().equals(sticker.getId())){
                return fail("名称已存在");
            }
            if(!updateById(sticker)){
                return fail("更新失败");
            }
        }
        return success();
    }

    @Override
    public IPage<Sticker> paginationApi(MyPage<Sticker> page, QSticker q) {
        return stickerMapper.paginationApi(page,q);
    }

    @Override
    public Sticker findById(Integer id) {
        return stickerMapper.findById(id);
    }

    @Override
    public Sticker getBigEmoji() {
        return stickerMapper.getBigEmoji();
    }

    @Override
    public List<Sticker> getHot() {
        return stickerMapper.getHot();
    }
    @Override
    public List<Sticker> getEmojis() {
        return stickerMapper.getEmojis();
    }

    @Override
    public Sticker findByName(String name) {
        return stickerMapper.findByName(name);
    }

}
