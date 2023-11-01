package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Notice;
import org.jeecg.modules.im.entity.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.query_helper.QNotice;

/**
 * <p>
 * 公告 服务类
 * </p>
 *
 * @author junko
 * @since 2021-10-29
 */
public interface NoticeService extends IService<Notice> {
    IPage<Notice> pagination(MyPage<Notice> page, QNotice q);
    Result<Object> createOrUpdate(Notice notice);

    Notice findById(String id);
}
