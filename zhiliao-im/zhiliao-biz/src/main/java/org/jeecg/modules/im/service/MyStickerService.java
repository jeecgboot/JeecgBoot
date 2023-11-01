package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.MySticker;

import java.util.List;

/**
 * <p>
 * 我添加的贴纸 服务类
 * </p>
 *
 * @author junko
 * @since 2021-03-24
 */
public interface MyStickerService extends IService<MySticker> {
    //查找所有贴纸
    List<MySticker> findMyAll(Integer userId);
    //添加贴纸
    Result<Object> add(Integer stickerId);
    //已存在某贴纸
    MySticker getOne(Integer userId,Integer stickerId);
    //移除
    Result<Object> del(String ids);
}
