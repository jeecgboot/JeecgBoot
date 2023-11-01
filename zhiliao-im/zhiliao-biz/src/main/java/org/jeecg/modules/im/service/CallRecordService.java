package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.CallRecord;
import org.jeecg.modules.im.entity.CallRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.query_helper.QCallRecord;

/**
 * <p>
 * 通话记录 服务类
 * </p>
 *
 * @author junko
 * @since 2021-12-31
 */
public interface CallRecordService extends IService<CallRecord> {
    IPage<CallRecord> pagination(MyPage<CallRecord> page, QCallRecord q);
    Result<Object> del(String ids);
    Result<Object> findAll(int userId);

    Result<Object> launch(CallRecord callRecord);
}
