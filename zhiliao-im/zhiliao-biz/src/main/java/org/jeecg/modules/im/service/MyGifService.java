package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MyGif;
import org.jeecg.modules.im.entity.MyGif;
import org.jeecg.modules.im.entity.query_helper.QMyGif;

import java.util.List;

/**
 * <p>
 * 我的gif 服务类
 * </p>
 *
 * @author junko
 * @since 2021-11-27
 */
public interface MyGifService extends IService<MyGif> {
    //查询用户全部的gif
    List<MyGif> findAll(Integer userId);
    Result<Object> createOrUpdate(MyGif gifAlbum);
    Result<Object> delBatch(String ids);
    Result<Object> addGif(Integer userId,Integer gifId);

    MyGif findByGifId(Integer userId,Integer gifId);
}
