package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Activity;
import org.jeecg.modules.im.entity.Activity;
import org.jeecg.modules.im.entity.query_helper.QActivity;
import org.jeecg.modules.im.mapper.ActivityMapper;
import org.jeecg.modules.im.mapper.ActivityMapper;
import org.jeecg.modules.im.service.ActivityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 动态 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-11-13
 */
@Service
public class ActivityServiceImpl extends BaseServiceImpl<ActivityMapper, Activity> implements ActivityService {
    @Autowired
    private ActivityMapper mapper;

    @Override
    public IPage<Activity> pagination(MyPage<Activity> page, QActivity q) {
        return mapper.pagination(page,q);
    }

    @Override
    public Result<Object> createOrUpdate(Activity item) {
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
}
