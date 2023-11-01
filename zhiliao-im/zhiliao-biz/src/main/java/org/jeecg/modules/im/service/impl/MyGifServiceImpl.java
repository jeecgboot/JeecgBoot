package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.im.base.constant.ConstantCache;
import org.jeecg.modules.im.entity.Gif;
import org.jeecg.modules.im.entity.MyGif;
import org.jeecg.modules.im.mapper.MyGifMapper;
import org.jeecg.modules.im.service.GifService;
import org.jeecg.modules.im.service.MyGifService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 我的gif 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-11-27
 */
@Service
public class MyGifServiceImpl extends BaseServiceImpl<MyGifMapper, MyGif> implements MyGifService {

    @Autowired
    private MyGifMapper myGifMapper;
    @Autowired
    private GifService gifService;
    @Lazy
    @Resource
    private RedisUtil redisUtil;
    @Override
    public List<MyGif> findAll(Integer userId) {
        return myGifMapper.findAll(userId);
    }


    @Override
    public Result<Object> createOrUpdate(MyGif myGif) {
        if(myGif.getId()==null){
            myGif.setTsCreate(getTs());
            if(!save(myGif)){
                return fail("添加失败");
            }
        }else{
            if(!updateById(myGif)){
                return fail("更新失败");
            }
        }
        return success();
    }

    @Override
    public Result<Object> delBatch(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        myGifMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }

    @Override
    public Result<Object> addGif(Integer userId,Integer gifId) {
        Gif gif = gifService.getById(gifId);
        if(gif==null||gif.getIsLocked()){
            return fail("动图不存在或被禁用");
        }
        MyGif myGif = findByGifId(userId,gifId);
        if(myGif==null){
            myGif = new MyGif();
            myGif.setTsCreate(getTs());
            myGif.setTsLastSend(getTs());
            myGif.setGifId(gifId);
            myGif.setOrigin(gif.getOrigin());
            myGif.setThumb(gif.getThumb());
            myGif.setUserId(userId);
            if(!save(myGif)){
                return fail("添加失败");
            }
            redisUtil.incr(String.format(ConstantCache.GIF_ADD_TIMES,gifId),1);
            return success();
        }else{
            myGif.setTsLastSend(getTs());
            if(!updateById(myGif)){
                return fail("添加失败");
            }
        }
        return fail("添加失败,gif已存在");
    }

    @Override
    public MyGif findByGifId(Integer userId, Integer gifId) {
        return myGifMapper.findByGifId(userId,gifId);
    }
}
