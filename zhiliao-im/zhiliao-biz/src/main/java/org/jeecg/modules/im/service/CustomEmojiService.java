package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.CustomEmoji;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.CustomEmoji;
import org.jeecg.modules.im.entity.CustomEmoji;
import org.jeecg.modules.im.entity.query_helper.QCustomEmoji;

import java.util.List;

/**
 * <p>
 * 自定义表情 服务类
 * </p>
 *
 * @author junko
 * @since 2023-02-21
 */
public interface CustomEmojiService extends IService<CustomEmoji> {
    IPage<CustomEmoji> paginationApi(MyPage<CustomEmoji> page, QCustomEmoji q);
    Result<Object> del(String ids);
    List<CustomEmoji> findAll(Integer userId);
    
}
