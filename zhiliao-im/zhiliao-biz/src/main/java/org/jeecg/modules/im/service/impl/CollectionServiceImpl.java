package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Collection;
import org.jeecg.modules.im.entity.Collection;
import org.jeecg.modules.im.entity.query_helper.QCollection;
import org.jeecg.modules.im.mapper.CollectionMapper;
import org.jeecg.modules.im.mapper.CollectionMapper;
import org.jeecg.modules.im.service.CollectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 收藏的消息 服务实现类
 * </p>
 *
 * @author junko
 * @since 2023-03-14
 */
@Service
public class CollectionServiceImpl extends BaseServiceImpl<CollectionMapper, Collection> implements CollectionService {
    @Autowired
    private CollectionMapper mapper;
    @Override
    public IPage<Collection> pagination(MyPage<Collection> page, QCollection q) {
        return mapper.pagination(page,q);
    }

    @Override
    public List<Collection> paginationApi(MyPage<Collection> page, QCollection q) {
        return mapper.paginationApi(page,q);
    }

    //批量删除
    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        mapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }
}
