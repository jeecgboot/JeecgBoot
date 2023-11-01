package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.CallRecord;
import org.jeecg.modules.im.entity.CallRecord;
import org.jeecg.modules.im.entity.query_helper.QCallRecord;
import org.jeecg.modules.im.mapper.CallRecordMapper;
import org.jeecg.modules.im.service.CallRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

/**
 * <p>
 * 通话记录 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-12-31
 */
@Service
public class CallRecordServiceImpl extends BaseServiceImpl<CallRecordMapper, CallRecord> implements CallRecordService {
    @Autowired
    private CallRecordMapper callRecordMapper;
    @Override
    public IPage<CallRecord> pagination(MyPage<CallRecord> page, QCallRecord q) {
        return callRecordMapper.pagination(page, q);
    }

    //删除
    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        callRecordMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }

    @Override
    public Result<Object> findAll(int userId) {
        return success(callRecordMapper.findAll(userId));
    }

    @Override
    public Result<Object> launch(CallRecord callRecord) {
        //判断对方目前状态
        callRecord.setTsCreate(getTs());
        save(callRecord);
        return success(callRecord);
    }
}
