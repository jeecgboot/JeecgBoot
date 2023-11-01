package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.ActivityComment;
import org.jeecg.modules.im.entity.query_helper.QActivityComment;
import org.jeecg.modules.im.mapper.ActivityCommentMapper;
import org.jeecg.modules.im.service.ActivityCommentService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 动态评论列表 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-11-18
 */
@Service
public class ActivityCommentServiceImpl extends BaseServiceImpl<ActivityCommentMapper, ActivityComment> implements ActivityCommentService {
    @Autowired
    private ActivityCommentMapper mapper;

    @Override
    public IPage<ActivityComment> pagination(MyPage<ActivityComment> page, QActivityComment q) {
        return mapper.pagination(page,q);
    }

    @Override
    public Result<Object> createOrUpdate(ActivityComment item) {
        if(item.getId()==null){
            item.setTsCreate(getTs());
            if(!save(item)){
                return fail("添加失败");
            }
        }else{
            if(!updateById(item)){
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
        mapper.delLogicBatch(ids,getTs());
        return success();
    }
    //根据commentId查询评论列表
    @Override
    public Result<Object> findByCommentId(Integer commentId) {
        return success(mapper.findByCommentId(commentId));
    }
}
