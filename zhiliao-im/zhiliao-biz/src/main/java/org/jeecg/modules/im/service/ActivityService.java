package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Activity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.Activity;
import org.jeecg.modules.im.entity.query_helper.QActivity;

/**
 * <p>
 * 动态 服务类
 * </p>
 *
 * @author junko
 * @since 2021-11-13
 */
public interface ActivityService extends IService<Activity> {
    IPage<Activity> pagination(MyPage<Activity> page, QActivity q);
    Result<Object> createOrUpdate(Activity item);
    Result<Object> del(String ids);
}
