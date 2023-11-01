package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.MyStickerItem;
import org.jeecg.modules.im.entity.StickerItem;
import org.jeecg.modules.im.mapper.MyStickerItemMapper;
import org.jeecg.modules.im.mapper.StickerItemMapper;
import org.jeecg.modules.im.service.MyStickerItemService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 我收藏的贴纸项 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-12-12
 */
@Service
public class MyStickerItemServiceImpl extends BaseServiceImpl<MyStickerItemMapper, MyStickerItem> implements MyStickerItemService {


    @Autowired
    private MyStickerItemMapper myStickerItemMapper;
    @Autowired
    private StickerItemMapper stickerItemMapper;

    @Override
    public List<MyStickerItem> findAll(Integer userId) {
        return myStickerItemMapper.findAll(userId);
    }

    @Override
    public Result<Object> createOrUpdate(MyStickerItem myStickerItem) {
        if(myStickerItem.getId()==null){
            myStickerItem.setTsCreate(getTs());
            if(!save(myStickerItem)){
                return fail("添加失败");
            }
        }else{
            if(!updateById(myStickerItem)){
                return fail("更新失败");
            }
        }
        return success();
    }

    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        myStickerItemMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }

    @Override
    public Result<Object> addStickerItem(Integer stickerItemId) {
        StickerItem stickerItem = stickerItemMapper.selectById(stickerItemId);
        if(stickerItem==null||stickerItem.getIsLocked()){
            return fail("贴纸不存在或被禁用");
        }
        MyStickerItem star = new MyStickerItem();
        star.setTsCreate(getTs());
        star.setStickerItemId(stickerItemId);
        star.setStickerId(stickerItem.getStickerId());
        star.setOrigin(stickerItem.getOrigin());
        star.setThumb(stickerItem.getThumb());
        star.setLottie(stickerItem.getLottie());
        star.setUserId(getCurrentUserId());
        if(save(star)){
            return fail("添加失败");
        }
        return success();
    }
}
