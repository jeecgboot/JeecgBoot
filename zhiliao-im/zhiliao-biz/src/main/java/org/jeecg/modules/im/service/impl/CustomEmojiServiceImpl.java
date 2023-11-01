package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.CustomEmoji;
import org.jeecg.modules.im.entity.CustomEmoji;
import org.jeecg.modules.im.entity.query_helper.QCustomEmoji;
import org.jeecg.modules.im.mapper.CustomEmojiMapper;
import org.jeecg.modules.im.mapper.CustomEmojiMapper;
import org.jeecg.modules.im.service.CustomEmojiService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 自定义表情 服务实现类
 * </p>
 *
 * @author junko
 * @since 2023-02-21
 */
@Service
public class CustomEmojiServiceImpl extends BaseServiceImpl<CustomEmojiMapper, CustomEmoji> implements CustomEmojiService {

    @Autowired
    private CustomEmojiMapper mapper;
    @Override
    public IPage<CustomEmoji> paginationApi(MyPage<CustomEmoji> page, QCustomEmoji q) {
        return mapper.paginationApi(page, q);
    }
    //逻辑删除
    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        mapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }
    @Override
    public List<CustomEmoji> findAll(Integer userId) {
        return mapper.findAll(userId);
    }
}
