package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SensitiveWord;
import org.jeecg.modules.im.entity.query_helper.QSensitiveWord;

import java.util.List;

/**
 * <p>
 * 敏感词 服务类
 * </p>
 *
 * @author junko
 * @since 2021-02-20
 */
public interface SensitiveWordService extends IService<SensitiveWord> {
    List<SensitiveWord> findAll();

    IPage<SensitiveWord> pagination(MyPage<SensitiveWord> page, QSensitiveWord q);

    Result<Object> del(String ids);

    Result<Object> createOrUpdate(SensitiveWord sensitiveWord);
}
