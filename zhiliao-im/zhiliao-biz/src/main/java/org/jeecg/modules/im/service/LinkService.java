package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Link;
import org.jeecg.modules.im.entity.query_helper.QLink;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author junko
 * @since 2021-10-29
 */
public interface LinkService extends IService<Link> {
    IPage<Link> pagination(MyPage<Link> page, QLink q);
    Result<Object> createOrUpdate(Link link);

    Link findById(String id);
    Result<Object> del(String ids);
}
