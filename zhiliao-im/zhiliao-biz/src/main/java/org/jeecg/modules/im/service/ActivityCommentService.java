package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.ActivityComment;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.query_helper.QActivityComment;

/**
 * <p>
 * 动态评论列表 服务类
 * </p>
 *
 * @author junko
 * @since 2021-11-18
 */
public interface ActivityCommentService extends IService<ActivityComment> {
    IPage<ActivityComment> pagination(MyPage<ActivityComment> page, QActivityComment q);
    Result<Object> createOrUpdate(ActivityComment item);
    Result<Object> del(String ids);
    Result<Object> findByCommentId(Integer commentId);
}
