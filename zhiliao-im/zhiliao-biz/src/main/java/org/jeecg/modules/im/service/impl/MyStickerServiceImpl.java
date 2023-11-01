package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.im.base.constant.ConstantCache;
import org.jeecg.modules.im.base.util.Kv;
import org.jeecg.modules.im.entity.MySticker;
import org.jeecg.modules.im.mapper.MyStickerMapper;
import org.jeecg.modules.im.service.MyStickerService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 我添加的贴纸 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-03-24
 */
@Service
public class MyStickerServiceImpl extends BaseServiceImpl<MyStickerMapper, MySticker> implements MyStickerService {

    @Autowired
    private MyStickerMapper myStickerMapper;
    @Override
    public List<MySticker> findMyAll(Integer userId) {
        return myStickerMapper.findAll(userId);
    }
    @Lazy
    @Resource
    private RedisUtil redisUtil;
    @Override
    public Result<Object> add(Integer stickerId) {
        Integer userId = getCurrentUserId();
        if(getOne(userId,stickerId)!=null){
            return fail("该贴纸已添加过");
        }
        MySticker sticker = new MySticker();
        sticker.setStickerId(stickerId);
        sticker.setUserId(userId);
        sticker.setTsCreate(getTs());
        if(!save(sticker)){
            return fail("添加失败，请重试");
        }
        redisUtil.incr(String.format(ConstantCache.STICKER_ADD_TIMES,stickerId),1);
        return success(sticker);
    }

    @Override
    public MySticker getOne(Integer userId, Integer stickerId) {
        return myStickerMapper.findByIdOfUser(userId,stickerId);
    }

    @Override
    public Result<Object> del(String ids) {
        for (String id : StringUtils.split(ids, ",")) {
            Integer stickerId = Integer.parseInt(id);
            Integer userId = getCurrentUserId();
            MySticker mySticker = getOne(userId,stickerId);
            if(null==mySticker){
                return fail("未添加过该贴纸");
            }
            if(removeById(mySticker)){
                return success(mySticker.getId());
            }
            redisUtil.decr(String.format(ConstantCache.STICKER_ADD_TIMES,stickerId),1);
            return fail("删除失败");
        }
        return success();
    }

}
