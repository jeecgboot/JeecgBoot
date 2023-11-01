package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.OfUser;
import org.jeecg.modules.im.entity.Tag;
import org.jeecg.modules.im.entity.query_helper.QTag;

import java.util.List;

/**
 * <p>
 * 用户的标签 服务类
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
public interface OfUserService extends IService<OfUser> {

    OfUser findByUsername(String username);
}
